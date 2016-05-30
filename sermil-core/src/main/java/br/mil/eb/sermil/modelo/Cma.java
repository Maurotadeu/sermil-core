package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entidade CMA (Comando Militar de Área).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=10)
@PrimaryKey(validation=IdValidation.NULL)
public final class Cma implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -6654214842787703523L;

   @Id
   private Integer codigo;

   private Byte cta;

   private String descricao;

   private String sigla;

   public Cma() {
      super();
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
      Cma other = (Cma) obj;
      if (codigo == null) {
         if (other.codigo != null)
            return false;
      } else if (!codigo.equals(other.codigo))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return this.getSigla() == null ? "CMA" : this.getSigla();
   }

   public Integer getCodigo() {
      return this.codigo;
   }

   public Byte getCta() {
      return this.cta;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public String getSigla() {
      return this.sigla;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public void setCta(Byte cta) {
      this.cta = cta;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public void setSigla(String sigla) {
      this.sigla = sigla;
   }

}
