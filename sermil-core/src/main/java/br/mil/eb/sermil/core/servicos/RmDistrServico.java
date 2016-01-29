package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.DstbParametroDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.DstbParametro;

/** Serviço de processamento da distribuição através de chamadas das packages no BD oracle.
 * @author Abreu Lopes
 * @since 4.5
 * @version $Id: RmDistrServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("rmDistrServico")
public class RmDistrServico {

  protected static final Logger logger = LoggerFactory.getLogger(RmDistrServico.class);

  @Inject
	private DstbParametroDao parametroDao;

	public RmDistrServico() {
	  logger.debug("RmDistrServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
	public DstbParametro recuperarParametros(final DstbParametro parametro) throws SermilException {
    if (parametro == null || parametro.getPk() == null) {
      throw new SermilException("Informe a RM e o tipo de OM");
    } else {
      return this.parametroDao.findById(parametro.getPk());
    }
  }

	@Transactional
  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
	public void salvarParametros(final DstbParametro parametro) throws SermilException {
		this.parametroDao.save(parametro);
  }	
	
  @Transactional
  @PreAuthorize("hasRole('adm')")
  public String prepara(final Byte rm, final String cpf) throws SermilException {
    final TypedQuery<String> query = this.parametroDao.getEntityManager().createNamedQuery("Distribuicao.prepara", String.class);
    query.setParameter("P_RM", rm);
    query.setParameter("P_CPF", cpf);
    return query.getSingleResult();
  }

  @Transactional
  @PreAuthorize("hasRole('adm')")
  public String executa(final Byte rm, final String cpf) throws SermilException {
    final TypedQuery<String> query = this.parametroDao.getEntityManager().createNamedQuery("Distribuicao.executa", String.class);
    query.setParameter("P_RM", rm);
    query.setParameter("P_CPF", cpf);
    return query.getSingleResult();
  }

  @PreAuthorize("hasRole('adm')")
  public String verifica(final Byte rm) throws SermilException {
   final TypedQuery<String> query = this.parametroDao.getEntityManager().createNamedQuery("Distribuicao.verifica", String.class);
   query.setParameter("P_RM", rm);
   return query.getSingleResult();
  }

  @Transactional
  @PreAuthorize("hasRole('adm')")
  public String reverte(final Byte rm, final String cpf) throws SermilException {
    final TypedQuery<String> query = this.parametroDao.getEntityManager().createNamedQuery("Distribuicao.reverte", String.class);
    query.setParameter("P_RM", rm);
    query.setParameter("P_CPF", cpf);
    return query.getSingleResult();
  }
	
  @Transactional
  @PreAuthorize("hasRole('adm')")
  public String finaliza(final Byte rm, final String cpf) throws SermilException {
    final TypedQuery<String> query = this.parametroDao.getEntityManager().createNamedQuery("Distribuicao.finaliza", String.class);
    query.setParameter("P_RM", rm);
    query.setParameter("P_CPF", cpf);
    return query.getSingleResult();
  }

}
