package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;

/** Entidade Habilitacaoo.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=3070)
@NamedQuery(name = "Habilitacao.listar", query = "SELECT h.codigo, h.descricao FROM Habilitacao h ORDER BY h.descricao")
public final class Habilitacao implements Comparable<Habilitacao>, Serializable {

  private static final long serialVersionUID = 1692506743473001003L;

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
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
               .append(" - ")
               .append(this.getDescricao() == null ? "HABILITACAO" : this.getDescricao())
               .toString();
  }
  
	@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Habilitacao other = (Habilitacao) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
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
