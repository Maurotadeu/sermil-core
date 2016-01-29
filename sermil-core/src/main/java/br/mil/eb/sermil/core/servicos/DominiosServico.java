package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.DominiosDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Dominios;

/** Serviço para verificação dos domínios. (TABELA DOMINIOS)
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: DominiosServico.java 2428 2014-05-15 13:23:47Z wlopes $
 */
@Named("dominiosServico")
@RemoteProxy(name="dominiosServico")
public final class DominiosServico {

  protected static final Logger logger = LoggerFactory.getLogger(DominiosServico.class);

  @Inject
  private DominiosDao dao;
  
  public DominiosServico() {
    logger.debug("DominiosServico iniciado");
  }
  
  @RemoteMethod
  public String getDescricao(final Integer id, final Short valor) throws SermilException {
    String descricao = "NI";
    for (Dominios dom: this.listar(id)) {
      if (dom.getValor().equals(valor)) {
        descricao = dom.getDescricao();
      }
    }
    return descricao;
  }

  @RemoteMethod
  public List<Dominios> listar(final Integer id) throws SermilException {
    return dao.findByNamedQuery("Dominios.listarPorId", id);
  }

}
