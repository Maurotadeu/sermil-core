package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Escolaridade.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Escolaridade.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Escolaridade.listar", query = "SELECT e FROM Escolaridade e")
@PrimaryKey(validation=IdValidation.NULL)
public final class Escolaridade implements Comparable<Escolaridade>, Serializable {

  private static final long serialVersionUID = 3649134127198014570L;

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
    return new StringBuilder(this.getReferencia() == null ? "NULO" : this.getReferencia().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "NULO" : this.getDescricao())
      .toString();
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
