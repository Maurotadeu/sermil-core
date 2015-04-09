package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** Perfil de acesso.
 * @author Abreu Lopes
 * @since 3.5
 * @version $Id: Perfil.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Perfil.listar", query = "SELECT p FROM Perfil p")
public final class Perfil implements Comparable<Perfil>, Serializable {

  private static final long serialVersionUID = -2139527113542785550L;

  @Id
  private String codigo;

  private String descricao;

  public Perfil() {
    super();
  }

  @Override
  public int compareTo(Perfil o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }

  @Override
  public String toString() {
    return this.getDescricao() == null ? "PERFIL" : this.getDescricao();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

}
