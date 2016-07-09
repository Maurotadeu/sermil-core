package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.DstbParametroDao;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.DstbParametro;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.tipos.Alerta;

/**
 * Serviço de Alertas.
 * 
 * @author Anselmo Ribeiro, Abreu Lopes
 * @version 5.3.2
 * @since 5.3.2
 */
@Named("alertasServico")
public class AlertasServico {

   protected static final Logger logger = LoggerFactory.getLogger(AlertasServico.class);

   @Inject
   private Environment env;

   @Inject
   CsDao csDao;

   @Inject
   PgcServico pgcServico;

   @Inject
   DstbParametroDao DstbDao;

   public AlertasServico() {
      logger.debug("AlertasServico iniciado");
   }

   public Map<String, List<Alerta>> getPgcAlertas(Map<String, Object> session) {
      final Map<String, List<Alerta>> alertas = new LinkedHashMap<String, List<Alerta>>(5);
      
      alertas.put(this.env.getProperty("alistamento"), getAlistamentoAlerta(session));
      if (pgcServico.existePgcAnoAtual()) {
         alertas.put(this.env.getProperty("predispensa"), getPreDispensaAlerta(session));
         //alertas.put(this.env.getProperty("selecao"), getAlistamentoAlerta(session));
         //alertas.put(this.env.getProperty("distribuicao"), getDistribuicaoAlerta(session));
         //alertas.put(this.env.getProperty("selecao.complementar"), getSelecaoComplementarAlerta(session));
      }
      return alertas;
   }

   private void incrementAlertsCount(Map<String, Object> session) {
      session.put("alertsCount", (int) session.get("alertsCount") + 1);
   }

   /**
    * 01 - Alistamento
    */
   public List<Alerta> getAlistamentoAlerta(Map<String, Object> session) {
      final List<Alerta> alertas = new ArrayList<Alerta>(2);

      // PGC - Lançamento dos dados do PGC para o ano atual
      final Alerta alerta = new Alerta(env.getProperty("alistamento.lancamento.dados.ano.atual"), Alerta.TIPO_OK);
      if (!this.pgcServico.existePgcAnoAtual()) {
         alerta.addMessage(this.env.getProperty("alistamento.lancamento.dados.ano.atual.motivo"));
         alerta.setTipo(Alerta.TIPO_ERROR);
         incrementAlertsCount(session);
      }
      alertas.add(alerta);

      // PGC - Lançamento dos dados do PGC para o próximo ano
      final Alerta alerta2 = new Alerta(env.getProperty("alistamento.lancamento.dados.proximo.ano"), Alerta.TIPO_OK);
      if (!this.pgcServico.existePgcProximoAno()) {
         alerta2.addMessage(this.env.getProperty("alistamento.lancamento.dados.proximo.ano.motivo"));
         alerta2.setTipo(Alerta.TIPO_ERROR);
         incrementAlertsCount(session);
      }
      alertas.add(alerta2);

      return alertas;
   }

   /**
    * 02 - Pré Dispensa
    */
   public List<Alerta> getPreDispensaAlerta(Map<String, Object> session) {
      final List<Alerta> alertas = new ArrayList<Alerta>(3);

      // Pré-Dispensa - Lançamento de Parâmetros da Distribuição
      final Alerta alerta = new Alerta(env.getProperty("predispensa.lancamento.parametros.distribuicao"), Alerta.TIPO_OK);
      buildLancParDstbAlerta(session, alerta);
      alertas.add(alerta);

      // Pré-Dispensa - Alteração de dados de CS
      final Alerta alerta2 = new Alerta(env.getProperty("predispensa.alteracao.dados.cs"), Alerta.TIPO_OK);
      for (Cs cs : this.csDao.findAll()) {
         if (isDadosCsAlteradosForaDoPrazo(cs.getCodigo())) {
            alerta2.addMessage(cs.getCodigo() + " " + this.env.getProperty("predispensa.alteracao.dados.cs.msg"));
            alerta2.setTipo(Alerta.TIPO_ERROR);
            incrementAlertsCount(session);
         }
      }
      alertas.add(alerta2);

      // Pré-Dispensa - Alteração de tributação de município
      final Alerta alerta3 = new Alerta(env.getProperty("predispensa.alteracao.tributacao"), Alerta.TIPO_OK);
      // TODO achar jsm que mudaram tributacao
      final List<Jsm> jsms = new ArrayList<Jsm>();
      jsms.add(new Jsm());
      for (Jsm jsm : jsms) {
         if (this.isTributacaoJsmAlteradaForaDoPrazo(jsm)) {
            alerta3.setTipo(Alerta.TIPO_ERROR);
            alerta3.addMessage(
                  new StringBuilder().append(jsm.getCodigo()).append("/").append(jsm.getCsmCodigo()).append(" ").append(this.env.getProperty("predispensa.alteracao.tributacao.msg")).toString());
            incrementAlertsCount(session);
         }
      }
      alertas.add(alerta3);

      return alertas;

   }

   private void buildLancParDstbAlerta(Map<String, Object> session, final Alerta alerta) {
      byte[] rmCodigo = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
      for (byte i = 0; i < rmCodigo.length; i++) {
         int tot = getTotalDeParametrosDeDistribuicaoDoAnoAtual(i);
         if (tot < 4) {
            alerta.setTipo(Alerta.TIPO_ERROR);
            incrementAlertsCount(session);
            if (tot == 0)
               alerta.addMessage(String.valueOf(i + 1) + " " + this.env.getProperty("predispensa.rm.nao.lancou.parametros.distribuicao"));
            else
               alerta.addMessage(String.valueOf(i + 1) + " " + this.env.getProperty("predispensa.rm.lancou.poucos.parametros.distribuicao"));
         }
      }
   }

   public List<Alerta> getSelecaoAlerta() {

      final List<Alerta> alertas = new ArrayList<Alerta>(2);
      
      final Alerta alerta = new Alerta(env.getProperty("selecao.periodo.funcionamento.ano.atual"), Alerta.TIPO_OK);
      alertas.add(alerta);
      
      final Alerta alerta2 = new Alerta(env.getProperty("selecao.periodo.funcionamento.proxomo.ano"), Alerta.TIPO_OK);
      alertas.add(alerta2);
      
      return alertas;
   }

   public List<Alerta> getDistribuicaoAlerta(Map<String, Object> session) {
      final List<Alerta> alertas = new ArrayList<Alerta>(5);
      
      final Alerta alerta1 = new Alerta(env.getProperty("distribuicao.preenchimento.bolnec"), Alerta.TIPO_OK);
      alertas.add(alerta1);
      // TODO alerta.addMessage("Erro tal e tal");
      // alerta.setTipo(Alerta.TIPO_ERROR);
      // toda vez que encontrar um erro mudar tipo de alerta para erro ou
      // warning.
      final Alerta alerta2 = new Alerta(env.getProperty("distribuicao.lancamento.parametros"), Alerta.TIPO_OK);
      alertas.add(alerta2);
      final Alerta alerta3 = new Alerta(env.getProperty("distribuicao.alteracao.grupos.distribuicao"), Alerta.TIPO_OK);
      alertas.add(alerta3);
      final Alerta alerta4 = new Alerta(env.getProperty("distribuicao.consolidacao.bolnec"), Alerta.TIPO_OK);
      alertas.add(alerta4);
      final Alerta alerta5 = new Alerta(env.getProperty("distribuicao.bcciap.carregamento"), Alerta.TIPO_OK);
      alertas.add(alerta5);
      
      return alertas;
   }

   public List<Alerta> getSelecaoComplementarAlerta(Map<String, Object> session) {
      // TODO: implementar SelecaoComplementarAlerta
      final List<Alerta> alertas = new ArrayList<Alerta>();
      return alertas;
   }

   public int getTotalDeParametrosDeDistribuicaoDoAnoAtual(final Byte rmCodigo) {
      List<DstbParametro> dsts = this.DstbDao.findByNamedQuery("Distribuicao.ParamtrosPorAno&Rm", Calendar.getInstance().get(Calendar.YEAR), rmCodigo);
      return dsts.size();
   }

   public boolean isDadosCsAlteradosForaDoPrazo(final Integer csCodigo) {
      // TODO: implementar idDadosAlteradosDeCs
      return false;
   }

   public boolean isTributacaoJsmAlteradaForaDoPrazo(final Jsm jsm) {
      // TODO: isTributacaoDeJsmAlteradaForaDoPrazo
      return true;
   }

   public boolean isDistribuicaoProcessada() {
      // TODO: isDistribuicaoProcessada
      return false;
   }

}
