package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Empregado em Empresa de Segurança Nacional (EDRSN).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidEmpresa.java 2428 2014-05-15 13:23:47Z wlopes $
 */
@Entity
@Table(name = "CID_EMPRESA")
public final class CidEmpresa implements Comparable<CidEmpresa>, Serializable {

  private static final long serialVersionUID = 4315445418947269175L;

  @EmbeddedId
  private CidEmpresa.PK pk;

  private String cargo;

  private Byte importancia;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name = "EMPRESA_CODIGO", insertable = false, updatable = false, nullable = false)
  private Empresa empresa;

  public CidEmpresa() {
    this.setPk(new CidEmpresa.PK());
  }

  public CidEmpresa(final Long ra, final String codigo) {
    this.setPk(new CidEmpresa.PK(ra, codigo));
  }

  @Override
  public int compareTo(CidEmpresa o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getEmpresa() == null ? "EMPRESA" : this.getEmpresa().toString()).append(" - ").append(this.getCargo() == null ? "CARGO" : this.getCargo()).toString();
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
    CidEmpresa other = (CidEmpresa) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public String getCargo() {
    return this.cargo;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public Empresa getEmpresa() {
    return this.empresa;
  }

  public Byte getImportancia() {
    return this.importancia;
  }

  public CidEmpresa.PK getPk() {
    return this.pk;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidEmpresaCollection().contains(this)) {
      cid.getCidEmpresaCollection().add(this);
    }
  }

  public void setEmpresa(Empresa empresa) {
    this.empresa = empresa;
  }

  public void setImportancia(Byte importancia) {
    this.importancia = importancia;
  }

  public void setPk(CidEmpresa.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidEmpresa.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidEmpresa.java 2428 2014-05-15 13:23:47Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidEmpresa.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8570088121389060088L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name = "EMPRESA_CODIGO")
    private String empresaCodigo;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final String empresaCodigo) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setEmpresaCodigo(empresaCodigo);
    }

    @Override
    public int compareTo(CidEmpresa.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getEmpresaCodigo().compareTo(o.getEmpresaCodigo());
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
          + ((this.empresaCodigo == null) ? 0 : this.empresaCodigo.hashCode());
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
      if (this.empresaCodigo == null) {
        if (other.empresaCodigo != null)
          return false;
      } else if (!this.empresaCodigo.equals(other.empresaCodigo))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public String getEmpresaCodigo() {
      return this.empresaCodigo;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setEmpresaCodigo(String empresaCodigo) {
      this.empresaCodigo = empresaCodigo;
    }

  }

}
