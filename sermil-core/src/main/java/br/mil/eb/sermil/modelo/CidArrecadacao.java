package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Arrecadação de Taxas e Multas.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: 5.4
 */
@Entity
@Table(name = "CID_ARRECADACAO")
public final class CidArrecadacao implements Comparable<CidArrecadacao>, Serializable {

  private static final long serialVersionUID = 7755543613454347847L;

  @EmbeddedId
  private CidArrecadacao.PK pk;

  @Column(name = "VALOR_TOTAL")
  private BigDecimal valorTotal;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumns({
    @JoinColumn(name = "TAXA_MULTA_ARTIGO", referencedColumnName = "ARTIGO", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "TAXA_MULTA_NUMERO", referencedColumnName = "NUMERO", insertable = false, updatable = false, nullable = false)
  })
  private TaxaMulta taxaMulta;

  public CidArrecadacao() {
    this.setPk(new CidArrecadacao.PK());
  }

  public CidArrecadacao(final Long ra, final Date data, final Short artigo, final Short numero) {
    this.setPk(new CidArrecadacao.PK(ra, data, artigo, numero));
  }

  @Override
  public int compareTo(CidArrecadacao o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getTaxaMulta() == null ? "TAXA" : this.getTaxaMulta().toString())
      .append(" - ")
      .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
      .append(" - ")
      .append(this.getValorTotal() == null ? "VALOR" : DecimalFormat.getCurrencyInstance().format(this.getValorTotal()))
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
    CidArrecadacao other = (CidArrecadacao) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public void decode(String linha) throws Exception {
    this.getPk().setCidadaoRa(Long.valueOf(linha.substring(2, 14)));
    this.getPk().setTaxaMultaArtigo(Short.valueOf(linha.substring(14, 17)));
    this.getPk().setTaxaMultaNumero(Short.valueOf(linha.substring(17, 20)));
    final Calendar data = Calendar.getInstance();
    data.set(Integer.parseInt(linha.substring(24, 28)), Integer.parseInt(linha.substring(22, 24))-1, Integer.parseInt(linha.substring(20, 22)));
    this.setValorTotal(new BigDecimal(linha.substring(28, 32).replaceAll(",", ".")));
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public CidArrecadacao.PK getPk() {
    return this.pk;
  }

  public TaxaMulta getTaxaMulta() {
    return this.taxaMulta;
  }

  public BigDecimal getValorTotal() {
    return this.valorTotal;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidArrecadacaoCollection().contains(this)) {
      cid.getCidArrecadacaoCollection().add(this);
    }
  }

  public void setPk(CidArrecadacao.PK pk) {
    this.pk = pk;
  }

  public void setTaxaMulta(TaxaMulta taxaMulta) {
    this.taxaMulta = taxaMulta;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  /** Chave primária (PK) de CidArrecadacao.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidArrecadacao.java 2426 2014-05-14 15:01:41Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidArrecadacao.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 7939954661542924024L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA")
    private Date data;

    @Column(name = "TAXA_MULTA_ARTIGO")
    private Short taxaMultaArtigo;

    @Column(name = "TAXA_MULTA_NUMERO")
    private Short taxaMultaNumero;

    public PK() {
      super();
    }

    public PK(final Long cidadaoRa, final Date data, final Short taxaMultaArtigo, final Short taxaMultaNumero) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setData(data);
      this.setTaxaMultaArtigo(taxaMultaArtigo);
      this.setTaxaMultaNumero(taxaMultaNumero);
    }

    @Override
    public int compareTo(CidArrecadacao.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getData().compareTo(o.getData());
        if (status == 0) {
          status = this.getTaxaMultaArtigo().compareTo(o.getTaxaMultaArtigo());
          if (status == 0 ) {
            status = this.getTaxaMultaNumero().compareTo(o.getTaxaMultaNumero());
          }
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
      result = prime * result + ((this.taxaMultaArtigo == null) ? 0 : this.taxaMultaArtigo.hashCode());
      result = prime * result + ((this.taxaMultaNumero == null) ? 0 : this.taxaMultaNumero.hashCode());
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
      if (this.taxaMultaArtigo == null) {
        if (other.taxaMultaArtigo != null)
          return false;
      } else if (!this.taxaMultaArtigo.equals(other.taxaMultaArtigo))
        return false;
      if (this.taxaMultaNumero == null) {
        if (other.taxaMultaNumero != null)
          return false;
      } else if (!this.taxaMultaNumero.equals(other.taxaMultaNumero))
        return false;
      return true;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public Short getTaxaMultaArtigo() {
      return this.taxaMultaArtigo;
    }

    public Short getTaxaMultaNumero() {
      return this.taxaMultaNumero;
    }

    public void setData(Date data) {
      Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.data = data;
    }

    public void setTaxaMultaArtigo(Short taxaMultaArtigo) {
      this.taxaMultaArtigo = taxaMultaArtigo;
    }

    public void setTaxaMultaNumero(Short taxaMultaNumero) {
      this.taxaMultaNumero = taxaMultaNumero;
    }

  }

}
