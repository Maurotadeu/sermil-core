package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Perfil de acesso.
 * @author Abreu Lopes
 * @since 3.5
 * @version 5.4
 */
@Entity
@NamedQuery(name = "Perfil.listar", query = "SELECT p FROM Perfil p")
@PrimaryKey(validation=IdValidation.NULL)
public final class Perfil implements Comparable<Perfil>, Serializable {

  private static final long serialVersionUID = -7636313940403669612L;

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
    Perfil other = (Perfil) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
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
