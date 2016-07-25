package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
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

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.CsEnderecoDao;
import br.mil.eb.sermil.core.exceptions.ConsultaException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.CsEndereco;
import br.mil.eb.sermil.modelo.CsExclusaoData;
import br.mil.eb.sermil.modelo.CsFuncionamento;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de CS.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 5.3.2
 * @version 5.4.5
 */
@Named("csServico")
@RemoteProxy(name = "csServico")
public class CsServico {

  protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

  @Inject
  CsDao csDao;

  @Inject
  CsEnderecoDao csEnderecoDao;

  public CsServico() {
    logger.debug("CsServico iniciado");
  }

  public Lista[] listarCsPorRm(final Byte rm) throws SermilException {
    final TypedQuery<Object[]> query = this.csDao.getEntityManager().createNamedQuery("Cs.listarCsPorRm", Object[].class);
    query.setParameter(1, rm);
    return query.getResultList().stream().map(o -> new Lista(((Integer)o[0]).toString(), (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

  @RemoteMethod
  public List<Cs> listarPorRm(final Byte rm) throws ConsultaException {
    if (rm == null) {
      throw new ConsultaException("Informe o número da Região Militar");
    }
    final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorRm", rm);
    if (lista == null || lista.isEmpty()) {
      throw new ConsultaException("Não há CS cadastrada na " + rm + "ª Região Militar");
    }
    return lista;
  }

  @SuppressWarnings("unchecked")
  public List<Object[]>[] listarPorRmEnd(final Byte rm) throws ConsultaException {
    final TypedQuery<Object[]> query = this.csDao.getEntityManager().createNamedQuery("Cs.listarCsPorRmEnd", Object[].class);
    query.setParameter(1, rm);
    return query.getResultList().stream().collect(Collectors.toList()).toArray(new ArrayList[0]);
  }

  public List<Cs> listarPorNome(final String nome) throws ConsultaException {
    if (nome == null) {
      throw new ConsultaException("Informe o nome da CS");
    }
    final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorNome", nome);
    if (lista == null || lista.isEmpty()) {
      throw new ConsultaException("Não há CS cadastrada com o nome " + nome + ".");
    }
    return lista;
  }

  public List<CsEndereco> listarCsEnderecoMun(final Integer municipioCodigo) throws ConsultaException {
    if (municipioCodigo == null) {
      throw new ConsultaException("Informe o código do Município");
    }
    final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorMunicipio", municipioCodigo);
    if (lista == null || lista.isEmpty()) {
      throw new ConsultaException("Não há endereços de CS cadastrados no município código " + municipioCodigo);
    }
    return lista;
  }

  @RemoteMethod
  public List<CsEndereco> listarCsEnderecoRm(final Byte rmCodigo) throws ConsultaException {
    if (rmCodigo == null) {
      throw new ConsultaException("Informe o código da Região Militar");
    }
    final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorRm", rmCodigo);
    if (lista == null || lista.isEmpty()) {
      throw new ConsultaException("Não há endereços de CS cadastrados na " + rmCodigo + "ª Região Militar");
    }
    return lista;
  }

  @RemoteMethod
  public List<CsFuncionamento> listarCsFuncionamento(final Integer csCodigo) throws ConsultaException {
    if (csCodigo == null) {
      throw new ConsultaException("Informe o código da CS");
    }
    final List<CsFuncionamento> lista = this.recuperar(csCodigo).getCsFuncionamentoCollection();
    //if (lista == null || lista.isEmpty()) {
    //   throw new ConsultaException("Não há funcionamentos cadastrados para a CS " + csCodigo);
    //}
    return lista;
  }

  @RemoteMethod
  public List<CsExclusaoData> listarCsExclusaoData(final Integer csCodigo) throws ConsultaException {
    if (csCodigo == null) {
      throw new ConsultaException("Informe o código da CS");
    }
    final List<CsExclusaoData> lista = this.recuperar(csCodigo).getCsExclusaoDataCollection();
    //if (lista == null || lista.isEmpty()) {
    //   throw new ConsultaException("Não há exclusões de data cadastradas para a CS " + csCodigo);
    //}
    return lista;
  }

  public Cs recuperar(final Integer csCodigo) {
    return this.csDao.findById(csCodigo);
  }

  public CsEndereco recuperarEndereco(final Integer codigo) throws SermilException {
    if (codigo == null) {
      throw new SermilException("Informe o código do Endereço");
    }
    final CsEndereco end = this.csEnderecoDao.findById(codigo);
    if (end == null) {
      throw new NoDataFoundException(new StringBuilder("Endereço não foi encontrado (cod=").append(codigo).append(")").toString());
    }
    return end;
  }

  @Transactional
  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public String excluir(final Integer csCodigo) throws SermilException {
    final Cs cs = this.recuperar(csCodigo);
    this.csDao.delete(cs);
    logger.debug("Excluída: {}", cs);
    return new StringBuilder(cs.toString()).append(" excluida").toString();
  }

  @Transactional
  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public String excluirEndereco(final Integer codigo) throws SermilException {
    final CsEndereco csEnd = this.recuperarEndereco(codigo);
    this.csEnderecoDao.delete(csEnd);
    logger.debug("Excluído: {}", csEnd);
    return new StringBuilder(csEnd.toString()).append(" excluido").toString();
  }

  @Transactional
  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public String salvar(final Cs cs) throws SermilException {
    final Cs csSalva = this.csDao.save(cs);
    logger.debug("Salvo: {}", csSalva);
    return new StringBuilder(cs.toString()).append(" salva").toString();
  }

  @Transactional
  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public String salvarEndereco(final CsEndereco csEndereco) throws SermilException {
    final CsEndereco csEnd = this.csEnderecoDao.save(csEndereco);
    logger.debug("Salvo: {}", csEnd);
    return new StringBuilder(csEnd.toString()).append(" salvo").toString();
  }

}
