package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Grupo de Distribuição de OM (DSTB_GD_OM).
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: DstbGdOm.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name="DSTB_GD_OM")
public final class DstbGdOm implements Comparable<DstbGdOm>, Serializable {
  
  private static final long serialVersionUID = -2648942401258869569L;

  @EmbeddedId
	private PK pk;

	private Integer prioridade;
	
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name = "OM_CODIGO", referencedColumnName = "CODIGO", insertable = false, updatable = false, nullable = false)
  private Om om;
	
	@ManyToOne
	@JoinColumns({
	  @JoinColumn(name="RM_CODIGO", referencedColumnName="RM_CODIGO",  insertable=false, updatable=false),
	  @JoinColumn(name="DSTB_GD_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false)
  })
	private DstbGd dstbGd;

	public DstbGdOm() {
		this.setPk(new PK());
	}

  public DstbGdOm(final Byte rm , final Integer gd, final Integer codom, final String gpt, final Short nr) {
    this.setPk(new PK(rm, gd, codom, gpt, nr));
  }
	
	@Override
	public int compareTo(DstbGdOm o) {
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
    DstbGdOm other = (DstbGdOm) obj;
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
  
	public PK getPk() {
		return this.pk;
	}

	public Om getOm() {
    return om;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public void setPk(PK pk) {
		this.pk = pk;
	}

	public Integer getPrioridade() {
		return this.prioridade;
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
	
  /** Chave primária (PK) de DstbGdOm.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: DstbGdOm.java 1637 2011-11-25 13:52:11Z wlopes $
   */
	@Embeddable
	public static class PK implements Comparable<DstbGdOm.PK>, Serializable {
	  
    /** serialVersionUID. */
    private static final long serialVersionUID = 6264787477576617016L;

    @Column(name="RM_CODIGO")
    private Byte rmCodigo;

    @Column(name="DSTB_GD_CODIGO")
	  private Integer dstbGdCodigo;

	  @Column(name="OM_CODIGO")
	  private Integer omCodigo;

	  @Column(name="GPT_INCORP")
	  private String gptIncorp;

	  @Column(name="BOLNEC_NR")
	  private Short bolnecNr;

	  public PK() {
	    super();
	  }
	  
    public PK(final Byte rmCodigo, final Integer dstbGdCodigo, final Integer omCodigo, final String gptIncorp, final Short bolnecNr) {
      super();
      this.setRmCodigo(rmCodigo);
      this.setDstbGdCodigo(dstbGdCodigo);
      this.setOmCodigo(omCodigo);
      this.setGptIncorp(gptIncorp);
      this.setBolnecNr(bolnecNr);
    }

    @Override
    public int compareTo(PK o) {
      int status = this.getRmCodigo().compareTo(o.getRmCodigo());
      if (status == 0) {
        status = this.getDstbGdCodigo().compareTo(o.getDstbGdCodigo());
        if (status == 0) {
          status = this.getOmCodigo().compareTo(o.getOmCodigo());
          if (status == 0) {
            status = this.getGptIncorp().compareTo(o.getGptIncorp());
            if (status == 0) {
              status = this.getBolnecNr().compareTo(o.getBolnecNr());
            }
          }
        }
      }
      return status;
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getRmCodigo() == null ? "RM" : this.getRmCodigo().toString())
        .append(" RM - GD ")
        .append(this.getDstbGdCodigo() == null ? "GD" : this.getDstbGdCodigo())
        .append(" - OM ")
        .append(this.getOmCodigo() == null ? "OM" : this.getOmCodigo())
        .append(" - GPT ")
        .append(this.getGptIncorp() == null ? "GPT" : this.getGptIncorp())
        .append(" - BOL NR ")
        .append(this.getBolnecNr() == null ? "  BOL NR" : this.getBolnecNr())
        .toString();
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.bolnecNr == null) ? 0 : this.bolnecNr.hashCode());
      result = prime * result
          + ((this.dstbGdCodigo == null) ? 0 : this.dstbGdCodigo.hashCode());
      result = prime * result
          + ((this.gptIncorp == null) ? 0 : this.gptIncorp.hashCode());
      result = prime * result
          + ((this.omCodigo == null) ? 0 : this.omCodigo.hashCode());
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
      if (this.bolnecNr == null) {
        if (other.bolnecNr != null)
          return false;
      } else if (!this.bolnecNr.equals(other.bolnecNr))
        return false;
      if (this.dstbGdCodigo == null) {
        if (other.dstbGdCodigo != null)
          return false;
      } else if (!this.dstbGdCodigo.equals(other.dstbGdCodigo))
        return false;
      if (this.gptIncorp == null) {
        if (other.gptIncorp != null)
          return false;
      } else if (!this.gptIncorp.equals(other.gptIncorp))
        return false;
      if (this.omCodigo == null) {
        if (other.omCodigo != null)
          return false;
      } else if (!this.omCodigo.equals(other.omCodigo))
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
      return this.dstbGdCodigo;
    }

    public void setDstbGdCodigo(Integer dstbGdCodigo) {
      this.dstbGdCodigo = dstbGdCodigo;
    }

    public Integer getOmCodigo() {
      return this.omCodigo;
    }

    public void setOmCodigo(Integer omCodigo) {
      this.omCodigo = omCodigo;
    }

    public String getGptIncorp() {
      return this.gptIncorp;
    }

    public void setGptIncorp(String gptIncorp) {
      this.gptIncorp = gptIncorp;
    }

    public Short getBolnecNr() {
      return this.bolnecNr;
    }

    public void setBolnecNr(Short bolnecNr) {
      this.bolnecNr = bolnecNr;
    }

	}

}
