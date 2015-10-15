package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** CS - Anamnese.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.2.5
 */
@Entity
@PrimaryKey(validation=IdValidation.NULL)
public final class CsAnamnese implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 1L;

   @Id
   private Long cidadaoRa;

   private String Q1;

   private String Q1T;

   private String Q2;

   private String Q2T;

   private String Q3;

   private String Q3T;

   private String Q4;

   private String Q4T;

   private String Q5;

   private String Q5T;

   private String Q6;

   private String Q7;

   private String Q8;

   private String Q9;

   private String Q10;

   private String Q11;

   private String Q11T;

   private String Q12;

   private String Q12T;

   private String Q13;

   private String Q13T;

   private String Q14;

   private String Q14T;

   private String Q15;

   private String Q15T;

   private String Q16;

   private String Q17;

   private String Q18;

   private String Q19;

   private String Q19T;

   private String Q20;

   public CsAnamnese() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getCidadaoRa() == null ? "RA" : this.getCidadaoRa().toString())
            .append(" - ANAMNESE")
            .toString();
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((cidadaoRa == null) ? 0 : cidadaoRa.hashCode());
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
      CsAnamnese other = (CsAnamnese) obj;
      if (cidadaoRa == null) {
         if (other.cidadaoRa != null)
            return false;
      } else if (!cidadaoRa.equals(other.cidadaoRa))
         return false;
      return true;
   }

   public Long getCidadaoRa() {
      return cidadaoRa;
   }

   public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
   }

   public String getQ1() {
      return Q1;
   }

   public void setQ1(String q1) {
      Q1 = q1;
   }

   public String getQ1T() {
      return Q1T;
   }

   public void setQ1T(String q1t) {
      Q1T = q1t;
   }

   public String getQ2() {
      return Q2;
   }

   public void setQ2(String q2) {
      Q2 = q2;
   }

   public String getQ2T() {
      return Q2T;
   }

   public void setQ2T(String q2t) {
      Q2T = q2t;
   }

   public String getQ3() {
      return Q3;
   }

   public void setQ3(String q3) {
      Q3 = q3;
   }

   public String getQ3T() {
      return Q3T;
   }

   public void setQ3T(String q3t) {
      Q3T = q3t;
   }

   public String getQ4() {
      return Q4;
   }

   public void setQ4(String q4) {
      Q4 = q4;
   }

   public String getQ4T() {
      return Q4T;
   }

   public void setQ4T(String q4t) {
      Q4T = q4t;
   }

   public String getQ5() {
      return Q5;
   }

   public void setQ5(String q5) {
      Q5 = q5;
   }

   public String getQ5T() {
      return Q5T;
   }

   public void setQ5T(String q5t) {
      Q5T = q5t;
   }

   public String getQ6() {
      return Q6;
   }

   public void setQ6(String q6) {
      Q6 = q6;
   }

   public String getQ7() {
      return Q7;
   }

   public void setQ7(String q7) {
      Q7 = q7;
   }

   public String getQ8() {
      return Q8;
   }

   public void setQ8(String q8) {
      Q8 = q8;
   }

   public String getQ9() {
      return Q9;
   }

   public void setQ9(String q9) {
      Q9 = q9;
   }

   public String getQ10() {
      return Q10;
   }

   public void setQ10(String q10) {
      Q10 = q10;
   }

   public String getQ11() {
      return Q11;
   }

   public void setQ11(String q11) {
      Q11 = q11;
   }

   public String getQ11T() {
      return Q11T;
   }

   public void setQ11T(String q11t) {
      Q11T = q11t;
   }

   public String getQ12() {
      return Q12;
   }

   public void setQ12(String q12) {
      Q12 = q12;
   }

   public String getQ12T() {
      return Q12T;
   }

   public void setQ12T(String q12t) {
      Q12T = q12t;
   }

   public String getQ13() {
      return Q13;
   }

   public void setQ13(String q13) {
      Q13 = q13;
   }

   public String getQ13T() {
      return Q13T;
   }

   public void setQ13T(String q13t) {
      Q13T = q13t;
   }

   public String getQ14() {
      return Q14;
   }

   public void setQ14(String q14) {
      Q14 = q14;
   }

   public String getQ14T() {
      return Q14T;
   }

   public void setQ14T(String q14t) {
      Q14T = q14t;
   }

   public String getQ15() {
      return Q15;
   }

   public void setQ15(String q15) {
      Q15 = q15;
   }

   public String getQ15T() {
      return Q15T;
   }

   public void setQ15T(String q15t) {
      Q15T = q15t;
   }

   public String getQ16() {
      return Q16;
   }

   public void setQ16(String q16) {
      Q16 = q16;
   }

   public String getQ17() {
      return Q17;
   }

   public void setQ17(String q17) {
      Q17 = q17;
   }

   public String getQ18() {
      return Q18;
   }

   public void setQ18(String q18) {
      Q18 = q18;
   }

   public String getQ19() {
      return Q19;
   }

   public void setQ19(String q19) {
      Q19 = q19;
   }

   public String getQ19T() {
      return Q19T;
   }

   public void setQ19T(String q19t) {
      Q19T = q19t;
   }

   public String getQ20() {
      return Q20;
   }

   public void setQ20(String q20) {
      Q20 = q20;
   }

}
