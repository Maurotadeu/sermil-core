package br.mil.eb.sermil.tipos;

import java.io.Serializable;

/** Tipo destinado a carregar informação para apresentar nas listas HTML (id = value).
 * @author Abreu Lopes
 * @since 5.3.2
 * @version 5.3.2
 */
public class Lista implements Serializable {

  private static final long serialVersionUID = 2978454460515787055L;

  private String id;
  
  private String descricao;
  
  public Lista() {
  }

  public Lista(String id, String descricao) {
    this.id = id;
    this.descricao = descricao;
  }
  
  @Override
  public String toString() {
    return "Lista [id=" + id + ", descricao=" + descricao + "]";
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    Lista other = (Lista) obj;
    if (descricao == null) {
      if (other.descricao != null)
        return false;
    } else if (!descricao.equals(other.descricao))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
  
}
