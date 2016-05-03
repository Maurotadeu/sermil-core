package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CepDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cep;

/** Serviço de informações de CEP. (View CEP)
 * @author Abreu Lopes
 * @since 5.1
 * @version 5.4
 */
@Named("cepServico")
@RemoteProxy(name="cepServico")
public class CepServico {

  protected static final Logger logger = LoggerFactory.getLogger(CepServico.class);

  @Inject
  private CepDao cepDao;

  public CepServico() {
    logger.debug("CepServico iniciado");
  }

  @RemoteMethod
  public Cep recuperar(final String codigo) throws SermilException {
    return this.cepDao.findById(codigo);
  }

}
