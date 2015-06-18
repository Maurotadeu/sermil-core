package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.HabilitacaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Habilitacao;

/** Serviço de Habilitação Militar.
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: HabilitacaoServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("habilitacaoServico")
@RemoteProxy(name="habilitacaoServico")
public class HabilitacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(HabilitacaoServico.class);

  @Inject
  private HabilitacaoDao habilitacaoDao;
  
  public HabilitacaoServico() {
    logger.debug("HabilitacaoServico iniciado");
  }
  
  public List<Habilitacao> listar() {
    return this.habilitacaoDao.findAll();
  }

  @RemoteMethod
  public Habilitacao[] listarHabilitacao() throws SermilException {
    List<Habilitacao> lista = this.listar();
    return lista.toArray(new Habilitacao[0]);
  }
  
  public Habilitacao recuperar(String codigo) throws SermilException {
    return this.habilitacaoDao.findById(codigo);
  }

}
