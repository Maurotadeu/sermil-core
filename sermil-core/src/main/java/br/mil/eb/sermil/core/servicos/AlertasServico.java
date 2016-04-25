package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.PgcDao;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Pgc;
import br.mil.eb.sermil.tipos.Alerta;

/** Serviço de Alertas.
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
   PgcDao pgcDao;

   public AlertasServico() {
      logger.debug("AlertasServico iniciado");
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

   public Map<String, List<Alerta>> getPgcAlertas(Map<String, Object> session) {
      final Map<String, List<Alerta>> alertas = new HashMap<String, List<Alerta>>(5);
      alertas.put(this.env.getProperty("alistamento"), getAlistamentoAlerta(session));
      alertas.put(this.env.getProperty("predispensa"), getPreDispensaAlerta(session));
      alertas.put(this.env.getProperty("selecao"), getAlistamentoAlerta(session));
      alertas.put(this.env.getProperty("distribuicao"), getDistribuicaoAlerta(session));
      alertas.put(this.env.getProperty("selecao.complementar"), getSelecaoComplementarAlerta(session));
      return alertas;
   }
   
   private void incrementAlertsCount(Map<String, Object> session){
   	session.put("alertsCount", (int) session.get("alertsCount")+1);
   }

   public List<Alerta> getAlistamentoAlerta(Map<String, Object> session) {
      final Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("alistamento.lancamento.dados.ano.atual"));
      alerta.setTipo(Alerta.TIPO_OK);
      if (!existePgcAnoAtual()) {
         alerta.addMessage(this.env.getProperty("alistamento.lancamento.dados.ano.atual.motivo"));
         alerta.setTipo(Alerta.TIPO_ERROR);
         incrementAlertsCount(session);
      }
      final Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("alistamento.lancamento.dados.proximo.ano"));
      alerta2.setTipo(Alerta.TIPO_OK);
      if (!existePgcProximoAno()) {
         alerta2.addMessage(this.env.getProperty("alistamento.lancamento.dados.proximo.ano.motivo"));
         alerta2.setTipo(Alerta.TIPO_ERROR);
         incrementAlertsCount(session);
      }
      final List<Alerta> alertas = new ArrayList<Alerta>(2);
      alertas.add(alerta);
      alertas.add(alerta2);
      return alertas;
   }

   public List<Alerta> getPreDispensaAlerta(Map<String, Object> session) {
      final Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("predispensa.lancamento.parametros.distribuicao"));
      alerta.setTipo(Alerta.TIPO_OK);
      byte[] rmCodigo = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
      for (byte i = 0; i < rmCodigo.length; i++) {
         if (!existeParametroDistribuicao(i)) {
            alerta.addMessage(String.valueOf(i + 1) + " " + this.env.getProperty("predispensa.rm.nao.lancou.parametros.distribuicao"));
            alerta.setTipo(Alerta.TIPO_ERROR);
            incrementAlertsCount(session);
         }
      }
      final Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("predispensa.alteracao.dados.cs"));
      alerta2.setTipo(Alerta.TIPO_OK);
      for (Cs cs : this.csDao.findAll()) {
         if (isDadosCsAlteradosForaDoPrazo(cs.getCodigo())) {
            alerta2.addMessage(cs.getCodigo() + " " + this.env.getProperty("predispensa.alteracao.dados.cs.msg"));
            alerta2.setTipo(Alerta.TIPO_ERROR);
            incrementAlertsCount(session);
         }
      }
      final Alerta alerta3 = new Alerta();
      alerta3.setTitulo(this.env.getProperty("predispensa.alteracao.tributacao"));
      alerta3.setTipo(Alerta.TIPO_OK);
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
      final List<Alerta> alertas = new ArrayList<Alerta>(3);
      alertas.add(alerta);
      alertas.add(alerta2);
      alertas.add(alerta3);
      return alertas;

   }

   public List<Alerta> getSelecaoAlerta() {
      final Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("selecao.periodo.funcionamento.ano.atual"));
      alerta.setTipo(Alerta.TIPO_OK);
      final Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("selecao.periodo.funcionamento.proxomo.ano"));
      alerta2.setTipo(Alerta.TIPO_OK);
      final List<Alerta> alertas = new ArrayList<Alerta>(2);
      alertas.add(alerta);
      alertas.add(alerta2);
      return alertas;
   }

   public List<Alerta> getDistribuicaoAlerta(Map<String, Object> session) {
      final Alerta alerta1 = new Alerta();
      alerta1.setTitulo(this.env.getProperty("distribuicao.preenchimento.bolnec"));
      alerta1.setTipo(Alerta.TIPO_OK);
      // TODO alerta.addMessage("Erro tal e tal");
      // alerta.setTipo(Alerta.TIPO_ERROR);
      // toda vez que encontrar um erro mudar tipo de alerta para erro ou
      // warning.
      final Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("distribuicao.lancamento.parametros"));
      alerta2.setTipo(Alerta.TIPO_OK);
      final Alerta alerta3 = new Alerta();
      alerta3.setTitulo(this.env.getProperty("distribuicao.alteracao.grupos.distribuicao"));
      alerta3.setTipo(Alerta.TIPO_OK);
      final Alerta alerta4 = new Alerta();
      alerta4.setTitulo(this.env.getProperty("distribuicao.consolidacao.bolnec"));
      alerta4.setTipo(Alerta.TIPO_OK);
      final Alerta alerta5 = new Alerta();
      alerta5.setTitulo(this.env.getProperty("distribuicao.bcciap.carregamento"));
      alerta5.setTipo(Alerta.TIPO_OK);
      final List<Alerta> alertas = new ArrayList<Alerta>(5);
      alertas.add(alerta1);
      alertas.add(alerta2);
      alertas.add(alerta3);
      alertas.add(alerta4);
      alertas.add(alerta5);
      return alertas;
   }

   public List<Alerta> getSelecaoComplementarAlerta(Map<String, Object> session) {
      //TODO: implementar SelecaoComplementarAlerta
      final List<Alerta> alertas = new ArrayList<Alerta>();
      return alertas;
   }

   public boolean existeParametroDistribuicao(final Byte rmCodigo) {
      //TODO: implementar rmLancouParametrosDistribuicao
      return false;
   }

   public boolean isDadosCsAlteradosForaDoPrazo(final Integer csCodigo) {
      //TODO: implementar idDadosAlteradosDeCs
      return true;
   }

   public boolean isTributacaoJsmAlteradaForaDoPrazo(final Jsm jsm) {
      //TODO: isTributacaoDeJsmAlteradaForaDoPrazo
      return true;
   }

   public boolean isDistribuicaoProcessada() {
      //TODO: isDistribuicaoProcessada
      return false;
   }
   
}
