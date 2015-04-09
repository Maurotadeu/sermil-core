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

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Taxas e Multas.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: TaxaMulta.java 1934 2012-05-25 13:50:40Z gardino $
 */
@Entity
@Table(name = "TAXA_MULTA")
@NamedQueries({
@NamedQuery(name = "TaxaMulta.listarArtigo", query = "SELECT DISTINCT t.pk.artigo FROM TaxaMulta t"),
@NamedQuery(name = "TaxaMulta.listarPorArtigo", query = "SELECT t FROM TaxaMulta t WHERE t.pk.artigo = ?1 ")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class TaxaMulta implements Serializable {

  /** serialVersionUID.*/
  private static final long serialVersionUID = 3197230729467298267L;

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

  @Override
  public String toString() {
    return new StringBuilder()
      .append(this.getPk().getArtigo() == null ? "TAXA/MULTA" : "Art " + this.getPk().getArtigo().toString())
      .append(this.getPk().getNumero() == null ? "" : " Nr" + this.getPk().getNumero())
      .append(this.getDescricao() == null ? "" : " - " + this.getDescricao())
      .toString();
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

  /** Chave primária (PK) de TaxaMulta.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: TaxaMulta.java 1934 2012-05-25 13:50:40Z gardino $
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = -426702621081479446L;

    private Short artigo;

    private Short numero;

    public PK() {
      super();
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof PK)) {
        return false;
      }
      PK other = (PK) o;
      return this.numero.equals(other.numero)
          && this.artigo.equals(other.artigo);
    }

    @Override
    public int hashCode() {
      return this.numero.hashCode() ^ this.artigo.hashCode();
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
