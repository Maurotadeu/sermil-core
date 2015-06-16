package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.tipos.Ra;

/** Serviço de RA.
 * @author Abreu Lopes
 * @since 5.0
 * @version $Id: RaServico.java 2467 2014-06-12 14:17:52Z wlopes $
 */
@Named("raServico")
public class RaServico {

  protected static final Logger logger = LoggerFactory.getLogger(RaServico.class);

  @Inject
  private RaMestreDao raMestreDao;

  public RaServico() {
    logger.debug("RaServico iniciado");
  }

  @Transactional
  public Long gerar(final Byte csm, final Short jsm) throws NoDataFoundException  {
    final RaMestre raMestre = raMestreDao.findById(new RaMestre.PK(csm, jsm));
    if (raMestre == null) {
      throw new NoDataFoundException("CSM/JSM não existe na tabela de controle de RA (RA_MESTRE)");
    }
    raMestre.setSequencial(raMestre.getSequencial() + 1);
    this.raMestreDao.save(raMestre);
    return new Ra.Builder().csm(csm).jsm(jsm).sequencial(raMestre.getSequencial()).build().getValor();
  }

}
