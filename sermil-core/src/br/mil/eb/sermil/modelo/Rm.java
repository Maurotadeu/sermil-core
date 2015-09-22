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

/**
 * Região Militar.
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Rm.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Rm.listar", query = "SELECT r FROM Rm r")
@PrimaryKey(validation = IdValidation.NULL)
public final class Rm implements Comparable<Rm>, Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -8885833939779074521L;

   @Id
   private Byte codigo;

   private String descricao;

   private String sigla;

   @ManyToOne
   private Cma cma;

   public Rm() {
      super();
   }

   @Override
   public int compareTo(Rm o) {
      return this.getCodigo().compareTo(o.getCodigo());
   }

   @Override
   public String toString() {
      return this.getSigla() == null ? "NULO" : this.getSigla();
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

   @OneToMany(mappedBy = "rm", fetch = FetchType.LAZY)
   private List<Csel> cselCollection;

   public List<Csel> getCselCollection() {
      return cselCollection;
   }

   public void setCselCollection(List<Csel> cselCollection) {
      this.cselCollection = cselCollection;
   }

   public void addCsel(Csel csel) throws CselJaExisteException{
      if(this.cselCollection.contains(csel))
         throw new CselJaExisteException();
      if(this.cselCollection==null)
         this.cselCollection = new ArrayList<Csel>();
      this.cselCollection.add(csel);
   }
   
   public void removeCsel(Csel csel) throws CselNaoExisteException{
      if(!this.cselCollection.contains(csel))
         throw new CselNaoExisteException();
      this.cselCollection.remove(csel);
      csel.setRm(null);
   }

}
