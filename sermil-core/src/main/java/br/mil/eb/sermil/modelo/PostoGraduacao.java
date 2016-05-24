package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Posto ou Graduação.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "POSTO_GRADUACAO")
@NamedQuery(name = "PostoGraduacao.listar", query = "SELECT p FROM PostoGraduacao p ORDER BY p.codigo")
public final class PostoGraduacao implements Comparable<PostoGraduacao>, Serializable {

  private static final long serialVersionUID = 2632965290986788963L;

  @Id
  private String codigo;

  private String descricao;

  private String sigla;

  public PostoGraduacao() {
    super();
  }

  public PostoGraduacao(String codigo) {
    super();
    this.setCodigo(codigo);
  }

  @Override
  public int compareTo(PostoGraduacao o) {
    return this.getCodigo().compareTo(o.getCodigo());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "COD" : this.getCodigo())
    .append(" - ")
    .append(this.getDescricao() == null ? "P/G" : this.getDescricao())
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
    PostoGraduacao other = (PostoGraduacao) obj;
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

  public String getDescricao() {
    return this.descricao;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCodigo(String codigo) {
    this.codigo = (codigo == null || codigo.trim().isEmpty() ? null : codigo.trim());
  }

  public void setDescricao(String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setSigla(String sigla) {
    this.sigla = (sigla == null || sigla.trim().isEmpty() ? null : sigla.trim().toUpperCase());
  }

}
