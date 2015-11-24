package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.event.ListSelectionEvent;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.TaxaMultaDao;
import br.mil.eb.sermil.core.exceptions.EntityPersistenceErrorException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.EstatArrecadacao;
import br.mil.eb.sermil.modelo.TaxaMulta;

/**
 * Serviços de Taxa/Multa.
 * 
 * @author Abreu Lopes
 * @since 5.0
 * @version $Id: OmServico.java 2427 2014-05-15 13:23:38Z wlopes $
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
   public List<TaxaMulta> salvar(final List<TaxaMulta> taxaMultas) throws EntityPersistenceErrorException  {
      
      for (TaxaMulta taxaMulta : taxaMultas) {
         try {
            tmDao.save(taxaMulta);
         } catch (SermilException e) {
            throw new EntityPersistenceErrorException();
         }
      }
      return taxaMultas;
   } 
}
