package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsAnamneseDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CsAnamnese;

/** Gerenciamento de Ficha de Anamnese da CS.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.2.8
 */
@Named("csAnamneseServico")
public class CsAnamneseServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsAnamneseServico.class);

   @Inject
   private CsAnamneseDao anamneseDao;

   public CsAnamneseServico() {
      logger.debug("CsAnamneseServico iniciado");
   }

   public CsAnamnese recuperar(final Long ra) throws SermilException {
      return this.anamneseDao.findById(ra);
   }

   @Transactional
   public CsAnamnese salvar(final CsAnamnese anamnese) throws SermilException {
      CsAnamnese csa = null;
      if (anamnese != null && anamnese.getCidadaoRa() != null) {
         csa = this.anamneseDao.save(anamnese);
      }
      return csa;
   }

}
