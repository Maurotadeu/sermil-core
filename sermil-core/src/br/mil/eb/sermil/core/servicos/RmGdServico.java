package br.mil.eb.sermil.core.servicos;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.DstbBolNecDao;
import br.mil.eb.sermil.core.dao.DstbGdDao;
import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.DstbBolNec;
import br.mil.eb.sermil.modelo.DstbGd;
import br.mil.eb.sermil.modelo.DstbGdJsm;
import br.mil.eb.sermil.modelo.DstbGdOm;
import br.mil.eb.sermil.modelo.Jsm;

/** Serviços de gerenciamento dos Grupo de Distribuição (Tabelas DSTB_GD, DSTB_GD_JSM e DSTB_GD_OM).
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: RmGdServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("rmGdServico")
public class RmGdServico {

  protected static final Logger logger = LoggerFactory.getLogger(RmGdServico.class);

  @Inject
  private DstbGdDao gdDao;

  @Inject
  private DstbBolNecDao bolnecDao;

  @Inject
  private JsmDao jsmDao;

  public RmGdServico() {
    logger.debug("RmGdServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public DstbGd recuperar(final DstbGd.PK pk) throws SermilException {
    if (pk.getRmCodigo() == null || pk.getCodigo() == null) {
      throw new CriterioException("Informe a RM e o Nr do GD.");
    }
    DstbGd gd = this.gdDao.findById(pk);
    if (gd == null) {
      gd = new DstbGd(pk.getRmCodigo(), pk.getCodigo());
      this.gdDao.save(gd);
    }
    return gd;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public Map<Integer, DstbBolNec> listarBolNec(final Byte rm) throws SermilException {
    final Map<Integer, DstbBolNec> listaBolNec = new LinkedHashMap<Integer, DstbBolNec>();
    int i = 0;
    for(DstbBolNec b : this.bolnecDao.findByNamedQuery("DstbBolNec.listarPorRm", rm)) {
      listaBolNec.put(i++, b);
    }
    return listaBolNec;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public Map<Integer, Jsm> listarJsm(final Byte rm) throws SermilException {
    final Map<Integer, Jsm> listaJsm = new LinkedHashMap<Integer, Jsm>();
    int i = 0;
    final List<Jsm> lista = this.jsmDao.findByNamedQuery("Jsm.listarPorRm", rm);
    Collections.sort(lista);
    for(Jsm j: lista) {
      listaJsm.put(i++, j);
    }
    return listaJsm;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public List<DstbGd> listarGd(final Byte rm) throws SermilException {
    final List<DstbGd> lista = this.gdDao.findByNamedQuery("DstbGd.listarPorRm", rm);
    Collections.sort(lista);
    return lista;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
	public void salvar(final DstbGd gd) throws SermilException {
    this.gdDao.save(gd);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
	public void excluir(final DstbGd gd) throws SermilException {
    this.gdDao.delete(gd);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public DstbGd adicionarJsm(final DstbGd gd, final Jsm jsm, final Integer prioridade) throws SermilException {
    try {
      final DstbGd gdNovo = this.gdDao.findById(gd.getPk());
      final DstbGdJsm gdJsm = new DstbGdJsm(gdNovo.getPk().getRmCodigo(), gdNovo.getPk().getCodigo(), jsm.getCsmCodigo(), jsm.getCodigo());
      gdJsm.setDstbGd(gdNovo);
      gdJsm.setJsm(jsm);
      gdJsm.setPrioridade(prioridade);
      gdNovo.getDstbGdJsmCollection().add(gdJsm);
      return this.gdDao.save(gdNovo);
    } catch (Exception e) {
      throw new SermilException(e.getMessage(), e);
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public DstbGd excluirJsm(final DstbGdJsm gdJsm) throws SermilException {
    try {
      final DstbGd gdNovo = this.gdDao.findById(new DstbGd.PK(gdJsm.getPk().getRmCodigo(), gdJsm.getPk().getDstbGdCodigo()));
      gdJsm.setJsm(null);
      gdNovo.getDstbGdJsmCollection().remove(gdJsm);
      return this.gdDao.save(gdNovo);
    } catch (Exception e) {
      throw new SermilException(e.getMessage(), e);
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public DstbGd adicionarBolNec(final DstbGd gd, final DstbBolNec bolNec, final Integer prioridade) throws SermilException {
    try {
      final DstbGd gdNovo = this.gdDao.findById(gd.getPk());
      final DstbGdOm gdOm = new DstbGdOm(gd.getPk().getRmCodigo(), gd.getPk().getCodigo(), bolNec.getPk().getOmCodigo(), bolNec.getPk().getGptIncorp(), bolNec.getPk().getNumero());
      gdOm.setPrioridade(prioridade);
      gdOm.setOm(bolNec.getOm());
      gdOm.setDstbGd(gdNovo);
      gdNovo.getDstbGdOmCollection().add(gdOm);
      return this.gdDao.save(gdNovo);
    } catch (Exception e) {
      throw new SermilException(e.getMessage(), e);
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public DstbGd excluirBolNec(final DstbGdOm gdOm) throws SermilException {
    try {
      final DstbGd gdNovo = this.gdDao.findById(new DstbGd.PK(gdOm.getPk().getRmCodigo(), gdOm.getPk().getDstbGdCodigo()));
      gdOm.setDstbGd(null);
      gdNovo.getDstbGdOmCollection().remove(gdOm);
      return this.gdDao.save(gdNovo);
    } catch (Exception e) {
      throw new SermilException(e.getMessage(), e);
    }
  }

}
