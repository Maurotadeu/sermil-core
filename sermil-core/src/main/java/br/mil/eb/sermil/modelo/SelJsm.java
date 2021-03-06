package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.NamedStoredFunctionQueries;
import org.eclipse.persistence.annotations.NamedStoredFunctionQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;

/** Sele��o de JSM (TABELA SEL_JSM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name="SEL_JSM")
@Cache(type=CacheType.SOFT, size=1000, expiry=1200000)
@NamedQuery(name = "SelJsm.listarJsmTributaria", query = "SELECT s FROM SelJsm s WHERE EXISTS(SELECT c FROM Csm c WHERE c.codigo = s.pk.csmCodigo AND c.rm.codigo = ?1) AND EXISTS (SELECT j FROM Jsm j WHERE j.pk.codigo = s.pk.jsmCodigo AND j.pk.csmCodigo = s.pk.csmCodigo AND j.tributacao BETWEEN 1 AND 4)  ORDER BY s.pk.csmCodigo,s.pk.jsmCodigo ")
@NamedStoredFunctionQueries({
  @NamedStoredFunctionQuery(name="SelJsm.executa", functionName="sel_processamento.executa", parameters={@StoredProcedureParameter(queryParameter = "P_RM", mode=ParameterMode.IN), @StoredProcedureParameter(queryParameter = "P_CPF", mode=ParameterMode.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="SelJsm.reverte", functionName="sel_processamento.reverte", parameters={@StoredProcedureParameter(queryParameter = "P_RM", mode=ParameterMode.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG"))
})
public final class SelJsm implements Serializable {

  private static final long serialVersionUID = 55647247648237698L;

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
  @PrimaryKeyJoinColumns
  ({
    @PrimaryKeyJoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO"),
    @PrimaryKeyJoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO")
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
    return this.getJsm() != null ? this.getJsm().toString() : this.getPk().toString() + " [SelJsm]";
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
    SelJsm other = (SelJsm) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
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

  /** Chave prim�ria (PK) de SelJsm.
   * @author Abreu Lopes
   * @since 3.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Serializable {

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

    @Override
    public String toString() {
      return new StringBuilder()
        .append(this.getCsmCodigo() == null ? "CSM" : new DecimalFormat("00").format(this.getCsmCodigo()))
        .append("/")
        .append(this.getJsmCodigo() == null ? "JSM" : new DecimalFormat("000").format(this.getJsmCodigo()))
        .toString();
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
      result = prime * result + ((jsmCodigo == null) ? 0 : jsmCodigo.hashCode());
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

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

    public Short getJsmCodigo() {
      return this.jsmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }
    
  }

}
