package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** Bateria Classificatória de Conscrito (BCC).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidBcc.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "CID_BCC")
public final class CidBcc implements Serializable {

  private static final long serialVersionUID = 6060646651623051652L;

  @Id
  @Column(name = "CIDADAO_RA")
  private Long cidadaoRa;

  @OneToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false)
  private Cidadao cidadao;

  @Column(name = "CD_IAP_1")
  private String cdIap1;

  @Column(name = "CD_IAP_2")
  private String cdIap2;

  @Column(name = "CD_IAP_3")
  private String cdIap3;

  @Column(name = "NT_APT_B")
  private Byte ntAptB;

  @Column(name = "NT_APT_E")
  private Byte ntAptE;

  @Column(name = "NT_APT_M")
  private Byte ntAptM;

  @Column(name = "NT_APT_N")
  private Byte ntAptN;

  @Column(name = "NT_APT_V")
  private Byte ntAptV;

  @Column(name = "NT_IAP_B")
  private Byte ntIapB;

  @Column(name = "NT_IAP_C")
  private Byte ntIapC;

  @Column(name = "NT_IAP_E")
  private Byte ntIapE;

  @Column(name = "NT_IAP_G")
  private Byte ntIapG;

  @Column(name = "NT_IAP_M")
  private Byte ntIapM;

  @Column(name = "NT_TC_EL")
  private Byte ntTcEl;

  @Column(name = "NT_TC_EN")
  private Byte ntTcEn;

  @Column(name = "NT_TC_MA")
  private Byte ntTcMa;

  public CidBcc() {
    super();
  }

  @Override
  public String toString() {
    return new StringBuilder("BCC: ")
      .append(this.cidadao == null ? "CIDADAO" : this.getCidadao())
      .toString();
  }

  public String getCdIap1() {
    return this.cdIap1;
  }

  public String getCdIap2() {
    return this.cdIap2;
  }

  public String getCdIap3() {
    return this.cdIap3;
  }

  public Cidadao getCidadao() {
    return cidadao;
  }

  public Long getCidadaoRa() {
    return this.cidadaoRa;
  }

  public Byte getNtAptB() {
    return this.ntAptB;
  }

  public Byte getNtAptE() {
    return this.ntAptE;
  }

  public Byte getNtAptM() {
    return this.ntAptM;
  }

  public Byte getNtAptN() {
    return this.ntAptN;
  }

  public Byte getNtAptV() {
    return this.ntAptV;
  }

  public Byte getNtIapB() {
    return this.ntIapB;
  }

  public Byte getNtIapC() {
    return this.ntIapC;
  }

  public Byte getNtIapE() {
    return this.ntIapE;
  }

  public Byte getNtIapG() {
    return this.ntIapG;
  }

  public Byte getNtIapM() {
    return this.ntIapM;
  }

  public Byte getNtTcEl() {
    return this.ntTcEl;
  }

  public Byte getNtTcEn() {
    return this.ntTcEn;
  }

  public Byte getNtTcMa() {
    return this.ntTcMa;
  }

  public void setCdIap1(String cdIap1) {
    this.cdIap1 = cdIap1;
  }

  public void setCdIap2(String cdIap2) {
    this.cdIap2 = cdIap2;
  }

  public void setCdIap3(String cdIap3) {
    this.cdIap3 = cdIap3;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

  public void setCidadaoRa(Long cidadaoRa) {
    this.cidadaoRa = cidadaoRa;
  }

  public void setNtAptB(Byte ntAptB) {
    this.ntAptB = ntAptB;
  }

  public void setNtAptE(Byte ntAptE) {
    this.ntAptE = ntAptE;
  }

  public void setNtAptM(Byte ntAptM) {
    this.ntAptM = ntAptM;
  }

  public void setNtAptN(Byte ntAptN) {
    this.ntAptN = ntAptN;
  }

  public void setNtAptV(Byte ntAptV) {
    this.ntAptV = ntAptV;
  }

  public void setNtIapB(Byte ntIapB) {
    this.ntIapB = ntIapB;
  }

  public void setNtIapC(Byte ntIapC) {
    this.ntIapC = ntIapC;
  }

  public void setNtIapE(Byte ntIapE) {
    this.ntIapE = ntIapE;
  }

  public void setNtIapG(Byte ntIapG) {
    this.ntIapG = ntIapG;
  }

  public void setNtIapM(Byte ntIapM) {
    this.ntIapM = ntIapM;
  }

  public void setNtTcEl(Byte ntTcEl) {
    this.ntTcEl = ntTcEl;
  }

  public void setNtTcEn(Byte ntTcEn) {
    this.ntTcEn = ntTcEn;
  }

  public void setNtTcMa(Byte ntTcMa) {
    this.ntTcMa = ntTcMa;
  }

}
