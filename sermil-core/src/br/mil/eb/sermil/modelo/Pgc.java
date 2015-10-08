package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Comissao de Selecao
 * 
 * @author Anselmo Ribeiro
 * @since 5.2.3
 */
@Entity
@Table(name = "PGC")
@NamedQuery(name = "findByAnoBase", query = "select p from pgc p where p.anoBase = ?1 ")
public final class Pgc implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 6479557479756080684L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "PGC")
   @TableGenerator(name = "PGC", allocationSize = 1)
   private Integer codigo;

   @Column(name = "classe", nullable = false, length = 4)
   private String classe;

   @Column(name = "ANO_BASE", unique = true)
   private String anoBase;

   /**
    * ALISTAMENTO
    */
   @Column(name = "ALISTAMENTO_DENTRO_PRAZO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoDentroPrazoInicio;

   @Column(name = "ALISTAMENTO_DENTRO_PRAZO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoDentroPrazoTermino;

   @Column(name = "ALISTAMENTO_FORA_PRAZO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoForaPrazoInicio;

   @Column(name = "ALISTAMENTO_FORA_PRAZO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoForaPrazoTermino;

   /* CA = CLASSES ANTERIORES */
   // Alistados de janeiro a junho
   @Column(name = "ALISTAMENTO_CA_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoCaInicio;

   // Alistados de janeiro a junho
   @Column(name = "ALISTAMENTO_CA_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoCaTermino;

   // Alistados de julho a dezembro
   @Column(name = "ALISTAMENTO_CA_2PERIODO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoCa2PeriodoInicio;

   // Alistados de julho a dezembro
   @Column(name = "ALISTAMENTO_CA_2PERIODO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date alistamentoCa2PeriodoTermino;

   /**
    * PRE DISPENSA
    */
   @Column(name = "PREDISPENSA_CS_ALTERACAO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaCsAlteracaoInicio;

   @Column(name = "PREDISPENSA_CS_ALTERACAO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaCsAlteracaoTermino;

   @Column(name = "PREDISPENSA_TRIBUTACAO_ALTERACAO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaTributacaoAlteracaoInicio;

   @Column(name = "PREDISPENSA_TRIBUTACAO_ALTERACAO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaTributacaoAlteracaoTermino;

   @Column(name = "PREDISPENSA_PARAMETRO_LANCAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaParametroLancamentoInicio;

   @Column(name = "PREDISPENSA__PARAMETRO_LANCAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaParametroLancamentoTermino;

   @Column(name = "PREDISPENSA_PROCESSAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaProcessamentoInicio;

   @Column(name = "PREDISPENSA__PROCESSAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date predispensaProcessamentoTermino;

   /**
    * SELECAO
    */
   @Column(name = "SELECAO_GERAL_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralInicio;

   @Column(name = "SELECAO_GERAL_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralTermino;

   @Column(name = "SELECAO_GERAL_OMA_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralOmaInicio;

   @Column(name = "SELECAO_GERAL_OMA_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralOmaTermino;

   @Column(name = "SELECAO_GERAL_TG_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralTgInicio;

   @Column(name = "SELECAO_GERAL_TG_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralTgTermino;

   @Column(name = "SELECAO_GERAL_ESIM_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralEsimInicio;

   @Column(name = "SELECAO_GERAL_ESIM_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralEsimTermino;

   @Column(name = "SELECAO_GERAL_CPOR_NPOR_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralCporNporInicio;

   @Column(name = "SELECAO_GERAL_CPOR_NPOR_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralCporNporTermino;

   @Column(name = "SELECAO_GERAL_MFDV_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralMfdvInicio;

   @Column(name = "SELECAO_GERAL_MFDV_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoGeralMfdvTermino;

   /**
    * SELECAO COMPLEMENTAR
    */

   @Column(name = "SELECAO_COMPLEMENTAR_GPTA_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarGptaInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_GPTA_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarGptaTermino;

   @Column(name = "SELECAO_COMPLEMENTAR_GPTB_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarGptbInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_GPTB_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarGptbTermino;

   @Column(name = "SELECAO_COMPLEMENTAR_NPOR_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarNporInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_NPOR_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarNporTermino;

   @Column(name = "SELECAO_COMPLEMENTAR_TG_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarTgInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_TG_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarTgTermino;

   @Column(name = "SELECAO_COMPLEMENTAR_ESIM_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarEsimInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_ESIM_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarEsimTermino;

   @Column(name = "SELECAO_COMPLEMENTAR_MFDV_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarMfdvInicio;

   @Column(name = "SELECAO_COMPLEMENTAR_MFDV_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date selecaoComplementarMfdvTermino;

   /**
    * DISTRIBUICAO
    */
   @Column(name = "DISTRIBUICAO_CONHECIMENTO_GPTA_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoGptaInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_GPTA_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoGptaTermino;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_GPTB_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoGptbInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_GPTB_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoGptbTermino;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_MFDV_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoMfdvInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_MFDV_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoMfdvTermino;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_CPOR_NPOR_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoCporNporInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_CPOR_NPOR_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoCporNporTermino;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_TG_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhemcimentoTgInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_TG_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoTgTermino;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_ESIM_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoEsimInicio;

   @Column(name = "DISTRIBUICAO_CONHECIMENTO_ESIM_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoConhecimentoEsimTermino;

   @Column(name = "DISTRIBUICAO_BOLNEC_LANCAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoBolnedLancamentoInicio;

   @Column(name = "DISTRIBUICAO_BOLNEC_LANCAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoBolnecLancamentoTermino;

   @Column(name = "DISTRIBUICAO_PARAMETRO_LANCAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoParametroLancamentoInicio;

   @Column(name = "DISTRIBUICAO_PARAMETRO_LANCAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoParametroLancamentoTermino;

   @Column(name = "DISTRIBUICAO_GD_LANCAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoGdLancamentoInicio;

   @Column(name = "DISTRIBUICAO_GD_LANCAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoGdLancamentoTermino;

   @Column(name = "DISTRIBUICAO_BOLNEC_CONSOLIDACAO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoBolnecConsolidacaoInicio;

   @Column(name = "DISTRIBUICAO_BOLNEC_CONSOLIDACAO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoBolnecConsolidacaoTermino;

   @Column(name = "DISTRIBUICAO_PROCESSAMENTO_INICIO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoProcessamentoInicio;

   @Column(name = "DISTRIBUICAO_PROCESSAMENTO_TERMINO", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date distribuicaoProcessamentoTermino;

   /**
    * SELECAO COMPLEMENTAR
    */
   @Column(name = "INCORPORACAO_GPTA", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoGpta;

   @Column(name = "INCORPORACAO_TG", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoTg;

   @Column(name = "INCORPORACAO_MFDV", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoMdfv;

   @Column(name = "INCORPORACAO_GPTB", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoGptb;

   @Column(name = "INCORPORACAO_SVTT", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoSvtt;

   @Column(name = "INCORPORACAO_EIPOT", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoEipot;

   @Column(name = "INCORPORACAO_EIC", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date incorporacaoEic;

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public String getClasse() {
      return classe;
   }

   public void setClasse(String classe) {
      this.classe = classe;
   }

   public Date getAlistamentoCaInicio() {
      return alistamentoCaInicio;
   }

   public void setAlistamentoCaInicio(Date alistamentoCaInicio) {
      this.alistamentoCaInicio = alistamentoCaInicio;
   }

   public Date getAlistamentoCaTermino() {
      return alistamentoCaTermino;
   }

   public void setAlistamentoCaTermino(Date alistamentoCaTermino) {
      this.alistamentoCaTermino = alistamentoCaTermino;
   }

   public Date getPredispensaCsAlteracaoInicio() {
      return predispensaCsAlteracaoInicio;
   }

   public void setPredispensaCsAlteracaoInicio(Date predispensaCsAlteracaoInicio) {
      this.predispensaCsAlteracaoInicio = predispensaCsAlteracaoInicio;
   }

   public Date getPredispensaCsAlteracaoTermino() {
      return predispensaCsAlteracaoTermino;
   }

   public void setPredispensaCsAlteracaoTermino(Date predispensaCsAlteracaoTermino) {
      this.predispensaCsAlteracaoTermino = predispensaCsAlteracaoTermino;
   }

   public Date getPredispensaTributacaoAlteracaoInicio() {
      return predispensaTributacaoAlteracaoInicio;
   }

   public void setPredispensaTributacaoAlteracaoInicio(Date predispensaTributacaoAlteracaoInicio) {
      this.predispensaTributacaoAlteracaoInicio = predispensaTributacaoAlteracaoInicio;
   }

   public Date getPredispensaTributacaoAlteracaoTermino() {
      return predispensaTributacaoAlteracaoTermino;
   }

   public void setPredispensaTributacaoAlteracaoTermino(Date predispensaTributacaoAlteracaoTermino) {
      this.predispensaTributacaoAlteracaoTermino = predispensaTributacaoAlteracaoTermino;
   }

   public Date getPredispensaParametroLancamentoInicio() {
      return predispensaParametroLancamentoInicio;
   }

   public void setPredispensaParametroLancamentoInicio(Date predispensaParametroLancamentoInicio) {
      this.predispensaParametroLancamentoInicio = predispensaParametroLancamentoInicio;
   }

   public Date getPredispensaParametroLancamentoTermino() {
      return predispensaParametroLancamentoTermino;
   }

   public void setPredispensaParametroLancamentoTermino(Date predispensaParametroLancamentoTermino) {
      this.predispensaParametroLancamentoTermino = predispensaParametroLancamentoTermino;
   }

   public Date getPredispensaProcessamentoInicio() {
      return predispensaProcessamentoInicio;
   }

   public void setPredispensaProcessamentoInicio(Date predispensaProcessamentoInicio) {
      this.predispensaProcessamentoInicio = predispensaProcessamentoInicio;
   }

   public Date getPredispensaProcessamentoTermino() {
      return predispensaProcessamentoTermino;
   }

   public void setPredispensaProcessamentoTermino(Date predispensaProcessamentoTermino) {
      this.predispensaProcessamentoTermino = predispensaProcessamentoTermino;
   }

   public Date getSelecaoGeralOmaInicio() {
      return selecaoGeralOmaInicio;
   }

   public void setSelecaoGeralOmaInicio(Date selecaoGeralOmaInicio) {
      this.selecaoGeralOmaInicio = selecaoGeralOmaInicio;
   }

   public Date getSelecaoGeralOmaTermino() {
      return selecaoGeralOmaTermino;
   }

   public void setSelecaoGeralOmaTermino(Date selecaoGeralOmaTermino) {
      this.selecaoGeralOmaTermino = selecaoGeralOmaTermino;
   }

   public Date getSelecaoGeralTgInicio() {
      return selecaoGeralTgInicio;
   }

   public void setSelecaoGeralTgInicio(Date selecaoGeralTgInicio) {
      this.selecaoGeralTgInicio = selecaoGeralTgInicio;
   }

   public Date getSelecaoGeralTgTermino() {
      return selecaoGeralTgTermino;
   }

   public void setSelecaoGeralTgTermino(Date selecaoGeralTgTermino) {
      this.selecaoGeralTgTermino = selecaoGeralTgTermino;
   }

   public Date getSelecaoGeralEsimInicio() {
      return selecaoGeralEsimInicio;
   }

   public void setSelecaoGeralEsimInicio(Date selecaoGeralEsimInicio) {
      this.selecaoGeralEsimInicio = selecaoGeralEsimInicio;
   }

   public Date getSelecaoGeralEsimTermino() {
      return selecaoGeralEsimTermino;
   }

   public void setSelecaoGeralEsimTermino(Date selecaoGeralEsimTermino) {
      this.selecaoGeralEsimTermino = selecaoGeralEsimTermino;
   }

   public Date getSelecaoGeralCporNporInicio() {
      return selecaoGeralCporNporInicio;
   }

   public void setSelecaoGeralCporNporInicio(Date selecaoGeralCporNporInicio) {
      this.selecaoGeralCporNporInicio = selecaoGeralCporNporInicio;
   }

   public Date getSelecaoGeralCporNporTermino() {
      return selecaoGeralCporNporTermino;
   }

   public void setSelecaoGeralCporNporTermino(Date selecaoGeralCporNporTermino) {
      this.selecaoGeralCporNporTermino = selecaoGeralCporNporTermino;
   }

   public Date getSelecaoGeralMfdvInicio() {
      return selecaoGeralMfdvInicio;
   }

   public void setSelecaoGeralMfdvInicio(Date selecaoGeralMfdvInicio) {
      this.selecaoGeralMfdvInicio = selecaoGeralMfdvInicio;
   }

   public Date getSelecaoGeralMfdvTermino() {
      return selecaoGeralMfdvTermino;
   }

   public void setSelecaoGeralMfdvTermino(Date selecaoGeralMfdvTermino) {
      this.selecaoGeralMfdvTermino = selecaoGeralMfdvTermino;
   }

   public Date getSelecaoComplementarGptaInicio() {
      return selecaoComplementarGptaInicio;
   }

   public void setSelecaoComplementarGptaInicio(Date selecaoComplementarGptaInicio) {
      this.selecaoComplementarGptaInicio = selecaoComplementarGptaInicio;
   }

   public Date getSelecaoComplementarGptaTermino() {
      return selecaoComplementarGptaTermino;
   }

   public void setSelecaoComplementarGptaTermino(Date selecaoComplementarGptaTermino) {
      this.selecaoComplementarGptaTermino = selecaoComplementarGptaTermino;
   }

   public Date getSelecaoComplementarGptbInicio() {
      return selecaoComplementarGptbInicio;
   }

   public void setSelecaoComplementarGptbInicio(Date selecaoComplementarGptbInicio) {
      this.selecaoComplementarGptbInicio = selecaoComplementarGptbInicio;
   }

   public Date getSelecaoComplementarGptbTermino() {
      return selecaoComplementarGptbTermino;
   }

   public void setSelecaoComplementarGptbTermino(Date selecaoComplementarGptbTermino) {
      this.selecaoComplementarGptbTermino = selecaoComplementarGptbTermino;
   }

   public Date getSelecaoComplementarNporInicio() {
      return selecaoComplementarNporInicio;
   }

   public void setSelecaoComplementarNporInicio(Date selecaoComplementarNporInicio) {
      this.selecaoComplementarNporInicio = selecaoComplementarNporInicio;
   }

   public Date getSelecaoComplementarNporTermino() {
      return selecaoComplementarNporTermino;
   }

   public void setSelecaoComplementarNporTermino(Date selecaoComplementarNporTermino) {
      this.selecaoComplementarNporTermino = selecaoComplementarNporTermino;
   }

   public Date getSelecaoComplementarTgInicio() {
      return selecaoComplementarTgInicio;
   }

   public void setSelecaoComplementarTgInicio(Date selecaoComplementarTgInicio) {
      this.selecaoComplementarTgInicio = selecaoComplementarTgInicio;
   }

   public Date getSelecaoComplementarTgTermino() {
      return selecaoComplementarTgTermino;
   }

   public void setSelecaoComplementarTgTermino(Date selecaoComplementarTgTermino) {
      this.selecaoComplementarTgTermino = selecaoComplementarTgTermino;
   }

   public Date getSelecaoComplementarEsimInicio() {
      return selecaoComplementarEsimInicio;
   }

   public void setSelecaoComplementarEsimInicio(Date selecaoComplementarEsimInicio) {
      this.selecaoComplementarEsimInicio = selecaoComplementarEsimInicio;
   }

   public Date getSelecaoComplementarEsimTermino() {
      return selecaoComplementarEsimTermino;
   }

   public void setSelecaoComplementarEsimTermino(Date selecaoComplementarEsimTermino) {
      this.selecaoComplementarEsimTermino = selecaoComplementarEsimTermino;
   }

   public Date getSelecaoComplementarMfdvInicio() {
      return selecaoComplementarMfdvInicio;
   }

   public void setSelecaoComplementarMfdvInicio(Date selecaoComplementarMfdvInicio) {
      this.selecaoComplementarMfdvInicio = selecaoComplementarMfdvInicio;
   }

   public Date getSelecaoComplementarMfdvTermino() {
      return selecaoComplementarMfdvTermino;
   }

   public void setSelecaoComplementarMfdvTermino(Date selecaoComplementarMfdvTermino) {
      this.selecaoComplementarMfdvTermino = selecaoComplementarMfdvTermino;
   }

   public Date getDistribuicaoConhecimentoGptaInicio() {
      return distribuicaoConhecimentoGptaInicio;
   }

   public void setDistribuicaoConhecimentoGptaInicio(Date distribuicaoConhecimentoGptaInicio) {
      this.distribuicaoConhecimentoGptaInicio = distribuicaoConhecimentoGptaInicio;
   }

   public Date getDistribuicaoConhecimentoGptaTermino() {
      return distribuicaoConhecimentoGptaTermino;
   }

   public void setDistribuicaoConhecimentoGptaTermino(Date distribuicaoConhecimentoGptaTermino) {
      this.distribuicaoConhecimentoGptaTermino = distribuicaoConhecimentoGptaTermino;
   }

   public Date getDistribuicaoConhecimentoGptbInicio() {
      return distribuicaoConhecimentoGptbInicio;
   }

   public void setDistribuicaoConhecimentoGptbInicio(Date distribuicaoConhecimentoGptbInicio) {
      this.distribuicaoConhecimentoGptbInicio = distribuicaoConhecimentoGptbInicio;
   }

   public Date getDistribuicaoConhecimentoGptbTermino() {
      return distribuicaoConhecimentoGptbTermino;
   }

   public void setDistribuicaoConhecimentoGptbTermino(Date distribuicaoConhecimentoGptbTermino) {
      this.distribuicaoConhecimentoGptbTermino = distribuicaoConhecimentoGptbTermino;
   }

   public Date getDistribuicaoConhecimentoMfdvInicio() {
      return distribuicaoConhecimentoMfdvInicio;
   }

   public void setDistribuicaoConhecimentoMfdvInicio(Date distribuicaoConhecimentoMfdvInicio) {
      this.distribuicaoConhecimentoMfdvInicio = distribuicaoConhecimentoMfdvInicio;
   }

   public Date getDistribuicaoConhecimentoMfdvTermino() {
      return distribuicaoConhecimentoMfdvTermino;
   }

   public void setDistribuicaoConhecimentoMfdvTermino(Date distribuicaoConhecimentoMfdvTermino) {
      this.distribuicaoConhecimentoMfdvTermino = distribuicaoConhecimentoMfdvTermino;
   }

   public Date getDistribuicaoConhecimentoCporNporInicio() {
      return distribuicaoConhecimentoCporNporInicio;
   }

   public void setDistribuicaoConhecimentoCporNporInicio(Date distribuicaoConhecimentoCporNporInicio) {
      this.distribuicaoConhecimentoCporNporInicio = distribuicaoConhecimentoCporNporInicio;
   }

   public Date getDistribuicaoConhecimentoCporNporTermino() {
      return distribuicaoConhecimentoCporNporTermino;
   }

   public void setDistribuicaoConhecimentoCporNporTermino(Date distribuicaoConhecimentoCporNporTermino) {
      this.distribuicaoConhecimentoCporNporTermino = distribuicaoConhecimentoCporNporTermino;
   }

   public Date getDistribuicaoConhemcimentoTgInicio() {
      return distribuicaoConhemcimentoTgInicio;
   }

   public void setDistribuicaoConhemcimentoTgInicio(Date distribuicaoConhemcimentoTgInicio) {
      this.distribuicaoConhemcimentoTgInicio = distribuicaoConhemcimentoTgInicio;
   }

   public Date getDistribuicaoConhecimentoTgTermino() {
      return distribuicaoConhecimentoTgTermino;
   }

   public void setDistribuicaoConhecimentoTgTermino(Date distribuicaoConhecimentoTgTermino) {
      this.distribuicaoConhecimentoTgTermino = distribuicaoConhecimentoTgTermino;
   }

   public Date getDistribuicaoConhecimentoEsimInicio() {
      return distribuicaoConhecimentoEsimInicio;
   }

   public void setDistribuicaoConhecimentoEsimInicio(Date distribuicaoConhecimentoEsimInicio) {
      this.distribuicaoConhecimentoEsimInicio = distribuicaoConhecimentoEsimInicio;
   }

   public Date getDistribuicaoConhecimentoEsimTermino() {
      return distribuicaoConhecimentoEsimTermino;
   }

   public void setDistribuicaoConhecimentoEsimTermino(Date distribuicaoConhecimentoEsimTermino) {
      this.distribuicaoConhecimentoEsimTermino = distribuicaoConhecimentoEsimTermino;
   }

   public Date getDistribuicaoBolnedLancamentoInicio() {
      return distribuicaoBolnedLancamentoInicio;
   }

   public void setDistribuicaoBolnedLancamentoInicio(Date distribuicaoBolnedLancamentoInicio) {
      this.distribuicaoBolnedLancamentoInicio = distribuicaoBolnedLancamentoInicio;
   }

   public Date getDistribuicaoBolnecLancamentoTermino() {
      return distribuicaoBolnecLancamentoTermino;
   }

   public void setDistribuicaoBolnecLancamentoTermino(Date distribuicaoBolnecLancamentoTermino) {
      this.distribuicaoBolnecLancamentoTermino = distribuicaoBolnecLancamentoTermino;
   }

   public Date getDistribuicaoParametroLancamentoInicio() {
      return distribuicaoParametroLancamentoInicio;
   }

   public void setDistribuicaoParametroLancamentoInicio(Date distribuicaoParametroLancamentoInicio) {
      this.distribuicaoParametroLancamentoInicio = distribuicaoParametroLancamentoInicio;
   }

   public Date getDistribuicaoParametroLancamentoTermino() {
      return distribuicaoParametroLancamentoTermino;
   }

   public void setDistribuicaoParametroLancamentoTermino(Date distribuicaoParametroLancamentoTermino) {
      this.distribuicaoParametroLancamentoTermino = distribuicaoParametroLancamentoTermino;
   }

   public Date getDistribuicaoGdLancamentoInicio() {
      return distribuicaoGdLancamentoInicio;
   }

   public void setDistribuicaoGdLancamentoInicio(Date distribuicaoGdLancamentoInicio) {
      this.distribuicaoGdLancamentoInicio = distribuicaoGdLancamentoInicio;
   }

   public Date getDistribuicaoGdLancamentoTermino() {
      return distribuicaoGdLancamentoTermino;
   }

   public void setDistribuicaoGdLancamentoTermino(Date distribuicaoGdLancamentoTermino) {
      this.distribuicaoGdLancamentoTermino = distribuicaoGdLancamentoTermino;
   }

   public Date getDistribuicaoBolnecConsolidacaoInicio() {
      return distribuicaoBolnecConsolidacaoInicio;
   }

   public void setDistribuicaoBolnecConsolidacaoInicio(Date distribuicaoBolnecConsolidacaoInicio) {
      this.distribuicaoBolnecConsolidacaoInicio = distribuicaoBolnecConsolidacaoInicio;
   }

   public Date getDistribuicaoBolnecConsolidacaoTermino() {
      return distribuicaoBolnecConsolidacaoTermino;
   }

   public void setDistribuicaoBolnecConsolidacaoTermino(Date distribuicaoBolnecConsolidacaoTermino) {
      this.distribuicaoBolnecConsolidacaoTermino = distribuicaoBolnecConsolidacaoTermino;
   }

   public Date getDistribuicaoProcessamentoInicio() {
      return distribuicaoProcessamentoInicio;
   }

   public void setDistribuicaoProcessamentoInicio(Date distribuicaoProcessamentoInicio) {
      this.distribuicaoProcessamentoInicio = distribuicaoProcessamentoInicio;
   }

   public Date getDistribuicaoProcessamentoTermino() {
      return distribuicaoProcessamentoTermino;
   }

   public void setDistribuicaoProcessamentoTermino(Date distribuicaoProcessamentoTermino) {
      this.distribuicaoProcessamentoTermino = distribuicaoProcessamentoTermino;
   }

   public Date getIncorporacaoGpta() {
      return incorporacaoGpta;
   }

   public void setIncorporacaoGpta(Date incorporacaoGpta) {
      this.incorporacaoGpta = incorporacaoGpta;
   }

   public Date getIncorporacaoTg() {
      return incorporacaoTg;
   }

   public void setIncorporacaoTg(Date incorporacaoTg) {
      this.incorporacaoTg = incorporacaoTg;
   }

   public Date getIncorporacaoMdfv() {
      return incorporacaoMdfv;
   }

   public void setIncorporacaoMdfv(Date incorporacaoMdfv) {
      this.incorporacaoMdfv = incorporacaoMdfv;
   }

   public Date getIncorporacaoGptb() {
      return incorporacaoGptb;
   }

   public void setIncorporacaoGptb(Date incorporacaoGptb) {
      this.incorporacaoGptb = incorporacaoGptb;
   }

   public Date getIncorporacaoSvtt() {
      return incorporacaoSvtt;
   }

   public void setIncorporacaoSvtt(Date incorporacaoSvtt) {
      this.incorporacaoSvtt = incorporacaoSvtt;
   }

   public Date getIncorporacaoEipot() {
      return incorporacaoEipot;
   }

   public void setIncorporacaoEipot(Date incorporacaoEipot) {
      this.incorporacaoEipot = incorporacaoEipot;
   }

   public Date getIncorporacaoEic() {
      return incorporacaoEic;
   }

   public void setIncorporacaoEic(Date incorporacaoEic) {
      this.incorporacaoEic = incorporacaoEic;
   }

   public String getAnoBase() {
      return anoBase;
   }

   public void setAnoBase(String anoBase) {
      this.anoBase = anoBase;
   }

   public Date getSelecaoGeralInicio() {
      return selecaoGeralInicio;
   }

   public void setSelecaoGeralInicio(Date selecaoGeralInicio) {
      this.selecaoGeralInicio = selecaoGeralInicio;
   }

   public Date getSelecaoGeralTermino() {
      return selecaoGeralTermino;
   }

   public void setSelecaoGeralTermino(Date selecaoGeralTermino) {
      this.selecaoGeralTermino = selecaoGeralTermino;
   }

   public Date getAlistamentoDentroPrazoInicio() {
      return alistamentoDentroPrazoInicio;
   }

   public void setAlistamentoDentroPrazoInicio(Date alistamentoDentroPrazoInicio) {
      this.alistamentoDentroPrazoInicio = alistamentoDentroPrazoInicio;
   }

   public Date getAlistamentoDentroPrazoTermino() {
      return alistamentoDentroPrazoTermino;
   }

   public void setAlistamentoDentroPrazoTermino(Date alistamentoDentroPrazoTermino) {
      this.alistamentoDentroPrazoTermino = alistamentoDentroPrazoTermino;
   }

   public Date getAlistamentoForaPrazoInicio() {
      return alistamentoForaPrazoInicio;
   }

   public void setAlistamentoForaPrazoInicio(Date alistamentoForaPrazoInicio) {
      this.alistamentoForaPrazoInicio = alistamentoForaPrazoInicio;
   }

   public Date getAlistamentoForaPrazoTermino() {
      return alistamentoForaPrazoTermino;
   }

   public void setAlistamentoForaPrazoTermino(Date alistamentoForaPrazoTermino) {
      this.alistamentoForaPrazoTermino = alistamentoForaPrazoTermino;
   }

   public Date getAlistamentoCa2PeriodoInicio() {
      return alistamentoCa2PeriodoInicio;
   }

   public void setAlistamentoCa2PeriodoInicio(Date alistamentoCa2PeriodoInicio) {
      this.alistamentoCa2PeriodoInicio = alistamentoCa2PeriodoInicio;
   }

   public Date getAlistamentoCa2PeriodoTermino() {
      return alistamentoCa2PeriodoTermino;
   }

   public void setAlistamentoCa2PeriodoTermino(Date alistamentoCa2PeriodoTermino) {
      this.alistamentoCa2PeriodoTermino = alistamentoCa2PeriodoTermino;
   }

}
