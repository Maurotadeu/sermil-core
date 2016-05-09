package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** QCP_OBSERVACAO.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name="QCP_OBSERVACAO")
@NamedQuery(name = "QcpObservacao.listar", query = "SELECT o FROM QcpObservacao o ORDER BY o.codigo")
@PrimaryKey(validation=IdValidation.NULL)
public final class QcpObservacao implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 555971040195386288L;

  @Id
	private String codigo;

	private String descricao;

	public QcpObservacao() {
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
