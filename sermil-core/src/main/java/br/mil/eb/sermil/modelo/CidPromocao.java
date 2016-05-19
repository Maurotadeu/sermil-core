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

/** Entidade CidPromocao.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "CID_PROMOCAO")
public final class CidPromocao implements Comparable<CidPromocao>, Serializable {

  private static final long serialVersionUID = -3372194819754473425L;

  @EmbeddedId
  private CidPromocao.PK pk;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  private String documento;

  public CidPromocao() {
    this.setPk(new CidPromocao.PK());
  }

  public CidPromocao(final Long ra, final Date data, final String postoGraduacaoCodigo) throws SermilException {
    this.setPk(new CidPromocao.PK(ra, data, postoGraduacaoCodigo));
  }

  @Override
  public int compareTo(CidPromocao o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getPostoGraduacaoCodigo() == null ? "P/G" : this.getPk().getPostoGraduacaoCodigo())
    .append(" - ")
    .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
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
    CidPromocao other = (CidPromocao) obj;
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

  public String getDocumento() {
    return this.documento;
  }

  public CidPromocao.PK getPk() {
    return this.pk;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidPromocaoCollection().contains(this)) {
      cid.getCidPromocaoCollection().add(this);
    }
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public void setPk(CidPromocao.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidPromocao.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<CidPromocao.PK>, Serializable {

    private static final long serialVersionUID = 7252812287797303248L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA")
    private Date data;

    @Column(name = "POSTO_GRADUACAO_CODIGO")
    private String postoGraduacaoCodigo;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Date data, final String postoGraduacaoCodigo) throws SermilException {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setData(data);
      this.setPostoGraduacaoCodigo(postoGraduacaoCodigo);
    }

    @Override
    public int compareTo(CidPromocao.PK e) {
      int status = this.getCidadaoRa().compareTo(e.getCidadaoRa());
      if (status == 0 ) {
        status = this.getData().compareTo(e.getData());
        if (status == 0 ) {
          status = this.getPostoGraduacaoCodigo().compareTo(e.getPostoGraduacaoCodigo());
        }
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
      result = prime * result + ((this.postoGraduacaoCodigo == null) ? 0 : this.postoGraduacaoCodigo.hashCode());
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
      if (this.postoGraduacaoCodigo == null) {
        if (other.postoGraduacaoCodigo != null)
          return false;
      } else if (!this.postoGraduacaoCodigo.equals(other.postoGraduacaoCodigo))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public String getPostoGraduacaoCodigo() {
      return this.postoGraduacaoCodigo;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setData(Date data) throws SermilException {
      Calendar cal = Calendar.getInstance();
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

    public void setPostoGraduacaoCodigo(String postoGraduacaoCodigo) {
      this.postoGraduacaoCodigo = postoGraduacaoCodigo;
    }

  }

}
