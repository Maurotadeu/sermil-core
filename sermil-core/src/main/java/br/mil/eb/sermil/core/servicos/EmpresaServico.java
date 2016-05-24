package br.mil.eb.sermil.core.servicos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.EmpresaDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Empresa;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de Empresas de Segurança Nacional (EDRSN).
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
 */
@Named("empresaServico")
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

  public Lista[] listarEmpresas() {
    final TypedQuery<Object[]> query = this.empresaDao.getEntityManager().createNamedQuery("Empresa.listar", Object[].class);
    return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

  public Empresa recuperar(String codigo) throws SermilException {
    return this.empresaDao.findById(codigo);
  }
  
}
