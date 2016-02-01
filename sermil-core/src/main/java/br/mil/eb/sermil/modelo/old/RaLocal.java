package br.mil.eb.sermil.modelo.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.mil.eb.sermil.modelo.Jsm;

/** Sequência de RA em BD Local.
 * @author WLOPES
 * @since 3.0
 * @version $Id: RaLocal.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "RA_LOCAL")
@NamedQuery(name = "RaLocal.listarRa", query = "SELECT r FROM RaLocal r")
@Deprecated
public final class RaLocal implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RaLocal.PK pk;

  private Integer sequencial;

  @ManyToOne(optional = false)
  @JoinColumns({
    @JoinColumn(name = "CSM_CODIGO", referencedColumnName="CSM_CODIGO", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "JSM_CODIGO", referencedColumnName="CODIGO", insertable = false, updatable = false, nullable = false)
  })
  private Jsm jsm;

  public RaLocal() {
    this.setPk(new RaLocal.PK());
  }

  public RaLocal(final Byte csm, final Short jsm, final Integer seq) {
    this();
    this.getPk().setCsmCodigo(csm);
    this.getPk().setJsmCodigo(jsm);
    this.setSequencial(seq);
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk() == null ? "NULO" : this.getPk().toString())
      .append(" - ")
      .append(this.getSequencial() == null ? "NULO" : this.getSequencial())
      .toString();
  }
  
  public RaLocal.PK getPk() {
    return this.pk;
  }

  public void setPk(RaLocal.PK pk) {
    this.pk = pk;
  }

  public Integer getSequencial() {
    return this.sequencial;
  }

  public void setSequencial(Integer sequencial) {
    this.sequencial = sequencial;
  }

  public Jsm getJsm() {
    return this.jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -4133353092023415461L;

    @Column(name = "CSM_CODIGO")
    private Byte csmCodigo;

    @Column(name = "JSM_CODIGO")
    private Short jsmCodigo;

    public PK() {
      super();
    }

    public PK(final Byte csm, final Short jsm) {
      this.setCsmCodigo(csm);
      this.setJsmCodigo(jsm);
    }
    
    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public void setCsmCodigo(Byte codigo) {
      this.csmCodigo = codigo;
    }

    public Short getJsmCodigo() {
      return this.jsmCodigo;
    }

    public void setJsmCodigo(Short codigo) {
      this.jsmCodigo = codigo;
    }
  }
}