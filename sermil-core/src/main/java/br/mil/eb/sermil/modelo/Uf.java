package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entidade UF.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=30)
@NamedQuery(name = "Uf.listar", query = "SELECT u FROM Uf u ORDER BY u.sigla")
@PrimaryKey(validation=IdValidation.NULL)
public final class Uf implements Comparable<Uf>, Serializable {

   /** serialVersionUID.*/
   private static final long serialVersionUID = -2069832972596296621L;

   @Id
   private String sigla;

   private Integer capital;

   private String descricao;

   public Uf() {
      super();
   }

   public Uf(String uf) {
      this.setSigla(uf);
   }

   @Override
   public int compareTo(Uf o) {
      return this.getSigla().compareTo(o.getSigla());
   }

   @Override
   public String toString() {
      return this.getSigla() == null ? "UF" : this.getSigla();
   }

   public Integer getCapital() {
      return this.capital;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public String getSigla() {
      return this.sigla;
   }

   public void setCapital(Integer capital) {
      this.capital = capital;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public void setSigla(String sigla) {
      this.sigla = sigla;
   }

}
