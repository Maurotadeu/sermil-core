package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.QmDao;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Qm;

/** Serviços de QM.
 * @author Abreu Lopes
 * @since 5.0
 * @version $Id: OmServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("qmServico")
@RemoteProxy(name="qmServico")
public class QmServico {

  protected static final Logger logger = LoggerFactory.getLogger(QmServico.class);

  @Inject
  private QmDao qmDao;

  public QmServico() {
    logger.debug("QmServico iniciado");
  }

  public List<Qm> listar() throws SermilException {
    final List<Qm> lista = this.qmDao.findAll();
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  public List<Qm> listar(String tipo) throws SermilException {
    final List<Qm> lista = this.qmDao.findByNamedQuery(tipo);
    logger.debug("QM:{}", lista);
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }
  
  @RemoteMethod
  public Qm[] listarQm() throws SermilException {
    return this.listar().toArray(new Qm[0]);
  }

	public Qm recuperar(String id) throws SermilException {
	  return this.qmDao.findById(id);
  }

}
