package br.mil.eb.sermil.core.servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.RaMestreException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.Ra;
import br.mil.eb.sermil.tipos.TipoEvento;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Serviço de Mobilização de Pessoal.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.7
 */
@Named("mobilizacaoServico")
public class MobilizacaoServico {

   protected static final Logger logger = LoggerFactory.getLogger(MobilizacaoServico.class);

   @Inject
   private CidadaoDao cidadaoDao;

   @Inject
   private RaMestreDao raMestreDao;

   public MobilizacaoServico() {
      logger.debug("MobilizacaoServico iniciado");
   }

   @Transactional
   public Cidadao cadastrar(final Cidadao cid, final Date dtAlist, final CidDocApres cda, final Usuario usr, final String msg) throws SermilException {
      // Verificar se já está cadastrado
      if (!this.cidadaoDao.findByNamedQuery("Cidadao.listarUnico", cid.getNome(), cid.getMae(), cid.getNascimentoData()).isEmpty()) {
         throw new CidadaoCadastradoException(cid.getNome(), cid.getMae(), cid.getNascimentoData());
      }
      // Verificar Data de Nascimento
      if (cid.getNascimentoData() == null) {
         throw new SermilException("Data de nascimento é obrigatória.");
      } else {
         final Calendar cal = Calendar.getInstance();
         cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 16);
         if (cid.getNascimentoData().after(cal.getTime())) {
            throw new SermilException("Data de nascimento é muito recente.");
         }
      }
      
      // Configurar Cidadão
      final Calendar cal = Calendar.getInstance();
      cal.setTime(cid.getNascimentoData());
      cid.setVinculacaoAno(cal.get(Calendar.YEAR) + 18);
      if (cid.getDispensa() == null) {
         cid.setDispensa(Byte.valueOf("0"));
      }
      if (cid.getPostoGraduacao() != null && cid.getPostoGraduacao().getCodigo() == null) {
         cid.setPostoGraduacao(null);
      }
      if (cid.getOm() != null && cid.getOm().getCodigo() == null) {
         cid.setOm(null);
      }
      
      // Gerar RA se for nulo
      if (cid.getRa() == null) {
         final RaMestre raMestre = this.raMestreDao.findById(new RaMestre.PK(cid.getJsm().getPk().getCsmCodigo(), cid.getJsm().getPk().getCodigo()));
         if (raMestre == null) {
            throw new RaMestreException("CSM/JSM não existe na tabela de controle de RA (RA_MESTRE)");
         }
         this.cidadaoDao.getEntityManager().lock(raMestre, LockModeType.PESSIMISTIC_WRITE);
         raMestre.setSequencial(raMestre.getSequencial() + 1);
         cid.setRa(new Ra.Builder().csm(raMestre.getPk().getCsmCodigo()).jsm(raMestre.getPk().getJsmCodigo()).sequencial(raMestre.getSequencial()).build().getValor());
         this.raMestreDao.save(raMestre);
         logger.debug("RA gerado: {} (JSM={} - MESTRE SEQUENCIAL: {})", cid.getRa(), cid.getJsm(), raMestre.getSequencial());
      }
      
      // Gerar Evento
      if (cid.getSituacaoMilitar() == TipoSituacaoMilitar.ALISTADO.ordinal()) {
         final Calendar dataEvento = Calendar.getInstance();
         if (dtAlist != null) {
            dataEvento.setTime(dtAlist);
         }
         final CidEvento evento = new CidEvento();
         evento.getPk().setCidadaoRa(cid.getRa());
         evento.getPk().setCodigo(TipoEvento.ALISTAMENTO.ordinal());
         evento.getPk().setData(dataEvento.getTime());
         evento.setAnotacao(msg);
         cid.addCidEvento(evento);
      }

      // Documento apresentado
      if (cda != null) {
        cda.getPk().setCidadaoRa(cid.getRa()); 
        cid.addCidDocApres(cda);
      }
   
      // Gerar Auditoria
      final CidAuditoria aud = new CidAuditoria(cid.getRa(), new Date(), msg.substring(0, msg.length() > 500 ? 500 : msg.length()), usr.getAcessoIp(), usr.getCpf());
      cid.addCidAuditoria(aud);

      //Finaliza
      this.cidadaoDao.save(cid);
      return cid;
   }

   public List<Object[]> pesquisar(final Cidadao cidadao, final String dataLic) throws SermilException {
      if (cidadao == null) {
         throw new CriterioException();
      }
      try {
         final EntityManager em = this.cidadaoDao.getEntityManager();
         final CriteriaBuilder cb = em.getCriteriaBuilder();
         final CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
         final Root<Cidadao> cid = cq.from(Cidadao.class);
         final Join<Cidadao, CidEvento> evt = cid.join("cidEventoCollection");
         cq.multiselect(cid.get("ra"),
               cid.get("nome"),
               cid.get("postoGraduacao"),
               cid.get("qm"),
               cid.get("cidHabilitacaoCollection").as(List.class),
               cid.get("om"),
               cid.get("endereco"), 
               cid.get("bairro"),
               cid.get("cep"),
               cid.get("municipioResidencia"),
               cid.get("email"),
               cid.get("telefone"),
               evt.get("pk").get("data"),
               cid.get("mobSituacao"),
               cid.get("mobDestino"));
         Predicate condicao = cb.equal(evt.get("pk").get("codigo"), 12);
         if (cidadao.getMobDestino() != null) {
            condicao = cb.and(condicao,cb.equal(cid.<String>get("mobDestino"), cidadao.getMobDestino()));
         }
         if (cidadao.getMunicipioResidencia().getCodigo() != null) {
            condicao = cb.and(condicao, cb.equal(cid.<String>get("municipioResidencia"), cidadao.getMunicipioResidencia()));
         }
         if (cidadao.getPostoGraduacao().getCodigo() != null) {
            condicao = cb.and(condicao, cb.equal(cid.<String>get("postoGraduacao"), cidadao.getPostoGraduacao()));
         }
         if (cidadao.getQm().getCodigo() != null) {
            condicao = cb.and(condicao, cb.equal(cid.<String>get("qm"), cidadao.getQm()));
         }
         if (!dataLic.equals("")) {
            condicao = cb.and(condicao, cb.equal(cid.<String>get("cidEventoCollection.pk.codigo"), 12));
         }
         cq.where(condicao);
         cq.orderBy(cb.asc(cid.<String>get("nome")));
         final List<Object[]> lista = em.createQuery(cq).getResultList();
         return lista;
      } catch (Exception e) {
         throw new SermilException(e);
      }
   }

}
