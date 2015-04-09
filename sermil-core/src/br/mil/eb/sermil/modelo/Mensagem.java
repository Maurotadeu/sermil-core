package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** Mensagem de crítica.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Mensagem.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Mensagem.listar", query = "SELECT m FROM Mensagem m")
public final class Mensagem implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -6311317713566416152L;

  @Id
  private Short codigo;

  private String descricao;

  private String tipo;

  public Mensagem() {
    super();
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

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "NULO" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "NULO" : this.getDescricao())
      .toString();
  }

}
