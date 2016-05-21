package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Entidade País.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=260)
@NamedQuery(name = "Pais.listar", query = "SELECT p.codigo, p.descricao FROM Pais p ORDER BY p.descricao")
@PrimaryKey(validation = IdValidation.NULL)
public final class Pais implements Comparable<Pais>, Serializable {

  private static final long serialVersionUID = -6188376616817666259L;

  @Id
  private Short codigo;

  private Short ddi;

  private String descricao;

  @Column(name = "NOME_OFICIAL")
  private String nomeOficial;

  private String sigla;

  public Pais() {
    super();
  }

  public Pais(final Short codigo, final String descricao) {
    this.setCodigo(codigo);
    this.setDescricao(descricao);
  }
  
  @Override
  public int compareTo(Pais o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "PAIS" : this.getDescricao())
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
    Pais other = (Pais) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Short getCodigo() {
    return this.codigo;
  }

  public Short getDdi() {
    return this.ddi;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getNomeOficial() {
    return this.nomeOficial;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCodigo(Short codigo) {
    this.codigo = codigo;
  }

  public void setDdi(Short ddi) {
    this.ddi = ddi;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setNomeOficial(String nomeOficial) {
    this.nomeOficial = nomeOficial;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

}
