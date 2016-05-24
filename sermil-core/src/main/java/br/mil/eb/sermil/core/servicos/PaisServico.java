package br.mil.eb.sermil.core.servicos;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.PaisDao;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de informações de pais. (Tabela PAIS)
 * @author Abreu Lopes
 * @since 5.4
 * @version 5.4
 */
@Named("paisServico")
public class PaisServico {

  protected static final Logger logger = LoggerFactory.getLogger(PaisServico.class);

  @Inject
  private PaisDao paisDao;

  public PaisServico() {
    logger.debug("PaisServico iniciado");
  }

  public Lista[] listarPaises() {
    final TypedQuery<Object[]> query = this.paisDao.getEntityManager().createNamedQuery("Pais.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista(((Short)o[0]).toString(), (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

}
