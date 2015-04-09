package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Grupo de Distribuição (DSTB_GD).
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: DstbGd.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name="DSTB_GD")
@NamedQuery(name = "DstbGd.listarPorRm", query = "SELECT d FROM DstbGd d WHERE d.pk.rmCodigo = ?1")
public final class DstbGd implements Comparable<DstbGd>, Serializable {

  private static final long serialVersionUID = -6505056357978267778L;

  @EmbeddedId
  private PK pk;

	private Integer aptos;

	private Integer nec;

	private Integer maj;

  @ManyToOne
  @JoinColumn(name="RM_CODIGO", insertable=false, updatable=false)
  private Rm rm;

  @OneToMany(mappedBy="dstbGd", fetch=FetchType.EAGER, orphanRemoval=true)
	private List<DstbGdJsm> dstbGdJsmCollection;

	@OneToMany(mappedBy="dstbGd", fetch=FetchType.EAGER, orphanRemoval=true)
	private List<DstbGdOm> dstbGdOmCollection;

	public DstbGd() {
		this.setPk(new PK());
	}

	public DstbGd(final Byte rm, final Integer codigo) {
	  this.setPk(new PK(rm, codigo));
	}

	@Override
	public int compareTo(DstbGd o) {
	  return this.getPk().compareTo(o.getPk());
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
    DstbGd other = (DstbGd) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.getPk().toString();
  }

	public Integer getAptos() {
    return this.aptos;
  }

  public void setAptos(Integer aptos) {
    this.aptos = aptos;
  }

  public Integer getNec() {
    return this.nec;
  }

  public void setNec(Integer nec) {
    this.nec = nec;
  }

  public Integer getMaj() {
    return this.maj;
  }

  public void setMaj(Integer maj) {
    this.maj = maj;
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Rm getRm() {
    return this.rm;
  }

  public void setRm(Rm rm) {
    this.rm = rm;
  }

  public List<DstbGdJsm> getDstbGdJsmCollection() {
		return this.dstbGdJsmCollection;
	}

	public void setDstbGdJsmCollection(List<DstbGdJsm> dstbGdJsmCollection) {
		this.dstbGdJsmCollection = dstbGdJsmCollection;
	}

	public List<DstbGdOm> getDstbGdOmCollection() {
		return this.dstbGdOmCollection;
	}

	public void setDstbGdOmCollection(List<DstbGdOm> dstbGdOmCollection) {
		this.dstbGdOmCollection = dstbGdOmCollection;
	}

  public void addDstbGdJsm(final DstbGdJsm jsm) throws SermilException {
    if (this.getDstbGdJsmCollection() == null) {
      this.setDstbGdJsmCollection(new ArrayList<DstbGdJsm>(1));
    }
    if (this.getDstbGdJsmCollection().contains(jsm)) {
      throw new SermilException("JSM já está incluída no GD");
    }
    this.getDstbGdJsmCollection().add(jsm);
    if (jsm.getDstbGd() != this) {
      jsm.setDstbGd(this);
    }
  }

  public void addDstbGdOm(final DstbGdOm om) throws SermilException {
    if (this.getDstbGdOmCollection() == null) {
      this.setDstbGdOmCollection(new ArrayList<DstbGdOm>(1));
    }
    if (this.getDstbGdOmCollection().contains(om)) {
      throw new SermilException("Bol Nec da OM já está incluído no GD");
    }
    this.getDstbGdOmCollection().add(om);
    if (om.getDstbGd() != this) {
      om.setDstbGd(this);
    }
  }

  /** Chave primária (PK) de DstbGd.
   * @author Abreu Lopes
   * @since 3.5
   * @version $Id: DstbGd.java 1637 2011-11-25 13:52:11Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<DstbGd.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8595581444880728765L;

    @Column(name="RM_CODIGO")
    private Byte rmCodigo;

    private Integer codigo;

    public PK() {
      super();
    }

    public PK(final Byte rmCodigo, final Integer codigo) {
      super();
      this.setRmCodigo(rmCodigo);
      this.setCodigo(codigo);
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getRmCodigo() == null ? "NULO" : this.getRmCodigo().toString())
                 .append("ª RM - GD Nr ")
                 .append(this.getCodigo() == null ? "NULO" : this.getCodigo())
                 .toString();
    }

    @Override
    public int compareTo(PK o) {
      int status = this.getRmCodigo().compareTo(o.getRmCodigo());
      if (status == 0) {
        status = this.getCodigo().compareTo(o.getCodigo());
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
      if (this.codigo == null) {
        if (other.codigo != null)
          return false;
      } else if (!this.codigo.equals(other.codigo))
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

    public Integer getCodigo() {
      return this.codigo;
    }

    public void setCodigo(Integer codigo) {
      this.codigo = codigo;
    }

  }

}
