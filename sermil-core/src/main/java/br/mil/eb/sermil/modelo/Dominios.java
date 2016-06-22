package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;

/** Domínios (ID/VALOR).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4.2
 */
@Entity
@Cache(type=CacheType.FULL, size=450)
@NamedQueries({
  @NamedQuery(name = "Dominios.listar", query = "SELECT DISTINCT d.pk.id, d.dominio FROM Dominios d ORDER BY d.dominio"),
  @NamedQuery(name = "Dominios.listarPorId", query = "SELECT d FROM Dominios d WHERE d.pk.id = ?1 ORDER BY d.pk.valor")
})
public final class Dominios implements Comparable<Dominios>, Serializable {

  private static final long serialVersionUID = -4309176920407314617L;

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
        .append(" - ").append(this.getDominio())
        .append(" (").append(this.getPk().getValor())
        .append(" = ").append(this.getDescricao())
        .append(")").toString();
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
    Dominios other = (Dominios) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
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

  /**Chave primária (PK) de Dominios.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<Dominios.PK>, Serializable {

    private static final long serialVersionUID = 6607445017568521403L;

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
      return this.getId().compareTo(o.getId()) == 0 ? this.getValor().compareTo(o.getValor()) : this.getId().compareTo(o.getId());
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      if (valor == null) {
        if (other.valor != null)
          return false;
      } else if (!valor.equals(other.valor))
        return false;
      return true;
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
