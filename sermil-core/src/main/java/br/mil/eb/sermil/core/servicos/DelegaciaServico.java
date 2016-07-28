package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
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
 * @version 5.4.6
 */
@Named("delegaciaServico")
@RemoteProxy(name="delegaciaServico")
public class DelegaciaServico {

  protected static final Logger logger = LoggerFactory.getLogger(DelegaciaServico.class);

  private static final String SQL = "select d.codigo, d.csm_codigo, o.endereco||' - '||o.bairro||' - '||m.descricao||' - '||m.uf_sigla, d.latitude, d.longitude, decode(u.nome,null, null, p.sigla||' '||u.nome||' (CPF '||u.cpf||')'), d.telefone, d.email, count(c.ra)" +
  " from delegacia d join jsm j on d.csm_codigo = j.csm_codigo and d.codigo = j.delsm join cidadao c on j.csm_codigo = c.csm_codigo and j.codigo = c.jsm_codigo join om o on d.om_codigo = o.codigo join municipio m on o.municipio_codigo = m.codigo left outer join usuario u on d.delegado = u.cpf left outer join posto_graduacao p on u.posto_graduacao_codigo = p.codigo" +
  " where vinculacao_ano = ?1" +
  " group by d.codigo, d.csm_codigo, o.endereco, o.bairro, m.descricao, m.uf_sigla, d.latitude, d.longitude, p.sigla, u.nome, u.cpf, d.telefone, d.email";
  
  @Inject
  private DelegaciaDao dsmDao;

  public DelegaciaServico() {
    logger.debug("DelegaciaServico iniciado");
  }

  public List<Delegacia> listar(final Delegacia del) throws SermilException {
    if (del == null || (del.getPk().getCsmCodigo() == null && del.getPk().getCodigo() == null)) {
      throw new CriterioException();
    }
    List<Delegacia> lista = null;
    if (del.getOm().getMunicipio().getCodigo() != null) {
      lista = this.dsmDao.findByNamedQuery("Del.listarPorMun", del.getOm().getMunicipio().getCodigo());
    } else {
      if (del.getPk().getCsmCodigo() != null && del.getPk().getCodigo() != null) {
        lista = new ArrayList<Delegacia>(1);
        lista.add(this.recuperar(del.getPk().getCsmCodigo(), del.getPk().getCodigo()));
      } else if (del.getPk().getCsmCodigo() != null && del.getPk().getCodigo() == null) {
        lista = this.dsmDao.findByNamedQuery("Del.listarPorCsm", del.getPk().getCsmCodigo());
      }
    }
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  public Lista[] listarPorCsm(final Byte csm) throws SermilException {
    final TypedQuery<Object[]> query = this.dsmDao.getEntityManager().createNamedQuery("Del.listarPorCsm", Object[].class);
    query.setParameter(1, csm);
    return query.getResultList().stream().map(o -> new Lista(((Byte)o[0]).toString(), ((Byte)o[0]).toString())).collect(Collectors.toList()).toArray(new Lista[0]);
  }

  @RemoteMethod
  public Object[] listarCidadaosPorDelegacia(final Integer ano) throws SermilException {
    final List<Object[]> result = this.dsmDao.findBySQL(SQL, ano == null ? Calendar.getInstance().get(Calendar.YEAR) : ano);
    return result.toArray(new Object[0]);
  }
  
  public Delegacia recuperar(final Byte csmCodigo, final Byte codigo) throws SermilException {
    return this.dsmDao.findById(new Delegacia.PK(csmCodigo, codigo));
  }

  @PreAuthorize("hasAnyRole('adm','dsm','del')")
  @Transactional
  public Delegacia salvar(final Delegacia del) throws SermilException {
    if(del != null && del.getDelegado() != null && del.getDelegado().getCpf() == null) {
      del.setDelegado(null);
    }
    if(del != null && del.getOm() != null && del.getOm().getCodigo() == null) {
      del.setOm(null);
    }
    return this.dsmDao.save(del);
  }

}
