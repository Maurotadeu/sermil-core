package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Tributação de Junta de Serviço Militar.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name = "SEL_TRIBUTACAO")
@NamedQuery(name = "SelTributacao.listarPorCsm", query = "SELECT t FROM SelTributacao t WHERE t.pk.csmCodigo = :csm ORDER BY t.pk.csmCodigo")
public final class SelTributacao implements Serializable {

  private static final long serialVersionUID = -6094601616744082583L;

  @EmbeddedId
  private PK pk;

  private Byte tipo;

  private Byte aprovado;

  private String descricao;

  private Short cs;

  private Short delsm;

  private String infor;

  private Byte tributacao;

  @ManyToOne
  @JoinColumn(name = "MUNICIPIO_CODIGO", nullable = false)
  private Municipio municipio;

  @ManyToOne
  @JoinColumn(name = "RM_CODIGO", nullable = false)
  private Rm rm;

  public SelTributacao() {
    this.setPk(new PK());
    this.setRm(new Rm());
    this.setMunicipio(new Municipio());
  }

  public SelTributacao(final Byte csm, final Short jsm) {
    this();
    this.getPk().setCsmCodigo(csm);
    this.getPk().setJsmCodigo(jsm);
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString())
      .append(" - ")
      .append(this.descricao == null ? "SEL TRIBUTACAO" : this.descricao)
      .toString();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
    SelTributacao other = (SelTributacao) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Rm getRm() {
  	return rm;
  }

  public Byte getTipo() {
	  	return tipo;
  }

  public Byte getAprovado() {
	  	return aprovado;
  }

  public Short getCs() {
    return this.cs;
  }

  public Short getDelsm() {
    return this.delsm;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getInfor() {
    return this.infor;
  }

  public Byte getTributacao() {
    return this.tributacao;
  }

  public Municipio getMunicipio() {
	    return this.municipio;
  }

  public void setRm(Rm rm) {
  	this.rm = rm;
  }

  public void setTipo(Byte tipo) {
  	this.tipo = tipo;
  }

  public void setAprovado(Byte aprovado) {
  	this.aprovado = aprovado;
  }

  public void setDescricao(String descricao) {
	    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setCs(Short cs) {
    this.cs = cs;
  }

  public void setDelsm(Short delsm) {
    this.delsm = delsm;
  }

  public void setInfor(String infor) {
    this.infor = "S".equalsIgnoreCase(infor) ? "S" : "N";
  }

  public void setTributacao(Byte tributacao) {
    this.tributacao = tributacao;
  }

  public void setMunicipio(Municipio municipio) {
	    this.municipio = municipio;
  }

  /** Chave primária (PK) de SelTributacao.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = -6513320966430841701L;

    @Column(name = "CSM_CODIGO")
    Byte csmCodigo;

    @Column(name = "JSM_CODIGO")
    Short jsmCodigo;

    public PK() {
      super();
    }

    public PK(final Byte csmCodigo, final Short jsmCodigo) {
      this.setCsmCodigo(csmCodigo);
      this.setJsmCodigo(jsmCodigo);
    }

    @Override
    public String toString() {
      return new StringBuilder()
        .append(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
        .append("/")
        .append(this.getJsmCodigo() == null ? "000" : new DecimalFormat("000").format(this.getJsmCodigo()))
        .toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
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
      if (jsmCodigo == null) {
        if (other.jsmCodigo != null)
          return false;
      } else if (!jsmCodigo.equals(other.jsmCodigo))
        return false;
      return true;
    }

    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public Short getJsmCodigo() {
        return this.jsmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

  }

}
