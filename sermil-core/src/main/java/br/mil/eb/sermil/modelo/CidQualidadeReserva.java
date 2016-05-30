package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Qualidade da Reserva.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name = "CID_QUALIDADE_RESERVA")
public final class CidQualidadeReserva implements Serializable {

  private static final long serialVersionUID = -1058660213597670639L;

  @EmbeddedId
  private CidQualidadeReserva.PK pk;

  private Byte escolaridade;

  @Column(name = "EMPREGO_TIPO")
  private Byte empregoTipo;

  @Column(name = "EMPREGO_NIVEL")
  private Byte empregoNivel;

  private Byte renda;

  @Column(name = "AREA_ATIVIDADE")
  private Byte areaAtividade;

  @Column(name = "MISSAO_PAZ")
  private String missaoPaz;

  @Column(name = "NOME_ATIVIDADE")
  private String nomeAtividade;

  @Column(name = "SC_ANO")
  private Integer scAno;

  @Column(name = "SC_EMPREGO")
  private Byte scEmprego;

  @Column(name = "SC_EFETIVIDADE")
  private Byte scEfetividade;

  @Column(name = "MEIO_COMUNICACAO")
  private Byte meioComunicacao;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  public CidQualidadeReserva() {
    this.setPk(new CidQualidadeReserva.PK());
  }

  public CidQualidadeReserva(final Long ra, final Integer ano) {
    this.setPk(new CidQualidadeReserva.PK(ra, ano));
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
    CidQualidadeReserva other = (CidQualidadeReserva) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public Byte getEscolaridade() {
    return escolaridade;
  }

  public void setEscolaridade(Byte escolaridade) {
    this.escolaridade = escolaridade;
  }

  public Byte getEmpregoTipo() {
    return empregoTipo;
  }

  public void setEmpregoTipo(Byte empregoTipo) {
    this.empregoTipo = empregoTipo;
  }

  public Byte getEmpregoNivel() {
    return empregoNivel;
  }

  public void setEmpregoNivel(Byte empregoNivel) {
    this.empregoNivel = empregoNivel;
  }

  public Byte getRenda() {
    return renda;
  }

  public void setRenda(Byte renda) {
    this.renda = renda;
  }

  public Byte getAreaAtividade() {
    return areaAtividade;
  }

  public void setAreaAtividade(Byte areaAtividade) {
    this.areaAtividade = areaAtividade;
  }

  public String getMissaoPaz() {
    return missaoPaz;
  }

  public void setMissaoPaz(String missaoPaz) {
    this.missaoPaz = missaoPaz;
  }

  public String getNomeAtividade() {
    return nomeAtividade;
  }

  public void setNomeAtividade(String nomeAtividade) {
    this.nomeAtividade = nomeAtividade;
  }

  public Integer getScAno() {
    return scAno;
  }

  public void setScAno(Integer scAno) {
    this.scAno = scAno;
  }

  public Byte getScEmprego() {
    return scEmprego;
  }

  public void setScEmprego(Byte scEmprego) {
    this.scEmprego = scEmprego;
  }

  public Byte getScEfetividade() {
    return scEfetividade;
  }

  public void setScEfetividade(Byte scEfetividade) {
    this.scEfetividade = scEfetividade;
  }

  public Byte getMeioComunicacao() {
    return this.meioComunicacao;
  }

  public void setMeioComunicacao(Byte meioComunicacao) {
    this.meioComunicacao = meioComunicacao;
  }

  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidQualidadeReservaCollection().contains(this)) {
      cid.getCidQualidadeReservaCollection().add(this);
    }
  }

  public CidQualidadeReserva.PK getPk() {
    return pk;
  }

  public void setPk(CidQualidadeReserva.PK pk) {
    this.pk = pk;
  }
  
  /** Chave primária (PK) de CidQualidadeReserva.
   * @author Abreu Lopes
   * @since 3.4
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = 5628917229777511550L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    private Integer ano;

    public PK() {
      super();
    }

    public PK(final Long cidadaoRa, final Integer ano) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setAno(ano);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((ano == null) ? 0 : ano.hashCode());
      result = prime * result + ((cidadaoRa == null) ? 0 : cidadaoRa.hashCode());
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
      if (cidadaoRa == null) {
        if (other.cidadaoRa != null)
          return false;
      } else if (!cidadaoRa.equals(other.cidadaoRa))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return cidadaoRa;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public Integer getAno() {
      return ano;
    }

    public void setAno(Integer ano) {
      this.ano = ano;
    }

  }

}
