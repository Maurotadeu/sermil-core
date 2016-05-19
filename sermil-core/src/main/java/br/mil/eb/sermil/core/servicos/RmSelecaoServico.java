package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.SelJsmDao;
import br.mil.eb.sermil.core.dao.SelTributacaoDao;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.SelJsm;
import br.mil.eb.sermil.modelo.SelTributacao;

/** Serviços de Pré-seleção (Tabelas SEL_JSM e SEL_TRIBUTACAO).
 * @author Abreu Lopes
 * @since 3.5
 * @version 5.4
 */
@Named("rmSelecaoServico")
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
  public List<SelJsm> listarJsm(final Byte rm) throws SermilException {
    return this.selJsmDao.findByNamedQuery("SelJsm.listarJsmTributaria", rm);
  }


  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public String salvarParamSelecao(final List<SelJsm> paramSelecao, final Byte rm) throws SermilException {
    if (paramSelecao == null || paramSelecao.isEmpty()) {
      throw new NoDataFoundException("Não há informações a serem processadas.");
    }
    final List<SelJsm> lista = this.listarJsm(rm);
    for (SelJsm paramJsm: paramSelecao) {
      final SelJsm paramFinal = lista.get(lista.indexOf(paramJsm));
      paramFinal.setAptos(paramJsm.getAptos() == null ? 0 : paramJsm.getAptos());
      paramFinal.setDispensaEscolaridade(paramJsm.getDispensaEscolaridade());
      this.selJsmDao.save(paramFinal);
    }
    return "Parametros salvos";
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
