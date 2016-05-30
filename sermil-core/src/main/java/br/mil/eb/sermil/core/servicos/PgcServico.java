package br.mil.eb.sermil.core.servicos;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.PgcDao;
import br.mil.eb.sermil.core.exceptions.PgcNaoExisteException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Pgc;

/**
 * Serviço de PGC.
 * 
 * @author Anselmo Ribeiro, Abreu Lopes
 * @version 5.3.2
 * @since 5.3.2
 */
@Named("pgcServico")
public class PgcServico {

   protected static final Logger logger = LoggerFactory.getLogger(PgcServico.class);

   @Inject
   PgcDao pgcDao;

   public PgcServico() {
      logger.debug("PgcServico iniciado");
   }

   public boolean isAnoBaseUnico(final String anoBase) {
      final List<Pgc> lista = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, anoBase);
      return lista.size() > 1 ? false : true;
   }

   public List<Pgc> listarPcg(final String anoBase) throws PgcNaoExisteException {
      List<Pgc> lista = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, anoBase);
      if (lista == null || lista.isEmpty()) {
         throw new PgcNaoExisteException();

      }
      return lista;
   }

   @Transactional
   public Pgc salvar(final Pgc pgc) throws SermilException {
      try {
         return this.pgcDao.save(pgc);
      } catch (SermilException se) {
         logger.error(se.getMessage());
         throw se;
      }
   }

   public List<Pgc> getPgcList() {
      return this.pgcDao.findAll();
   }

   public boolean existePgcAnoAtual() {
      int ano = Calendar.getInstance().get(Calendar.YEAR);
      final List<Pgc> lista = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, String.valueOf(ano));
      return lista.size() > 0 ? true : false;
   }

   public boolean existePgcProximoAno() {
      int ano = Calendar.getInstance().get(Calendar.YEAR);
      final List<Pgc> lista = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, String.valueOf(ano + 1));
      return lista.size() > 0 ? true : false;
   }

}
