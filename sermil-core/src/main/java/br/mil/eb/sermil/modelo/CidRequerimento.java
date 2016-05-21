package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Requerimento na CSM.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "CID_REQUERIMENTO")
public final class CidRequerimento implements Comparable<CidRequerimento>, Serializable {

  private static final long serialVersionUID = -5042652882303681814L;

  @EmbeddedId
  private CidRequerimento.PK pk;

  @Temporal(TemporalType.DATE)
  @Column(name = "BI_ABI_DATA")
  private Date biAbiData;

  @Column(name = "BI_ABI_NR")
  private String biAbiNr;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  private Byte despacho;

  private Byte motivo;

  public CidRequerimento() {
    this.setPk(new CidRequerimento.PK());
  }

  @Override
  public int compareTo(CidRequerimento o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getDocNr() == null ? "DOC" : this.getPk().getDocNr())
    .append(" - ")
    .append(this.getPk().getDocData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getDocData()))
    .append(" - ")
    .append(this.getMotivo() == null ? "MOTIVO REQUERIMENTO" : this.getMotivo())
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
    CidRequerimento other = (CidRequerimento) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Date getBiAbiData() {
    return this.biAbiData;
  }

  public String getBiAbiNr() {
    return this.biAbiNr;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public Byte getDespacho() {
    return this.despacho;
  }

  public Byte getMotivo() {
    return this.motivo;
  }

  public CidRequerimento.PK getPk() {
    return this.pk;
  }

  public void setBiAbiData(Date biAbiData) {
    this.biAbiData = biAbiData;
  }

  public void setBiAbiNr(String biAbiNr) {
    this.biAbiNr = biAbiNr;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidRequerimentoCollection().contains(this)) {
      cid.getCidRequerimentoCollection().add(this);
    }
  }

  public void setDespacho(Byte despacho) {
    this.despacho = despacho;
  }

  public void setMotivo(Byte motivo) {
    this.motivo = motivo;
  }

  public void setPk(CidRequerimento.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidRequerimento.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<CidRequerimento.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 6205210446446407069L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DOC_DATA")
    private Date docData;

    @Column(name = "DOC_NR")
    private String docNr;

    public PK() {
      super();
    }

    @Override
    public int compareTo(CidRequerimento.PK e) {
      int status = this.getCidadaoRa().compareTo(e.getCidadaoRa());
      if (status == 0 ) {
        status = this.getDocData().compareTo(e.getDocData());
        if (status == 0 ) {
          status = this.getDocNr().compareTo(e.getDocNr());
        }
      }
      return status;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result + ((this.docData == null) ? 0 : this.docData.hashCode());
      result = prime * result + ((this.docNr == null) ? 0 : this.docNr.hashCode());
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
      if (this.docData == null) {
        if (other.docData != null)
          return false;
      } else if (!this.docData.equals(other.docData))
        return false;
      if (this.docNr == null) {
        if (other.docNr != null)
          return false;
      } else if (!this.docNr.equals(other.docNr))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getDocData() {
      return this.docData;
    }

    public String getDocNr() {
      return this.docNr;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setDocData(Date data) throws SermilException {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new SermilException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new SermilException("Data menor que 01/01/1980.");
        }
      }
      this.docData = data;
    }

    public void setDocNr(String docNr) {
      this.docNr = docNr;
    }

  }

}
