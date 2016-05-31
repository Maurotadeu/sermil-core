package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** CS - Agendamento.
 * @author Abreu Lopes
 * @since 5.2.8
 * @version 5.4
 */
@Entity
@Table(name = "CS_AGENDAMENTO")
@PrimaryKey(validation=IdValidation.NULL)
@NamedQueries({
   @NamedQuery(name = "CsAgendamento.listarPorCs", query = "SELECT c FROM CsAgendamento c WHERE c.pk.csCodigo = ?1"),
   @NamedQuery(name = "CsAgendamento.listarPorCsData", query = "SELECT c.ra, c.nome, c.email, c.telefone, a.dataSelecao FROM Cidadao c JOIN CsAgendamento a ON c.ra = a.pk.cidadaoRa WHERE a.pk.csCodigo = ?1 AND a.dataSelecao = ?2"),
   @NamedQuery(name = "CsAgendamento.listarPorRa", query = "SELECT c FROM CsAgendamento c WHERE c.pk.cidadaoRa = ?1"),
   @NamedQuery(name = "CsAgendamento.gruparPorEmail", query = "SELECT c.email, COUNT(c) FROM CsAgendamento c GROUP BY c.email") 
})
public final class CsAgendamento implements Serializable {

   private static final long serialVersionUID = 4296531679466838124L;

   @EmbeddedId
   private PK pk;

   @Column(name="DATA_SELECAO")
   @Temporal(TemporalType.TIMESTAMP)
   private Date dataSelecao;

   private String email;

   public CsAgendamento() {
      super();
   }

   public CsAgendamento(final Integer cs, final Long ra) {
      this.pk = new CsAgendamento.PK(cs, ra);
   }

   @Override
   public String toString() {
      return new StringBuilder("AGENDAMENTO: ").append(this.getPk()).append(" - ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.getDataSelecao())).toString();
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
      CsAgendamento other = (CsAgendamento) obj;
      if (pk == null) {
         if (other.pk != null)
            return false;
      } else if (!pk.equals(other.pk))
         return false;
      return true;
   }


   /** Chave primária (PK) de CsAgendamento.
    * @author Abreu Lopes
    * @since 5.2.8
    * @version 5.4
    */
   @Embeddable
   public static class PK implements Comparable<CsAgendamento.PK>, Serializable {

      private static final long serialVersionUID = 4663394082030794278L;

      @Column(name="CS_CODIGO")
      private Integer csCodigo;
      
      @Column(name="CIDADAO_RA")
      private Long cidadaoRa;

      public PK() {
         super();
      }

      public PK(final Integer csCodigo, final Long cidadaoRa) {
         this.setCsCodigo(csCodigo);
         this.setCidadaoRa(cidadaoRa);
      }

      @Override
      public String toString() {
         return new StringBuilder(this.getCsCodigo() == null ? "CS" : this.getCsCodigo().toString())
               .append(" - ")
               .append(this.getCidadaoRa() == null ? "RA" : this.getCidadaoRa().toString())
               .toString();
      }
      
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result
               + ((cidadaoRa == null) ? 0 : cidadaoRa.hashCode());
         result = prime * result
               + ((csCodigo == null) ? 0 : csCodigo.hashCode());
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
         PK other = (PK) obj;
         if (cidadaoRa == null) {
            if (other.cidadaoRa != null)
               return false;
         } else if (!cidadaoRa.equals(other.cidadaoRa))
            return false;
         if (csCodigo == null) {
            if (other.csCodigo != null)
               return false;
         } else if (!csCodigo.equals(other.csCodigo))
            return false;
         return true;
      }

      @Override
      public int compareTo(PK o) {
         return this.getCsCodigo().compareTo(o.getCsCodigo()) == 0 ? this.getCidadaoRa().compareTo(o.getCidadaoRa()) : this.getCsCodigo().compareTo(o.getCsCodigo());
      }

      public Integer getCsCodigo() {
         return csCodigo;
      }

      public void setCsCodigo(Integer csCodigo) {
         this.csCodigo = csCodigo;
      }

      public Long getCidadaoRa() {
         return cidadaoRa;
      }

      public void setCidadaoRa(Long cidadaoRa) {
         this.cidadaoRa = cidadaoRa;
      }
      
   }

   public PK getPk() {
      return pk;
   }

   public void setPk(PK pk) {
      this.pk = pk;
   }

   public Date getDataSelecao() {
      return dataSelecao;
   }

   public void setDataSelecao(Date dataSelecao) {
      this.dataSelecao = dataSelecao;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
   
}
