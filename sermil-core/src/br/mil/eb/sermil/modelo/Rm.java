package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Região Militar.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Rm.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Rm.listar", query = "SELECT r FROM Rm r")
@PrimaryKey(validation=IdValidation.NULL)
public final class Rm implements Comparable<Rm>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -8885833939779074521L;

  @Id
  private Byte codigo;

  private String descricao;

  private String sigla;

  @ManyToOne
  private Cma cma;

  public Rm() {
    super();
  }

  @Override
  public int compareTo(Rm o) {
    return this.getCodigo().compareTo(o.getCodigo());
  }
  
  @Override
  public String toString() {
    return this.getSigla() == null ? "NULO" : this.getSigla();
  }

  public Cma getCma() {
    return this.cma;
  }

  public Byte getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCma(Cma cma) {
    this.cma = cma;
  }

  public void setCodigo(Byte codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

}
