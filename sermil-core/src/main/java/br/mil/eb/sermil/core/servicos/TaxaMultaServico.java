package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.TaxaMultaDao;
import br.mil.eb.sermil.core.exceptions.EntityPersistenceErrorException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.TaxaMulta;

/** Serviços de Taxa/Multa.
 * @author Abreu Lopes, Aryene
 * @since 5.2.6
 * @version 5.2.6
 */
@Named("taxaMultaServico")
@RemoteProxy(name = "taxaMultaServico")
public class TaxaMultaServico {

   protected static final Logger logger = LoggerFactory.getLogger(TaxaMultaServico.class);

   @Inject
   private TaxaMultaDao tmDao;

   public TaxaMultaServico() {
      logger.debug("TaxaMultaServico iniciado");
   }

   public List<TaxaMulta> listar() throws SermilException {
      final List<TaxaMulta> lista = this.tmDao.findAll();
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   @RemoteMethod
   public Object[] listarArtigo() throws SermilException {
      return this.tmDao.findByNamedQuery("TaxaMulta.listarArtigo").toArray(new Object[0]);
   }

   @RemoteMethod
   public TaxaMulta[] listarPorArtigo(final Integer artigo) throws SermilException {
      return this.tmDao.findByNamedQuery("TaxaMulta.listarPorArtigo", artigo).toArray(new TaxaMulta[0]);
   }

   public TaxaMulta recuperar(TaxaMulta.PK pk) throws SermilException {
      return this.tmDao.findById(pk);
   }

   @Transactional
   public List<TaxaMulta> salvar(final List<TaxaMulta> taxaMultas) throws SermilException  {
      for (TaxaMulta tm : taxaMultas) {
         try {
            this.tmDao.save(tm);
            logger.debug("Entidade: TaxaMulta - Valores={}, {}, {}", tm.toString(), tm.getTipo(), tm.getValor());
         } catch(Exception e) {
            throw new EntityPersistenceErrorException(e);
         }
      }
      return taxaMultas;
   } 

}
