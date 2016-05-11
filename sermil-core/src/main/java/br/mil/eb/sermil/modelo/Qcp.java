package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Quadro de Cargos (QC).
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name="QCP") 
@NamedQueries({
  @NamedQuery(name = "Qcp.listarPorPostoGraduacao", query = "SELECT q FROM Qcp q WHERE q.pk.omCodigo = :om AND q.postoGraduacao.codigo = :postoGraduacao ORDER BY q.postoGraduacao.codigo"),
  @NamedQuery(name = "Qcp.listarPorOm", query =  "SELECT q FROM Qcp q WHERE q.pk.omCodigo = ?1 AND q.fracaoTipo = 2 ORDER BY q.pk.fracaoId "),
  @NamedQuery(name = "Qcp.listarPorOm2", query =  "SELECT q.pk.fracaoId, q.cargoDescricao FROM Qcp q WHERE q.pk.omCodigo = ?1 AND q.fracaoTipo = 2 ORDER BY q.pk.fracaoId "),
  @NamedQuery(name = "Qcp.listarPorOmTudo", query = "SELECT q FROM Qcp q WHERE q.pk.omCodigo = ?1 ORDER BY FUNC('TO_NUMBER',SUBSTRING(q.pk.fracaoId,1, CASE WHEN (SUBSTRING(q.pk.fracaoId,2,1) = '.') THEN 1 ELSE 2 END)), " +
      "TRIM(' ' FROM TRIM('.' FROM SUBSTRING(q.pk.fracaoId,1,LENGTH(q.pk.fracaoId)))) ")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Qcp implements Comparable<Qcp>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -471838432443953608L;

  @EmbeddedId
  private PK pk;

  @Column(name="FRACAO_TIPO")
  private Byte fracaoTipo;

  @Column(name="CARGO_DESCRICAO")
  private String cargoDescricao;

  @Column(name="CARGO_QO_QTD")
  private Short cargoQoQtd;

  @Column(name="CARGO_FLEXIBILIDADE_QTD")
  private Short cargoFlexibilidadeQtd;

  @Column(name="CARGO_PREVISTO_QTD")
  private Short cargoPrevistoQtd;

  @Column(name="NAO_ARREGIMENTADO")
  private String naoArregimentado;

  @Column(name="CARGO_CATEGORIA")
  private Byte cargoCategoria;

  @Column(name="CARGO_EFETIVO_TIPO")
  private Byte cargoEfetivoTipo;

  @Column(name = "EV_QTD")
  private Short evQtd;

  @Column(name = "CARGO_NB_EV")
  private String cargoNbEv;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "QM_CODIGO", insertable = false, updatable = false)
  private Qm qm;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "POSTO_GRADUACAO_CODIGO", insertable = false, updatable = false)
  private PostoGraduacao postoGraduacao;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "HABILITACAO_1_CODIGO", insertable = false, updatable = false)
  private QcpHabilitacao habilitacao1;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "HABILITACAO_2_CODIGO", insertable = false, updatable = false)
  private QcpHabilitacao habilitacao2;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "QCP_LEGENDA_CODIGO", insertable = false, updatable = false)
  private QcpLegenda qcpLegenda;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "QCP_OBS_CODIGO", insertable = false, updatable = false)
  private QcpObservacao qcpObservacao;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "OM_CODIGO", insertable = false, updatable = false)
  private Om om;


  public Qcp() {
    this.setPk(new PK());
  }

  public Qcp(final Integer omCodigo, final String fracao) {
    this.setPk(new PK(omCodigo, fracao));
  }

  @Override
  public int compareTo(Qcp o) {
    return this.getPk().compareTo(o.getPk());
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Byte getFracaoTipo() {
    return fracaoTipo;
  }

  public void setFracaoTipo(Byte fracaoTipo) {
    this.fracaoTipo = fracaoTipo;
  }

  public String getCargoDescricao() {
    return cargoDescricao;
  }

  public void setCargoDescricao(String cargoDescricao) {
    this.cargoDescricao = cargoDescricao;
  }

  public Short getCargoQoQtd() {
    return cargoQoQtd;
  }

  public void setCargoQoQtd(Short cargoQoQtd) {
    this.cargoQoQtd = cargoQoQtd;
  }

  public Short getCargoFlexibilidadeQtd() {
    return cargoFlexibilidadeQtd;
  }

  public void setCargoFlexibilidadeQtd(Short cargoFlexibilidadeQtd) {
    this.cargoFlexibilidadeQtd = cargoFlexibilidadeQtd;
  }

  public Short getCargoPrevistoQtd() {
    return cargoPrevistoQtd;
  }

  public void setCargoPrevistoQtd(Short cargoPrevistoQtd) {
    this.cargoPrevistoQtd = cargoPrevistoQtd;
  }

  public String getNaoArregimentado() {
    return naoArregimentado;
  }

  public void setNaoArregimentado(String naoArregimentado) {
    this.naoArregimentado = naoArregimentado;
  }

  public Byte getCargoCategoria() {
    return cargoCategoria;
  }

  public void setCargoCategoria(Byte cargoCategoria) {
    this.cargoCategoria = cargoCategoria;
  }

  public Byte getCargoEfetivoTipo() {
    return cargoEfetivoTipo;
  }

  public void setCargoEfetivoTipo(Byte cargoEfetivoTipo) {
    this.cargoEfetivoTipo = cargoEfetivoTipo;
  }

  public Short getEvQtd() {
    return evQtd;
  }

  public void setEvQtd(Short evQtd) {
    this.evQtd = evQtd;
  }

  public String getCargoNbEv() {
    return cargoNbEv;
  }

  public void setCargoNbEv(String cargoNbEv) {
    this.cargoNbEv = cargoNbEv;
  }

  public Om getOm() {
    return this.om;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public Qm getQm() {
    return this.qm;
  }

  public void setQmCodigo(Qm qm) {
    this.qm = qm;
  }

  public PostoGraduacao getPostoGraduacao() {
    return this.postoGraduacao;
  }

  public void setPostoGraduacao(PostoGraduacao postoGraduacao) {
    this.postoGraduacao = postoGraduacao;
  }

  public QcpHabilitacao getHabilitacao1() {
    return this.habilitacao1;
  }

  public void setHabilitacao1(QcpHabilitacao habilitacao1) {
    this.habilitacao1 = habilitacao1;
  }

  public QcpHabilitacao getHabilitacao2() {
    return this.habilitacao2;
  }

  public void setHabilitacao2(QcpHabilitacao habilitacao2) {
    this.habilitacao2 = habilitacao2;
  }

  public QcpLegenda getQcpLegenda() {
    return this.qcpLegenda;
  }

  public void setQcpLegenda(QcpLegenda qcpLegenda) {
    this.qcpLegenda = qcpLegenda;
  }

  public QcpObservacao getQcpObservacao() {
    return this.qcpObservacao;
  }

  public void setQcpObservacao(QcpObservacao qcpObservacao) {
    this.qcpObservacao = qcpObservacao;
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString())
    .append(" - ")
    .append(this.getCargoDescricao())
    .toString();
  }

  /** Chave primária (PK) de Qcp.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<Qcp.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -4957470859211162903L;

    @Column(name="OM_CODIGO")
    private Integer omCodigo;

    @Column(name="FRACAO_ID")
    private String fracaoId;

    public PK() {
      super();
    }

    public PK(final Integer codom, final String fracao) {
      this.setOmCodigo(codom);
      this.setFracaoId(fracao);
    }

    @Override
    public int compareTo(PK o) {
      return this.getOmCodigo().compareTo(o.getOmCodigo()) == 0 ? this.getFracaoId().compareTo(o.getFracaoId()) : this.getOmCodigo().compareTo(o.getOmCodigo());
    }

    public Integer getOmCodigo() {
      return this.omCodigo;
    }

    public void setOmCodigo(Integer omCodigo) {
      this.omCodigo = omCodigo;
    }

    public String getFracaoId() {
      return this.fracaoId;
    }

    public void setFracaoId(String fracaoId) {
      this.fracaoId = fracaoId;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if ( ! (o instanceof PK)) {
        return false;
      }
      PK other = (PK) o;
      return (this.omCodigo == other.omCodigo)
          && this.fracaoId.equals(other.fracaoId);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + ((int) (this.omCodigo ^ (this.omCodigo >>> 32)));
      hash = hash * prime + this.fracaoId.hashCode();
      return hash;
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getOmCodigo() == null ? "NULO" : this.getOmCodigo().toString())
      .append(" - ")
      .append(this.getFracaoId() == null ? "NULO" : this.getFracaoId())
      .toString();
    }

  }
}
