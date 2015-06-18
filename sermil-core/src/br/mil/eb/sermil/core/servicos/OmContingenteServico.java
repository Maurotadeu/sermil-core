package br.mil.eb.sermil.core.servicos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.OmBoletimDao;
import br.mil.eb.sermil.core.dao.PostoGraduacaoDao;
import br.mil.eb.sermil.core.dao.QmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.OmBoletim;
import br.mil.eb.sermil.modelo.OmBoletimCidadao;
import br.mil.eb.sermil.modelo.Usuario;

/**
 * Serviço de gerenciamento de contingente incorporado de OM.
 * 
 * @author Abreu Lopes, Gardino
 * @since 4.0
 * @version $Id: OmContingenteServico.java 2437 2014-05-27 19:30:45Z wlopes $
 */
@Named("omEfetivoServico")
public class OmContingenteServico {

   protected static final Logger logger = LoggerFactory.getLogger(OmContingenteServico.class);

   @Inject
   private CidadaoDao cidadaoDao;

   @Inject
   private PostoGraduacaoDao pgDao;

   @Inject
   private QmDao qmDao;

   @Inject
   private OmBoletimDao omBoletimDao;

   public OmContingenteServico() {
      logger.debug("OmContingenteServico iniciado");
   }

   /**
    * Listagem do efetivo da OM.
    * 
    * @param codom Código da OM
    * @param ano Ano da distribuição
    * @param situacao Situação Militar (7 = distribuído ou 12 = incorporado)
    * @param data data default para o evento a ser gerado
    * @param porNome true se listar por nome
    * @return Lista de eventos (CID_EVENTO) para cada cidadão relacionado
    * @throws SermilException erro na consulta
    */
   public List<CidEvento> listarEfetivo(final Integer codom, final Integer ano, final Short situacao, final Date data, final String biAbiNr, final boolean porNome) throws SermilException {
      List<CidEvento> listaEvt = null;
      final List<Object[]> listaEfetivo = this.cidadaoDao.findBySQL(
            "SELECT c.ra, c.nome, c.padrao_Codigo, c.gpt_Incorp FROM Cidadao c, Cid_evento e WHERE c.ra = e.cidadao_ra and c.om_codigo = ? AND c.situacao_Militar = ? AND e.codigo = 7 AND extract(year from e.data) = ?  order by c.gpt_Incorp", codom,
            situacao, ano);
      // final List<Object[]> listaCid =
      // cidadaoDao.findByNamedQueryArray("Cidadao.listarPorOmSituacao", codom, ano.substring(2, 4),
      // situacao);
      final Date dataEvt = (data != null ? data : new Date());
      final Byte codigo = (situacao.equals(Short.valueOf("7")) ? Byte.valueOf("9") : Byte.valueOf("15"));
      if (listaEfetivo != null && !listaEfetivo.isEmpty()) {
         listaEvt = new ArrayList<CidEvento>(listaEfetivo.size());
         for (Object[] c : listaEfetivo) {
            final CidEvento e = new CidEvento(((BigDecimal) c[0]).longValue(), codigo, dataEvt);
            e.setBiAbiNr(biAbiNr);
            e.setAnotacao((String) c[1]);
            final Cidadao cid = new Cidadao();
            cid.setRa(((BigDecimal) c[0]).longValue());
            cid.setNome((String) c[1]);
            cid.setPadraoCodigo((String) c[2]);
            cid.setGptIncorp((String) c[3]);
            cid.addCidEvento(e);
            listaEvt.add(e);
         }
         if (porNome) {
            Collections.sort(listaEvt, new OmContingenteServico.OrdenaNome());
         }
      }
      return listaEvt;
   }

   public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom) throws SermilException {
      return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimOm", ano, codom);
   }

   public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom, final Integer codigo) throws SermilException {
      return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimCod", ano, codom, codigo);
   }

   /**
    * Processamento de incorporação de OM.
    * 
    * @param lista lista de eventos de cidadãos a serem incorporados
    * @param usr usuário responsável pela informação
    * @throws SermilException erro no processamento das informações
    */
   @Transactional
   public void incorporar(final List<CidEvento> lista, final Usuario usr, final List<OmBoletim> listaBoletim) throws SermilException {
      final Calendar cal = Calendar.getInstance();
      for (int i = 0; i < lista.size(); i++) {
         final CidEvento e = lista.get(i);
         if (e != null && e.getPk() != null && e.getPk().getCodigo() != -1) {
            byte sitMil = 7; // Distribuido
            switch (e.getPk().getCodigo()) {
            case 6:
               sitMil = 9;
               break; // Excesso
            case 8:
               sitMil = 10;
               break; // Insubmisso
            case 9:
               sitMil = 12;
               break; // Incorporado
            case 23:
               sitMil = 11;
               break; // Refratário
            }
            e.setAnotacao("USR: " + usr.getCpf());
            final Cidadao c = this.cidadaoDao.findById(e.getPk().getCidadaoRa());
            c.addCidEvento(e);
            c.setSituacaoMilitar(sitMil);
            final CidAuditoria aud = new CidAuditoria(c.getRa(), new Date(), "EVENTO " + e.getPk().getCodigo(), usr.getAcessoIp(), usr.getCpf());
            c.addCidAuditoria(aud);
            this.cidadaoDao.save(c);
            // Salva Boletim Incorp
            if (sitMil == 12) {
               if (listaBoletim.get(i).getPk().getCodigo() != -1) {
                  final OmBoletim o = listaBoletim.get(i);
                  final List<OmBoletim> listaBol = this.listarBoletim(cal.get(Calendar.YEAR), c.getOm().getCodigo(), o.getPk().getCodigo());
                  final OmBoletim bol = listaBol.get(0);
                  final OmBoletimCidadao obc = new OmBoletimCidadao();
                  obc.setAno(bol.getPk().getAno());
                  obc.setBoletimCod(bol.getPk().getCodigo());
                  obc.setOmCodigo(bol.getPk().getOmCodigo());
                  obc.getPk().setCidadaoRa(Long.valueOf(c.getRa()));
                  obc.setCidadaoNome(c.getNome());
                  obc.getPk().setGptIncorp(bol.getPk().getGptIncorp());
                  bol.getOmBoletimCidadao().add(obc);
                  this.omBoletimDao.save(bol);
               }
            }
         }
      }
   }

   /**
    * Averbação de eventos na vida militar do cidadão incorporado.
    * 
    * @param lista lista de eventos para averbação
    * @param qm lista de QM
    * @param pg lista de postos ou graduações
    * @param usr usuário responsável pela informação
    * @throws SermilException erro no processamento das informações
    */
   @Transactional
   public void averbarSituacoes(final List<CidEvento> lista, final List<String> qm, final List<String> pg, final Usuario usr) throws SermilException {
      for (int i = 0; i < lista.size(); i++) {
         final CidEvento e = lista.get(i);
         if (e.getPk().getCodigo() != -1) {
            byte sitMil = 9; // Incorporado
            switch (e.getPk().getCodigo()) {
            case 10:
               sitMil = 12;
               break; // Qualificação
            case 11:
               sitMil = 12;
               break; // Engajamento
            case 12:
               sitMil = 15;
               break; // Licenciamento
            case 15:
               sitMil = 15;
               break; // Exclusão a bem da Disciplina
            case 30:
               sitMil = 12;
               break; // Reengajamento
            case 13:
               sitMil = 15;
               break; // Anulação de Incorporação/Matrícula
            case 14:
               sitMil = 20;
               break; // Desincorporação
            case 16:
               sitMil = 20;
               break; // Deserção
            case 17:
               sitMil = 15;
               break; // Trancamento de Matrícula
            case 18:
               sitMil = 15;
               break; // Reforma
            case 19:
               sitMil = 12;
               break; // Desaparecimento
            case 20:
               sitMil = 12;
               break; // Extravio
            case 21:
               sitMil = 12;
               break; // Reinclusão
            case 24:
               sitMil = 12;
               break; // Reabilitação
            case 25:
               sitMil = 12;
               break; // Falecido
            }
            e.setAnotacao("USR: " + usr.getCpf());
            final Cidadao c = this.cidadaoDao.findById(e.getPk().getCidadaoRa());
            c.addCidEvento(e);
            c.setSituacaoMilitar(sitMil);
            c.setDispensa(Byte.valueOf("0"));
            if (!"-1".equals(qm.get(i))) {
               c.setQm(this.qmDao.findById(qm.get(i)));
            }
            if (!"-1".equals(pg.get(i))) {
               c.setPostoGraduacao(this.pgDao.findById(pg.get(i)));
            }
            // força armada
            byte forca = 3;
            switch (c.getOm().getOmTipo()) {
            case 7:
               forca = 1;
               break;
            case 8:
               forca = 2;
               break;
            default:
               forca = 3;
            }
            c.setForcaArmada(forca);
            // informações de mobilização
            c.setMobSituacao(Byte.valueOf("1"));
            if (sitMil == 15) {
               c.setMobDestino(Byte.valueOf("2"));
            } else {
               c.setMobDestino(Byte.valueOf("1"));
            }
            if (c.getPostoGraduacao() != null && c.getQm() != null) {
               int cpg = Integer.parseInt(c.getPostoGraduacao().getCodigo());
               if (cpg < 20) { // Oficial
                  if (c.getQm().getCodigo().startsWith("6") || c.getQm().getCodigo().startsWith("7") || c.getQm().getCodigo().startsWith("8") || c.getQm().getCodigo().startsWith("9")) {
                     c.setClasseReserva(Byte.valueOf("1")); // R1
                  } else if (c.getQm().getCodigo().startsWith("T")) {
                     c.setClasseReserva(Byte.valueOf("2")); // R2
                  }
               } else { // Praça
                  if (cpg == 24 && c.getQm().getCodigo().startsWith("S")) { // Sgt Tmpr
                     c.setClasseReserva(Byte.valueOf("4")); // Praça 2 Cat
                  } else if (cpg == 21) { // ST
                     c.setClasseReserva(Byte.valueOf("5")); // Reserva Remunerada
                  } else if (cpg > 21 && (!c.getQm().getCodigo().equals("2000") || !c.getQm().getCodigo().equals("2100"))) {
                     c.setClasseReserva(Byte.valueOf("3")); // Praça 1 Cat
                  } else {
                     c.setClasseReserva(Byte.valueOf("4")); // Praça 2 Cat
                  }
               }
            }
            final CidAuditoria aud = new CidAuditoria(c.getRa(), new Date(), "EVENTO " + e.getPk().getCodigo(), usr.getAcessoIp(), usr.getCpf());
            c.addCidAuditoria(aud);
            this.cidadaoDao.save(c);
         }
      }
   }

   /**
    * Implementa um comparator de eventos por data e código.
    * 
    * @author AbreuLopes
    */
   private class OrdenaNome implements Comparator<CidEvento> {

      @Override
      public int compare(CidEvento e1, CidEvento e2) {
         int status = e1.getCidadao().getNome().compareTo(e2.getCidadao().getNome());
         if (status == 0) { // mesmo nome
            if (e1.getPk().getCodigo() < e2.getPk().getCodigo()) {
               status = -1;
            } else if (e1.getPk().getCodigo() > e2.getPk().getCodigo()) {
               status = 1;
            } else { // mesmo código
               if (e1.getPk().getData().before(e2.getPk().getData())) {
                  status = -1;
               } else if (e1.getPk().getData().after(e2.getPk().getData())) {
                  status = 1;
               }
            }
         }
         return status;
      }

   }

}
