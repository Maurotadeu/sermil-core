package br.mil.eb.sermil.core.servicos;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.DominiosDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Dominios;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço para verificação dos domínios. (TABELA DOMINIOS)
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
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
    return this.listar(id).stream().filter(d -> d.getValor().equals(valor)).map(d -> d.getDescricao()).collect(Collectors.toList()).get(0);
    /*
    String descricao = "NI";
    for (Dominios dom: this.listar(id)) {
      if (dom.getValor().equals(valor)) {
        descricao = dom.getDescricao();
      }
    }
    return descricao;
    */
  }

  @RemoteMethod
  public List<Dominios> listar(final Integer id) throws SermilException {
    return this.dao.findByNamedQuery("Dominios.listarPorId", id);
  }

  @RemoteMethod
  public Lista[] listar() throws SermilException {
    final TypedQuery<Object[]> query = this.dao.getEntityManager().createNamedQuery("Dominios.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista(((Integer) o[0]).toString(), (String) o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }
  
}
