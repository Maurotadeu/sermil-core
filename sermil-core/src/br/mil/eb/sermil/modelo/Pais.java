package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** País.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Pais.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Pais.listar", query = "SELECT p FROM Pais p ORDER BY p.descricao")
public final class Pais implements Comparable<Pais>, Serializable {

  /** serialVersionUID.*/
  private static final long serialVersionUID = 3623544886077852655L;

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
    return new StringBuilder(this.getCodigo() == null ? "NULO" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getDescricao() == null ? "NULO" : this.getDescricao())
      .toString();
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
