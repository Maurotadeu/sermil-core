package br.mil.eb.sermil.core.servicos;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.QmDao;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Qm;
import br.mil.eb.sermil.tipos.Lista;

/** Serviços de QM.
 * @author Abreu Lopes
 * @since 5.0
 * @version 5.4
 */
@Named("qmServico")
public class QmServico {

  protected static final Logger logger = LoggerFactory.getLogger(QmServico.class);

  @Inject
  private QmDao qmDao;

  public QmServico() {
    logger.debug("QmServico iniciado");
  }

  public List<Qm> listar(String tipo) throws SermilException {
    final List<Qm> lista = this.qmDao.findByNamedQuery(tipo);
    logger.debug("QM:{}", lista);
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }
  
  public Lista[] listarQm() throws SermilException {
    final TypedQuery<Object[]> query = this.qmDao.getEntityManager().createNamedQuery("Qm.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

	public Qm recuperar(String id) throws SermilException {
	  return this.qmDao.findById(id);
  }

}
