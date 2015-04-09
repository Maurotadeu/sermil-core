package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Habilitação militar ou civil.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidHabilitacao.java 2426 2014-05-14 15:01:41Z wlopes $
 */
@Entity
@Table(name = "CID_HABILITACAO")
public class CidHabilitacao implements Comparable<CidHabilitacao>, Serializable {

  private static final long serialVersionUID = -8013079592385222333L;

  @EmbeddedId
  private CidHabilitacao.PK pk;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name = "HABILITACAO_CODIGO", insertable = false, updatable = false, nullable = false)
  private Habilitacao habilitacao;

  public CidHabilitacao() {
    this.setPk(new CidHabilitacao.PK());
  }

  public CidHabilitacao(final Long ra, final String codigo) {
    this.setPk(new CidHabilitacao.PK(ra, codigo));
  }

  @Override
  public int compareTo(CidHabilitacao o) {
    return this.getPk().compareTo(o.getPk());
  }
  
 @Override
  public String toString() {
    return this.getHabilitacao() == null ? "HABILITACAO" : this.getHabilitacao().toString();
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
    CidHabilitacao other = (CidHabilitacao) obj;
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

  public Habilitacao getHabilitacao() {
    return habilitacao;
  }

  public CidHabilitacao.PK getPk() {
    return this.pk;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidHabilitacaoCollection().contains(this)) {
      cid.getCidHabilitacaoCollection().add(this);
    }
  }

  public void setHabilitacao(Habilitacao habilitacao) {
    this.habilitacao = habilitacao;
  }

  public void setPk(CidHabilitacao.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidHabilitacao.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidHabilitacao.java 2426 2014-05-14 15:01:41Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidHabilitacao.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1352771489108327100L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name = "HABILITACAO_CODIGO")
    private String habilitacaoCodigo;

    public PK() {
      super();
    }
    
    public PK(Long cidadaoRa, String habilitacaoCodigo) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setHabilitacaoCodigo(habilitacaoCodigo);
    }

    @Override
    public int compareTo(CidHabilitacao.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getHabilitacaoCodigo().compareTo(o.getHabilitacaoCodigo());
      }
      return status;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime
          * result
          + ((this.habilitacaoCodigo == null) ? 0 : this.habilitacaoCodigo
              .hashCode());
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
      if (this.habilitacaoCodigo == null) {
        if (other.habilitacaoCodigo != null)
          return false;
      } else if (!this.habilitacaoCodigo.equals(other.habilitacaoCodigo))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public String getHabilitacaoCodigo() {
      return this.habilitacaoCodigo;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setHabilitacaoCodigo(String habilitacaoCodigo) {
      this.habilitacaoCodigo = habilitacaoCodigo;
    }

  }

}
