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

/** Entidade Taxa/Multa.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "TAXA_MULTA")
@NamedQueries({
    @NamedQuery(name = "TaxaMulta.listarArtigo", query = "SELECT DISTINCT t.pk.artigo FROM TaxaMulta t"),
    @NamedQuery(name = "TaxaMulta.listarPorArtigo", query = "SELECT t FROM TaxaMulta t WHERE t.pk.artigo = ?1 ")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class TaxaMulta implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8885102090104378398L;

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

    public TaxaMulta(Short artigo, Short numero) {
        this.setPk(new PK(artigo, numero));
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getPk().toString())
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

    /** Chave primária (PK) de Taxa/Multa.
     * @author Abreu Lopes
     * @since 3.0
     * @version 5.4
     */
    @Embeddable
    public static class PK implements Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -4526365569255936911L;

        private Short artigo;

        private Short numero;

        public PK() {
            super();
        }

        public PK(Short artigo, Short numero) {
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
