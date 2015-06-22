package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidFotoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidFoto;

/** Serviço de Foto de Cidadão.
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: CidFotoServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("cidFotoServico")
public class CidFotoServico {

  protected static final Logger logger = LoggerFactory.getLogger(CidFotoServico.class);

  @Inject
  private CidFotoDao cidFotoDao;

  public CidFotoServico() {
    logger.debug("CidFotoServico iniciado");
  }

  public CidFoto obter(final Long ra) throws SermilException {
    return this.cidFotoDao.findById(ra);
  }

  @Transactional
  public void excluir(final CidFoto foto) throws SermilException {
    this.cidFotoDao.delete(foto);
  }

  @Transactional
  public void salvar(final CidFoto foto) throws SermilException {
    this.cidFotoDao.save(foto);
  }

}
