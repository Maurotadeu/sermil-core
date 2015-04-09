package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Estatísticas do EXARNET.
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: EstatExar.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "ESTAT_EXAR")
@NamedQuery(name = "EstatExar.listarPorAno", query = "SELECT r FROM EstatExar r WHERE r.pk.ano = ?1")
@PrimaryKey(validation=IdValidation.NULL)
public final class EstatExar implements Serializable {

  private static final long serialVersionUID = -5127454320388798642L;

  @EmbeddedId
  private EstatExar.PK pk;

  private Integer total;

  public EstatExar() {
    this.setPk(new EstatExar.PK());
  }

  public EstatExar(Integer ano, Integer codigo) {
    this.setPk(new EstatExar.PK(ano, codigo));
  }

  public EstatExar.PK getPk() {
    return this.pk;
  }

  public void setPk(EstatExar.PK pk) {
    this.pk = pk;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  /** Chave primária (PK) de EstatExar.
   * @author Abreu Lopes
   * @since 3.2
   * @version $Id: EstatExar.java 1637 2011-11-25 13:52:11Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 5420706239198522340L;

    private Integer ano;

    private Integer codigo;

    public PK() {
      super();
    }

    public PK(final Integer ano, final Integer codigo) {
      this.setAno(ano);
      this.setCodigo(codigo);
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((ano == null) ? 0 : ano.hashCode());
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
      PK other = (PK) obj;
      if (ano == null) {
        if (other.ano != null)
          return false;
      } else if (!ano.equals(other.ano))
        return false;
      if (codigo == null) {
        if (other.codigo != null)
          return false;
      } else if (!codigo.equals(other.codigo))
        return false;
      return true;
    }
    
    public Integer getAno() {
      return ano;
    }

    public void setAno(Integer ano) {
      this.ano = ano;
    }

    public Integer getCodigo() {
      return codigo;
    }

    public void setCodigo(Integer codigo) {
      this.codigo = codigo;
    }

  }
  
}
