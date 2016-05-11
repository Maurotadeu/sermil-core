package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** QCP_HABILITACAO.
 * @author Neckel
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name = "QCP_HABILITACAO")
@NamedQuery(name = "QcpHabilitacao.listar", query = "SELECT a FROM QcpHabilitacao a ORDER BY a.codigo")
@PrimaryKey(validation=IdValidation.NULL)
public final class QcpHabilitacao implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 8299663748487294314L;

  @Id
	private String codigo;

	private String descricao;

	public QcpHabilitacao() {
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
