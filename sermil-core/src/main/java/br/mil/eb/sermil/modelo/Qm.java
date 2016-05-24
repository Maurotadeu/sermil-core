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

/** Qualificação Militar (QM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Cache(type=CacheType.FULL, size=600)
@NamedQueries(value = {
  @NamedQuery(name = "Qm.listar", query = "SELECT q.codigo, CONCAT(q.codigo, ' - ', q.descricao) FROM Qm q ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaCbSd", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) IN ('0','1','2','3') ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaStSgt", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = '5' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaQao", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = '6' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaQco", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = '7' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaOf", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = '8' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaGen", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = '9' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaOtt", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = 'T' ORDER BY q.codigo"),
  @NamedQuery(name = "Qm.listaStt", query = "SELECT q FROM Qm q WHERE SUBSTRING(q.codigo,1,1) = 'S' ORDER BY q.codigo")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Qm implements Comparable<Qm>, Serializable {

  private static final long serialVersionUID = -1119212672194025136L;

  @Id
  private String codigo;

  private String descricao;

  private String sigla;

  private String tipo;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "ARMA_QD_SV_CODIGO")
  private ArmaQdSv armaQdSv;

  public Qm() {
    super();
  }

  @Override
  public int compareTo(Qm o) {
    return this.getDescricao().compareTo(o.getDescricao());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo())
    .append(" - ")
    .append(this.getDescricao() == null ? "QM" : this.getDescricao())
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
    Qm other = (Qm) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public ArmaQdSv getArmaQdSv() {
    return this.armaQdSv;
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

  public String getTipo() {
    return this.tipo;
  }

  public void setArmaQdSv(ArmaQdSv armaQdSv) {
    this.armaQdSv = armaQdSv;
  }

  public void setCodigo(String codigo) {
    this.codigo = (codigo == null || codigo.trim().isEmpty() ? null : codigo.trim().toUpperCase());
  }

  public void setDescricao(String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setSigla(String sigla) {
    this.sigla = (sigla == null || sigla.trim().isEmpty() ? null : sigla.trim().toUpperCase());
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}
