package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/** Domínios do sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Dominios.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Dominios.listar", query = "SELECT d FROM Dominios d ORDER BY d.pk.id, d.pk.valor"),
  @NamedQuery(name = "Dominios.listarPorId", query = "SELECT d FROM Dominios d WHERE d.pk.id = ?1 ORDER BY d.pk.valor")
})
public final class Dominios implements Comparable<Dominios>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -5090737257099795858L;

  @EmbeddedId
  private PK pk;

  private String descricao;

  private String dominio;

  public Dominios() {
    this.setPk(new Dominios.PK());
  }

  public Dominios(final Integer id, final Short valor) {
    this.setPk(new Dominios.PK(id, valor));
  }

  @Override
  public int compareTo(Dominios o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getId().toString())
           .append(" - ")
           .append(this.getDominio())
           .append(" (")
           .append(this.getPk().getValor())
           .append(" = ")
           .append(this.getDescricao())
           .append(")")
           .toString();
  }
  
  public String getDescricao() {
    return this.descricao;
  }

  public String getDominio() {
    return this.dominio;
  }

  public Integer getId() {
    return this.getPk().getId();
  }

  public Short getValor() {
    return this.getPk().getValor();
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setDominio(String dominio) {
    this.dominio = dominio;
  }

  public void setId(Integer id) {
    this.pk.id = id;
  }

  public void setValor(Short valor) {
    this.pk.valor = valor;
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de Dominios.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: Dominios.java 1637 2011-11-25 13:52:11Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<Dominios.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8881875591982293061L;

    private Integer id;

    private Short valor;

    public PK() {
      super();
    }

    public PK(final Integer id, final Short valor) {
      super();
      this.setId(id);
      this.setValor(valor);
    }

    @Override
    public int compareTo(Dominios.PK o) {
      return this.getId().compareTo(o.getId()) == 0 ? this.getValor().compareTo(o.getValor()): this.getId().compareTo(o.getId());
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
      return this.id.equals(other.id) && this.valor.equals(other.valor);
    }

    @Override
    public int hashCode() {
      return this.id.hashCode() ^ this.valor.hashCode();
    }

    public Integer getId() {
      return this.id;
    }

    public Short getValor() {
      return this.valor;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public void setValor(Short valor) {
      this.valor = valor;
    }

  }

}
