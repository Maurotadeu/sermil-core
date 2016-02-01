package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidContatoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.servicos.HabilitacaoServico;
import br.mil.eb.sermil.modelo.CidContato;

/** Contatos do cidadão para mobilização.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidContatoServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("cidContatoServico")
public class CidContatoServico {

  protected static final Logger logger = LoggerFactory.getLogger(HabilitacaoServico.class);

  @Inject
  private CidContatoDao cidContatoDao;

  public CidContatoServico() {
    logger.debug("CidContatoServico iniciado");
  }

	public CidContato recuperar(final CidContato.PK id) throws SermilException {
	  return this.cidContatoDao.findById(id);
  }

}
