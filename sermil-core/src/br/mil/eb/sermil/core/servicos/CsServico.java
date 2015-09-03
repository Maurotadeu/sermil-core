package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.modelo.CS;

/** Serviço de informações de CEP. (View CEP)
 * @author Abreu Lopes
 * @since 5.1
 * @version $Id$
 */
@Named("csServico")
@RemoteProxy(name="csServico")
public class CsServico {

  protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

  @Inject
  private CsDao dao;

  public CsServico() {
    logger.debug("CsServico iniciado");
  }
  
  public List<CS> listarPorRM(Byte rm_codigo){
     return (List<CS>) dao.findByNamedQuery("cs.listarPorRM", rm_codigo );
  }
  
  public List<CS> ListarPorNome (String nome){
     return (List<CS>) dao.findByNamedQuery("cs.listarPorNome", nome);
  }

}
