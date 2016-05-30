package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entidade Taxa/Multa.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=15)
@Table(name = "TAXA_MULTA")
@NamedQueries({
  @NamedQuery(name = "TaxaMulta.listarArtigos", query = "SELECT DISTINCT t.pk.artigo, 1 FROM TaxaMulta t ORDER BY t.pk.artigo"),
  @NamedQuery(name = "TaxaMulta.listarPorArtigo", query = "SELECT DISTINCT t.pk.numero, t.descricao FROM TaxaMulta t WHERE t.pk.artigo = ?1 ORDER BY t.pk.numero")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class TaxaMulta implements Serializable {

  private static final long serialVersionUID = -4036114692896365051L;

  @EmbeddedId
  private PK pk;

  private String descricao;

  @Column(name = "MINIMO_QTD")
  private BigDecimal minimoQtd;

  private String tipo;

  private BigDecimal valor;

  public TaxaMulta() {
    this.setPk(new PK());
  }

  public TaxaMulta(final Short artigo, final Short numero) {
    this.setPk(new PK(artigo, numero));
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString())
        .append(this.getDescricao() == null ? "" : " - " + this.getDescricao())
        .toString();
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
    TaxaMulta other = (TaxaMulta) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Short getArtigo() {
    return this.pk.artigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public BigDecimal getMinimoQtd() {
    return this.minimoQtd;
  }

  public Short getNumero() {
    return this.pk.numero;
  }

  public String getTipo() {
    return this.tipo;
  }

  public BigDecimal getValor() {
    return this.valor;
  }

  public void setArtigo(Short artigo) {
    this.pk.artigo = artigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setMinimoQtd(BigDecimal minimoQtd) {
    this.minimoQtd = minimoQtd;
  }

  public void setNumero(Short numero) {
    this.pk.numero = numero;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  /** Chave primária (PK) de Taxa/Multa.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = -4526365569255936911L;

    private Short artigo;

    private Short numero;

    public PK() {
      super();
    }

    public PK(final Short artigo, final Short numero) {
      this.artigo = artigo;
      this.numero = numero;
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append(this.getArtigo() == null ? "Taxa/Multa" : "Art " + this.getArtigo())
          .append(this.getNumero() == null || this.getNumero() == 0 ? "" : " Nr " + this.getNumero())
          .toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((artigo == null) ? 0 : artigo.hashCode());
      result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
      if (artigo == null) {
        if (other.artigo != null)
          return false;
      } else if (!artigo.equals(other.artigo))
        return false;
      if (numero == null) {
        if (other.numero != null)
          return false;
      } else if (!numero.equals(other.numero))
        return false;
      return true;
    }

    public Short getArtigo() {
      return this.artigo;
    }

    public Short getNumero() {
      return this.numero;
    }

    public void setArtigo(Short artigo) {
      this.artigo = artigo;
    }

    public void setNumero(Short numero) {
      this.numero = numero;
    }

  }

}
