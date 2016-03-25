package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.CselJaExisteException;
import br.mil.eb.sermil.core.exceptions.CselNaoExisteException;

/** Entidade RM.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.6
 */
@Entity
@NamedQuery(name = "Rm.listar", query = "SELECT r FROM Rm r")
@PrimaryKey(validation = IdValidation.NULL)
public final class Rm implements Comparable<Rm>, Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 1013478439652344523L;

   @Id
   private Byte codigo;

   private String descricao;

   private String sigla;

   @ManyToOne
   private Cma cma;

   @OneToMany(mappedBy = "rm", fetch = FetchType.LAZY)
   private List<Cs> cselCollection;

   public Rm() {
      super();
   }

   public Rm(final Byte codigo) {
      this.codigo = codigo;
   }

   @Override
   public int compareTo(Rm o) {
      return this.getCodigo().compareTo(o.getCodigo());
   }

   @Override
   public String toString() {
      return this.getSigla() == null ? "RM" : this.getSigla();
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
      Rm other = (Rm) obj;
      if (codigo == null) {
         if (other.codigo != null)
            return false;
      } else if (!codigo.equals(other.codigo))
         return false;
      return true;
   }

   public Cma getCma() {
      return this.cma;
   }

   public Byte getCodigo() {
      return this.codigo;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public String getSigla() {
      return this.sigla;
   }

   public void setCma(Cma cma) {
      this.cma = cma;
   }

   public void setCodigo(Byte codigo) {
      this.codigo = codigo;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public void setSigla(String sigla) {
      this.sigla = sigla;
   }

   public List<Cs> getCselCollection() {
      return cselCollection;
   }

   public void setCselCollection(final List<Cs> cselCollection) {
      this.cselCollection = cselCollection;
   }

   public void addCsel(final Cs csel) throws CselJaExisteException{
      if(this.cselCollection.contains(csel)) {
         throw new CselJaExisteException();
      }
      if(this.cselCollection == null) {
         this.cselCollection = new ArrayList<Cs>();
      }
      this.cselCollection.add(csel);
   }
   
   public void removeCsel(final Cs csel) throws CselNaoExisteException{
      if(!this.cselCollection.contains(csel)) {
         throw new CselNaoExisteException();
      }
      this.cselCollection.remove(csel);
      csel.setRm(null);
   }

}
