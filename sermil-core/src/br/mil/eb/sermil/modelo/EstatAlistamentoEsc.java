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
 * @since 4.5
 * @version 5.2.7
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

    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof PK)) {
        return false;
      }
      PK castOther = (PK)other;
      return (this.csmCodigo == castOther.csmCodigo) && (this.jsmCodigo == castOther.jsmCodigo) && (this.escolaridade == castOther.escolaridade);
    }

    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + ((int) (this.csmCodigo ^ (this.csmCodigo >>> 32)));
      hash = hash * prime + ((int) (this.jsmCodigo ^ (this.jsmCodigo >>> 32)));
      hash = hash * prime + ((int) (this.escolaridade ^ (this.escolaridade >>> 32)));
      return hash;
    }

  }

}
