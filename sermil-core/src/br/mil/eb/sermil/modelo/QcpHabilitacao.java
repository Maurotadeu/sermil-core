package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** QCP_HABILITACAO.
 * @author Neckel
 * @since 3.4
 * @version $Id: QcpHabilitacao.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "QCP_HABILITACAO")
@NamedQuery(name = "QcpHabilitacao.listar", query = "SELECT a FROM QcpHabilitacao a ORDER BY a.codigo")
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
