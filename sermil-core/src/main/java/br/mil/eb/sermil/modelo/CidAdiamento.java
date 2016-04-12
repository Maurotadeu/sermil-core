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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Adiamento de incorporação.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.2
 */
@Entity
@Table(name = "CID_ADIAMENTO")
@NamedQuery(name = "CidAdiamento.listarPorRa", query = "SELECT a FROM CidAdiamento a WHERE a.pk.cidadaoRa = ?1")
public final class CidAdiamento implements Comparable<CidAdiamento>, Serializable {

  private static final long serialVersionUID = -6367822028531187502L;

  @EmbeddedId
  private CidAdiamento.PK pk;

  @Column(name = "ANOS_QTD")
  private Byte anosQtd;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @Column(name = "DOC_NR")
  private String docNr;

  public CidAdiamento() {
    super();
  }

  @Override
  public int compareTo(CidAdiamento o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
    .append(" - ")
    .append(this.getAnosQtd() == null ? "QTD" : this.getAnosQtd().toString() + " ano(s)")
    .toString();
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
    CidAdiamento other = (CidAdiamento) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Byte getAnosQtd() {
    return this.anosQtd;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public void setCidadao(Cidadao cid) {
     this.cidadao = cid;
     if (!cid.getCidAdiamentoCollection().contains(this)) {
       cid.getCidAdiamentoCollection().add(this);
     }
   }

  public String getDocNr() {
    return this.docNr;
  }

  public CidAdiamento.PK getPk() {
    return this.pk;
  }

  public void setAnosQtd(Byte anosQtd) {
    this.anosQtd = anosQtd;
  }

  public void setDocNr(String docNr) {
    this.docNr = docNr;
  }

  public void setPk(CidAdiamento.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidAdiamento.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidAdiamento.java 2426 2014-05-14 15:01:41Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidAdiamento.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 5660330114261984034L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA")
    private Date data;

    @Column(name = "MOTIVO")
    private Byte motivo;

    public PK() {
      super();
    }

    @Override
    public String toString() {
      return new StringBuilder()
      .append(this.cidadaoRa == null ? "RA" : this.cidadaoRa)
      .append(" - ")
      .append(this.motivo == null ? "MOTIVO" : this.motivo.toString())
      .append(" - ")
      .append(this.data == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.data))
      .toString();
    }

    @Override
    public int compareTo(CidAdiamento.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getData().compareTo(o.getData());
      }
      return status;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result
          + ((this.data == null) ? 0 : this.data.hashCode());
      result = prime * result
          + ((this.motivo == null) ? 0 : this.motivo.hashCode());
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
      if (this.motivo == null) {
        if (other.motivo != null)
          return false;
      } else if (!this.motivo.equals(other.motivo))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public Byte getMotivo() {
      return this.motivo;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setData(Date data) {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1970, 0, 1); // 01-01-1900
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1970.");
        }
      }
      this.data = data;
    }

    public void setMotivo(Byte motivo) {
      this.motivo = motivo;
    }

  }

}
