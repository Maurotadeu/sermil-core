package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** Habilitação.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Habilitacao.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Habilitacao.listar", query = "SELECT h FROM Habilitacao h ORDER BY h.descricao")
public final class Habilitacao implements Comparable<Habilitacao>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -6018755254258650309L;

  @Id
	private String codigo;

	private String descricao;

	private String sigla;

	@Column(name="CODIGO_ANTIGO")
	private String codigoAntigo;

	@Column(name="NC_COD_NAT_CURSO")
	private Byte ncCodNatCurso;

	@Column(name="TC_COD_TIPO_CURSO")
	private Byte tcCodTipoCurso;

	@Column(name="UNIVERSO_CURSO")
	private Byte universoCurso;

	public Habilitacao() {
		super();
	}

	@Override
	public int compareTo(Habilitacao o) {
	  return this.getDescricao().compareTo(o.getDescricao());
	}
  
  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "COD" : this.getCodigo())
               .append(" - ")
               .append(this.getDescricao() == null ? "DESC" : this.getDescricao())
               .toString();
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

	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCodigoAntigo() {
		return this.codigoAntigo;
	}

	public void setCodigoAntigo(String codigoAntigo) {
		this.codigoAntigo = codigoAntigo;
	}
	
  public Byte getNcCodNatCurso() {
    return this.ncCodNatCurso;
  }

  public void setNcCodNatCurso(Byte ncCodNatCurso) {
    this.ncCodNatCurso = ncCodNatCurso;
  }

  public Byte getTcCodTipoCurso() {
    return this.tcCodTipoCurso;
  }

  public void setTcCodTipoCurso(Byte tcCodTipoCurso) {
    this.tcCodTipoCurso = tcCodTipoCurso;
  }

  public Byte getUniversoCurso() {
    return this.universoCurso;
  }

  public void setUniversoCurso(Byte universoCurso) {
    this.universoCurso = universoCurso;
  }
  
}
