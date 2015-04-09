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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Averbações de cidadão.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidAverbacao.java 2426 2014-05-14 15:01:41Z wlopes $
 */
@Entity
@Table(name = "CID_AVERBACAO")
public final class CidAverbacao implements Comparable<CidAverbacao>, Serializable {

  private static final long serialVersionUID = -6446501904225971275L;

  @EmbeddedId
  private CidAverbacao.PK pk;

  @Column(name = "BI_ABI_NR")
  private String biAbiNr;

  private String descricao;

  private String responsavel;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  public CidAverbacao() {
    this.setPk(new CidAverbacao.PK());
  }

  @Override
  public int compareTo(CidAverbacao o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
    .append(" - ")
    .append(this.getDescricao() == null ? "DESC" : this.getDescricao())
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
    CidAverbacao other = (CidAverbacao) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public void decode(String linha) throws Exception {
    this.getPk().setCidadaoRa(Long.valueOf(linha.substring(2, 14)));
    final Calendar data = Calendar.getInstance();
    data.set(Integer.parseInt(linha.substring(18, 22)), Integer.parseInt(linha.substring(16, 18))-1, Integer.parseInt(linha.substring(14, 16)));
    this.setDescricao(linha.substring(22, 262).replaceAll("\\n|" + System.lineSeparator(), ""));
    this.setResponsavel("Módulo JSM");
  }

  public String getBiAbiNr() {
    return this.biAbiNr;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public CidAverbacao.PK getPk() {
    return this.pk;
  }

  public String getResponsavel() {
    return this.responsavel;
  }

  public void setBiAbiNr(String biAbiNr) {
    this.biAbiNr = biAbiNr;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidAverbacaoCollection().contains(this)) {
      cid.getCidAverbacaoCollection().add(this);
    }
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setPk(CidAverbacao.PK pk) {
    this.pk = pk;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  /** Chave primária (PK) de CidAverbação.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidAverbacao.java 2426 2014-05-14 15:01:41Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidAverbacao.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -6045393147997775112L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    private Date data;

    public PK() {
      super();
    }

    @Override
    public int compareTo(CidAverbacao.PK o) {
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
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setData(Date data) {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1970, 0, 1); // 01-01-1970
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1970.");
        }
      }
      this.data = data;
    }

  }

}
