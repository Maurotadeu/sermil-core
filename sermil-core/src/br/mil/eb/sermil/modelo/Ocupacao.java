package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Ocupação.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Ocupacao.java 2467 2014-06-12 14:17:52Z wlopes $
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Ocupacao.listarPorDescricao", query = "SELECT o FROM Ocupacao o WHERE o.descricao LIKE CONCAT(?1,'%') ORDER BY o.descricao"),
  @NamedQuery(name = "Ocupacao.listarPorOrdem", query = "SELECT o FROM Ocupacao o ORDER BY o.descricao")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Ocupacao implements Comparable<Ocupacao>, Serializable {

  private static final long serialVersionUID = 8782812248669638313L;

  @Id
  private String codigo;

  private String descricao;

  private String eb;

  public Ocupacao() {
  }

  public Ocupacao(String codigo) {
    this.codigo = codigo;
  }

  @Override
  public int compareTo(Ocupacao o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
    Ocupacao other = (Ocupacao) obj;
    if (this.codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!this.codigo.equals(other.codigo))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
      .append(" - ")
      .append(this.getDescricao() == null ? "DESCRICAO" : this.getDescricao())
      .toString();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getEb() {
    return this.eb;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setEb(String eb) {
    this.eb = eb;
  }

}
