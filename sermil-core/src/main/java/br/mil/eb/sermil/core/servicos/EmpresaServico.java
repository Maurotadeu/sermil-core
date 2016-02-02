package br.mil.eb.sermil.core.servicos;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.EmpresaDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Empresa;

/** Serviço de Empresas de Segurança Nacional (EDRSN).
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: EmpresaServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("empresaServico")
@RemoteProxy(name="empresaServico")
public class EmpresaServico {

  protected static final Logger logger = LoggerFactory.getLogger(EmpresaServico.class);

  @Inject
  private EmpresaDao empresaDao;
  
  public EmpresaServico() {
    logger.debug("EmpresaServico iniciado");
  }
  
  public List<Empresa> listar() {
    final List<Empresa> lista = this.empresaDao.findAll();
    Collections.sort(lista);
    return lista;
  }

  @RemoteMethod
  public Object[] listarEmpresas() {
    return this.listar().toArray();
  }

  public Empresa recuperar(String codigo) throws SermilException {
    return this.empresaDao.findById(codigo);
  }
  
}
