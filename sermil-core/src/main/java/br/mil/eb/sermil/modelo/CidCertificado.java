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

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Entidade CidCertificado. (TABELA CID_CERTIFICADO)
 * @author Abreu Lopes, Anselmo Ribeiro
 * @since 3.0
 * @version 5.4.2
 */
@Entity
@Table(name = "CID_CERTIFICADO")
@NamedQueries({
   @NamedQuery(name = "Certificado.cidadaoTemCdi", query = " SELECT c.numero FROM CidCertificado c WHERE c.pk.cidadaoRa = ?1 and c.pk.tipo = 3 "),
   @NamedQuery(name = "Certificado.listarPorRa", query = " SELECT c FROM CidCertificado c WHERE c.pk.cidadaoRa = ?1")
})
public final class CidCertificado implements Comparable<CidCertificado>, Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 5949927084657236653L;

   @EmbeddedId
   private CidCertificado.PK pk;

   private String motivo;

   private Integer numero;

   private String responsavel;

   private String serie;

   @Column(name = "SITUACAO_ESPECIAL")
   private String situacaoEspecial;

   private String entregue;

   private String anulado;

   @ManyToOne
   @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
   private Cidadao cidadao;

   @ManyToOne
   @JoinColumn(name = "OM_CODIGO", referencedColumnName = "CODIGO")
   private Om om;

   public CidCertificado() {
      this.setPk(new CidCertificado.PK());
   }

   public CidCertificado(final Long ra, final Integer tipo, final Date data) throws SermilException {
      this.setPk(new CidCertificado.PK(ra, tipo, data));
   }

   @Override
   public int compareTo(CidCertificado o) {
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
      CidCertificado other = (CidCertificado) obj;
      if (this.pk == null) {
         if (other.pk != null)
            return false;
      } else if (!this.pk.equals(other.pk))
         return false;
      return true;
   }

   public void decode(String linha) throws Exception {
      this.getPk().setCidadaoRa(Long.valueOf(linha.substring(2, 14)));
      this.getPk().setTipo(Integer.valueOf(linha.substring(14, 16)));
      final Calendar data = Calendar.getInstance();
      data.set(Integer.parseInt(linha.substring(20, 24)), Integer.parseInt(linha.substring(18, 20)) - 1, Integer.parseInt(linha.substring(16, 18)));
      this.getPk().setData(data.getTime());
      this.setNumero(Integer.valueOf(linha.substring(24, 35).replaceAll("[^0-9 ]", "").trim()));
      this.setSerie(linha.substring(24, 35).replaceAll("[^A-Z ]", ""));
      this.setResponsavel("M�dulo JSM");
   }

   public Cidadao getCidadao() {
      return cidadao;
   }

   public void setCidadao(Cidadao cid) {
      this.cidadao = cid;
      if (!cid.getCidCertificadoCollection().contains(this)) {
         cid.getCidCertificadoCollection().add(this);
      }
   }

   public String getMotivo() {
      return motivo;
   }

   public Integer getNumero() {
      return numero;
   }

   public Om getOm() {
      return om;
   }

   public CidCertificado.PK getPk() {
      return pk;
   }

   public String getResponsavel() {
      return responsavel;
   }

   public String getSerie() {
      return serie;
   }

   public String getSituacaoEspecial() {
      return situacaoEspecial;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public void setNumero(Integer numero) {
      this.numero = numero;
   }

   public void setOm(Om om) {
      this.om = om;
   }

   public void setPk(CidCertificado.PK pk) {
      this.pk = pk;
   }

   public void setResponsavel(String responsavel) {
      this.responsavel = responsavel;
   }

   public void setSerie(String serie) {
      this.serie = serie;
   }

   public void setSituacaoEspecial(String situacaoEspecial) {
      this.situacaoEspecial = situacaoEspecial;
   }

   public String getEntregue() {
      return entregue;
   }

   public void setEntregue(String entregue) {
      this.entregue = entregue;
   }

   public String getAnulado() {
      return anulado;
   }

   public void setAnulado(String anulado) {
      this.anulado = anulado;
   }

   /** Chave prim�ria (PK) de CidCertificado.
    * @author Abreu Lopes
    * @since 3.0
    * @version 5.4
    */
   @Embeddable
   public static class PK implements Comparable<CidCertificado.PK>, Serializable {

      private static final long serialVersionUID = 6039795906546448337L;

      @Column(name = "CIDADAO_RA")
      private Long cidadaoRa;

      @Temporal(TemporalType.DATE)
      @Column(name = "DATA")
      private Date data;

      @Column(name = "TIPO")
      private Integer tipo;

      public PK() {
         super();
      }

      public PK(final Long cidadaoRa, final Integer tipo, final Date data) throws SermilException {
         super();
         this.setCidadaoRa(cidadaoRa);
         this.setTipo(tipo);
         this.setData(data);
      }

      @Override
      public int compareTo(CidCertificado.PK o) {
         int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
         if (status == 0) {
            status = this.getData().compareTo(o.getData());
            if (status == 0) {
               status = this.getTipo().compareTo(o.getTipo());
            }
         }
         return status;
      }

      @Override
      public String toString() {
         return new StringBuilder(this.getCidadaoRa() == null ? "RA" : this.getCidadaoRa().toString())
               .append(" - ").append(this.getTipo() == null ? "TIPO" : this.getTipo())
               .append(" - ").append(this.getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getData()))
               .toString();
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
         result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
         result = prime * result + ((this.tipo == null) ? 0 : this.tipo.hashCode());
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
         if (this.data == null) {
            if (other.data != null)
               return false;
         } else if (!this.data.equals(other.data))
            return false;
         if (this.tipo == null) {
            if (other.tipo != null)
               return false;
         } else if (!this.tipo.equals(other.tipo))
            return false;
         return true;
      }

      public Long getCidadaoRa() {
         return this.cidadaoRa;
      }

      public Date getData() {
         return this.data;
      }

      public Integer getTipo() {
         return this.tipo;
      }

      public void setCidadaoRa(Long cidadaoRa) {
         this.cidadaoRa = cidadaoRa;
      }

      public void setData(Date data) throws SermilException {
         final Calendar cal = Calendar.getInstance();
         if (cal.getTime().before(data)) {
            throw new SermilException("Data maior que a data atual.");
         } else {
            cal.set(1900, 0, 1); // 01-01-1900
            if (cal.getTime().after(data)) {
               throw new SermilException("Data menor que 01/01/1900.");
            }
         }
         this.data = data;
      }

      public void setTipo(Integer tipo) {
         this.tipo = tipo;
      }

   }

}
