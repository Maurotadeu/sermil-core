package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Campo.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Campo.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@PrimaryKey(validation=IdValidation.NULL)
public final class Campo implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 8681885885635100892L;

  @Id
  private Integer codigo;

  private String coluna;

  private String descricao;

  public Campo() {
    super();
  }

  public Integer getCodigo() {
    return this.codigo;
  }

  public String getColuna() {
    return this.coluna;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public void setColuna(String coluna) {
    this.coluna = (coluna == null || coluna.trim().isEmpty() ? null : coluna.trim().toUpperCase());
  }

  public void setDescricao(String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "COD" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "DESC" : this.getDescricao())
      .toString();
  }

}
