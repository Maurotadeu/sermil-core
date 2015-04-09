package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Direction;
import org.eclipse.persistence.annotations.NamedStoredFunctionQueries;
import org.eclipse.persistence.annotations.NamedStoredFunctionQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;

/** Seleção de JSM (TABELA SEL_JSM).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: SelJsm.java 2459 2014-06-06 11:41:50Z wlopes $
 */
@Entity
@Table(name="SEL_JSM")
@NamedQueries({
	  @NamedQuery(name = "SelJsm.listarJsmTributaria", query = "SELECT s FROM SelJsm s WHERE EXISTS(SELECT c FROM Csm c WHERE c.codigo = s.pk.csmCodigo AND c.rm.codigo = ?1) AND EXISTS (SELECT j FROM Jsm j WHERE j.pk.codigo = s.pk.jsmCodigo AND j.pk.csmCodigo = s.pk.csmCodigo AND j.tributacao BETWEEN 1 AND 4)  ORDER BY s.pk.csmCodigo,s.pk.jsmCodigo ")
  //@NamedQuery(name = "SelJsm.listarPorJsm", query = "SELECT c.jsm.pk.csmCodigo, c.jsm.pk.codigo, c.escolaridade, COUNT(c.ra) FROM Cidadao c, CidEvento e WHERE c.ra = e.pk.cidadaoRa AND e.pk.codigo = 1 AND e.pk.data BETWEEN :data1 AND :data2 GROUP BY c.jsm.pk.csmCodigo, c.jsm.pk.codigo, c.escolaridade")
})
@NamedStoredFunctionQueries({
  @NamedStoredFunctionQuery(name="SelJsm.executa", functionName="sel_processamento.executa", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN), @StoredProcedureParameter(queryParameter = "P_CPF",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="SelJsm.reverte", functionName="sel_processamento.reverte", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG"))
})
public final class SelJsm implements Serializable {

  private static final long serialVersionUID = -1102580263807345626L;

  @EmbeddedId
  private PK pk;

  @Column(name="FS_INICIAL")
  private Integer fsInicial;

  @Column(name="FS_FINAL")
  private Integer fsFinal;

  @Column(name="DISPENSA_PERCENTUAL")
  private Byte dispensaPercentual;

  @Column(name="DISPENSA_ESCOLARIDADE")
  private Byte dispensaEscolaridade;

  @Column(name="DISPENSA_PERCENTUAL_H")
  private Byte dispensaPercentualH;

  @Column(name="DISPENSA_ESCOLARIDADE_H")
  private Byte dispensaEscolaridadeH;

  private Short aptos;

  @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
  @JoinColumns({
    @JoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO", insertable=false, updatable=false, nullable=false),
    @JoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false, nullable=false)
  })
  private Jsm jsm;

  public SelJsm() {
    this.setPk(new PK());
  }

  public SelJsm(final Byte csm, final Short jsm) {
    this.setPk(new PK(csm, jsm));
  }

  @Override
  public String toString() {
    return this.getJsm() != null ? this.getJsm().toString() : this.getPk().toString();
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Jsm getJsm() {
    return this.jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  public Integer getFsInicial() {
    return this.fsInicial;
  }

  public void setFsInicial(Integer fsInicial) {
    this.fsInicial = fsInicial;
  }

  public Integer getFsFinal() {
    return this.fsFinal;
  }

  public void setFsFinal(Integer fsFinal) {
    this.fsFinal = fsFinal;
  }

  public Byte getDispensaPercentual() {
    return this.dispensaPercentual;
  }

  public void setDispensaPercentual(Byte dispensaPercentual) {
    this.dispensaPercentual = dispensaPercentual;
  }

  public Byte getDispensaEscolaridade() {
    return this.dispensaEscolaridade;
  }

  public void setDispensaEscolaridade(Byte dispensaEscolaridade) {
    this.dispensaEscolaridade = dispensaEscolaridade;
  }

  public Byte getDispensaPercentualH() {
    return this.dispensaPercentualH;
  }

  public void setDispensaPercentualH(Byte dispensaPercentualH) {
    this.dispensaPercentualH = dispensaPercentualH;
  }

  public Byte getDispensaEscolaridadeH() {
    return this.dispensaEscolaridadeH;
  }

  public void setDispensaEscolaridadeH(Byte dispensaEscolaridadeH) {
    this.dispensaEscolaridadeH = dispensaEscolaridadeH;
  }

  public Short getAptos() {
    return this.aptos;
  }

  public void setAptos(Short aptos) {
    this.aptos = aptos;
  }

/** Chave primária (PK) de SelJsm.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: SelJsm.java 2459 2014-06-06 11:41:50Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 6573419847324692853L;

    @Column(name="CSM_CODIGO")
    private Byte csmCodigo;

    @Column(name="JSM_CODIGO")
    private Short jsmCodigo;

    public PK() {
      super();
    }

    public PK(final Byte csmCodigo, final Short jsmCodigo) {
      this.setCsmCodigo(csmCodigo);
      this.setJsmCodigo(jsmCodigo);
    }

    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

    public Short getJsmCodigo() {
      return this.jsmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
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
      return (this.csmCodigo == other.csmCodigo) && (this.jsmCodigo == other.jsmCodigo);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + ((int) (this.csmCodigo ^ (this.csmCodigo >>> 32)));
      hash = hash * prime + ((int) (this.jsmCodigo ^ (this.jsmCodigo >>> 32)));
      return hash;
    }

    @Override
    public String toString() {
      return new StringBuilder()
        .append(this.getCsmCodigo() == null ? "CSM" : new DecimalFormat("00").format(this.getCsmCodigo()))
        .append("/")
        .append(this.getJsmCodigo() == null ? "JSM" : new DecimalFormat("000").format(this.getJsmCodigo()))
        .toString();
    }

  }

}
