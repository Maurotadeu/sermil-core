package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** CEP.
 * @author Abreu Lopes
 * @since 5.1
 * @version 5.1.15
 */
@Entity
@PrimaryKey(validation=IdValidation.NULL)
public final class Cep implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3390686171102289283L;

  @Id
  private String codigo;

  private String logradouro;

  private String bairro;
  
  private String municipio;
  
  private String uf;

  public Cep() {
    super();
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CEP" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getLogradouro() == null ? "Log" : this.getLogradouro())
      .toString();
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }
  
}
