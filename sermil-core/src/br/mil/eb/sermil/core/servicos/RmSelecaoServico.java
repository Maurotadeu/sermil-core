package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.SelJsmDao;
import br.mil.eb.sermil.core.dao.SelTributacaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.SelJsm;
import br.mil.eb.sermil.modelo.SelTributacao;

/** Serviços de Pré-seleção (Tabelas SEL_JSM e SEL_TRIBUTACAO).
 * @author Abreu Lopes
 * @since 3.5
 * @version $Id: RmSelecaoServico.java 2458 2014-06-06 11:41:40Z wlopes $
 */
@Named("rmSelecaoServico")
@RemoteProxy(name="selecaoServico")
public class RmSelecaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(RmSelecaoServico.class);

  @Inject
  private SelJsmDao selJsmDao;

  @Inject
  private SelTributacaoDao selTributacaoDao;

  public RmSelecaoServico() {
    logger.debug("RmSelecaoServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @RemoteMethod
  public Object[] listarCs(final Byte rm){
    final List<Object[]> lista = selJsmDao.findBySQL("SELECT distinct(cs) FROM CS where rm = ? order by cs", rm);
    return lista.toArray();
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public List<SelJsm> listarJsm(Byte rm) throws SermilException {
    return this.selJsmDao.findByNamedQuery("SelJsm.listarJsmTributaria", rm);
  }


  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public List<SelJsm> salvarPreSelecao(final List<SelJsm> preSelecao) throws SermilException {
    final List<SelJsm> salvo = new ArrayList<SelJsm>();
    for (SelJsm dispensa: preSelecao) {
      if (dispensa.getAptos() == null) {
        dispensa.setAptos(Short.valueOf("0"));
      }
      salvo.add(this.selJsmDao.save(dispensa));
    }
    return salvo;
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public String executarPreSelecao(final Byte rm, final String cpf) throws SermilException {
    final TypedQuery<String> query = this.selJsmDao.getEntityManager().createNamedQuery("SelJsm.executa", String.class);
    query.setParameter("P_RM", rm);
    query.setParameter("P_CPF", cpf);
    return query.getSingleResult();
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public String reverterPreSelecao(final Byte rm) throws SermilException {
    final TypedQuery<String> query = this.selJsmDao.getEntityManager().createNamedQuery("SelJsm.reverte", String.class);
    query.setParameter("P_RM", rm);
    return query.getSingleResult();
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public void salvarTributacao(final List<SelTributacao> tributacoes, final Byte rm) throws SermilException {
    for (SelTributacao tributacao : tributacoes) {
      if(tributacao.getPk().getCsmCodigo() != null && tributacao.getPk().getJsmCodigo() != null){
        tributacao.setAprovado(Byte.valueOf("1")); // 0 = Nao , 1 = RM aprovou e 2= DSM aprovou
        tributacao.getRm().setCodigo(rm);
        this.selTributacaoDao.save(tributacao);
      }
    }
  }

}
