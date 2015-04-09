package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Informações de eximição.
 * @author Abreu Lopes
 * @since 2.0
 * @version $Id: CidEximido.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name="CID_EXIMIDO")
public final class CidEximido implements Serializable {

  private static final long serialVersionUID = 2765503596949495988L;

  @Id
  @Column(name="CIDADAO_RA")
  private Long cidadaoRa;

  @Column(name="ABI_RM_DATA")
  @Temporal(TemporalType.DATE)
  private Date abiRmData;

  @Column(name="ABI_RM_NR")
  private Integer abiRmNr;

  @Temporal(TemporalType.DATE)
  @Column(name="BI_RM_DATA")
  private Date biRmData;

  @Column(name="BI_RM_NR")
  private Integer biRmNr;

  @OneToOne
  @JoinColumn(name="CIDADAO_RA", insertable=false, updatable=false)
  private Cidadao cidadao;

  @Column(name="COMPARER_SEL_ANO")
  private Integer comparerSelAno;

  @Temporal(TemporalType.DATE)
  @Column(name="DOC_CSM_DATA")
  private Date docCsmData;

  @Column(name="DOC_CSM_NR")
  private Integer docCsmNr;

  @Column(name="DOC_CSM_TIPO")
  private String docCsmTipo;

  @Column(name="EXIMICAO_DOU_DATA")
  @Temporal(TemporalType.DATE)
  private Date eximicaoDouData;

  @Column(name="EXIMICAO_DOU_NR")
  private String eximicaoDouNr;

  @Column(name="OF_DSM_JUSTICA_DATA")
  @Temporal(TemporalType.DATE)
  private Date ofDsmJusticaData;

  @Column(name="OF_DSM_JUSTICA_NR")
  private Integer ofDsmJusticaNr;

  @Column(name="PDP_DOU_DATA")
  @Temporal(TemporalType.DATE)
  private Date pdpDouData;

  @Column(name="PDP_MOTIVO")
  private Integer pdpMotivo;

  @Column(name="PROC_JSM_NR")
  private String procJsmNr;

  @Column(name="REABILITACAO_DOU_DATA")
  @Temporal(TemporalType.DATE)
  private Date reabilitacaoDouData;

  @Column(name="REABILITACAO_DOU_NR")
  private String reabilitacaoDouNr;

  public CidEximido() {
    super();
  }

  public Date getAbiRmData() {
    return this.abiRmData;
  }

  public Integer getAbiRmNr() {
    return this.abiRmNr;
  }

  public String getBanner() {
    return this.toString();
  }

  public Date getBiRmData() {
    return this.biRmData;
  }

  public Integer getBiRmNr() {
    return this.biRmNr;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public Long getCidadaoRa() {
    return this.cidadaoRa;
  }

  public Integer getComparerSelAno() {
    return this.comparerSelAno;
  }

  public Date getDocCsmData() {
    return this.docCsmData;
  }

  public Integer getDocCsmNr() {
    return this.docCsmNr;
  }

  public String getDocCsmTipo() {
    return this.docCsmTipo;
  }

  public Date getEximicaoDouData() {
    return this.eximicaoDouData;
  }

  public String getEximicaoDouNr() {
    return this.eximicaoDouNr;
  }

  public Date getOfDsmJusticaData() {
    return this.ofDsmJusticaData;
  }

  public Integer getOfDsmJusticaNr() {
    return this.ofDsmJusticaNr;
  }

  public Date getPdpDouData() {
    return this.pdpDouData;
  }

  public Integer getPdpMotivo() {
    return this.pdpMotivo;
  }

  public String getProcJsmNr() {
    return this.procJsmNr;
  }

  public Date getReabilitacaoDouData() {
    return this.reabilitacaoDouData;
  }

  public String getReabilitacaoDouNr() {
    return this.reabilitacaoDouNr;
  }

  public void setAbiRmData(Date abiRmData) {
    this.abiRmData = abiRmData;
  }

  public void setAbiRmNr(Integer abiRmNr) {
    this.abiRmNr = abiRmNr;
  }

  public void setBiRmData(Date biRmData) {
    this.biRmData = biRmData;
  }

  public void setBiRmNr(Integer biRmNr) {
    this.biRmNr = biRmNr;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

  public void setCidadaoRa(Long cidadaoRa) {
    this.cidadaoRa = cidadaoRa;
  }

  public void setComparerSelAno(Integer comparerSelAno) {
    this.comparerSelAno = comparerSelAno;
  }

  public void setDocCsmData(Date data) {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.docCsmData = data;
    }
  }

  public void setDocCsmNr(Integer docCsmNr) {
    this.docCsmNr = docCsmNr;
  }

  public void setDocCsmTipo(String docCsmTipo) {
    this.docCsmTipo = docCsmTipo;
  }

  public void setEximicaoDouData(Date data) {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.eximicaoDouData = data;
    }
  }

  public void setEximicaoDouNr(String eximicaoDouNr) {
    this.eximicaoDouNr = eximicaoDouNr;
  }

  public void setOfDsmJusticaData(Date data) {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.ofDsmJusticaData = data;
    }
  }

  public void setOfDsmJusticaNr(Integer ofDsmJusticaNr) {
    this.ofDsmJusticaNr = ofDsmJusticaNr;
  }

  public void setPdpDouData(Date data) {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.pdpDouData = data;
    }
  }

  public void setPdpMotivo(Integer pdpMotivo) {
    this.pdpMotivo = pdpMotivo;
  }

  public void setProcJsmNr(String procJsmNr) {
    this.procJsmNr = procJsmNr;
  }

  public void setReabilitacaoDouData(Date data) {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new IllegalArgumentException("Data maior que a data atual.");
      } else {
        cal.set(1980, 0, 1); // 01-01-1980
        if (cal.getTime().after(data)) {
          throw new IllegalArgumentException("Data menor que 01/01/1980.");
        }
      }
      this.reabilitacaoDouData = data;
    }
  }

  public void setReabilitacaoDouNr(String reabilitacaoDouNr) {
    this.reabilitacaoDouNr = reabilitacaoDouNr;
  }

  @Override
  public String toString() {
    return new StringBuilder("EXIMIÇÃO: ")
           .append(this.getCidadao() == null ? "RA" : this.getCidadao())
           .toString();
  }

}
