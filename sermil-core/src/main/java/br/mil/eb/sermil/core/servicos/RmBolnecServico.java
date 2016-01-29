package br.mil.eb.sermil.core.servicos;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.DstbBolNecDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.DstbBolNec;
import br.mil.eb.sermil.modelo.DstbNecessidade;
import br.mil.eb.sermil.modelo.Usuario;

/** Serviços de Boletim de Necessidade (Tabelas DSTB_BOLNEC e DSTB_NECESSIDADE).
 * @author Abreu Lopes
 * @since 3.9
 * @version $Id: RmBolnecServico.java 2501 2014-08-14 17:53:19Z wlopes $
 */
@Named("rmBolnecServico")
@RemoteProxy(name="bnServico")
public class RmBolnecServico {

  protected static final Logger logger = LoggerFactory.getLogger(RmBolnecServico.class);

  @Inject
  private DstbBolNecDao bolnecDao;

  @Inject
  private CidadaoDao cidadaoDao;

  public RmBolnecServico() {
    logger.debug("RmBolnecServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  @Transactional
  public void excluir(final DstbBolNec bolnec) throws SermilException {
    this.bolnecDao.delete(bolnec);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  @Transactional
  public void majorar() throws SermilException {
    this.bolnecDao.execute("DstbBolNec.majorar");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  @Transactional
  public void salvar(final DstbBolNec bolnec, final List<DstbNecessidade> listaNec, final Usuario usr) throws SermilException {
    Short total = 0;
    for (DstbNecessidade nec: listaNec) {
      if(nec.getNecHist() == null) {
        throw new SermilException(new StringBuilder(nec.getPk().getPadraoCodigo()).append(" sem quantidade, verifique.").toString());
      }
      logger.debug(nec.toString());
      bolnec.addDstbNecessidade(nec);
      total = (short) (total + nec.getNecHist());
    }
    bolnec.setNec(total);
    bolnec.setUsuario(usr);
    bolnec.setCriacaoData(new Date());
    logger.debug(bolnec.toString());
    this.bolnecDao.save(bolnec);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  public Map<Integer, DstbBolNec> listarBolNecOm(final Integer codom) throws SermilException {
    return this.listarBolNec("DstbBolNec.listarPorOm", codom);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  public Map<Integer, DstbBolNec> listarBolNecRm(final Integer rm) throws SermilException {
    return this.listarBolNec("DstbBolNec.listarPorRm", rm);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  private Map<Integer, DstbBolNec> listarBolNec(final String namedQuery, final Integer param) throws SermilException {
    final Map<Integer, DstbBolNec> lista = new TreeMap<Integer, DstbBolNec>();
    int i = 0;
    for(DstbBolNec b : this.bolnecDao.findByNamedQuery(namedQuery, param)) {
      lista.put(i++, b);
    }
    return lista;
  }

  @RemoteMethod
  public DstbBolNec[] listar(final Byte rm) throws SermilException {
    final List<DstbBolNec> lista = this.bolnecDao.findByNamedQuery("DstbBolNec.listarPorRm", rm);
    return lista.toArray(new DstbBolNec[0]);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','om')")
  public List<Cidadao> listarDistribuidos(final Integer codom, final Integer ano) throws SermilException {
    return this.cidadaoDao.findByNamedQuery("Cidadao.listarDistribuidos", codom, ano);
  }

}
