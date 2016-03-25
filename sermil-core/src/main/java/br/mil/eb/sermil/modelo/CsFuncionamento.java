package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Periodos e Locais de funcionamento das CSs.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 5.3.2
 */
@Entity
@Table(name = "CS_FUNCIONAMENTO")
@NamedQueries({
   @NamedQuery(name = "CsFuncionamento.listarPorCs", query = "select f from CsFuncionamento f where f.cs.codigo = ?1"),
   @NamedQuery(name = "CsFuncionamento.listarPorCsAno", query = "select f from CsFuncionamento f where f.cs.codigo = ?1 and f.anoBase = ?2")
})
@NamedNativeQueries({
   @NamedNativeQuery(resultClass=CsFuncionamento.class, name="CsFuncionamento.listarPorCsNativo", query="select f.* from cs inner join CS_FUNCIONAMENTO f on f.CS_CODIGO = cs.CODIGO where cs.codigo = ?1")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class CsFuncionamento implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CS_FUNCIONAMENTO")
   @TableGenerator(name = "CS_FUNCIONAMENTO", allocationSize = 1)
   private Integer codigo;

   @Column(name = "ANO_BASE", nullable = false, length = 4)
   private String anoBase;

   @Column(name = "INICIO_DATA", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date inicioData;

   @Column(name = "TERMINO_DATA", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date terminoData;

   @ManyToOne
   @JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO", nullable = false)
   private Cs cs;

   @OneToOne
   @JoinColumn(name = "CS_ENDERECO_CODIGO", referencedColumnName = "CODIGO", nullable = false)
   private CsEndereco csEndereco;

   public CsFuncionamento() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder("CS ").append(this.getCs())
            .append("/").append(this.getAnoBase())
            .append(" - ").append(new SimpleDateFormat("dd/mm/yyyy").format(getInicioData()))
            .append(" a ").append(new SimpleDateFormat("dd/mm/yyyy").format(this.getTerminoData()))
            .toString();
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((anoBase == null) ? 0 : anoBase.hashCode());
      result = prime * result + ((cs == null) ? 0 : cs.hashCode());
      result = prime * result
            + ((inicioData == null) ? 0 : inicioData.hashCode());
      result = prime * result
            + ((terminoData == null) ? 0 : terminoData.hashCode());
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
      CsFuncionamento other = (CsFuncionamento) obj;
      if (anoBase == null) {
         if (other.anoBase != null)
            return false;
      } else if (!anoBase.equals(other.anoBase))
         return false;
      if (cs == null) {
         if (other.cs != null)
            return false;
      } else if (!cs.equals(other.cs))
         return false;
      if (inicioData == null) {
         if (other.inicioData != null)
            return false;
      } else if (!inicioData.equals(other.inicioData))
         return false;
      if (terminoData == null) {
         if (other.terminoData != null)
            return false;
      } else if (!terminoData.equals(other.terminoData))
         return false;
      return true;
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public Cs getCs() {
      return cs;
   }

   public void setCs(Cs cs) {
      this.cs = cs;
   }

   public CsEndereco getCsEndereco() {
      return csEndereco;
   }

   public void setCsEndereco(CsEndereco csEndereco) {
      this.csEndereco = csEndereco;
   }

   public String getAnoBase() {
      return anoBase;
   }

   public void setAnoBase(String anoBase) {
      this.anoBase = anoBase;
   }

   public Date getInicioData() {
      return inicioData;
   }

   public void setInicioData(Date inicioData) {
      this.inicioData = inicioData;
   }

   public Date getTerminoData() {
      return terminoData;
   }

   public void setTerminoData(Date terminoData) {
      this.terminoData = terminoData;
   }

}
