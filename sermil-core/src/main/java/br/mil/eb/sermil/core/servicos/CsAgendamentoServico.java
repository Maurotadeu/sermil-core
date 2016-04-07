package br.mil.eb.sermil.core.servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CsAgendamentoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CsAgendamento;
import br.mil.eb.sermil.modelo.CsAgendamento.PK;

/** Gerenciamento de Agenda da CS.
 * @author Abreu Lopes
 * @since 5.3.2
 * @version 5.3.2
 */
@Named("csAgendamentoServico")
public class CsAgendamentoServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsAgendamentoServico.class);

   @Inject
   private CsAgendamentoDao csAgendamentoDao;
   
   public CsAgendamentoServico() {
      logger.debug("CsAgendamentoServico iniciado");
   }

   public List<Object[]> listarCsData(final Integer csCodigo, final Date data, boolean tarde) throws SermilException {
      final Calendar dataHora = Calendar .getInstance();
      if (data != null) {
        dataHora.setTime(data);
      }
      dataHora.set(Calendar.HOUR_OF_DAY, tarde ? 13 : 7);
      dataHora.set(Calendar.MINUTE, 0);
      dataHora.set(Calendar.SECOND, 0);
      dataHora.set(Calendar.MILLISECOND, 0);
      final List<Object[]> lista = this.csAgendamentoDao.findByNamedQueryArray("CsAgendamento.listarPorCsData", csCodigo, dataHora.getTime());
      return lista;
   }
   
   public CsAgendamento recuperar(final PK pk) throws SermilException {
      return this.csAgendamentoDao.findById(pk);
   }

}
