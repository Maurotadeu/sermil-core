package br.mil.eb.sermil.core.servicos;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.OmBoletimDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.OmBoletim;
import br.mil.eb.sermil.modelo.OmBoletimCidadao;

/** Serviço de Boletim de Incorporação.
 * @author Abreu Lopes, Daniel Gardino
 * @since 4.6
 * @version 5.3.1
 */
@Named("omAdtBoletimServico")
public class OmAdtBoletimServico {

  protected static final Logger logger = LoggerFactory.getLogger(OmAdtBoletimServico.class);

  @Inject
  private OmBoletimDao omBoletimDao;

  @Inject
  private CidadaoDao cidadaoDao;

  public OmAdtBoletimServico() {
    logger.debug("OmAdtBoletimServico iniciado");
  }

  @Transactional
  public OmBoletim adicionarSU(final OmBoletim omBoletim) throws SermilException {
    if (omBoletim == null || omBoletim.getPk().getAno() == null || omBoletim.getPk().getOmCodigo() == null) {
       throw new SermilException("Informe o ANO e o CODOM da OM.");
    }
    logger.debug("BOLETIM = {}", omBoletim);
    final List<?> lista = this.omBoletimDao.findBySQL("SELECT MAX(CODIGO) FROM OM_BOLETIM WHERE OM_CODIGO = ? AND ANO = ? ", omBoletim.getPk().getOmCodigo(), omBoletim.getPk().getAno());
    if(lista.get(0) == null) {
      omBoletim.getPk().setCodigo(1);
    } else {
      omBoletim.getPk().setCodigo(((BigDecimal)lista.get(0)).intValue()+1);
    }
    this.omBoletimDao.save(omBoletim);
    return omBoletim;
  }

  @Transactional
  public OmBoletim adicionarCidadao(final OmBoletim.PK omBoletim, final Long ra) throws SermilException {
    final OmBoletim ob = this.omBoletimDao.findById(omBoletim);
    final OmBoletimCidadao obc = new OmBoletimCidadao();
    final Cidadao cid = this.cidadaoDao.findById(ra);
    obc.setAno(ob.getPk().getAno());
    obc.setOmCodigo(ob.getPk().getOmCodigo());
    obc.setBoletimCod(ob.getPk().getCodigo());
    obc.getPk().setCidadaoRa(ra);
    obc.getPk().setGptIncorp(ob.getPk().getGptIncorp());
    obc.setCidadaoNome(cid.getNome());
    ob.addOmBoletimCidadao(obc);
    return this.omBoletimDao.save(ob);
  }

  @Transactional
  public OmBoletim excluirCidadao(final OmBoletim.PK omBoletim, final OmBoletimCidadao obc) throws SermilException {
    final OmBoletim ob = this.omBoletimDao.findById(omBoletim);
    ob.getOmBoletimCidadao().remove(obc);
    return this.omBoletimDao.save(ob);
  }

  @Transactional
  public void excluirSU(final OmBoletim omBoletim) throws SermilException {
    final OmBoletim ob = this.omBoletimDao.findById(omBoletim.getPk());
    this.omBoletimDao.delete(ob);
  }

  public List<Cidadao> listarEfetivo(final OmBoletim.PK pk) throws SermilException {
    if (pk == null) {
      throw new SermilException("Informe ANO, CODOM e GPT de incorporação.");
    }
    List<Cidadao> cidadaos = this.cidadaoDao.findByNamedQuery("OmBoletim.listarEfetivo", pk.getAno() - 1, pk.getOmCodigo(), pk.getGptIncorp(), pk.getAno());
    return cidadaos;
  }

  public List<OmBoletim> listarBoletim(final OmBoletim.PK pk) throws SermilException {
    if (pk == null) {
      throw new SermilException("Informe ANO, CODOM e GPT de incorporação.");
    }
    return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimGpt", pk.getAno(), pk.getOmCodigo(), pk.getGptIncorp());
  }

  public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom, final Integer codigo) throws SermilException {
    return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimCod", ano, codom, codigo);
  }

  public OmBoletim listarBoletim(final Integer ano, final Integer codom, final String gpt, final String subunidade) throws SermilException {
    return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimSU", ano, codom, gpt, subunidade).get(0);
  }

}
