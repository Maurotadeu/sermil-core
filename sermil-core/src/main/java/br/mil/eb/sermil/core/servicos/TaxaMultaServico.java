package br.mil.eb.sermil.core.servicos;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.TaxaMultaDao;
import br.mil.eb.sermil.core.exceptions.EntityPersistenceErrorException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.TaxaMulta;
import br.mil.eb.sermil.tipos.Lista;

/** Serviços de Taxa/Multa.
 * @author Abreu Lopes, Aryene
 * @since 5.2.6
 * @version 5.4
 */
@Named("taxaMultaServico")
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

   public Lista[] listarArtigos() throws SermilException {
     final TypedQuery<Object[]> query = this.tmDao.getEntityManager().createNamedQuery("TaxaMulta.listarArtigos", Object[].class);
     return query.getResultList().stream().map(o -> new Lista(((Short)o[0]).toString(), ((Short)o[0]).toString())).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   public Lista[] listarPorArtigo(final Short artigo) throws SermilException {
     final TypedQuery<Object[]> query = this.tmDao.getEntityManager().createNamedQuery("TaxaMulta.listarPorArtigo", Object[].class);
     query.setParameter(1, artigo);
     return query.getResultList().stream().map(o -> new Lista(((Short)o[0]).toString(), (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
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
