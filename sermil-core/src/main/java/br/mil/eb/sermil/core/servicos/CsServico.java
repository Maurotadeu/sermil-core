package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CselDao;
import br.mil.eb.sermil.core.dao.CselEnderecoDao;
import br.mil.eb.sermil.core.dao.CselFuncionamentoDao;
import br.mil.eb.sermil.core.dao.DominiosDao;
import br.mil.eb.sermil.core.dao.DstbBolNecDao;
import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.dao.OmDao;
import br.mil.eb.sermil.core.dao.PgcDao;
import br.mil.eb.sermil.core.dao.RmDao;
import br.mil.eb.sermil.core.exceptions.AnoBaseNaoEhUnicoException;
import br.mil.eb.sermil.core.exceptions.CsPersistErrorException;
import br.mil.eb.sermil.core.exceptions.EntityPersistenceException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoAnoBaseException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataInicioErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataTerminoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDeletarErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoFeriadoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoJaExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoNaoExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentosSobrepostosException;
import br.mil.eb.sermil.core.exceptions.PgcNaoExisteException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Csel;
import br.mil.eb.sermil.modelo.CselEndereco;
import br.mil.eb.sermil.modelo.CselFeriado;
import br.mil.eb.sermil.modelo.CselFuncionamento;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Om;
import br.mil.eb.sermil.modelo.Pgc;
import br.mil.eb.sermil.modelo.Rm;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.Alerta;

/**
 * Serviço de CS.
 * 
 * @author Anselmo Ribeiro, Abreu Lopes
 * @version 5.2.4
 * @since 5.2.8
 */
@Named("csServico")
@RemoteProxy(name = "csServico")
public class CsServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

   @Inject
   private Environment env;

   @Inject
   CselDao cselDao;

   @Inject
   CselFuncionamentoDao funcionamentoDao;

   @Inject
   RmDao rmDao;

   @Inject
   CselEnderecoDao enderecoDao;

   @Inject
   PgcDao pgcDao;

   @Inject
   JsmDao jsmDao;

   @Inject
   DstbBolNecDao bolNecDao;
   
   @Inject 
   OmDao omDao;

   @Inject
   DominiosDao dominiosDao;

   public CsServico() {
      logger.debug("CsServico iniciado");
   }

   public Map<Integer, String> getCselEnderecos(Integer cselCodigo) {
      Map<Integer, String> ret = new HashMap<Integer, String>();
      List<CselEndereco> enderecos = enderecoDao.findByNamedQuery("listarEnderecosDeCselNative", cselCodigo);
      enderecos.forEach(end -> ret.put(end.getCodigo(), end.toString()));
      return ret;
   }

   public List<CselFuncionamento> listarFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("Funcionamento.listarFuncionamentosDeCsel", cselCodigo);
   }

   public List<Csel> listarPorRM(Byte rm_codigo) {
      return (List<Csel>) cselDao.findByNamedQuery("Csel.listarPorRM", rm_codigo);
   }

   public List<Csel> listarPorRmDoUsuario(Byte rm_codigo, Usuario usu) {
      List<Csel> ret = new ArrayList<Csel>();
      if (usu.getAuthorities().stream().anyMatch(x -> x.getAuthority().contains("adm")))
         ret = cselDao.findByNamedQuery("Csel.listarPorRM", rm_codigo);
      else
         ret = cselDao.findByNamedQuery("Csel.listarPorRM", usu.getOm().getRm().getCodigo());
      return ret;
   }

   public List<Csel> listarPorNome(String nome) {
      return (List<Csel>) cselDao.findByNamedQuery("Csel.listarPorNome", nome);
   }

   @Transactional
   public Csel persistir(Csel cs) throws CsPersistErrorException {
      try {
         cs = cselDao.save(cs);
      } catch (Exception e) {
         logger.error(e.getMessage());
         e.printStackTrace(); // TODO: tirar stacktrace antes de entrar em
                              // producao
         throw new CsPersistErrorException();
      }
      return cs;
   }

   public Csel recuperar(Integer cs_codigo) {
      return cselDao.findById(cs_codigo);
   }

   public Map<String, String> getTributacoes() {
      HashMap<String, String> tributacoes = new HashMap<String, String>();
      tributacoes.put(Csel.TRIBUTACAO_EB, Csel.TRIBUTACAO_EB);
      tributacoes.put(Csel.TRIBUTACAO_FAB, Csel.TRIBUTACAO_FAB);
      tributacoes.put(Csel.TRIBUTACAO_MAR, Csel.TRIBUTACAO_MAR);
      tributacoes.put(Csel.TRIBUTACAO_TG, Csel.TRIBUTACAO_TG);
      return tributacoes;
   }

   /**
    * Obter RM.
    * 
    * @param usuRm
    *           Rm a rm do usuario. Tente: Rm rm = ((Usuario) ((SecurityContext)
    *           this.session.get("SPRING_SECURITY_CONTEXT")).getAuthentication()
    *           .getPrincipal()). getOm().getRm();
    * 
    * @param isAdm
    *           se o usuario é ou nao administrador Tente: boolean isAdm =
    *           ServletActionContext.getRequest().isUserInRole("adm");
    * 
    * @return Map codigo -> sigla
    */
   public Map<Byte, String> getRms(Rm usuRm, boolean isAdm) {
      Map<Byte, String> mappedRms = new HashMap<Byte, String>();
      List<Rm> rms = rmDao.findAll();
      for (int i = 0; i < rms.size(); i++) {
         if (rms.get(i).getCodigo() == 0) {
            rms.remove(i);
            break;
         }
      }
      for (Rm rm2 : rms) {
         if (isAdm)
            mappedRms.put(rm2.getCodigo(), rm2.getSigla());
         else if (usuRm.getCodigo() == rm2.getCodigo())
            mappedRms.put(rm2.getCodigo(), rm2.getSigla());
      }
      return mappedRms;
   }

   public CselEndereco getEndereco(Integer enderecoCodigo) {
      return enderecoDao.findById(enderecoCodigo);
   }

   @Transactional(propagation = Propagation.NESTED)
   public Csel salvarCselEFuncionamento(Csel cs, CselFuncionamento funcionamento, List<CselFeriado> feriados, CselEndereco endereco)
         throws FuncionamentoJaExisteException, CsPersistErrorException, AnoBaseNaoEhUnicoException, FuncionamentoDataInicioErroException, FuncionamentoDataTerminoErroException,
         FuncionamentoFeriadoErroException, FuncionamentosSobrepostosException, FuncionamentoAnoBaseException {

      feriados.forEach(fer -> {
         if (fer == null)
            feriados.remove(fer);
         else
            fer.setFuncionamento(funcionamento);
      });
      funcionamento.setFeriados(feriados);

      // Endereco
      if (endereco.getCodigo() != null)
         endereco = enderecoDao.findById(endereco.getCodigo());
      funcionamento.setEndereco(endereco);

      // Funcionamento
      cs.addFuncionamento(funcionamento);

      // persistir
      // if (isFuncionamentoDeCsCorreto(funcionamento) &&
      // isFeriadosDeFuncionamentoCorretos(feriados, funcionamento))
      persistir(cs);
      return cs;
   }

   /**
    * Regras de Negocio para Funcionamento de CS
    * 
    * @return boolean
    * @throws PgcNaoExisteException
    */
   public boolean isFuncionamentoDeCsCorreto(CselFuncionamento func) throws AnoBaseNaoEhUnicoException, FuncionamentoDataInicioErroException, FuncionamentoDataTerminoErroException,
         FuncionamentoFeriadoErroException, FuncionamentosSobrepostosException, FuncionamentoAnoBaseException, PgcNaoExisteException {

      // ano base de PGC tem que ser unico
      if (!this.isAnoBaseDePgcEhUnico(func.getAnoBase())) {
         logger.error("Exite um PGC com dois lancamento de ano base. Ano base: " + func.getAnoBase());
         throw new AnoBaseNaoEhUnicoException();
      }
      List<Pgc> pgcs = pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, func.getAnoBase());
      if (pgcs.size() < 1)
         throw new PgcNaoExisteException();

      Pgc p = pgcs.get(0);

      // O inicio da CS nao pode ser antes do inicio no PGC
      if (func.getInicioData().before(p.getSelecaoGeralInicio()))
         throw new FuncionamentoDataInicioErroException();

      // O termino da CS nao pode ser depois do PGC
      if (func.getTerminoData().after(p.getSelecaoGeralTermino()))
         throw new FuncionamentoDataTerminoErroException();

      // Os blocos de cada inicio e termino de funcionamento para o mesmo ano
      // base nao podem se
      // sobrepor
      List<CselFuncionamento> funcs = cselDao.findById(func.getCsel().getCodigo()).getFuncionamentos();
      for (CselFuncionamento f : funcs) {
         if (func.getInicioData().before(f.getTerminoData()) || func.getTerminoData().after(f.getInicioData()))
            throw new FuncionamentosSobrepostosException();
      }

      // A CS so pode cadastrar funcionamento com ano base ja cadastrado no PGC
      List<Pgc> ps = pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, func.getAnoBase());
      if (ps.size() == 0)
         throw new FuncionamentoAnoBaseException();

      return true;
   }

   /**
    * Regras de Negocio para Feriados de Funcionamento de CS.
    * 
    * @return boolean
    */
   public boolean isFeriadosDeFuncionamentoCorretos(List<CselFeriado> feriados, CselFuncionamento func) throws FuncionamentoFeriadoErroException {
      // Os feriados tem que estar dentro do periodo declarado
      for (CselFeriado fer : feriados) {
         if (fer.getFeriadoData().before(func.getInicioData()) || fer.getFeriadoData().after(func.getTerminoData()))
            throw new FuncionamentoFeriadoErroException();
      }
      return true;
   }

   public boolean isAnoBaseDePgcEhUnico(String anoBase) {
      List<Pgc> pgcs = pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, anoBase);
      int size = pgcs.size();
      return size > 1 ? false : true;
   }

   public List<CselFuncionamento> getFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("listarFuncionamentosDeCsel", cselCodigo);
   }

   @Transactional
   public void deletarFuncionamento(Integer codigo) throws FuncionamentoNaoExisteException, FuncionamentoDeletarErroException {
      CselFuncionamento func = funcionamentoDao.findById(codigo);
      if (func == null) {
         logger.error(new StringBuilder("Usuario tentou deletar Funcionamento de CS mas funcionamento nao existe. Funcionamento codigo =  ").append(codigo).toString());
         throw new FuncionamentoNaoExisteException();
      }
      try {
         funcionamentoDao.delete(func);
      } catch (SermilException e) {
         logger.error("Erro ao deletar funcionamento: " + func.toString());
         throw new FuncionamentoDeletarErroException();
      }
   }

   public List<Pgc> getPgcList() {
      return pgcDao.findAll();
   }

   public int rodarDistribuicao() {
      // TODO retornar de 0 a 100
      return 0;
   }

   public boolean distribuicaoJaRodou() {
      return false;
   }

   @Transactional
   public Pgc salvarPgc(Pgc pgc) throws EntityPersistenceException {
      try {
         return this.pgcDao.save(pgc);
      } catch (Exception e) {
         logger.error(e.getMessage());
         throw new EntityPersistenceException();
      }
   }

   public Map<String, List<Alerta>> getPgcAlertas(Map<String, Object> session) {
      Map<String, List<Alerta>> alertas = new LinkedHashMap<String, List<Alerta>>();
      alertas.put(this.env.getProperty("alistamento"), getAlistamentoAlerta(session));
      //alertas.put(this.env.getProperty("predispensa"), getPreDispensaAlerta(session));
      //alertas.put(this.env.getProperty("selecao"), getAlistamentoAlerta(session));
      alertas.put(this.env.getProperty("distribuicao"), getDistribuicaoAlerta(session));
      //alertas.put(this.env.getProperty("selecao.complementar"), getSelecaoComplementarAlerta(session));
      return alertas;
   } 
   
   private void incrementProcessados(Map<String, Object> session){
      session.put("processados", (int)session.get("processados")+1);
   }

   /**
    * 01 - Alistamento
    * @param session 
    */
   public List<Alerta> getAlistamentoAlerta(Map<String, Object> session) {
      List<Alerta> alertas = new ArrayList<Alerta>();

      // PGC - Lançamento dos dados do PGC para o ano atual
      Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("alistamento.lancamento.dados.ano.atual"));
      alerta.setTipo(Alerta.TIPO_OK);
      if (!isPgcLancadoParaAnoAtual()) {
         alerta.addMessage(this.env.getProperty("alistamento.lancamento.dados.ano.atual.motivo"));
         alerta.setTipo(Alerta.TIPO_ERROR);
         incrementProcessados(session);
      }
      alertas.add(alerta);

      // PGC - Lançamento dos dados do PGC para o próximo ano
      session.put("processados", (int)session.get("processados")+9);
      Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("alistamento.lancamento.dados.proximo.ano"));
      alerta2.setTipo(Alerta.TIPO_OK);
      if (!isPgcLancadoParaProximoAno()) {
         alerta2.addMessage(this.env.getProperty("alistamento.lancamento.dados.proximo.ano.motivo"));
         alerta2.setTipo(Alerta.TIPO_ERROR);
         incrementProcessados(session);
      }
      alertas.add(alerta2);

      return alertas;
   }

   /**
    * 02 - Pré Dispensa
    * @param session 
    */
   public List<Alerta> getPreDispensaAlerta(Map<String, Object> session) {
      List<Alerta> alertas = new ArrayList<Alerta>();

      // Pré-Dispensa - Lançamento de Parâmetros da Distribuição
      Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("predispensa.lancamento.parametros.distribuicao"));
      alerta.setTipo(Alerta.TIPO_OK);
      alertas.add(alerta); // TODO criar alerta

      // Pré-Dispensa - Alteração de dados de CS
      Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("predispensa.alteracao.dados.cs"));
      alerta2.setTipo(Alerta.TIPO_OK);
      for (Csel cs : cselDao.findAll()) {
         if (isDadosDeCsAlteradosForaDoPrazo(cs.getCodigo())) {
            alerta2.addMessage(cs.getCodigo() + " " + this.env.getProperty("predispensa.alteracao.dados.cs.msg"));
            alerta2.setTipo(Alerta.TIPO_ERROR);
            incrementProcessados(session);
         }
      }
      alertas.add(alerta2);

      // Pré-Dispensa - Alteração de tributação de município
      Alerta alerta3 = new Alerta();
      alerta3.setTitulo(this.env.getProperty("predispensa.alteracao.tributacao"));
      alerta3.setTipo(Alerta.TIPO_OK);
      // TODO achar jsm que mudaram tributacao
      List<Jsm> jsms = new ArrayList<Jsm>();
      jsms.add(new Jsm());
      for (Jsm jsm : jsms) {
         if (isTributacaoDeJsmAlteradaForaDoPrazo(jsm.getCsmCodigo(), jsm.getCodigo())) {
            alerta3.addMessage(
                  new StringBuilder().append(jsm.getCodigo()).append("/").append(jsm.getCsmCodigo()).append(" ").append(this.env.getProperty("predispensa.alteracao.tributacao.msg")).toString());
            alerta3.setTipo(Alerta.TIPO_ERROR);
            incrementProcessados(session);
         }
      }
      alertas.add(alerta3);

      return alertas;

   }

   /**
    * 03 - Seleção
    */
   public List<Alerta> getSelecaoAlerta() {
      List<Alerta> alertas = new ArrayList<Alerta>();

      // CS - Preenchimento do período de funcionamento para o ano atual por CS
      Alerta alerta = new Alerta();
      alerta.setTitulo(this.env.getProperty("selecao.periodo.funcionamento.ano.atual"));
      alerta.setTipo(Alerta.TIPO_OK);
      alertas.add(alerta);

      // CS - Preenchimento do período de funcionamento para o proximo ano por
      // CS
      Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("selecao.periodo.funcionamento.proxomo.ano"));
      alerta2.setTipo(Alerta.TIPO_OK);
      alertas.add(alerta2);

      return alertas;

   }

   /**
    * 4 - DISTRIBUICAO
    * @param session 
    */
   @Transactional
   public List<Alerta> getDistribuicaoAlerta(Map<String, Object> session) {
      List<Alerta> alertas = new ArrayList<Alerta>();

      // Distribuição - Om Sem BolNec na RM
      Alerta alerta1 = new Alerta();
      alerta1.setTitulo("Distribuição - Om Sem BolNec na RM");
      alerta1.setTipo(Alerta.TIPO_OK);
      List<String> rmCodigos = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
      rmCodigos.forEach(c->{
         omDao.findByNamedQuery("alerta.OmSemBolnecNaRm", c).forEach(om->{
            alerta1.addMessage(om.toString() + " não tem BolNec na " + om.getRm().toString());
            alerta1.setTipo(Alerta.TIPO_WARNING);
            incrementProcessados(session);
         });
      });
      alertas.add(alerta1);

      // Distribuição - Lançamento de Parâmetros de Distribuição
      Alerta alerta2 = new Alerta();
      alerta2.setTitulo(this.env.getProperty("distribuicao.lancamento.parametros"));
      alerta2.setTipo(Alerta.TIPO_OK);
      rmCodigos.forEach(c -> {
         dominiosDao.findByNamedQuery("rm.RmNaoLancouParametroDistribuicao", c).forEach(dom -> {
            alerta2.addMessage("A " + c + " RM nao lancou os parametro de distribuicao de OM Tipo " + dom.getDescricao());
            alerta2.setTipo(Alerta.TIPO_ERROR);
            incrementProcessados(session);
         });
      });
      alertas.add(alerta2);

      // Distribuição - Carregamento de BCC-IAP
      Alerta alerta5 = new Alerta();
      alerta5.setTitulo(this.env.getProperty("distribuicao.bcciap.carregamento"));
      alerta5.setTipo(Alerta.TIPO_OK);
      rmCodigos.forEach(rmCodigo -> {
         List<Rm> rms = rmDao.findByNamedQuery("rm.rmComProblemaDeBCCIAP", rmCodigo);
         if (rms.size() == 1) {
            alerta5.addMessage(rms.get(0).toString() + " está com discrepância de BCC/IAP");
            alerta5.setTipo(Alerta.TIPO_ERROR);
            incrementProcessados(session);
         }
      });
      alertas.add(alerta5);

      return alertas;
   }

   /**
    * 05 - Seleção Complementar
    * @param session 
    */
   public List<Alerta> getSelecaoComplementarAlerta(Map<String, Object> session) {
      List<Alerta> alertas = new ArrayList<Alerta>();
      return alertas;
   }

   public boolean isPgcLancadoParaAnoAtual() {
      int year = Calendar.getInstance().get(Calendar.YEAR);
      List<Pgc> pgcs = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, String.valueOf(year));
      if (pgcs.size() > 0)
         return true;
      return false;
   }

   public boolean isPgcLancadoParaProximoAno() {
      int year = Calendar.getInstance().get(Calendar.YEAR);
      List<Pgc> pgcs = this.pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, String.valueOf(year + 1));
      if (pgcs.size() > 0)
         return true;
      return false;
   }

   public boolean isDadosDeCsAlteradosForaDoPrazo(Integer csCodigo) {
      // TODO implementar idDadosAlteradosDeCs
      return true;
   }

   public boolean isTributacaoDeJsmAlteradaForaDoPrazo(Byte csmCodigo, Short jsmCodigo) {
      return true;
   }

}
