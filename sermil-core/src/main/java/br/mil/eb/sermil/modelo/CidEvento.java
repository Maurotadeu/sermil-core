package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
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

/** Entidade CidEvento. (Tabela CID_EVENTO)
 * @author Abreu Lopes
 * @since 2.0
 * @version 5.3.0
 */
@Entity
@Table(name = "CID_EVENTO")
@NamedQueries({@NamedQuery(name = "Evento.cidadaoPodeImprimirCdi", query = "SELECT e.pk.cidadaoRa FROM CidEvento e WHERE e.pk.cidadaoRa = ?1 and e.pk.codigo in (3,6,13,14,24)"),
               @NamedQuery(name = "Evento.listarPorCodigo", query = "SELECT e FROM CidEvento e WHERE e.pk.cidadaoRa = ?1 and e.pk.codigo = ?2"),
               @NamedQuery(name = "Evento.listarPorRa", query = "SELECT e FROM CidEvento e WHERE e.pk.cidadaoRa = ?1") })
public final class CidEvento implements Comparable<CidEvento>, Serializable {

   private static final long serialVersionUID = -5598327262769541831L;

   @EmbeddedId
   private CidEvento.PK pk;

   private String anotacao;

   @Column(name = "BI_ABI_NR")
   private String biAbiNr;

   public CidEvento() {
      this.setPk(new CidEvento.PK());
   }

   public CidEvento(final Long ra, final Integer codigo, final Date data) {
      this.setPk(new CidEvento.PK(ra, codigo, data));
   }

   @Override
   public int compareTo(CidEvento o) {
      return this.getPk().compareTo(o.getPk());
   }

   @Override
   public String toString() {
      return this.getPk().toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.pk == null) ? 0 : this.pk.hashCode());
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
      CidEvento other = (CidEvento) obj;
      if (this.pk == null) {
         if (other.pk != null)
            return false;
      } else if (!this.pk.equals(other.pk))
         return false;
      return true;
   }

   public String getAnotacao() {
      return this.anotacao;
   }

   public String getBiAbiNr() {
      return this.biAbiNr;
   }

   public CidEvento.PK getPk() {
      return this.pk;
   }

   public void setAnotacao(String anotacao) {
      this.anotacao = anotacao;
   }

   public void setBiAbiNr(String biAbiNr) {
      this.biAbiNr = biAbiNr;
   }

   public void setPk(CidEvento.PK pk) {
      this.pk = pk;
   }

   /** Chave primária (PK) de CidEvento.
    * @author Abreu Lopes
    * @since 3.0
    * @version 5.3.0
    */
   @Embeddable
   public static class PK implements Comparable<CidEvento.PK>, Serializable {

      private static final long serialVersionUID = -5871449519288198767L;

      @Column(name = "CIDADAO_RA")
      private Long cidadaoRa;

      @Column(name = "CODIGO")
      private Integer codigo;

      @Temporal(TemporalType.DATE)
      @Column(name = "DATA")
      private Date data;

      public PK() {
         super();
      }

      public PK(final Long cidadaoRa, final Integer codigo, final Date data) {
         super();
         this.setCidadaoRa(cidadaoRa);
         this.setCodigo(codigo);
         this.setData(data);
      }

      @Override
      public String toString() {
         return new StringBuilder(this.getCodigo() == null ? "EVENTO" : this.getCodigo().toString()).append(" - ")
               .append(this.getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getData())).toString();
      }

      @Override
      public int compareTo(CidEvento.PK e) {
         int status = this.getCidadaoRa().compareTo(e.getCidadaoRa());
         if (status == 0) {
            status = this.getData().compareTo(e.getData());
            if (status == 0) {
               status = this.getCodigo().compareTo(e.getCodigo());
            }
         }
         return status;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
         result = prime * result + ((this.codigo == null) ? 0 : this.codigo.hashCode());
         result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
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
         if (this.cidadaoRa == null) {
            if (other.cidadaoRa != null)
               return false;
         } else if (!this.cidadaoRa.equals(other.cidadaoRa))
            return false;
         if (this.codigo == null) {
            if (other.codigo != null)
               return false;
         } else if (!this.codigo.equals(other.codigo))
            return false;
         if (this.data == null) {
            if (other.data != null)
               return false;
         } else if (!this.data.equals(other.data))
            return false;
         return true;
      }

      public Long getCidadaoRa() {
         return this.cidadaoRa;
      }

      public Integer getCodigo() {
         return this.codigo;
      }

      public Date getData() {
         return this.data;
      }

      public void setCidadaoRa(Long cidadaoRa) {
         this.cidadaoRa = cidadaoRa;
      }

      public void setCodigo(Integer codigo) {
         this.codigo = codigo;
      }

      public void setData(Date data) {
         this.data = data;
      }

   }

}
