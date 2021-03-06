package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Empresa diretamente relacionada a seguranša nacional (EDRSN).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Empresa.listar", query = "SELECT DISTINCT e.codigo, e.descricao FROM Empresa e ORDER BY e.descricao")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Empresa implements Comparable<Empresa>, Serializable {

  private static final long serialVersionUID = -2441628302226227645L;

  @Id
  private String codigo;

  private String descricao;

  private String endereco;

  private String bairro;

  private String cep;

  private String fax;

  private String telefone;

  private Byte atividade;
  
  @ManyToOne
  private Municipio municipio;
  
  public Empresa() {
    super();
  }

  @Override
  public int compareTo(Empresa o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
      .append(" - ")
      .append(this.getDescricao() == null ? "EMPRESA" : this.getDescricao())
      .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
    Empresa other = (Empresa) obj;
    if (this.codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!this.codigo.equals(other.codigo))
      return false;
    return true;
  }
  
  public Byte getAtividade() {
    return this.atividade;
  }

  public String getBairro() {
    return this.bairro;
  }

  public String getCep() {
    return this.cep;
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public String getFax() {
    return this.fax;
  }

  public Municipio getMunicipio() {
    return this.municipio;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setAtividade(Byte atividade) {
    this.atividade = atividade;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

}
