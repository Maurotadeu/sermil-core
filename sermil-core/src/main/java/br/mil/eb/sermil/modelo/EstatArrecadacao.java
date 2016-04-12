package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Estatística de arrecadação de taxas e multas.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.5
 */
@Entity
@Table(name = "ESTAT_ARRECADACAO")
@NamedQueries({
   @NamedQuery(name = "EstatArrecadacao.listarJsmAno", query = "SELECT e FROM EstatArrecadacao e WHERE e.jsm = ?1 AND e.ano = ?2 ORDER BY e.mes"),
   @NamedQuery(name = "EstatArrecadacao.excluirJsmAno", query="DELETE FROM EstatArrecadacao e WHERE e.jsm = ?1 AND e.ano = ?2")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class EstatArrecadacao implements Serializable {

   private static final long serialVersionUID = 6416891037986372782L;

   @Id
   @GeneratedValue(strategy=GenerationType.TABLE, generator="ESTAT_ARRECADACAO")
   @TableGenerator(name="ESTAT_ARRECADACAO", allocationSize=1)
   private Integer codigo;

   private Short ano;

   private Byte mes;

   @Column(name = "MULTA_BB")
   private BigDecimal multaBB;

   @Column(name = "MULTA_CEF")
   private BigDecimal multaCEF;

   @Column(name = "MULTA_EBCT")
   private BigDecimal multaEBCT;

   @Column(name = "MULTA_ISENTO")
   private BigDecimal multaIsento;

   @Column(name = "TAXA_BB")
   private BigDecimal taxaBB;

   @Column(name = "TAXA_CEF")
   private BigDecimal taxaCEF;

   @Column(name = "TAXA_EBCT")
   private BigDecimal taxaEBCT;

   @Column(name = "TAXA_ISENTO")
   private BigDecimal taxaIsento;

   @ManyToOne
   @JoinColumns({
      @JoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO"),
      @JoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO")
   })
   private Jsm jsm;

   public EstatArrecadacao() {
      this.setJsm(new Jsm());
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getJsm() != null ? this.getJsm().toString() : "00/000")
            .append(" - ")
            .append(this.getMes() != null ? this.getMes() : "00")
            .append("/")
            .append(this.getAno() != null ? this.getAno() : "0000")
            .toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
      EstatArrecadacao other = (EstatArrecadacao) obj;
      if (this.codigo == null) {
         if (other.codigo != null)
            return false;
      } else if (!this.codigo.equals(other.codigo))
         return false;
      return true;
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public Short getAno() {
      return ano;
   }

   public void setAno(Short ano) {
      this.ano = ano;
   }

   public Byte getMes() {
      return mes;
   }

   public void setMes(Byte mes) {
      this.mes = mes;
   }

   public BigDecimal getMultaBB() {
      return multaBB;
   }

   public void setMultaBB(BigDecimal multaBB) {
      this.multaBB = multaBB;
   }

   public BigDecimal getMultaCEF() {
      return multaCEF;
   }

   public void setMultaCEF(BigDecimal multaCEF) {
      this.multaCEF = multaCEF;
   }

   public BigDecimal getMultaEBCT() {
      return multaEBCT;
   }

   public void setMultaEBCT(BigDecimal multaEBCT) {
      this.multaEBCT = multaEBCT;
   }

   public BigDecimal getMultaIsento() {
      return multaIsento;
   }

   public void setMultaIsento(BigDecimal multaIsento) {
      this.multaIsento = multaIsento;
   }

   public BigDecimal getTaxaBB() {
      return taxaBB;
   }

   public void setTaxaBB(BigDecimal taxaBB) {
      this.taxaBB = taxaBB;
   }

   public BigDecimal getTaxaCEF() {
      return taxaCEF;
   }

   public void setTaxaCEF(BigDecimal taxaCEF) {
      this.taxaCEF = taxaCEF;
   }

   public BigDecimal getTaxaEBCT() {
      return taxaEBCT;
   }

   public void setTaxaEBCT(BigDecimal taxaEBCT) {
      this.taxaEBCT = taxaEBCT;
   }

   public BigDecimal getTaxaIsento() {
      return taxaIsento;
   }

   public void setTaxaIsento(BigDecimal taxaIsento) {
      this.taxaIsento = taxaIsento;
   }

   public Jsm getJsm() {
      return jsm;
   }

   public void setJsm(Jsm jsm) {
      this.jsm = jsm;
   }

}
