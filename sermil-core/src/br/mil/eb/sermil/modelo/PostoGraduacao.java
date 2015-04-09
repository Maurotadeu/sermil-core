package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Posto ou Graduação.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: PostoGraduacao.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "POSTO_GRADUACAO")
@NamedQuery(name = "PostoGraduacao.listar", query = "SELECT p FROM PostoGraduacao p ORDER BY p.codigo")
public final class PostoGraduacao implements Comparable<PostoGraduacao>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -7903007662610302473L;

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
