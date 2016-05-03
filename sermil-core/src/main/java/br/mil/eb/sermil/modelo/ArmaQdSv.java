package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Arma, Quadro e Serviço.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.2
 */
@Entity
@Table(name = "ARMA_QD_SV")
@NamedQuery(name = "ArmaQdSv.listar", query = "SELECT a FROM ArmaQdSv a ORDER BY a.sigla")
public final class ArmaQdSv implements Comparable<ArmaQdSv>, Serializable {

  private static final long serialVersionUID = -3448978886018262145L;

  @Id
  private String codigo;

  private String descricao;

  @ManyToOne
  @JoinColumn(name = "PG_FIM_CODIGO")
  private PostoGraduacao pgFim;

  @ManyToOne
  @JoinColumn(name = "PG_INI_CODIGO")
  private PostoGraduacao pgIni;

  private String sigla;

  public ArmaQdSv() {
    super();
  }

  @Override
  public int compareTo(ArmaQdSv o) {
    return this.getSigla().compareTo(o.getSigla());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
      .append(" - ")
      .append(this.getDescricao() == null ? "AQS DESCRICAO" : this.getDescricao())
      .toString();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public PostoGraduacao getPgFim() {
    return this.pgFim;
  }

  public PostoGraduacao getPgIni() {
    return this.pgIni;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCodigo(final String codigo) {
    this.codigo = (codigo == null || codigo.trim().isEmpty() ? null : codigo.trim().toUpperCase());
  }

  public void setDescricao(final String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setPgFim(final PostoGraduacao pgFim) {
    this.pgFim = pgFim;
  }

  public void setPgIni(final PostoGraduacao pgIni) {
    this.pgIni = pgIni;
  }

  public void setSigla(final String sigla) {
    this.sigla = (sigla == null || sigla.trim().isEmpty() ? null : sigla.trim().toUpperCase());
  }

}
