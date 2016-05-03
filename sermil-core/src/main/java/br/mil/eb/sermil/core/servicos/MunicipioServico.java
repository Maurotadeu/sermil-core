package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

/** Gerenciamento de informações de Municípios. (Tabela MUNICIPIO)
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
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

  public Municipio recuperar(final Integer codigo) throws SermilException {
    return this.municipioDao.findById(codigo);
  }

  @Transactional
  public Municipio salvar(Municipio municipio) throws SermilException {
    return this.municipioDao.save(municipio);
  }

  @RemoteMethod
  public Object[] listarAlistadoMunicipio(final Integer ano, final String uf) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("SELECT m.descricao, m.latitude, m.longitude, m.microregiao, m.mesoregiao, COUNT(*) FROM municipio_novo m JOIN cidadao c ON c.municipio_residencia_codigo = m.codigo WHERE vinculacao_ano = ? AND uf_sigla = ? GROUP BY m.descricao, m.latitude, m.longitude, m.microregiao, m.mesoregiao", ano == null ? Calendar.getInstance().get(Calendar.YEAR) : ano, uf);
    return result.toArray(new Object[0]);
  }

  @RemoteMethod
  public Object[] listarAlistadoMesoregiao(final Integer ano) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("SELECT m.mesoregiao mesoregiao, COUNT(*) total FROM cidadao c JOIN municipio_novo m ON c.municipio_residencia_codigo = m.codigo WHERE c.vinculacao_ano = ? GROUP BY m.mesoregiao order by 2 desc", ano == null ? Calendar.getInstance().get(Calendar.YEAR) : ano);
    return result.toArray(new Object[0]);
  }

  @RemoteMethod
  public Object[] listarAlistadoMicroregiao(final Integer ano) throws SermilException {
    final List<Object[]> result = this.municipioDao.findBySQL("SELECT m.microregiao microregiao, COUNT(*) total FROM cidadao c JOIN municipio_novo m ON c.municipio_residencia_codigo = m.codigo WHERE c.vinculacao_ano = ? GROUP BY m.microregiao order by 2 desc", ano == null ? Calendar.getInstance().get(Calendar.YEAR) : ano);
    return result.toArray(new Object[0]);
  }
  
  @RemoteMethod
  public List<Object[]> listarPorDescricao(final String descricao) throws SermilException {
    if (descricao == null || descricao.isEmpty()) {
      throw new CriterioException("Informe o nome do município para pesquisar.");
    }
    return this.municipioDao.findByNamedQueryArray("Municipio.listarPorDescricao", descricao);
  }

  @RemoteMethod
  public Object[] listarJsm(final Integer mun) throws SermilException {
    final List<Object[]> result = this.jsmDao.findBySQL("SELECT csm_codigo, codigo, descricao FROM jsm WHERE municipio_codigo = ?", mun);
    // Ordena pela descrição da JSM
    Collections.sort(result, new Comparator<Object[]>() {
       @Override
       public int compare(final Object[] obj1, final Object[] obj2) {
           return ((String)obj1[2]).compareTo((String)obj2[2]);
       }
      } );
    return result.toArray(new Object[0]);
  }
  
}
