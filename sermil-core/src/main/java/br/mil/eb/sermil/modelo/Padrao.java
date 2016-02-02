package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/** Padr�o Funcional.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Padrao.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQuery(name = "Padrao.listar", query = "SELECT p.codigo FROM Padrao p WHERE SUBSTRING(p.codigo,2,2) != '99' AND p.codigo NOT IN ('F01','F02')")
public final class Padrao implements Comparable<Padrao>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -116567940870452513L;

  @Id
  private String codigo;

  @Column(name = "NV_APT_B")
  private Byte nvAptB;

  @Column(name = "NV_APT_E")
  private Byte nvAptE;

  @Column(name = "NV_APT_M")
  private Byte nvAptM;

  @Column(name = "NV_APT_N")
  private Byte nvAptN;

  @Column(name = "NV_APT_V")
  private Byte nvAptV;

  @Column(name = "NV_ESC")
  private Byte nvEsc;

  @Column(name = "NV_TC_EL")
  private Byte nvTcEl;

  @Column(name = "NV_TC_EN")
  private Byte nvTcEn;

  @Column(name = "NV_TC_MA")
  private Byte nvTcMa;

  @Column(name = "NV_TF")
  private Byte nvTf;

  @Column(name = "NV_TSI_I")
  private Byte nvTsiI;

  @Column(name = "ORDEM_NR")
  private Byte ordemNr;

  @Column(name = "ORDEM_PQ_NR")
  private Byte ordemPqNr;

  @Column(name = "RE_AC_AUD")
  private Byte reAcAud;

  @Column(name = "RE_ALTURA")
  private Byte reAltura;

  @Column(name = "RE_ESCOLA")
  private Byte reEscola;

  @Column(name = "RE_EXP_ORAL")
  private Byte reExpOral;

  @Column(name = "RE_FORCA")
  private Byte reForca;

  public Padrao() {
    super();
  }

  @Override
  public int compareTo(Padrao o) {
    return this.getCodigo().compareTo(o.getCodigo());
  }
  
  @Override
  public String toString() {
    return new StringBuilder(this.getOrdemNr() == null ? "NULO" : this.getOrdemNr().toString())
      .append(" - ")
      .append(this.getCodigo() == null ? "NULO" : this.getCodigo())
      .toString();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public Byte getNvAptB() {
    return this.nvAptB;
  }

  public Byte getNvAptE() {
    return this.nvAptE;
  }

  public Byte getNvAptM() {
    return this.nvAptM;
  }

  public Byte getNvAptN() {
    return this.nvAptN;
  }

  public Byte getNvAptV() {
    return this.nvAptV;
  }

  public Byte getNvEsc() {
    return this.nvEsc;
  }

  public Byte getNvTcEl() {
    return this.nvTcEl;
  }

  public Byte getNvTcEn() {
    return this.nvTcEn;
  }

  public Byte getNvTcMa() {
    return this.nvTcMa;
  }

  public Byte getNvTf() {
    return this.nvTf;
  }

  public Byte getNvTsiI() {
    return this.nvTsiI;
  }

  public Byte getOrdemNr() {
    return this.ordemNr;
  }

  public Byte getOrdemPqNr() {
    return this.ordemPqNr;
  }

  public Byte getReAcAud() {
    return this.reAcAud;
  }

  public Byte getReAltura() {
    return this.reAltura;
  }

  public Byte getReEscola() {
    return this.reEscola;
  }

  public Byte getReExpOral() {
    return this.reExpOral;
  }

  public Byte getReForca() {
    return this.reForca;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setNvAptB(Byte nvAptB) {
    this.nvAptB = nvAptB;
  }

  public void setNvAptE(Byte nvAptE) {
    this.nvAptE = nvAptE;
  }

  public void setNvAptM(Byte nvAptM) {
    this.nvAptM = nvAptM;
  }

  public void setNvAptN(Byte nvAptN) {
    this.nvAptN = nvAptN;
  }

  public void setNvAptV(Byte nvAptV) {
    this.nvAptV = nvAptV;
  }

  public void setNvEsc(Byte nvEsc) {
    this.nvEsc = nvEsc;
  }

  public void setNvTcEl(Byte nvTcEl) {
    this.nvTcEl = nvTcEl;
  }

  public void setNvTcEn(Byte nvTcEn) {
    this.nvTcEn = nvTcEn;
  }

  public void setNvTcMa(Byte nvTcMa) {
    this.nvTcMa = nvTcMa;
  }

  public void setNvTf(Byte nvTf) {
    this.nvTf = nvTf;
  }

  public void setNvTsiI(Byte nvTsiI) {
    this.nvTsiI = nvTsiI;
  }

  public void setOrdemNr(Byte ordemNr) {
    this.ordemNr = ordemNr;
  }

  public void setOrdemPqNr(Byte ordemPqNr) {
    this.ordemPqNr = ordemPqNr;
  }

  public void setReAcAud(Byte reAcAud) {
    this.reAcAud = reAcAud;
  }

  public void setReAltura(Byte reAltura) {
    this.reAltura = reAltura;
  }

  public void setReEscola(Byte reEscola) {
    this.reEscola = reEscola;
  }

  public void setReExpOral(Byte reExpOral) {
    this.reExpOral = reExpOral;
  }

  public void setReForca(Byte reForca) {
    this.reForca = reForca;
  }

}