package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** Mensagem de crítica.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@NamedQuery(name = "Mensagem.listar", query = "SELECT m FROM Mensagem m")
public final class Mensagem implements Serializable {

  private static final long serialVersionUID = 9186323130785851795L;

  @Id
  private Short codigo;

  private String descricao;

  private String tipo;

  public Mensagem() {
    super();
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "MENSAGEM" : this.getDescricao())
      .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
    Mensagem other = (Mensagem) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Short getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getTipo() {
    return this.tipo;
  }

  public void setCodigo(Short codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}
