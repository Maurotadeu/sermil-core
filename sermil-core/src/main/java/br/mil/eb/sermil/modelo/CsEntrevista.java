package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entrevista de cidadao na Comissão de Seleção.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 5.0
 * @version 5.4
 */
@Entity
@Table(name = "CS_ENTREVISTA")
@PrimaryKey(validation = IdValidation.NULL)
public final class CsEntrevista implements Serializable {

   private static final long serialVersionUID = 8912995736225963946L;

   @Id
   @Column(name = "CIDADAO_RA") 
   private Long ra;

   private String A1;    // Você estuda atualmente?
   private String A11;   // Já concluiu os estudos?

   private String B3;    // Já fez algum curso profissionalizante?
   private String B4;    // Quais?
   private String B5;    // Tem comprovante?

   private String C5;    // Possui alguma experiência profissional?
   private String C6;    // Quais?
   private String C61;   // Tem comprovante?

   private String D7;    // Possui Carteira Nacional de Habilitação (CNH)?
   private String D71;   // Está realizando curso para a habilitação?
   private String D72;   // Qual a categoria?

   private String EA;    // Padrão de Pré-qualificação 1
   private String EB;    // Padrão de Pré-qualificação 2

   private String F8;    // Pratica esportes?
   private String F8A;   // Qual(is)?
   private String F81;   // É ou já foi federado?
   private String F9;    // Sabe nadar?

   private String G10;   // Com quem mora?
   private String G11;   // 
   private String G12;   // Possui filhos?
   private String G121;  // Quantos?
   private String G13;   // Quem trabalha na familia?
   private String G13A;  // Outros
   private String G14;   // Quem sustenta a familia?
   private String G14A;  // Outros
   private String G15;   // Recebe auxílio do Governo?
   private String G15A;  // Qual?

   private String H1;    // Situação de Arrimo

   private String I16;   // O que costuma fazer nas horas de lazer?

   private String J17;   // Já teve algum problema de saúde?
   private String J171;  // Qual(is)?
   private String J18;   // Usa algum remédio controlado?
   private String J181;  // Qual(is)?
   private String J182;  // Para que?
   private String J183;  // Há quanto tempo?
   private String J184;  // Por quanto tempo ainda usará?
   private String J19;   // Já esteve internado em hospital ou clínica psiquiátrica?
   private String J191;  // Qual foi o motivo?
   private String J192;  // Por quanto tempo?
   private String J20;   // Fuma?
   private String J20A;  // Há quanto tempo?
   private String J21;   // Faz uso de bebida alcoólica?
   private String J211;  // Com que frequência?
   private String J22;   // Já experimentou droga?
   private String J221;  // Qual?
   private String J222;  // Ainda faz uso?
   private String J222A; // Com que frequência?
   private String J223;  // Quando foi a última vez que usou?
   private String J23;   // Possui algum parente usuário de drogas (álcool ou outros tipos de droga)?
   private String J231;  // Quem?
   private String J231A; // Isso afeta sua vida diretamente?
   private String J231B; // Como?
   private String J232;  // Isso afeta sua vida diretamente?
   private String J232A; // Como?
   private String J233;  // Possui algum parente com histórico de transtorno psiquiátrico?
   private String J234;  // Quem?
   private String J235;  // 

   private String K24;   // Já foi detido pela polícia?
   private String K241;  // Qual a infração?
   private String K241A; // Outros

   private String L1;    // Problema Social?

   private String M25;   // Deseja Servir?
   private String M26;   // Qual Força?
   private String M27;   // CS Especial?

   private String N27;   // Tem alguma coisa que não foi perguntada e que gostaria de acrescentar?
   private String N28;   // Observações Regionais

   private String O29;   // Expressão Oral

   private String Ind1;  // INDICADOR: Apresentou muita dificuldade para compreender as perguntas?
   private String Ind2;  // INDICADOR: Apresentou muita dificuldade para responder as perguntas?
   private String Ind3;  // INDICADOR: Apresentou comportamentos estranhos?
   private String Ind3A; // Qual?
   private String Ind4;  // INDICADOR: Apresentou agitação corporal excessiva?
   private String Ind5;  // INDICADOR: Apresentou sinais de agressividade ou irritabilidade?
   private String Ind5A; // Qual?
   private String Ind6;  // INDICADOR: Demonstrou apatia, desânimo, choro, indiferença, medo ou fobias? 
   private String Ind7;  // INDICADOR: Declarou intenção de acabar com a própria vida?
   private String Ind8;  // INDICADOR: Mencionou problemas emocionais ou internação psiquiátrica?
   private String Ind9;  // INDICADOR: AVALIAÇÃO FINAL

   private String pendencia;

   public CsEntrevista() {
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
      CsEntrevista other = (CsEntrevista) obj;
      if (ra == null) {
         if (other.ra != null)
            return false;
      } else if (!ra.equals(other.ra))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return new StringBuilder("Entrevista ").append(ra.toString()).toString();
   }

   public Long getRa() {
      return ra;
   }

   public void setRa(Long ra) {
      this.ra = ra;
   }

   public String getA1() {
      return A1;
   }

   public void setA1(String a1) {
      A1 = a1;
   }

   public String getA11() {
      return A11;
   }

   public void setA11(String a11) {
      A11 = a11;
   }

   public String getInd1() {
      return Ind1;
   }

   public void setInd1(String ind1) {
      Ind1 = ind1;
   }

   public String getInd2() {
      return Ind2;
   }

   public void setInd2(String ind2) {
      Ind2 = ind2;
   }

   public String getB3() {
      return B3;
   }

   public void setB3(String b3) {
      B3 = b3;
   }

   public String getB4() {
      return B4;
   }

   public void setB4(String b4) {
      B4 = b4;
   }

   public String getB5() {
      return B5;
   }

   public void setB5(String b5) {
      B5 = b5;
   }

   public String getC5() {
      return C5;
   }

   public void setC5(String c5) {
      C5 = c5;
   }

   public String getC6() {
      return C6;
   }

   public void setC6(String c6) {
      C6 = c6;
   }

   public String getC61() {
      return C61;
   }

   public void setC61(String c61) {
      C61 = c61;
   }

   public String getD7() {
      return D7;
   }

   public void setD7(String d7) {
      D7 = d7;
   }

   public String getD71() {
      return D71;
   }

   public void setD71(String d71) {
      D71 = d71;
   }

   public String getD72() {
      return D72;
   }

   public void setD72(String d72) {
      D72 = d72;
   }

   public String getEA() {
      return EA;
   }

   public void setEA(String eA) {
      EA = eA;
   }

   public String getEB() {
      return EB;
   }

   public void setEB(String eB) {
      EB = eB;
   }

   public String getF8() {
      return F8;
   }

   public void setF8(String f8) {
      F8 = f8;
   }

   public String getF81() {
      return F81;
   }

   public void setF81(String f81) {
      F81 = f81;
   }

   public String getF9() {
      return F9;
   }

   public void setF9(String f9) {
      F9 = f9;
   }

   public List<String> getG10() {
      List<String> val = Arrays.asList(G10.split(","));
      return val;
   }

   public void setG10(List<String> g10) {
      G10 = StringUtils.join(g10, ",").toString();
   }

   public String getG11() {
      return G11;
   }

   public void setG11(String g11) {
      G11 = g11;
   }

   public String getG12() {
      return G12;
   }

   public void setG12(String g12) {
      G12 = g12;
   }

   public String getG121() {
      return G121;
   }

   public void setG121(String g121) {
      G121 = g121;
   }

   public List<String> getG13() {
      return Arrays.asList(G13.split(","));
   }

   public void setG13(List<String> g13) {
      G13 = StringUtils.join(g13, ",").toString();
   }

   public List<String> getG14() {
      return Arrays.asList(G14.split(","));
   }

   public void setG14(List<String> g14) {
      G14 = StringUtils.join(g14, ",").toString();
   }

   public String getG15() {
      return G15;
   }

   public void setG15(String g15) {
      G15 = g15;
   }

   public String getH1() {
      return H1;
   }

   public void setH1(String h1) {
      H1 = h1;
   }

   public String getI16() {
      return I16;
   }

   public void setI16(String i16) {
      I16 = i16;
   }

   public String getJ17() {
      return J17;
   }

   public void setJ17(String j17) {
      J17 = j17;
   }

   public String getJ171() {
      return J171;
   }

   public void setJ171(String j171) {
      J171 = j171;
   }

   public String getJ18() {
      return J18;
   }

   public void setJ18(String j18) {
      J18 = j18;
   }

   public String getJ181() {
      return J181;
   }

   public void setJ181(String j181) {
      J181 = j181;
   }

   public String getJ182() {
      return J182;
   }

   public void setJ182(String j182) {
      J182 = j182;
   }

   public String getJ183() {
      return J183;
   }

   public void setJ183(String j183) {
      J183 = j183;
   }

   public String getJ184() {
      return J184;
   }

   public void setJ184(String j184) {
      J184 = j184;
   }

   public String getJ19() {
      return J19;
   }

   public void setJ19(String j19) {
      J19 = j19;
   }

   public String getJ191() {
      return J191;
   }

   public void setJ191(String j191) {
      J191 = j191;
   }

   public String getJ192() {
      return J192;
   }

   public void setJ192(String j192) {
      J192 = j192;
   }

   public String getJ20() {
      return J20;
   }

   public void setJ20(String j20) {
      J20 = j20;
   }

   public String getJ21() {
      return J21;
   }

   public void setJ21(String j21) {
      J21 = j21;
   }

   public String getJ211() {
      return J211;
   }

   public void setJ211(String j211) {
      J211 = j211;
   }

   public String getJ222() {
      return J222;
   }

   public void setJ222(String j222) {
      J222 = j222;
   }

   public String getJ223() {
      return J223;
   }

   public void setJ223(String j223) {
      J223 = j223;
   }

   public String getJ23() {
      return J23;
   }

   public void setJ23(String j23) {
      J23 = j23;
   }

   public String getJ231() {
      return J231;
   }

   public void setJ231(String j231) {
      J231 = j231;
   }

   public String getJ232() {
      return J232;
   }

   public void setJ232(String j232) {
      J232 = j232;
   }

   public String getJ233() {
      return J233;
   }

   public void setJ233(String j233) {
      J233 = j233;
   }

   public String getJ234() {
      return J234;
   }

   public void setJ234(String j234) {
      J234 = j234;
   }

   public String getJ235() {
      return J235;
   }

   public void setJ235(String j235) {
      J235 = j235;
   }

   public String getK24() {
      return K24;
   }

   public void setK24(String k24) {
      K24 = k24;
   }

   public List<String> getK241() {
      return Arrays.asList(K241.split(","));
   }

   public void setK241(List<String> k241) {
      K241 = StringUtils.join(k241, ",").toString();
   }

   public String getL1() {
      return L1;
   }

   public void setL1(String l1) {
      L1 = l1;
   }

   public String getM25() {
      return M25;
   }

   public void setM25(String m25) {
      M25 = m25;
   }

   public String getM26() {
      return M26;
   }

   public void setM26(String m26) {
      M26 = m26;
   }

   public String getN27() {
      return N27;
   }

   public void setN27(String n27) {
      N27 = n27;
   }

   public String getN28() {
      return N28;
   }

   public void setN28(String n28) {
      N28 = n28;
   }

   public String getO29() {
      return O29;
   }

   public void setO29(String o29) {
      O29 = o29;
   }

   public String getF8A() {
      return F8A;
   }

   public void setF8A(String f8a) {
      F8A = f8a;
   }

   public String getG13A() {
      return G13A;
   }

   public void setG13A(String g13a) {
      G13A = g13a;
   }

   public String getG14A() {
      return G14A;
   }

   public void setG14A(String g14a) {
      G14A = g14a;
   }

   public String getG15A() {
      return G15A;
   }

   public void setG15A(String g15a) {
      G15A = g15a;
   }

   public String getJ20A() {
      return J20A;
   }

   public void setJ20A(String j20a) {
      J20A = j20a;
   }

   public String getJ22() {
      return J22;
   }

   public void setJ22(String j22) {
      J22 = j22;
   }

   public String getJ221() {
      return J221;
   }

   public void setJ221(String j221) {
      J221 = j221;
   }

   public String getJ222A() {
      return J222A;
   }

   public void setJ222A(String j222a) {
      J222A = j222a;
   }

   public String getInd3() {
      return Ind3;
   }

   public void setInd3(String ind3) {
      Ind3 = ind3;
   }

   public String getInd4() {
      return Ind4;
   }

   public void setInd4(String ind4) {
      Ind4 = ind4;
   }

   public String getInd5() {
      return Ind5;
   }

   public void setInd5(String ind5) {
      Ind5 = ind5;
   }

   public String getInd6() {
      return Ind6;
   }

   public void setInd6(String ind6) {
      Ind6 = ind6;
   }

   public String getInd7() {
      return Ind7;
   }

   public void setInd7(String ind7) {
      Ind7 = ind7;
   }

   public String getInd8() {
      return Ind8;
   }

   public void setInd8(String ind8) {
      Ind8 = ind8;
   }

   public String getInd9() {
      return Ind9;
   }

   public void setInd9(String ind9) {
      Ind9 = ind9;
   }

   public String getInd5A() {
      return Ind5A;
   }

   public void setInd5A(String ind5a) {
      Ind5A = ind5a;
   }

   public String getK241A() {
      return K241A;
   }

   public void setK241A(String k241a) {
      K241A = k241a;
   }

   public String getInd3A() {
      return Ind3A;
   }

   public void setInd3A(String ind3a) {
      Ind3A = ind3a;
   }

   public String getJ232A() {
      return J232A;
   }

   public void setJ232A(String j232a) {
      J232A = j232a;
   }

   public String getJ231A() {
      return J231A;
   }

   public void setJ231A(String j231a) {
      J231A = j231a;
   }

   public String getM27() {
      return M27;
   }

   public void setM27(String m27) {
      M27 = m27;
   }

   public String getJ231B() {
      return J231B;
   }

   public void setJ231B(String j231b) {
      J231B = j231b;
   }

   public String getPendencia() {
      return pendencia;
   }

   public void setPendencia(String pendencia) {
      this.pendencia = pendencia;
   }

}
