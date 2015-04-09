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

/** Exercício de mobilização.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidMobilizacao.java 2428 2014-05-15 13:23:47Z wlopes $
 */
@Entity
@Table(name = "CID_MOBILIZACAO")
public final class CidMobilizacao implements Comparable<CidMobilizacao>, Serializable {

  private static final long serialVersionUID = 5504096533657054913L;

  @EmbeddedId
  private CidMobilizacao.PK pk;

  private String observacao;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name = "OM_CODIGO", updatable = false, nullable = false)
  private Om om;

  public CidMobilizacao() {
    this.setPk(new CidMobilizacao.PK());
  }

  public CidMobilizacao(final Long ra, final Date data) {
    this.setPk(new CidMobilizacao.PK(ra, data));
  }

  @Override
  public int compareTo(CidMobilizacao o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getOm() == null ? "OM" : this.getOm().toString())
      .append(" - ")
      .append(this.getPk().getApresentacaoData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getApresentacaoData()))
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
    CidMobilizacao other = (CidMobilizacao) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public String getObservacao() {
    return this.observacao;
  }

  public Om getOm() {
    return om;
  }

  public CidMobilizacao.PK getPk() {
    return this.pk;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidMobilizacaoCollection().contains(this)) {
      cid.getCidMobilizacaoCollection().add(this);
    }
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public void setPk(CidMobilizacao.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidMobilizacao.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidMobilizacao.java 2428 2014-05-15 13:23:47Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidMobilizacao.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 2686055299438185483L;

    @Temporal(TemporalType.DATE)
    @Column(name = "APRESENTACAO_DATA")
    private Date apresentacaoData;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Date apresentacaoData) {
      super();
      this.setApresentacaoData(apresentacaoData);
      this.setCidadaoRa(cidadaoRa);
    }

    @Override
    public int compareTo(CidMobilizacao.PK e) {
      int status = this.getCidadaoRa().compareTo(e.getCidadaoRa());
      if (status == 0 ) {
        status = this.getApresentacaoData().compareTo(e.getApresentacaoData());
      }
      return status;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime
          * result
          + ((this.apresentacaoData == null) ? 0 : this.apresentacaoData
              .hashCode());
      result = prime * result
          + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
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
      if (this.apresentacaoData == null) {
        if (other.apresentacaoData != null)
          return false;
      } else if (!this.apresentacaoData.equals(other.apresentacaoData))
        return false;
      if (this.cidadaoRa == null) {
        if (other.cidadaoRa != null)
          return false;
      } else if (!this.cidadaoRa.equals(other.cidadaoRa))
        return false;
      return true;
    }

    public Date getApresentacaoData() {
      return this.apresentacaoData;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public void setApresentacaoData(Date data) {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.apresentacaoData = data;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

  }

}
