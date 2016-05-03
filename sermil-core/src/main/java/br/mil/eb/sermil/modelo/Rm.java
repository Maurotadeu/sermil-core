package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entidade RM.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@NamedQuery(name = "Rm.listar", query = "SELECT r FROM Rm r")
@NamedNativeQueries({
  @NamedNativeQuery(resultClass = Rm.class, name = "rm.rmComProblemaDeBCCIAP", query = "select * from rm where CODIGO = ?1 and  (SELECT  COUNT(*) FROM cidadao PARTITION(ano_2015) c JOIN cid_bcc b ON c.ra = b.cidadao_ra JOIN csm m ON c.csm_codigo = m.codigo and  m.RM_CODIGO = ?1) > 1.1*(SELECT  count(*) total FROM cidadao PARTITION(ano_2015) c JOIN jsm j ON c.csm_codigo = j.csm_codigo AND c.jsm_codigo = j.codigo JOIN csm m ON j.csm_codigo = m.codigo WHERE c.situacao_militar = 4 AND c.escolaridade > 9 AND c.dispensa = 0 AND j.tributacao IN (1,2,4) and m.rm_codigo = ?1 )")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class Rm implements Comparable<Rm>, Serializable {

   private static final long serialVersionUID = 1408413835050749386L;

   @Id
   private Byte codigo;

   private String descricao;

   private String sigla;

   @ManyToOne(cascade=CascadeType.REFRESH)
   private Cma cma;

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

}
