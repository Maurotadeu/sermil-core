package br.mil.eb.sermil.core.servicos;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidDocumento;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoEvento;

/** Gerenciamento de informações de Cidadão.
 * @author Abreu Lopes, Anselmo Ribeiro
 * @since 3.0
 * @version 5.3.2
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

  public Cidadao recuperar(final Long ra) throws CriterioException, CidadaoNotFoundException  {
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

  public Cidadao recuperar(final String cpf) throws CriterioException, CidadaoNotFoundException  {
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
    final Cidadao c = this.cidadaoDao.save(cid);
    logger.debug("{}: salvo", c);
    return c;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','cs')")
  @Transactional
  public Cidadao salvarSelecao(final Cidadao cidadao, final Usuario usr) throws SermilException {
    final Cidadao cidBd = this.recuperar(cidadao.getRa());
    if (cidadao.getDiagnostico() != null) {
      cidBd.setSituacaoMilitar(cidadao.getDiagnostico() == 1 ? 4 : 5);
    }
    cidBd.setDispensa(cidadao.getDispensa());
    cidBd.setAnotacoes(cidadao.getAnotacoes());
    cidBd.setCs(cidadao.getCs());
    cidBd.setFsNr(cidadao.getFsNr());
    cidBd.setDiagnostico(cidadao.getDiagnostico());
    cidBd.setCid(cidadao.getCid());
    cidBd.setMedicoCrm(cidadao.getMedicoCrm());
    cidBd.setAcuidadeVisual(cidadao.getAcuidadeVisual());
    cidBd.setAcuidadeAuditiva(cidadao.getAcuidadeAuditiva());
    cidBd.setExpressaoOral(cidadao.getExpressaoOral());
    cidBd.setTsiP(cidadao.getTsiP());
    cidBd.setTsiI(cidadao.getTsiI());
    cidBd.setPadraoPq1Codigo(cidadao.getPadraoPq1Codigo());
    cidBd.setPadraoPq2Codigo(cidadao.getPadraoPq2Codigo());
    cidBd.setDesejaServir(cidadao.getDesejaServir());
    cidBd.setSabeNadar(cidadao.getSabeNadar());
    cidBd.setCseIndicacao(cidadao.getCseIndicacao());
    cidBd.setCseResultado(cidadao.getCseResultado());
    cidBd.setAltura(cidadao.getAltura());
    cidBd.setForcaMuscular(cidadao.getForcaMuscular());
    cidBd.setPeso(cidadao.getPeso());
    cidBd.setCabeca(cidadao.getCabeca());
    cidBd.setCintura(cidadao.getCintura());
    cidBd.setCalcado(cidadao.getCalcado());
    cidBd.setUniforme(cidadao.getUniforme());

    final Calendar dataAtual = Calendar.getInstance();

    // Documento do sistema (FS)
    final CidDocumento cd = new CidDocumento(cidBd.getRa(), dataAtual.getTime(), Byte.parseByte("2"));
    cd.setServico(new StringBuilder("2").append(new DecimalFormat("00").format(cidBd.getJsm().getCsm().getRm().getCodigo())).append(new SimpleDateFormat("yy").format(dataAtual.getTime())).append(new DecimalFormat("000").format(cidBd.getCs().getCodigo())).append("888").toString());
    cd.setTarefa(Short.parseShort("0"));
    cd.setDocumento(Byte.parseByte("0"));
    try {
      cidBd.addCidDocumento(cd);
    } catch(SermilException e) {
      cidBd.getCidDocumentoCollection().remove(cidBd.getCidDocumentoCollection().indexOf(cd));
      cidBd.addCidDocumento(cd);
    }

    // Evento de alistamento
    final CidEvento ce = new CidEvento(cidBd.getRa(), TipoEvento.SELECAO.getCodigo(), dataAtual.getTime());
    ce.setAnotacao("Seleção " + dataAtual.get(Calendar.YEAR));
    try {
      cidBd.addCidEvento(ce);
    } catch(SermilException e) {
      cidBd.getCidEventoCollection().remove(cidBd.getCidEventoCollection().indexOf(ce));
      cidBd.addCidEvento(ce);
    }
    return this.salvar(cidBd, usr, "SELECAO " + dataAtual.get(Calendar.YEAR));
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

}
