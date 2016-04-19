package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidFotoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidFoto;

/** Serviço de arquivo de foto de um cidadão.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.3.2
 */
@Named("cidFotoServico")
public class CidFotoServico {

   protected static final Logger logger = LoggerFactory.getLogger(CidFotoServico.class);

   @Inject
   private CidFotoDao cidFotoDao;

   public CidFotoServico() {
      logger.debug("CidFotoServico iniciado");
   }

   public CidFoto obter(final Long ra) throws SermilException {
      if (ra == null) {
         throw new SermilException("Informe um RA válido.");
      }
      logger.debug("RA = {}", ra);
      return this.cidFotoDao.findById(ra);
   }

   @Transactional
   public void excluir(final CidFoto foto) throws SermilException {
      if (foto == null) {
         throw new SermilException("Foto não existe.");
      }
      logger.debug("Foto = {}", foto);
      this.cidFotoDao.delete(foto);
   }

   @Transactional
   public void salvar(final CidFoto foto) throws SermilException {
      if (foto == null) {
         throw new SermilException("Foto não existe.");
      }
      logger.debug("Foto = {}", foto);
      this.cidFotoDao.save(foto);
   }

}
