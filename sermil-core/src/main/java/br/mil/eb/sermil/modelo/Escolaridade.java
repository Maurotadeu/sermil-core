package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Escolaridade.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=35)
@NamedQuery(name = "Escolaridade.listar", query = "SELECT e FROM Escolaridade e")
@PrimaryKey(validation=IdValidation.NULL)
public final class Escolaridade implements Comparable<Escolaridade>, Serializable {

  private static final long serialVersionUID = 9074538464873438270L;

  @Id
  private Byte codigo;

  private String descricao;

  private Byte referencia;

  public Escolaridade() {
    super();
  }

  @Override
  public int compareTo(Escolaridade o) {
    return this.getCodigo().compareTo(o.getCodigo());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getReferencia() == null ? "REF" : this.getReferencia().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "ESCOLARIDADE" : this.getDescricao())
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
    Escolaridade other = (Escolaridade) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Byte getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public Byte getReferencia() {
    return this.referencia;
  }

  public void setCodigo(Byte codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setReferencia(Byte referencia) {
    this.referencia = referencia;
  }

}
