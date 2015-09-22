package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.InfoLocalDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.InfoLocal;
import br.mil.eb.sermil.modelo.Usuario;

/**
 * Serviços de Info Local (Tabela INFO_LOCAL).
 * 
 * @author Abreu Lopes
 * @since 4.5
 * @version $Id: RmProcessoServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("rmProcessoServico")
public class RmProcessoServico {

   protected static final Logger logger = LoggerFactory.getLogger(RmProcessoServico.class);

   @Inject
   private InfoLocalDao infoLocalDao;

   public RmProcessoServico() {
      logger.debug("RmProcessoServico iniciado");
   }

   @PreAuthorize("hasRole('adm')")
   @Transactional
   public void salvar(final List<InfoLocal> lista, final Usuario usr) throws SermilException {
      for (InfoLocal status : lista) {
         status.setSelStatus(status.getSelStatus() == null ? 0 : status.getSelStatus());
         status.setDistrStatus(status.getDistrStatus() == null ? 0 : status.getDistrStatus());
         infoLocalDao.save(status);
      }
   }

   @PreAuthorize("hasRole('adm')")
   public List<InfoLocal> listar() throws SermilException {
      return infoLocalDao.findByNamedQuery("InfoLocal.listar");
   }

}
