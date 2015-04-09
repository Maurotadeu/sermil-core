package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Grupo de Distribuição de JSM (DSTB_GD_JSM).
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: DstbGdJsm.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name="DSTB_GD_JSM")
public final class DstbGdJsm implements Comparable<DstbGdJsm>, Serializable {
  
  private static final long serialVersionUID = 5342503811855861185L;

  @EmbeddedId
	private PK pk;

	private Integer prioridade;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name="RM_CODIGO", referencedColumnName="RM_CODIGO",  insertable=false, updatable=false),
    @JoinColumn(name="DSTB_GD_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false)
  })
	private DstbGd dstbGd;

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumns({
    @JoinColumn(name = "CSM_CODIGO", referencedColumnName = "CSM_CODIGO", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "JSM_CODIGO", referencedColumnName = "CODIGO", insertable = false, updatable = false, nullable = false)
  })
  private Jsm jsm;
	
	public DstbGdJsm() {
    this.setPk(new PK());
	}

  public DstbGdJsm(final Byte rm, final Integer gd, final Byte csm, final Short jsm) {
    this.setPk(new PK(rm, gd, csm, jsm));
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
    DstbGdJsm other = (DstbGdJsm) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  @Override
	public int compareTo(DstbGdJsm o) {
	  return this.getPk().compareTo(o.getPk());
	}	
	
  @Override
  public String toString() {
    return this.getPk().toString();
  }
  
	public PK getPk() {
		return this.pk;
	}

	public void setPk(PK pk) {
		this.pk = pk;
	}

	public Jsm getJsm() {
    return jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Integer prioridade) {
    this.prioridade = prioridade;
  }

  public DstbGd getDstbGd() {
		return this.dstbGd;
	}

	public void setDstbGd(DstbGd dstbGd) {
		this.dstbGd = dstbGd;
	}

  /** Chave primária (PK) de DstbGdJsm.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: DstbGdJsm.java 1637 2011-11-25 13:52:11Z wlopes $
   */
	@Embeddable
	public static class PK implements Comparable<DstbGdJsm.PK>, Serializable {
	  
    private static final long serialVersionUID = 2797764839541959689L;

    @Column(name="RM_CODIGO")
    private Byte rmCodigo;

    @Column(name="DSTB_GD_CODIGO")
    private Integer dstbGdCodigo;

	  @Column(name="CSM_CODIGO")
	  private Byte csmCodigo;

	  @Column(name="JSM_CODIGO")
	  private Short jsmCodigo;

	  public PK() {
	    super();
	  }
	  
    public PK(final Byte rmCodigo, final Integer dstbGdCodigo, final Byte csmCodigo, final Short jsmCodigo) {
      super();
      this.setRmCodigo(rmCodigo);
      this.setDstbGdCodigo(dstbGdCodigo);
      this.setCsmCodigo(csmCodigo);
      this.setJsmCodigo(jsmCodigo);
    }

    @Override
    public int compareTo(DstbGdJsm.PK o) {
      int status = this.getRmCodigo().compareTo(o.getRmCodigo());
      if (status == 0) {
        status = this.getDstbGdCodigo().compareTo(o.getDstbGdCodigo());
        if (status == 0) {
          status = this.getCsmCodigo().compareTo(o.getCsmCodigo());
          if (status == 0) {
            status = this.getJsmCodigo().compareTo(o.getJsmCodigo());
          }
        }
      }
      return status;
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getRmCodigo() == null ? "00" : this.getRmCodigo().toString())
        .append(" RM - GD ")
        .append(this.getDstbGdCodigo() == null ? "0" : this.getDstbGdCodigo())
        .append(" - ")
        .append(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
        .append("/")
        .append(this.getJsmCodigo() == null ? "000" : new DecimalFormat("000").format(this.getJsmCodigo()))
        .toString();
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.csmCodigo == null) ? 0 : this.csmCodigo.hashCode());
      result = prime * result
          + ((this.dstbGdCodigo == null) ? 0 : this.dstbGdCodigo.hashCode());
      result = prime * result
          + ((this.jsmCodigo == null) ? 0 : this.jsmCodigo.hashCode());
      result = prime * result
          + ((this.rmCodigo == null) ? 0 : this.rmCodigo.hashCode());
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
      if (this.csmCodigo == null) {
        if (other.csmCodigo != null)
          return false;
      } else if (!this.csmCodigo.equals(other.csmCodigo))
        return false;
      if (this.dstbGdCodigo == null) {
        if (other.dstbGdCodigo != null)
          return false;
      } else if (!this.dstbGdCodigo.equals(other.dstbGdCodigo))
        return false;
      if (this.jsmCodigo == null) {
        if (other.jsmCodigo != null)
          return false;
      } else if (!this.jsmCodigo.equals(other.jsmCodigo))
        return false;
      if (this.rmCodigo == null) {
        if (other.rmCodigo != null)
          return false;
      } else if (!this.rmCodigo.equals(other.rmCodigo))
        return false;
      return true;
    }

    public Byte getRmCodigo() {
      return this.rmCodigo;
    }

    public void setRmCodigo(Byte rmCodigo) {
      this.rmCodigo = rmCodigo;
    }

    public Integer getDstbGdCodigo() {
      return dstbGdCodigo;
    }

    public void setDstbGdCodigo(Integer dstbGdCodigo) {
      this.dstbGdCodigo = dstbGdCodigo;
    }

    public Byte getCsmCodigo() {
      return csmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

    public Short getJsmCodigo() {
      return jsmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }

	}
	
}
