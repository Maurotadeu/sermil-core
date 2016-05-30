package br.mil.eb.sermil.core.servicos;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.HabilitacaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Habilitacao;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de Habilitação Militar.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
 */
@Named("habilitacaoServico")
public class HabilitacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(HabilitacaoServico.class);

  @Inject
  private HabilitacaoDao habDao;
  
  public HabilitacaoServico() {
    logger.debug("HabilitacaoServico iniciado");
  }
  
  public List<Habilitacao> listar() {
    return this.habDao.findAll();
  }

  public Lista[] listarHabilitacoes() throws SermilException {
    final TypedQuery<Object[]> query = this.habDao.getEntityManager().createNamedQuery("Habilitacao.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }
  
  public Habilitacao recuperar(String codigo) throws SermilException {
    return this.habDao.findById(codigo);
  }

}
