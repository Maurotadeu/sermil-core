package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Eventos de cidadão.
 * 
 * @author Abreu Lopes
 * @since 2.0
 * @version $Id: CidEvento.java 2423 2014-05-13 17:00:54Z wlopes $
 */
@Entity
@Table(name = "CID_EVENTO")
@NamedQueries({ @NamedQuery(name = "Evento.cidadaoPodeImprimirCdi", query = "SELECT e.cidadao.ra FROM CidEvento e WHERE e.cidadao.ra = ?1 and e.pk.codigo in (3,6,13,14,24)"),
      @NamedQuery(name = "Evento.listarPorCodigo", query = "SELECT e FROM CidEvento e WHERE e.cidadao.ra = ?1 and e.pk.codigo = ?2") })
public final class CidEvento implements Comparable<CidEvento>, Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -8698276787632308527L;

   public final static Byte EXCESSO_DE_CONTINGENTE_CODIGO = 6; 

   @EmbeddedId
   private CidEvento.PK pk;

   private String anotacao;

   @Column(name = "BI_ABI_NR")
   private String biAbiNr;

   @ManyToOne
   @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
   private Cidadao cidadao;

   public CidEvento() {
      this.setPk(new CidEvento.PK());
   }

   public CidEvento(final Long ra, final Byte codigo, final Date data) {
      this.setPk(new CidEvento.PK(ra, codigo, data));
   }

   @Override
   public int compareTo(CidEvento o) {
      return this.getPk().compareTo(o.getPk());
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getPk().getCodigo() == null ? "EVENTO" : this.getPk().getCodigo().toString()).append(" - ")
            .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData())).toString();
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

   public Cidadao getCidadao() {
      return this.cidadao;
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

   public void setCidadao(Cidadao cid) {
      this.cidadao = cid;
      if (!cid.getCidEventoCollection().contains(this)) {
         cid.getCidEventoCollection().add(this);
      }
   }

   public void setPk(CidEvento.PK pk) {
      this.pk = pk;
   }

   /**
    * Chave primária (PK) de CidEvento.
    * 
    * @author Abreu Lopes
    * @since 3.0
    * @version $Id: CidEvento.java 2423 2014-05-13 17:00:54Z wlopes $
    */
   @Embeddable
   public static class PK implements Comparable<CidEvento.PK>, Serializable {

      private static final long serialVersionUID = 7201122436880705606L;

      @Column(name = "CIDADAO_RA")
      private Long cidadaoRa;

      @Column(name = "CODIGO")
      private Byte codigo;

      @Temporal(TemporalType.DATE)
      @Column(name = "DATA")
      private Date data;

      public PK() {
         super();
      }

      public PK(final Long cidadaoRa, final Byte codigo, final Date data) {
         super();
         this.setCidadaoRa(cidadaoRa);
         this.setCodigo(codigo);
         this.setData(data);
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

      public Byte getCodigo() {
         return this.codigo;
      }

      public Date getData() {
         return this.data;
      }

      public void setCidadaoRa(Long cidadaoRa) {
         this.cidadaoRa = cidadaoRa;
      }

      public void setCodigo(Byte codigo) {
         this.codigo = codigo;
      }

      public void setData(Date data) {
         Calendar cal = Calendar.getInstance();
         if (cal.getTime().before(data)) {
            throw new IllegalArgumentException("Data maior que a data atual.");
         } else {
            cal.set(1900, 0, 1); // 01-01-1900
            if (cal.getTime().after(data)) {
               throw new IllegalArgumentException("Data menor que 01/01/1900.");
            }
         }
         this.data = data;
      }

   }

}
