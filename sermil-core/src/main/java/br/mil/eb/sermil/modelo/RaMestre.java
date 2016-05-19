package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** Entidade controle de RA (RA_MESTRE).
 * @author Abreu Lopes
 * @since 3.3
 * @version 5.4
 */
@Entity
@Table(name="RA_MESTRE")
public final class RaMestre implements Comparable<RaMestre>, Serializable {

  private static final long serialVersionUID = 5738976781405746745L;

  @EmbeddedId
  private PK pk;

  private Integer sequencial;

  @OneToOne
  @JoinColumns({
    @JoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO", insertable=false, updatable=false),
    @JoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false)
  })
  private Jsm jsm;

  public RaMestre() {
    this.setPk(new RaMestre.PK());
  }

  public RaMestre(final Byte csm, final Short jsm) {
    this.setPk(new RaMestre.PK(csm, jsm));
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString())
        .append(" - ")
        .append(this.getSequencial() == null ? "000000" : this.getSequencial())
        .toString();
  }

  @Override
  public int compareTo(RaMestre o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
    RaMestre other = (RaMestre) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public Jsm getJsm() {
    return jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Integer getSequencial() {
    return this.sequencial;
  }

  public void setSequencial(Integer sequencial) {
    this.sequencial = sequencial;
  }

  /** Chave primária (PK) de RaMestre.
   * @author Abreu Lopes
   * @since 3.3
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<RaMestre.PK>, Serializable {

    private static final long serialVersionUID = -1835332668247036168L;

    @Column(name="CSM_CODIGO")
    private Byte csmCodigo;

    @Column(name="JSM_CODIGO")
    private Short jsmCodigo;

    public PK() {
      super();
    }

    public PK(final Byte csm, final Short jsm) {
      this.setCsmCodigo(csm);
      this.setJsmCodigo(jsm);
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
          .append("/")
          .append(this.getJsmCodigo() == null ? "000" : new DecimalFormat("000").format(this.getJsmCodigo()))
          .toString();
    }

    @Override
    public int compareTo(PK o) {
      return this.getCsmCodigo().compareTo(o.getCsmCodigo()) == 0 ? this.getJsmCodigo().compareTo(o.getJsmCodigo()) : this.getCsmCodigo().compareTo(o.getCsmCodigo());
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
      result = prime * result
          + ((jsmCodigo == null) ? 0 : jsmCodigo.hashCode());
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
      if (csmCodigo == null) {
        if (other.csmCodigo != null)
          return false;
      } else if (!csmCodigo.equals(other.csmCodigo))
        return false;
      if (jsmCodigo == null) {
        if (other.jsmCodigo != null)
          return false;
      } else if (!jsmCodigo.equals(other.jsmCodigo))
        return false;
      return true;
    }

    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

    public Short getJsmCodigo() {
      return this.jsmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }

  }

}
