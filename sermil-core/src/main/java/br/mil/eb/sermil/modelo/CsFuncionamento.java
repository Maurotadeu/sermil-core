package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @since 5.4
 */
@Entity
@Table(name = "CS_FUNCIONAMENTO")
@NamedQueries({
   @NamedQuery(name = "CsFuncionamento.listarPorCs", query = "select f from CsFuncionamento f where f.cs.codigo = ?1"),
   @NamedQuery(name = "CsFuncionamento.listarPorCsAno", query = "select f from CsFuncionamento f where f.cs.codigo = ?1 and f.anoBase = ?2")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class CsFuncionamento implements Serializable {

   private static final long serialVersionUID = 8060897827910910048L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CS_FUNCIONAMENTO")
   @TableGenerator(name = "CS_FUNCIONAMENTO", allocationSize = 1)
   private Integer codigo;

   @Column(name = "ANO_BASE")
   private String anoBase;

   @Column(name = "INICIO_DATA")
   @Temporal(TemporalType.DATE)
   private Date inicioData;

   @Column(name = "TERMINO_DATA")
   @Temporal(TemporalType.DATE)
   private Date terminoData;

   @ManyToOne(cascade = CascadeType.REFRESH)
   @JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO", nullable = false)
   private Cs cs;

   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "CS_ENDERECO_CODIGO", referencedColumnName = "CODIGO", nullable = false)
   private CsEndereco csEndereco;

   public CsFuncionamento() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getAnoBase())
            .append(": ").append(new SimpleDateFormat("dd/MM/yyyy").format(getInicioData()))
            .append(" a ").append(new SimpleDateFormat("dd/MM/yyyy").format(this.getTerminoData()))
            .toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
      if (codigo == null) {
         if (other.codigo != null)
            return false;
      } else if (!codigo.equals(other.codigo))
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
