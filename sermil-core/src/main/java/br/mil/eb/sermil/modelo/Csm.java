package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Circunscrição de Serviço Militar.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=30)
@NamedQueries({
  @NamedQuery(name="Csm.listar", query="SELECT c FROM Csm c WHERE c.ativo = 'S' ORDER BY c.codigo"),
  @NamedQuery(name="Csm.listarPorRm", query="SELECT c FROM Csm c WHERE c.rm.codigo = ?1 AND c.ativo = 'S' ORDER BY c.codigo")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Csm implements Comparable<Csm>, Serializable {

  private static final long serialVersionUID = -5025584683409331340L;

  @Id
  private Byte codigo;

  private String descricao;

  private String sigla;

  private String ativo;
  
  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name="RM_CODIGO", nullable=false)
  private Rm rm;

  public Csm() {
  }

  public Csm(Byte codigo) {
    this.codigo = codigo;
  }

  @Override
  public int compareTo(Csm o) {
    return this.getSigla().compareTo(o.getSigla());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
    Csm other = (Csm) obj;
    if (this.codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!this.codigo.equals(other.codigo))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.getSigla() == null ? "CSM" : this.getSigla();
  }

  public Byte getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public Rm getRm() {
    return this.rm;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setCodigo(Byte codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setRm(Rm rm) {
    this.rm = rm;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

  public String getAtivo() {
    return ativo;
  }

  public void setAtivo(String ativo) {
    this.ativo = ativo;
  }
  
}
