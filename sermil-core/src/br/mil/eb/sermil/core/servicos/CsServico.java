package br.mil.eb.sermil.core.servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.exceptions.CsPersistErrorException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Csel;

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
  
  public List<Csel> listarPorRM(Byte rm_codigo){
     return (List<Csel>) dao.findByNamedQuery("Csel.listarPorRM", rm_codigo );
  }
  
  public List<Csel> ListarPorNome (String nome){
     return (List<Csel>) dao.findByNamedQuery("Csel.listarPorNome", nome);
  }
  
  public void persistir(Csel cs) throws CsPersistErrorException{
     try {
      dao.save(cs);
   } catch (SermilException e) {
      logger.error(e.getMessage());
      throw new CsPersistErrorException();
   }
  }
  
  public Csel recuperar(Integer cs_codigo){
     return dao.findById(cs_codigo);
  }

  

  public Map<String, String> getTributacoes() {
     HashMap<String, String> tributacoes = new HashMap<String, String>();
     tributacoes.put(Csel.TRIBUTACAO_EB, Csel.TRIBUTACAO_EB);
     tributacoes.put(Csel.TRIBUTACAO_FAB, Csel.TRIBUTACAO_FAB);
     tributacoes.put(Csel.TRIBUTACAO_MAR, Csel.TRIBUTACAO_MAR);
     tributacoes.put(Csel.TRIBUTACAO_TG, Csel.TRIBUTACAO_TG);
     return tributacoes;
  }
  
}
