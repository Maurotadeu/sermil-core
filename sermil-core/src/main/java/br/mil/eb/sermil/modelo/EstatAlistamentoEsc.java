package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Estatística de Alistamento (TABELA ESTAT_ALISTAMENTO_ESC).
 * @author Abreu Lopes, Gardino
 * @since 4.6
 * @version 5.4.5
 */
@Entity
@Table(name="ESTAT_ALISTAMENTO_ESC")
@NamedQueries({
  @NamedQuery(name = "EstatAlistamentoEsc.listarEscJsm", query = "SELECT e FROM EstatAlistamentoEsc e where e.pk.csmCodigo = ?1 and e.pk.jsmCodigo = ?2")
})
public class EstatAlistamentoEsc implements Serializable {

  private static final long serialVersionUID = -2984540410377872799L;

  @EmbeddedId
  private PK pk;

  private String descricao;

  private String escd;

  @Column(insertable = false, updatable = false)
  private Integer escolaridade;

  private Integer escr;

  @Column(name="RM_CODIGO")
  private Byte rmCodigo;

  private Integer total;

  public EstatAlistamentoEsc() {
    this.setPk(new EstatAlistamentoEsc.PK());
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getEscd() {
    return escd;
  }

  public void setEscd(String escd) {
    this.escd = escd;
  }

  public Integer getEscolaridade() {
    return escolaridade;
  }

  public void setEscolaridade(Integer escolaridade) {
    this.escolaridade = escolaridade;
  }

  public Integer getEscr() {
    return escr;
  }

  public void setEscr(Integer escr) {
    this.escr = escr;
  }

  public Byte getRmCodigo() {
    return rmCodigo;
  }

  public void setRmCodigo(Byte rmCodigo) {
    this.rmCodigo = rmCodigo;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }


  /** Chave primária (PK) de EstatExar.
   * @author Abreu Lopes
   * @since 4.6
   * @version 5.2.6
   */
  @Embeddable
  public static class PK implements Serializable{

    private static final long serialVersionUID = 2666260728887708950L;

    @Column(name="CSM_CODIGO")
    private Byte csmCodigo;

    @Column(name="JSM_CODIGO")
    private Byte jsmCodigo;

    private Integer escolaridade;

    public PK() {
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
      result = prime * result
          + ((escolaridade == null) ? 0 : escolaridade.hashCode());
      result = prime * result
          + ((jsmCodigo == null) ? 0 : jsmCodigo.hashCode());
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
      if (csmCodigo == null) {
        if (other.csmCodigo != null)
          return false;
      } else if (!csmCodigo.equals(other.csmCodigo))
        return false;
      if (escolaridade == null) {
        if (other.escolaridade != null)
          return false;
      } else if (!escolaridade.equals(other.escolaridade))
        return false;
      if (jsmCodigo == null) {
        if (other.jsmCodigo != null)
          return false;
      } else if (!jsmCodigo.equals(other.jsmCodigo))
        return false;
      return true;
    }


    public long getCsmCodigo() {
      return this.csmCodigo;
    }
    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }
    public long getJsmCodigo() {
      return this.jsmCodigo;
    }
    public void setJsmCodigo(Byte jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }
    public long getEscolaridade() {
      return this.escolaridade;
    }
    public void setEscolaridade(Integer escolaridade) {
      this.escolaridade = escolaridade;
    }

  }

}
