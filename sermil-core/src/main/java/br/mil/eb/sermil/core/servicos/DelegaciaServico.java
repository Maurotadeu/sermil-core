package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.DelegaciaDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Delegacia;
import br.mil.eb.sermil.tipos.Lista;

/** Serviços de Delegacia de Serviço Militar (DelSM).
 * @author Abreu Lopes
 * @since 5.0
 * @version 5.4
 */
@Named("delegaciaServico")
public class DelegaciaServico {

  protected static final Logger logger = LoggerFactory.getLogger(DelegaciaServico.class);

  @Inject
  private DelegaciaDao delDao;

  public DelegaciaServico() {
    logger.debug("DelegaciaServico iniciado");
  }

  public List<Delegacia> listar(final Delegacia del) throws SermilException {
    if (del == null || (del.getPk().getCsmCodigo() == null && del.getPk().getCodigo() == null)) {
      throw new CriterioException();
    }
    List<Delegacia> lista = null;
    if (del.getOm().getMunicipio().getCodigo() != null) {
      lista = this.delDao.findByNamedQuery("Del.listarPorMun", del.getOm().getMunicipio().getCodigo());
    } else {
      if (del.getPk().getCsmCodigo() != null && del.getPk().getCodigo() != null) {
        lista = new ArrayList<Delegacia>(1);
        lista.add(this.recuperar(del.getPk().getCsmCodigo(), del.getPk().getCodigo()));
      } else if (del.getPk().getCsmCodigo() != null && del.getPk().getCodigo() == null) {
        lista = this.delDao.findByNamedQuery("Del.listarPorCsm", del.getPk().getCsmCodigo());
      }
    }
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  public Lista[] listarPorCsm(final Byte csm) throws SermilException {
    final TypedQuery<Object[]> query = this.delDao.getEntityManager().createNamedQuery("Del.listarPorCsm", Object[].class);
    query.setParameter(1, csm);
    return query.getResultList().stream().map(o -> new Lista(((Byte)o[0]).toString(), ((Byte)o[1]).toString())).collect(Collectors.toList()).toArray(new Lista[0]);
  }

  public Delegacia recuperar(final Byte csmCodigo, final Byte codigo) throws SermilException {
    return this.delDao.findById(new Delegacia.PK(csmCodigo, codigo));
  }

  @PreAuthorize("hasAnyRole('adm','dsm','del')")
  @Transactional
  public Delegacia salvar(final Delegacia del) throws SermilException {
    return this.delDao.save(del);
  }

}
