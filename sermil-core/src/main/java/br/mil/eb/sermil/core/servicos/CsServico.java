package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.CsEnderecoDao;
import br.mil.eb.sermil.core.dao.CsFuncionamentoDao;
import br.mil.eb.sermil.core.dao.DominiosDao;
import br.mil.eb.sermil.core.dao.DstbBolNecDao;
import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.dao.OmDao;
import br.mil.eb.sermil.core.dao.PgcDao;
import br.mil.eb.sermil.core.dao.RmDao;
import br.mil.eb.sermil.core.exceptions.AnoBaseNaoEhUnicoException;
import br.mil.eb.sermil.core.exceptions.ConsultaException;
import br.mil.eb.sermil.core.exceptions.EntityPersistenceException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoAnoBaseException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataInicioErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataTerminoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDeletarErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoFeriadoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoNaoExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentosSobrepostosException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.CsEndereco;
import br.mil.eb.sermil.modelo.CsExclusaoData;
import br.mil.eb.sermil.modelo.CsFeriado;
import br.mil.eb.sermil.modelo.CsFuncionamento;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Pgc;
import br.mil.eb.sermil.modelo.Rm;
import br.mil.eb.sermil.tipos.Alerta;

/**
 * Serviço de CS.
 * 
 * @author Anselmo Ribeiro, Abreu Lopes
 * @version 5.2.4
 * @since 5.3.2
 */
@Named("csServico")
@RemoteProxy(name = "csServico")
public class CsServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

   @Inject
   CsDao csDao;

   @Inject
   CsEnderecoDao csEnderecoDao;

   @Inject
   CsFuncionamentoDao funcionamentoDao;

   @Inject
   RmDao rmDao;

   @Inject
   CsEnderecoDao enderecoDao;

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

   @Inject
   PgcServico pgcServico;
   
   @Inject
   Environment env;

   public CsServico() {
      logger.debug("CsServico iniciado");
   }

   @RemoteMethod
   public List<Cs> listarPorRm(final Byte rm) throws ConsultaException {
      if (rm == null) {
         throw new ConsultaException("Informe o número da Região Militar");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorRm", rm);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS cadastrada na " + rm + "ª Região Militar");
      }
      return lista;
   }

   public List<Cs> listarPorNome(final String nome) throws ConsultaException {
      if (nome == null) {
         throw new ConsultaException("Informe o nome da CS");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorNome", nome);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS cadastra com o nome " + nome + ".");
      }
      return lista;
   }

   public List<CsEndereco> listarCsEnderecoMun(final Integer municipioCodigo) throws ConsultaException {
      if (municipioCodigo == null) {
         throw new ConsultaException("Informe o código do Município");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorMunicipio", municipioCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados no município código " + municipioCodigo);
      }
      return lista;
   }

   @RemoteMethod
   public List<CsEndereco> listarCsEnderecoRm(final Byte rmCodigo) throws ConsultaException {
      if (rmCodigo == null) {
         throw new ConsultaException("Informe o código da Região Militar");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorRm", rmCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados na " + rmCodigo + "ª Região Militar");
      }
      return lista;
   }

   @RemoteMethod
   public List<CsFuncionamento> listarCsFuncionamento(final Integer csCodigo) throws ConsultaException {
      if (csCodigo == null) {
         throw new ConsultaException("Informe o código da CS");
      }
      final List<CsFuncionamento> lista = this.recuperar(csCodigo).getCsFuncionamentoCollection();
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há funcionamentos cadastrados para a CS " + csCodigo);
      }
      return lista;
   }

   @RemoteMethod
   public List<CsExclusaoData> listarCsExclusaoData(final Integer csCodigo) throws ConsultaException {
      if (csCodigo == null) {
         throw new ConsultaException("Informe o código da CS");
      }
      final List<CsExclusaoData> lista = this.recuperar(csCodigo).getCsExclusaoDataCollection();
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há exclusões de data cadastradas para a CS " + csCodigo);
      }
      return lista;
   }

   public Cs recuperar(final Integer csCodigo) {
      return this.csDao.findById(csCodigo);
   }

   public CsEndereco recuperarEndereco(final Integer codigo) throws SermilException {
      if (codigo == null) {
         throw new SermilException("Informe o código do Endereço");
      }
      final CsEndereco end = this.csEnderecoDao.findById(codigo);
      if (end == null) {
         throw new NoDataFoundException(new StringBuilder("Endereço não foi encontrado (cod=").append(codigo).append(")").toString());
      }
      return end;
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String excluir(final Integer csCodigo) throws SermilException {
      final Cs cs = this.recuperar(csCodigo);
      this.csDao.delete(cs);
      logger.info("Excluída: {}", cs);
      return new StringBuilder(cs.toString()).append(" excluida").toString();
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String excluirEndereco(final Integer codigo) throws SermilException {
      final CsEndereco csEnd = this.recuperarEndereco(codigo);
      this.csEnderecoDao.delete(csEnd);
      logger.info("Excluído: {}", csEnd);
      return new StringBuilder(csEnd.toString()).append(" excluido").toString();
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String salvar(final Cs cs) throws SermilException {
      final Cs csSalva = this.csDao.save(cs);
      logger.info("Salvo: {}", csSalva);
      return new StringBuilder(cs.toString()).append(" salva").toString();
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String salvarEndereco(final CsEndereco csEndereco) throws SermilException {
      final CsEndereco csEnd = this.csEnderecoDao.save(csEndereco);
      logger.info("Salvo: {}", csEnd);
      return new StringBuilder(csEnd.toString()).append(" salvo").toString();
   }

   /* Verificar */
   public boolean isCsFuncionamentoCorreto(final CsFuncionamento funcionamento) throws SermilException {
      // ano base de PGC tem que ser unico
      if (!this.pgcServico.isAnoBaseUnico(funcionamento.getAnoBase())) {
         logger.error("Exite um PGC com dois lançamentos de ano base. Ano base: " + funcionamento.getAnoBase());
         throw new AnoBaseNaoEhUnicoException();
      }
      
      final Pgc pgc = this.pgcServico.listarPcg(funcionamento.getAnoBase()).get(0);

      // O inicio da CS nao pode ser antes do inicio no PGC
      if (funcionamento.getInicioData().before(pgc.getSelecaoGeralInicio()))
         throw new FuncionamentoDataInicioErroException();

      // O termino da CS nao pode ser depois do PGC
      if (funcionamento.getTerminoData().after(pgc.getSelecaoGeralTermino()))
         throw new FuncionamentoDataTerminoErroException();

      // Os blocos de cada inicio e termino de funcionamento para o mesmo ano
      // base nao podem se
      // sobrepor
      final List<CsFuncionamento> listaFunc = this.csDao.findById(funcionamento.getCs().getCodigo()).getCsFuncionamentoCollection();
      for (CsFuncionamento f : listaFunc) {
         if (funcionamento.getInicioData().before(f.getTerminoData()) || funcionamento.getTerminoData().after(f.getInicioData()))
            throw new FuncionamentosSobrepostosException();
      }

      // A CS so pode cadastrar funcionamento com ano base ja cadastrado no PGC
      List<Pgc> ps = pgcDao.findByNamedQuery(Pgc.NQ_FINDBY_ANO_BASE, funcionamento.getAnoBase());
      if (ps.size() == 0)
         throw new FuncionamentoAnoBaseException();

      return true;
   }

   /**
    * Regras de Negocio para Feriados de Funcionamento de CS.
    * 
    * @return boolean
    */
   public boolean isFeriadosDeFuncionamentoCorretos(List<CsFeriado> feriados, CsFuncionamento func) throws FuncionamentoFeriadoErroException {
      // Os feriados tem que estar dentro do periodo declarado
      for (CsFeriado fer : feriados) {
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

   public List<CsFuncionamento> getFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("listarFuncionamentosDeCsel", cselCodigo);
   }

   @Transactional
   public void deletarFuncionamento(Integer codigo) throws FuncionamentoNaoExisteException, FuncionamentoDeletarErroException {
      CsFuncionamento func = funcionamentoDao.findById(codigo);
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
      // alertas.put(this.env.getProperty("predispensa"),
      // getPreDispensaAlerta(session));
      // alertas.put(this.env.getProperty("selecao"),
      // getAlistamentoAlerta(session));
      alertas.put(this.env.getProperty("distribuicao"), getDistribuicaoAlerta(session));
      // alertas.put(this.env.getProperty("selecao.complementar"),
      // getSelecaoComplementarAlerta(session));
      return alertas;
   }

   private void incrementProcessados(Map<String, Object> session) {
      session.put("processados", (int) session.get("processados") + 1);
   }

   /**
    * 01 - Alistamento
    * 
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
      session.put("processados", (int) session.get("processados") + 9);
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
    * 
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
      for (Cs cs : csDao.findAll()) {
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
    * 
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
      rmCodigos.forEach(c -> {
         omDao.findByNamedQuery("alerta.OmSemBolnecNaRm", c).forEach(om -> {
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
    * 
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
      //TODO implementar
      return true;
   }

}
