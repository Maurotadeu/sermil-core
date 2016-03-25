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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Exclusão de datas de funcionamento da CS.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 5.3.0
 * @version 5.3.2
 */
@Entity
@Table(name = "CS_EXLUSAO_DATA")
@NamedQueries({
   @NamedQuery(name = "CsExclusaoData.listarPorCs", query = "select f from CsExclusaoData f where f.cs.codigo = ?1 ")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class CsExclusaoData implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CS_EXCLUSAO_DATA")
   @TableGenerator(name = "CS_EXCLUSAO_DATA", allocationSize = 1)
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO", nullable = false)
   private Cs cs;

   @Column(name = "EXCLUSAO_DATA", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date ExclusaoData;

   public CsExclusaoData() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(new SimpleDateFormat("dd/MM/yyyy").format(this.getExclusaoData())).toString();
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((ExclusaoData == null) ? 0 : ExclusaoData.hashCode());
      result = prime * result + ((cs == null) ? 0 : cs.hashCode());
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
      CsExclusaoData other = (CsExclusaoData) obj;
      if (ExclusaoData == null) {
         if (other.ExclusaoData != null)
            return false;
      } else if (!ExclusaoData.equals(other.ExclusaoData))
         return false;
      if (cs == null) {
         if (other.cs != null)
            return false;
      } else if (!cs.equals(other.cs))
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

   public Date getExclusaoData() {
      return ExclusaoData;
   }

   public void setExclusaoData(Date exclusaoData) {
      ExclusaoData = exclusaoData;
   }
   
}
