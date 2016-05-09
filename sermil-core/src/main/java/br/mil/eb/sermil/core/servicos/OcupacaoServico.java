package br.mil.eb.sermil.core.servicos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.OcupacaoDao;
import br.mil.eb.sermil.modelo.Ocupacao;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de informações de ocupações. (Tabela OCUPACAO)
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Named("ocupacaoServico")
public class OcupacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(OcupacaoServico.class);

  @Inject
  private OcupacaoDao ocupacaoDao;

  public OcupacaoServico() {
    logger.debug("OcupacaoServico iniciado");
  }

  public List<Ocupacao> listar() {
    final List<Ocupacao> lista = this.ocupacaoDao.findAll();
    Collections.sort(lista);
    return lista;
  }

  public List<Ocupacao> listarPorDescricao(final String descricao) {
    return this.ocupacaoDao.findByNamedQuery("Ocupacao.listarPorDescricao", descricao.trim());
  }

  public List<Ocupacao> listarPorOrdem() {
    return this.ocupacaoDao.findByNamedQuery("Ocupacao.listarPorOrdem");
  }

  public Lista[] listarOcupacoes() {
    final TypedQuery<Object[]> query = this.ocupacaoDao.getEntityManager().createNamedQuery("Ocupacao.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

}
