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

/** Movimentação de OM.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "CID_MOVIMENTACAO")
public final class CidMovimentacao implements Comparable<CidMovimentacao>, Serializable {

  private static final long serialVersionUID = 5125451402997497288L;

  @EmbeddedId
  private CidMovimentacao.PK pk;

  @Column(name="BI_ABI_NR")
  private String biAbiNr;

  @Temporal(TemporalType.DATE)
  @Column(name = "DESLIGAMENTO_DATA")
  private Date desligamentoData;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;
  
  @ManyToOne
  @JoinColumn(name = "OM_CODIGO", updatable = false, nullable = false)
  private Om om;

  public CidMovimentacao() {
    this.setPk(new CidMovimentacao.PK());
  }

  public CidMovimentacao(final Long ra, final Date data) throws SermilException {
    this.setPk(new CidMovimentacao.PK(ra, data));
  }

  @Override
  public int compareTo(CidMovimentacao o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getOm() == null ? "OM" : this.getOm().toString())
    .append(" - ")
    .append(this.getPk().getApresentacaoData() == null ? "DATA MOVIMENTACAO" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getApresentacaoData()))
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
    CidMovimentacao other = (CidMovimentacao) obj;
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
  
  public String getBiAbiNr() {
		return this.biAbiNr;
	}

  public Date getDesligamentoData() {
    return this.desligamentoData;
  }

  public Om getOm() {
    return om;
  }

  public CidMovimentacao.PK getPk() {
    return this.pk;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidMovimentacaoCollection().contains(this)) {
      cid.getCidMovimentacaoCollection().add(this);
    }
  }
  
	public void setBiAbiNr(String biAbiNr) {
		this.biAbiNr = biAbiNr;
	}

  public void setDesligamentoData(Date desligamentoData) {
    this.desligamentoData = desligamentoData;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public void setPk(CidMovimentacao.PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de CidMovimentacao.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<CidMovimentacao.PK>, Serializable {

    private static final long serialVersionUID = 3329767713560202117L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "APRESENTACAO_DATA")
    private Date apresentacaoData;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Date apresentacaoData) throws SermilException {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setApresentacaoData(apresentacaoData);
    }

    @Override
    public int compareTo(CidMovimentacao.PK e) {
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
      result = prime * result + ((this.apresentacaoData == null) ? 0 : this.apresentacaoData.hashCode());
      result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
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

    public void setApresentacaoData(Date data) throws SermilException {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new SermilException("Data maior que a data atual.");
      } else {
        cal.set(1900, 0, 1); // 01-01-1900
        if (cal.getTime().after(data)) {
          throw new SermilException("Data menor que 01/01/1900.");
        }
      }
      this.apresentacaoData = data;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

  }

}
