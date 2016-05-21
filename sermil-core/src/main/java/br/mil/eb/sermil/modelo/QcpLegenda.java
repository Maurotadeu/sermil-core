package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** QCP_LEGENDA.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name="QCP_LEGENDA")
@NamedQuery(name = "QcpLegenda.listar", query = "SELECT l FROM QcpLegenda l ORDER BY l.codigo")
@PrimaryKey(validation=IdValidation.NULL)
public final class QcpLegenda implements Serializable {

  private static final long serialVersionUID = -8980635206735841396L;

  @Id
  private String codigo;

  private String descricao;

  public QcpLegenda() {
    super();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @Override
  public String toString() {
    return this.getDescricao() == null ? "NULO" : this.getDescricao();
  }

}
