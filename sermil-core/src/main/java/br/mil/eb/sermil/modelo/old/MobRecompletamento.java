package br.mil.eb.sermil.modelo.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Om;

/** Recompletamento da Sec Mob.
 * @author Abreu Lopes, Neckel
 * @since 3.5
 * @version $Id: MobNecessidade.java 984 2008-09-23 11:13:52Z wlopes  - Neckel$
 */
@Entity
@Table(name = "MOB_RECOMPLETAMENTO")
@NamedQueries({
  @NamedQuery(name = "MobRecompletamento.listarSecMob", query = "SELECT DISTINCT o FROM MobRecompletamento n, Om o WHERE n.secMob = o.codigo"),
  @NamedQuery(name = "MobRecompletamento.listarPorSecMob", query = "SELECT n FROM MobRecompletamento n WHERE n.secMob = :secmob"),
  @NamedQuery(name = "MobRecompletamento.excluirPorSecMob", query = "DELETE FROM MobRecompletamento n WHERE n.secMob = :secmob")
})
@Deprecated
public final class MobRecompletamento implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name="IDT_MILITAR")
  private String idtMilitar;

  @Column(name="SEC_MOB")
  private Integer secMob;

  @Column(name="POSTO_CODIGO")
  private String postoCodigo;

  @Column(name="QM_CODIGO")
  private String qmCodigo;

  @Column(name="HABILITACAO_1_CODIGO")
  private String habilitacao1Codigo;

  @Column(name="HABILITACAO_2_CODIGO")
  private String habilitacao2Codigo;

  @Column(name="FRACAO_ID")
  private String fracaoId;

  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="IDT_MILITAR", insertable=false, updatable=false)
  private Cidadao cidadao;

  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="SEC_MOB", insertable=false, updatable=false)
  private Om om;
  
  public MobRecompletamento() {
    super();
  }

  @Override
  public String toString() {
    return this.getSecMob().toString();
  }
  
  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

  public Integer getSecMob() {
    return secMob;
  }

  public void setSecMob(Integer secMob) {
    this.secMob = secMob;
  }

  public String getPostoCodigo() {
    return postoCodigo;
  }

  public void setPostoCodigo(String postoCodigo) {
    this.postoCodigo = postoCodigo;
  }

  public String getQmCodigo() {
    return qmCodigo;
  }

  public void setQmCodigo(String qmCodigo) {
    this.qmCodigo = qmCodigo;
  }

  public String getHabilitacao1Codigo() {
    return habilitacao1Codigo;
  }

  public void setHabilitacao1Codigo(String habilitacao1Codigo) {
    this.habilitacao1Codigo = habilitacao1Codigo;
  }

  public String getHabilitacao2Codigo() {
    return habilitacao2Codigo;
  }

  public void setHabilitacao2Codigo(String habilitacao2Codigo) {
    this.habilitacao2Codigo = habilitacao2Codigo;
  }

  public String getIdtMilitar() {
    return idtMilitar;
  }

  public void setIdtMilitar(String idtMilitar) {
    this.idtMilitar = idtMilitar;
  }

  public String getFracaoId() {
    return fracaoId;
  }

  public void setFracaoId(String fracaoId) {
    this.fracaoId = fracaoId;
  }

  public Om getOm() {
    return om;
  }

  public void setOm(Om om) {
    this.om = om;
  }

}
