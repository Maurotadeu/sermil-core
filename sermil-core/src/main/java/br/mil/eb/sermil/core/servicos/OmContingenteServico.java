package br.mil.eb.sermil.core.servicos;

import java.util.Calendar;
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
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Gerenciamento do contingente incorporado na Organização Militar.
 * @author Abreu Lopes, Daniel Gardino
 * @since 4.0
 * @version 5.3.0
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

   public List<Object[]> listarEfetivo(final Integer codom, final Integer ano, final Integer situacao, final Date data, final String biAbiNr) throws SermilException {
      final List<Object[]> listaEfetivo = this.cidadaoDao.findBySQL("SELECT c.ra, c.nome, c.padrao_codigo, c.gpt_incorp FROM cidadao c JOIN cid_evento e ON c.ra = e.cidadao_ra WHERE c.om_codigo = ? AND c.situacao_militar = ? AND e.codigo = 7 AND extract(year from e.data) = ?  order by c.gpt_incorp", codom, situacao, ano);
      logger.debug("Parâmetros: codom={} situacao={} ano={}", codom, situacao, ano);
      logger.debug("Efetivo: {}", listaEfetivo);
      return listaEfetivo;
   }

   public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom) throws SermilException {
      final List<OmBoletim> lista = this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimOm", ano, codom);
      logger.debug("Parâmetros: ano={} codom={}", ano, codom);
      logger.debug("Boletim: {}", lista);
      return lista;
   }

   public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom, final Integer codigo) throws SermilException {
      final List<OmBoletim> lista = this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimCod", ano, codom, codigo);
      logger.debug("Parâmetros: ano={} codom={} codigo={}", ano, codom, codigo);
      logger.debug("Boletim: {}", lista);
      return lista;
   }

   @Transactional
   public void incorporar(final List<CidEvento> lista, final Usuario usr, final List<OmBoletim> listaBoletim) throws SermilException {
      final Calendar hoje = Calendar.getInstance();
      for (int i = 0; i < lista.size(); i++) {
         final CidEvento e = lista.get(i);
         if (e != null && e.getPk() != null && e.getPk().getCodigo() != -1) {
            int sitMil = TipoSituacaoMilitar.DISTRIBUIDO.ordinal();
            switch (e.getPk().getCodigo()) {
            case 6:
               sitMil = TipoSituacaoMilitar.EXCESSO_OM.ordinal();
               break;
            case 8:
               sitMil = TipoSituacaoMilitar.INSUBMISSO.ordinal();
               break;
            case 9:
               sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
               break;
            case 23:
               sitMil = TipoSituacaoMilitar.REFRATARIO.ordinal();
               break;
            }
            e.setAnotacao("USR: " + usr.getCpf());
            final Cidadao c = this.cidadaoDao.findById(e.getPk().getCidadaoRa());
            c.addCidEvento(e);
            c.setSituacaoMilitar(sitMil);
            final CidAuditoria aud = new CidAuditoria(c.getRa(), new Date(), "EVENTO: " + e.getPk().toString(), usr.getAcessoIp(), usr.getCpf());
            c.addCidAuditoria(aud);
            this.cidadaoDao.save(c);
            // Salva Boletim Incorp
            if (sitMil == 12) {
               if (listaBoletim.get(i).getPk().getCodigo() != -1) {
                  final OmBoletim o = listaBoletim.get(i);
                  final List<OmBoletim> listaBol = this.listarBoletim(hoje.get(Calendar.YEAR), c.getOm().getCodigo(), o.getPk().getCodigo());
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

   @Transactional
   public void averbarSituacoes(final List<CidEvento> lista, final List<String> qm, final List<String> pg, final Usuario usr) throws SermilException {
      if (lista == null || lista.isEmpty()) {
         throw new SermilException("Lista de eventos está vazia.");
      }
      for (int i = 0; i < lista.size(); i++) {
         final CidEvento e = lista.get(i);
         if (e != null && e.getPk() != null && e.getPk().getCodigo() != -1) {
            int sitMil = 12; // Incorporado
            switch (e.getPk().getCodigo()) {
            case 10:
               sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
               break; // Qualificação
            case 11:
               sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
               break; // Engajamento
            case 12:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Licenciamento
            case 13:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Anulação de Incorporação/Matrícula
            case 14:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Desincorporação
            case 15:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Exclusão a bem da Disciplina
            case 16:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Deserção
            case 17:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Trancamento de Matrícula
            case 18:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Reforma
            case 19:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Desaparecimento
            case 20:
               sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
               break; // Extravio
            case 21:
               sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
               break; // Reinclusão
            case 24:
               sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
               break; // Reabilitação
            case 25:
               sitMil = TipoSituacaoMilitar.EXCLUIDO.ordinal();
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

}
