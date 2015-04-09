package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Comando Militar de Área.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Cma.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@PrimaryKey(validation=IdValidation.NULL)
public final class Cma implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -6654214842787703523L;

  @Id
  private Integer codigo;

  private Byte cta;

  private String descricao;

  private String sigla;

  public Cma() {
    super();
  }

  @Override
  public String toString() {
    return this.getSigla() == null ? "NULO" : this.getSigla();
  }

  public Integer getCodigo() {
    return this.codigo;
  }

  public Byte getCta() {
    return this.cta;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public void setCta(Byte cta) {
    this.cta = cta;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

}
