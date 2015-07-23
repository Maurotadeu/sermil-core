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

/**
 * Entrevista de cidadao na Comissao de Selecao.
 * 
 * @author Anselmo S Ribeiro
 * @since 3.0
 * @version $Id: Csm.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "ENTREVISTA_CS")
@PrimaryKey(validation = IdValidation.NULL)
public final class EntrevistaCs implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 1408657925141269864L;

   @Id
   @Column(name = "CIDADAO_RA") 
   private Long ra;

   private String A1;
   private String A11;

   private String B3;
   private String B4;
   private String B5;

   private String C5;
   private String C6;
   private String C61;

   private String D7;
   private String D71;
   private String D72;

   private String EA;
   private String EB;

   private String F8;
   private String F8A;
   private String F81;
   private String F9;

   private String G10;
   private String G11;
   private String G12;
   private String G121;
   private String G13;
   private String G13A;
   private String G14;
   private String G14A;
   private String G15;
   private String G15A;

   private String H1;

   private String I16;

   private String J17;
   private String J171;
   private String J18;
   private String J181;
   private String J182;
   private String J183;
   private String J184;
   private String J19;
   private String J191;
   private String J192;
   private String J20;
   private String J20A;
   private String J21;
   private String J211;
   private String J22;
   private String J221;
   private String J222;
   private String J222A;
   private String J223;
   private String J23;
   private String J231;
   private String J231A;
   private String J231B;
   private String J232;
   private String J232A;
   private String J233;
   private String J234;
   private String J235;

   private String K24;
   private String K241;
   private String K241A;

   private String L1;

   private String M25;
   private String M26;
   private String M27;

   private String N27;
   private String N28;

   private String O29;

   private String Ind1;
   private String Ind2;
   private String Ind3;
   private String Ind3A;
   private String Ind4;
   private String Ind5;
   private String Ind5A;
   private String Ind6;
   private String Ind7;
   private String Ind8;
   private String Ind9;

   public EntrevistaCs() {
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.ra == null) ? 0 : this.ra.hashCode());
      return result;
   }

   @Override
   public String toString() {
      return "Entrevista " + ra.toString();
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

}
