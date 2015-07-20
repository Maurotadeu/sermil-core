package br.mil.eb.sermil.core.servicos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.dao.MunicipioDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Municipio;

/** Serviço de informações de Município. (Tabela MUNICIPIO)
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: MunicipioServico.java 2428 2014-05-15 13:23:47Z wlopes $
 */
@Named("municipioServico")
@RemoteProxy(name="municipioServico")
public class MunicipioServico {

  protected static final Logger logger = LoggerFactory.getLogger(MunicipioServico.class);

  @Inject
  private MunicipioDao municipioDao;

  @Inject
  private JsmDao jsmDao;

  public MunicipioServico() {
    logger.debug("MunicipioServico iniciado");
  }

  public List<Municipio> listar(final Municipio mun) throws SermilException {
    if (mun == null || (mun.getCodigo() == null && mun.getUf() == null && mun.getDescricao() == null)) {
      throw new CriterioException();
    }
    List<Municipio> lista = null;
    if (mun.getCodigo() != null) {
      lista = new ArrayList<Municipio>(1);
      lista.add(this.municipioDao.findById(mun.getCodigo()));
    } else if (mun.getUf() != null && !mun.getUf().getSigla().isEmpty()) {
      lista = this.municipioDao.findByNamedQuery("Municipio.listarPorUf", mun.getUf().getSigla());
    } else if (mun.getDescricao() != null) {
      lista = this.municipioDao.findByNamedQuery("Municipio.listarPorDescricao", mun.getDescricao());
    }
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  @RemoteMethod
  public Object[] listarJsm(final Integer mun) throws SermilException {
    final List<Object[]> result = this.jsmDao.findBySQL("SELECT csm_codigo, codigo, descricao FROM jsm WHERE municipio_codigo = ?", mun);
    return result.toArray(new Object[0]);
  }
  
  @RemoteMethod
  public Object[] listarMun(final String uf) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("SELECT m.descricao, m.latitude, m.longitude, m.microregiao, m.mesoregiao, count(*) FROM municipio_novo m JOIN cidadao c ON c.municipio_residencia_codigo = m.codigo WHERE VINCULACAO_ANO = 2015 AND uf_sigla = ? GROUP BY m.descricao, m.latitude, m.longitude, m.microregiao, m.mesoregiao", uf);
    return result.toArray(new Object[0]);
  }

  @RemoteMethod
  public Object[] listarMesoregiao(final Integer ano) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("select m.mesoregiao mesoregiao, count(*) total from cidadao c join municipio_novo m on c.municipio_residencia_codigo = m.codigo where c.vinculacao_ano = ? group by m.mesoregiao order by 2 desc", ano);
    return result.toArray(new Object[0]);
  }

  @RemoteMethod
  public Object[] listarMicroregiao(final Integer ano) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("select m.microregiao microregiao, count(*) total from cidadao c join municipio_novo m on c.municipio_residencia_codigo = m.codigo where c.vinculacao_ano = ? group by m.microregiao order by 2 desc", ano);
    return result.toArray(new Object[0]);
  }
  
  @RemoteMethod
  public Municipio[] listarPorUf(final String uf) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("SELECT codigo, descricao FROM municipio WHERE uf_sigla = ? ORDER BY descricao", uf);
    final List<Municipio> lista = new ArrayList<>(result.size());
    for(Object[] o: result){
      final Municipio mun = new Municipio();
      mun.setCodigo(((BigDecimal)o[0]).intValue());
      mun.setDescricao((String) o[1]);
      lista.add(mun);
    }
    return lista.toArray(new Municipio[0]);
  }

  /** Listagem de município por descrição, exibindo RM e CSM.
   * @param descricao nome do município
   * @return lista de Municípios
   * @throws SermilException erro no processamento
   */
  @RemoteMethod
  public List<Object[]> listarPorDescricao(final String descricao) throws SermilException {
    if (descricao == null || descricao.isEmpty()) {
      throw new CriterioException("Informe o nome do município para pesquisar.");
    }
    return this.municipioDao.findByNamedQueryArray("Municipio.listarPorDescricao", descricao);
  }

	public Municipio recuperar(final Integer codigo) throws SermilException {
	  return this.municipioDao.findById(codigo);
  }

	@Transactional
	public Municipio salvar(Municipio municipio) throws SermilException {
	  return this.municipioDao.save(municipio);
  }

}
