package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entrevista de cidadao na Comissão de Seleção.
 * @author Abreu Lopes
 * @since 5.4.6
 * @version 5.4.6
 */
@Entity
@Table(name = "CS_ENTREVISTA_2")
@PrimaryKey(validation = IdValidation.NULL)
public final class CsEntrevista2 implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CIDADAO_RA")
  private Long ra;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name="USUARIO_CPF", referencedColumnName="CPF", nullable=false)
  private Usuario usuario;

  @Column(name = "ATUALIZACAO_DATA")
  @Temporal(TemporalType.TIMESTAMP)
  private Date atualizacaoData;
  
  private String i01b; // INDICADOR: Apresentou muita dificuldade para compreender as perguntas?
  private String i02b; // INDICADOR: Apresentou muita dificuldade para responder as perguntas?
  private String i03b; // INDICADOR: Apresentou comportamentos estranhos?
  private String i03d; // INDICADOR: Quais?
  private String i04b; // INDICADOR: Apresentou agitação corporal excessiva?
  private String i05b; // INDICADOR: Apresentou sinais de agressividade ou irritabilidade?
  private String i05d; // INDICADOR: Quais?
  private String i06b; // INDICADOR: Demonstrou apatia, desânimo, choro, indiferença, medo ou fobias? 
  private String i07b; // INDICADOR: Declarou intenção de acabar com a própria vida?
  private String i08b; // INDICADOR: Mencionou problemas emocionais ou internação psiquiátrica?
  private String i09b; // INDICADOR: AVALIAÇÃO FINAL (APTO = A ou INAPTO = I)

  private String q01b; // Q01: Você estuda atualmente?

  private String q02b; // Q02: Já fez algum curso profissionalizante?
  private String q02d; // Q02: Quais?
  private String q02c; // Q02: Tem comprovante?

  private String q03b; // Q03: Possui alguma experiência profissional?
  private String q03d; // Q03: Quais?
  private String q03c; // Q03: Tem comprovante?

  private String q04b; // Q04: Possui Carteira Nacional de Habilitação (CNH)?
  private String q04h; // Q04: Está realizando curso para a habilitação?
  private String q04c; // Q04: Qual a categoria?

  private String q05b; // Q05: Pratica esportes?
  private String q05d; // Q05: Quais?
  private String q05f; // Q05: É ou já foi federado?

  private String q06l; // Q06: Com quem mora?

  private String q07b; // Q07: Possui filhos?
  private String q07n; // Q07: Quantos?

  private String q08l; // Q08: Quem trabalha na familia?
  private String q08d; // Q08: Outros

  private String q09l; // Q09: Quem sustenta a familia?
  private String q09d; // Q09: Outros

  private String q10b; // Q10: Recebe auxílio do Governo?
  private String q10d; // Q10: Qual?

  private String q11b; // Q11: Arrimo de família?

  private String q12d; // Q12: O que costuma fazer nas horas de lazer?

  private String q13b; // Q13: Já teve algum problema de saúde?
  private String q13d; // Q13: Quais?

  private String q14b; // Q14: Usa algum remédio controlado?
  private String q14d; // Q14: Quais?
  private String q14m; // Q14: Para que?
  private String q14t; // Q14: Há quanto tempo?
  private String q14u; // Q14: Por quanto tempo ainda usará?

  private String q15b; // Q15: Já esteve internado em hospital ou clínica psiquiátrica?
  private String q15d; // Q15: Qual foi o motivo?
  private String q15t; // Q15: Por quanto tempo?

  private String q16b; // Q16: Fuma?
  private String q16d; // Q16: Há quanto tempo?

  private String q17b; // Q17: Faz uso de bebida alcoólica?
  private String q17d; // Q17: Com que frequência?

  private String q18b; // Q18: Já experimentou droga?
  private String q18d; // Q18: Qual?

  private String q19b; // Q19: Ainda faz uso de drogas?
  private String q19d; // Q19: Com que frequência?
  private String q19t; // Q19: Quando foi a última vez que usou?

  private String q20b; // Q20: Possui algum parente usuário de drogas (álcool ou outros tipos de droga)?
  private String q20d; // Q20: Quem?

  private String q21b; // Q21: Isso afeta sua vida diretamente?
  private String q21d; // Q21: Como?

  private String q22b; // Q22: Possui algum parente com histórico de transtorno psiquiátrico?
  private String q22d; // Q22: Quem?

  private String q23b; // Q23: Isso afeta sua vida diretamente?
  private String q23d; // Q23: Como?

  private String q24b; // Q24: Já foi detido pela polícia?
  private String q24l; // Q24: Qual a infração?
  private String q24d; // Q24: Outros

  private String q25b; // Q25: Problema Social?

  private String q26d; // Q26: Tem alguma coisa que não foi perguntada e que gostaria de acrescentar?

  private String q27d; // Q27: Observações Regionais

  private String pendencia;

  public CsEntrevista2() {
    this.cidadao = new Cidadao();
    this.usuario = new Usuario();
  }
  
  @Override
  public String toString() {
    return new StringBuilder("Entrevista: ").append(this.getCidadao()).toString();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ra == null) ? 0 : ra.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsEntrevista2 other = (CsEntrevista2) obj;
    if (ra == null) {
      if (other.ra != null)
        return false;
    } else if (!ra.equals(other.ra))
      return false;
    return true;
  }

  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  
  public Date getAtualizacaoData() {
    return atualizacaoData;
  }

  public void setAtualizacaoData(Date atualizacaoData) {
    this.atualizacaoData = atualizacaoData;
  }

  public String getI01b() {
    return i01b;
  }

  public void setI01b(String i01b) {
    this.i01b = i01b;
  }

  public String getI02b() {
    return i02b;
  }

  public void setI02b(String i02b) {
    this.i02b = i02b;
  }

  public String getI03b() {
    return i03b;
  }

  public void setI03b(String i03b) {
    this.i03b = i03b;
  }

  public String getI03d() {
    return i03d;
  }

  public void setI03d(String i03d) {
    this.i03d = i03d;
  }

  public String getI04b() {
    return i04b;
  }

  public void setI04b(String i04b) {
    this.i04b = i04b;
  }

  public String getI05b() {
    return i05b;
  }

  public void setI05b(String i05b) {
    this.i05b = i05b;
  }

  public String getI05d() {
    return i05d;
  }

  public void setI05d(String i05d) {
    this.i05d = i05d;
  }

  public String getI06b() {
    return i06b;
  }

  public void setI06b(String i06b) {
    this.i06b = i06b;
  }

  public String getI07b() {
    return i07b;
  }

  public void setI07b(String i07b) {
    this.i07b = i07b;
  }

  public String getI08b() {
    return i08b;
  }

  public void setI08b(String i08b) {
    this.i08b = i08b;
  }

  public String getI09b() {
    return i09b;
  }

  public void setI09b(String i09b) {
    this.i09b = i09b;
  }

  public String getQ01b() {
    return q01b;
  }

  public void setQ01b(String q01b) {
    this.q01b = q01b;
  }

  public String getQ02b() {
    return q02b;
  }

  public void setQ02b(String q02b) {
    this.q02b = q02b;
  }

  public String getQ02d() {
    return q02d;
  }

  public void setQ02d(String q02d) {
    this.q02d = q02d;
  }

  public String getQ02c() {
    return q02c;
  }

  public void setQ02c(String q02c) {
    this.q02c = q02c;
  }

  public String getQ03b() {
    return q03b;
  }

  public void setQ03b(String q03b) {
    this.q03b = q03b;
  }

  public String getQ03d() {
    return q03d;
  }

  public void setQ03d(String q03d) {
    this.q03d = q03d;
  }

  public String getQ03c() {
    return q03c;
  }

  public void setQ03c(String q03c) {
    this.q03c = q03c;
  }

  public String getQ04b() {
    return q04b;
  }

  public void setQ04b(String q04b) {
    this.q04b = q04b;
  }

  public String getQ04h() {
    return q04h;
  }

  public void setQ04h(String q04h) {
    this.q04h = q04h;
  }

  public String getQ04c() {
    return q04c;
  }

  public void setQ04c(String q04c) {
    this.q04c = q04c;
  }

  public String getQ05b() {
    return q05b;
  }

  public void setQ05b(String q05b) {
    this.q05b = q05b;
  }

  public String getQ05d() {
    return q05d;
  }

  public void setQ05d(String q05d) {
    this.q05d = q05d;
  }

  public String getQ05f() {
    return q05f;
  }

  public void setQ05f(String q05f) {
    this.q05f = q05f;
  }

  public String getQ07b() {
    return q07b;
  }

  public void setQ07b(String q07b) {
    this.q07b = q07b;
  }

  public String getQ07n() {
    return q07n;
  }

  public void setQ07n(String q07n) {
    this.q07n = q07n;
  }

  public String getQ08d() {
    return q08d;
  }

  public void setQ08d(String q08d) {
    this.q08d = q08d;
  }

  public String getQ09d() {
    return q09d;
  }

  public void setQ09d(String q09d) {
    this.q09d = q09d;
  }

  public String getQ10b() {
    return q10b;
  }

  public void setQ10b(String q10b) {
    this.q10b = q10b;
  }

  public String getQ10d() {
    return q10d;
  }

  public void setQ10d(String q10d) {
    this.q10d = q10d;
  }

  public String getQ11b() {
    return q11b;
  }

  public void setQ11b(String q11b) {
    this.q11b = q11b;
  }

  public String getQ12d() {
    return q12d;
  }

  public void setQ12d(String q12d) {
    this.q12d = q12d;
  }

  public String getQ13b() {
    return q13b;
  }

  public void setQ13b(String q13b) {
    this.q13b = q13b;
  }

  public String getQ13d() {
    return q13d;
  }

  public void setQ13d(String q13d) {
    this.q13d = q13d;
  }

  public String getQ14b() {
    return q14b;
  }

  public void setQ14b(String q14b) {
    this.q14b = q14b;
  }

  public String getQ14d() {
    return q14d;
  }

  public void setQ14d(String q14d) {
    this.q14d = q14d;
  }

  public String getQ14m() {
    return q14m;
  }

  public void setQ14m(String q14m) {
    this.q14m = q14m;
  }

  public String getQ14t() {
    return q14t;
  }

  public void setQ14t(String q14t) {
    this.q14t = q14t;
  }

  public String getQ14u() {
    return q14u;
  }

  public void setQ14u(String q14u) {
    this.q14u = q14u;
  }

  public String getQ15b() {
    return q15b;
  }

  public void setQ15b(String q15b) {
    this.q15b = q15b;
  }

  public String getQ15d() {
    return q15d;
  }

  public void setQ15d(String q15d) {
    this.q15d = q15d;
  }

  public String getQ15t() {
    return q15t;
  }

  public void setQ15t(String q15t) {
    this.q15t = q15t;
  }

  public String getQ16b() {
    return q16b;
  }

  public void setQ16b(String q16b) {
    this.q16b = q16b;
  }

  public String getQ16d() {
    return q16d;
  }

  public void setQ16d(String q16d) {
    this.q16d = q16d;
  }

  public String getQ17b() {
    return q17b;
  }

  public void setQ17b(String q17b) {
    this.q17b = q17b;
  }

  public String getQ17d() {
    return q17d;
  }

  public void setQ17d(String q17d) {
    this.q17d = q17d;
  }

  public String getQ18b() {
    return q18b;
  }

  public void setQ18b(String q18b) {
    this.q18b = q18b;
  }

  public String getQ18d() {
    return q18d;
  }

  public void setQ18d(String q18d) {
    this.q18d = q18d;
  }

  public String getQ19b() {
    return q19b;
  }

  public void setQ19b(String q19b) {
    this.q19b = q19b;
  }

  public String getQ19d() {
    return q19d;
  }

  public void setQ19d(String q19d) {
    this.q19d = q19d;
  }

  public String getQ19t() {
    return q19t;
  }

  public void setQ19t(String q19t) {
    this.q19t = q19t;
  }

  public String getQ20b() {
    return q20b;
  }

  public void setQ20b(String q20b) {
    this.q20b = q20b;
  }

  public String getQ20d() {
    return q20d;
  }

  public void setQ20d(String q20d) {
    this.q20d = q20d;
  }

  public String getQ21b() {
    return q21b;
  }

  public void setQ21b(String q21b) {
    this.q21b = q21b;
  }

  public String getQ21d() {
    return q21d;
  }

  public void setQ21d(String q21d) {
    this.q21d = q21d;
  }

  public String getQ22b() {
    return q22b;
  }

  public void setQ22b(String q22b) {
    this.q22b = q22b;
  }

  public String getQ22d() {
    return q22d;
  }

  public void setQ22d(String q22d) {
    this.q22d = q22d;
  }

  public String getQ23b() {
    return q23b;
  }

  public void setQ23b(String q23b) {
    this.q23b = q23b;
  }

  public String getQ23d() {
    return q23d;
  }

  public void setQ23d(String q23d) {
    this.q23d = q23d;
  }

  public String getQ24b() {
    return q24b;
  }

  public void setQ24b(String q24b) {
    this.q24b = q24b;
  }

  public String getQ24d() {
    return q24d;
  }

  public void setQ24d(String q24d) {
    this.q24d = q24d;
  }

  public String getQ25b() {
    return q25b;
  }

  public void setQ25b(String q25b) {
    this.q25b = q25b;
  }

  public String getQ26d() {
    return q26d;
  }

  public void setQ26d(String q26d) {
    this.q26d = q26d;
  }

  public String getQ27d() {
    return q27d;
  }

  public void setQ27d(String q27d) {
    this.q27d = q27d;
  }

  public String getPendencia() {
    return pendencia;
  }

  public void setPendencia(String pendencia) {
    this.pendencia = pendencia;
  }

  public List<String> getQ06l() {
    if (!StringUtils.isEmpty(this.q06l)) {
      return Arrays.asList(this.q06l.split(","));
    }
    return new ArrayList<String>();
  }

  public void setQ06l(List<String> q06l) {
    this.q06l = StringUtils.join(q06l, ",").toString();
  }

  public List<String> getQ08l() {
    if (!StringUtils.isEmpty(this.q08l)) {
      return Arrays.asList(this.q08l.split(","));
    }
    return new ArrayList<String>();
  }

  public void setQ08l(List<String> q08l) {
    this.q08l = StringUtils.join(q08l, ",").toString();
  }

  public List<String> getQ09l() {
    if (!StringUtils.isEmpty(this.q09l)) {
      return Arrays.asList(this.q09l.split(","));
    }
    return new ArrayList<String>();
  }

  public void setQ09l(List<String> q09l) {
    this.q09l = StringUtils.join(q09l, ",").toString();
  }

  public List<String> getQ24l() {
    if (!StringUtils.isEmpty(this.q24l)) {
      return Arrays.asList(this.q24l.split(","));
    }
    return new ArrayList<String>();
  }

  public void setQ24l(List<String> q24l) {
    this.q24l = StringUtils.join(q24l, ",").toString();
  }

}
