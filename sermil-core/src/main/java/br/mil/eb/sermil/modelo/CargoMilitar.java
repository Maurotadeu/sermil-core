package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Cargo Militar.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.2
 */
@Entity
@Table(name = "CARGO_MILITAR")
public final class CargoMilitar implements Comparable<CargoMilitar>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 7483249408623757963L;

  @Id
  private String codigo;

  private String descricao;

  @ManyToOne
  private Padrao padrao;

  public CargoMilitar() {
    super();
  }

  @Override
  public int compareTo(CargoMilitar o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
      .append(" - ")
      .append(this.getDescricao() == null ? "CARGO MILITAR" : this.getDescricao())
      .toString();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public Padrao getPadrao() {
    return this.padrao;
  }

  public void setCodigo(String codigo) {
    this.codigo = (codigo == null || codigo.trim().isEmpty() ? null : codigo.trim());
  }

  public void setDescricao(String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setPadrao(Padrao padrao) {
    this.padrao = padrao;
  }

}
