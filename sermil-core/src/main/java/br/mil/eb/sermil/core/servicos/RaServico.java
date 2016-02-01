package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.exceptions.RaMestreException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.tipos.Ra;

/** Controle de emissão de RA.
 * @author Abreu Lopes
 * @since 5.0
 * @version 5.2.7
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
    public Long gerar(final Byte csm, final Short jsm) throws SermilException {
        final RaMestre raMestre = this.raMestreDao.findById(new RaMestre.PK(csm, jsm));
        if (raMestre == null) {
            throw new RaMestreException("CSM/JSM não existe na tabela de controle de RA (RA_MESTRE)");
        }
        final EntityManager em = this.raMestreDao.getEntityManager();
        em.lock(raMestre, LockModeType.PESSIMISTIC_WRITE);
        final Integer nrSequencial = raMestre.getSequencial() + 1;
        final Long ra = new Ra.Builder().csm(csm).jsm(jsm).sequencial(nrSequencial).build().getValor();
        raMestre.setSequencial(nrSequencial);
        this.raMestreDao.save(raMestre);
        em.flush();
        logger.debug("RA Gerado: {} (Sequencial = {} - RaMestre = {})", ra, nrSequencial, raMestre.getSequencial());
        return ra;
    }

    public RaMestre recuperar(final RaMestre.PK pk) {
        return this.raMestreDao.findById(pk);
    }

    @Transactional
    public void salvar(final RaMestre obj) throws SermilException {
        this.raMestreDao.save(obj);
    }
}
