package br.mil.eb.sermil.core.servicos;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.OcupacaoDao;
import br.mil.eb.sermil.modelo.Ocupacao;

/** Serviço de informações de ocupações. (Tabela OCUPACAO)
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: OcupacaoServico.java 2467 2014-06-12 14:17:52Z wlopes $
 */
@Named("ocupacaoServico")
@RemoteProxy(name="ocupacaoServico")
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

  @RemoteMethod
  public Ocupacao[] listarOcupacoes() {
    return this.listar().toArray(new Ocupacao[0]);
  }

}
