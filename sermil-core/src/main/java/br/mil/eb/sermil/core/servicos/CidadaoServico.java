package br.mil.eb.sermil.core.servicos;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemDocApresException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoEvento;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Gerenciamento de informações de Cidadão.
 * @author Abreu Lopes, Anselmo Ribeiro
 * @since 3.0
 * @version 5.3.1
 */
@Named("cidadaoServico")
public class CidadaoServico {

   protected static final Logger logger = LoggerFactory.getLogger(CidadaoServico.class);

   @Inject
   private CidadaoDao cidadaoDao;

   @Inject
   private CidAuditoriaDao cidAuditoriaDao;

   public CidadaoServico() {
      logger.debug("CidadaoServico iniciado");
   }

   public boolean isCadastrado(final Cidadao cidadao) throws SermilException {
      if (cidadao == null || StringUtils.isBlank(cidadao.getNome())) {
         throw new CriterioException("Informe o nome, nome da mãe e data de nascimento do cidadão a ser pesquisado.");
      }
      boolean status = false;
      if (!this.cidadaoDao.findByNamedQuery("Cidadao.listarUnico", cidadao.getNome(), cidadao.getMae(), cidadao.getNascimentoData()).isEmpty()) {
         status = true;
      }
      return status;
   }

   public boolean isCPFCadastrado(final String cpf) throws SermilException {
      try {
        this.recuperar(cpf);
        return true;
      } catch (CidadaoNotFoundException e) {
         return false;
      }
   }

   @PreAuthorize("hasAnyRole('adm','dsm')")
   @Transactional
   public String excluir(final Long ra, final Usuario usr) throws SermilException {
      if (ra == null || usr == null) {
         throw new SermilException("Exclusão de cidadão: informe o RA do cidadão a ser excluído e o usuário responsável.");
      }
      final Cidadao c = this.cidadaoDao.findById(ra);
      this.cidadaoDao.delete(c);
      this.cidAuditoriaDao.save(new CidAuditoria(ra, new Date(), "Exclusão de RA", usr.getAcessoIp(), usr.getCpf()));
      return new StringBuilder("Cidadão excluído: ").append(ra).toString();
   }

   public Cidadao recuperar(final Long ra) throws SermilException {
      if (ra == null) {
         throw new CriterioException("Informe o RA do cidadão a ser pesquisado.");
      }
      logger.debug("Critério: RA {}", ra);
      final Cidadao cid = this.cidadaoDao.findById(ra);
      if (cid == null) {
         throw new CidadaoNotFoundException();
      }
      logger.debug("Resultado: {}", cid);
      return cid;
   }

   public Cidadao recuperar(final String cpf) throws SermilException {
      if (StringUtils.isBlank(cpf)) {
         throw new CriterioException("Informe o CPF do cidadão a ser pesquisado.");
      }
      logger.debug("Critério: CPF {}", cpf);
      final List<Cidadao> lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", cpf);
      logger.debug("Resultado: {}", lista);
      if (lista == null || lista.isEmpty()) {
         throw new CidadaoNotFoundException();
      }
      return lista.get(0);
   }

   @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm','mob','om','smr','cs')")
   @Transactional
   public Cidadao salvar(final Cidadao cid, final Usuario usr, final String msg) throws SermilException {
      final CidAuditoria aud = new CidAuditoria(cid.getRa(), new Date(), msg.substring(0, msg.length() > 500 ? 500 : msg.length()), usr.getAcessoIp(), usr.getCpf());
      cid.addCidAuditoria(aud);
      logger.debug("Cidadão salvo: {}", cid);
      return this.cidadaoDao.save(cid);
   }

   public Map<String, String> listarAtributos() throws SermilException {
      final List<Object[]> cols = this.cidadaoDao.findBySQL("SELECT COLUMN_NAME, SUBSTR(COMMENTS,1,40) FROM all_col_comments WHERE table_name = 'CIDADAO' ORDER BY 2");
      final Map<String, String> res = new TreeMap<String, String>();
      for (Object[] item : cols) {
         res.put((String) item[0], (String) item[1]);
      }
      return res;
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','cs','csm','del','om','mob')")
   public List<CidAuditoria> listarAuditoria(final Long ra) throws SermilException {
      if (ra == null) {
         throw new CriterioException("Informe o RA do cidadão a ser pesquisado.");
      }
      logger.debug("Critério: RA {}", ra);
      final List<CidAuditoria> lista = this.cidAuditoriaDao.findByNamedQuery("CidAuditoria.listarPorRa", ra);
      logger.debug("Resultado: {}", lista);
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md','cs','convidado')")
   public List<Object[]> listar(final Cidadao cidadao) throws SermilException {
      if ((cidadao == null) ||
            (cidadao.getRa() == null &&
            cidadao.getNascimentoData() == null &&
            StringUtils.isBlank(cidadao.getNome()) &&
            StringUtils.isBlank(cidadao.getMae()) &&
            StringUtils.isBlank(cidadao.getCpf()) &&
            StringUtils.isBlank(cidadao.getIdtMilitar()))) {
         throw new CriterioException();
      }
      final CriteriaBuilder builder = this.cidadaoDao.getEntityManager().getCriteriaBuilder();
      final CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
      final Root<Cidadao> root = query.from(Cidadao.class);
      query.multiselect(root.<String> get("ra"), root.<String> get("nome"), root.<String> get("mae"), root.<String> get("nascimentoData"));
      Predicate condicao = null;
      if (cidadao.getRa() != null) {
         condicao = builder.equal(root.<String> get("ra"), cidadao.getRa());
      } else if (cidadao.getCpf() != null) {
         condicao = builder.equal(root.<String> get("cpf"), cidadao.getCpf());
      } else if (cidadao.getIdtMilitar() != null) {
         condicao = builder.equal(root.<String> get("idtMilitar"), cidadao.getIdtMilitar());
      } else {
         if (cidadao.getNome() != null) {
            condicao = builder.like(root.<String> get("nome"), cidadao.getNome().concat("%"));
         }
         if (cidadao.getMae() != null) {
            if (condicao == null) {
               condicao = builder.like(root.<String> get("mae"), cidadao.getMae().concat("%"));
            } else {
               condicao = builder.and(condicao, builder.like(root.<String> get("mae"), cidadao.getMae().concat("%")));
            }
         }
         if (cidadao.getNascimentoData() != null) {
            if (condicao == null) {
               condicao = builder.equal(root.get("nascimentoData"), cidadao.getNascimentoData());
            } else {
               condicao = builder.and(condicao, builder.equal(root.get("nascimentoData"), cidadao.getNascimentoData()));
            }
         }
      }
      query.where(condicao);
      final List<Object[]> lista = this.cidadaoDao.getEntityManager().createQuery(query).getResultList();
      if (lista == null || lista.isEmpty()) {
         throw new CidadaoNotFoundException();
      }
      return lista;
   }

   public boolean podeImprimirCertSitMilitar(final Long ra) throws SermilException {
      /* Recuperar cidadão */
      if (ra == null) {
         throw new CriterioException("Informe o RA do cidadão a ser verificado.");
      }
      final Cidadao cid = this.recuperar(ra);
      /* REGRAS DE NEGOCIO */
      if (cid.getSituacaoMilitar() != TipoSituacaoMilitar.LICENCIADO.ordinal()) {
         throw new SermilException("Cidadão não está na situação LICENCIADO (15).");
      }
      // Tem que ter evento licenciamento
      if (!cid.hasEvento(TipoEvento.LICENCIAMENTO.ordinal())) {
         throw new CidadaoNaoTemEventoException();
      }
      // Pelo menos um documento apresentado.
      if (cid.getCidDocApresColletion().size() <= 0) {
         throw new CidadaoNaoTemDocApresException();
      }
      return true;
   }

}
