package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Direction;
import org.eclipse.persistence.annotations.NamedStoredFunctionQueries;
import org.eclipse.persistence.annotations.NamedStoredFunctionQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;

/** Entidade DstbParametro.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
 */
@Entity
@Table(name = "DSTB_PARAMETRO")
@NamedStoredFunctionQueries({
  @NamedStoredFunctionQuery(name="Distribuicao.prepara", functionName="dis_processamento.prepara", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN), @StoredProcedureParameter(queryParameter = "P_CPF",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="Distribuicao.executa", functionName="dis_processamento.executa", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN), @StoredProcedureParameter(queryParameter = "P_CPF",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="Distribuicao.reverte", functionName="dis_processamento.reverte", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN), @StoredProcedureParameter(queryParameter = "P_CPF",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="Distribuicao.finaliza", functionName="dis_processamento.finaliza", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN), @StoredProcedureParameter(queryParameter = "P_CPF",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG")),
  @NamedStoredFunctionQuery(name="Distribuicao.verifica", functionName="dis_processamento.verifica", parameters={@StoredProcedureParameter(queryParameter = "P_RM",direction=Direction.IN)}, returnParameter=@StoredProcedureParameter(queryParameter="MSG"))
})
@NamedQueries({
   @NamedQuery(name="Distribuicao.ParamtrosPorAno&Rm", query="select d from DstbParametro d where EXTRACT(year FROM d.gptAData) = ?1 and d.pk.rmCodigo = ?2"),
})
public final class DstbParametro implements Comparable<DstbParametro>, Serializable {

  private static final long serialVersionUID = 2835566367775246960L;

  @EmbeddedId
  private PK pk;

  private Byte escolaridade;

  private Short altura;

  @Column(name = "TIPO_FISICO")
  private Short tipoFisico;

  @Column(name = "EXCLUIR_ANALFABETO")
  private String excluirAnalfabeto;

  @Column(name = "EXCLUIR_UNIVERSITARIO")
  private String excluirUniversitario;

  @Column(name = "MAJ_MENOR_150")
  private Short majMenor150;

  @Column(name = "MAJ_MAIOR_150")
  private Short majMaior150;

  @Column(name = "MAJ_B")
  private Short majB;

  @Temporal(TemporalType.DATE)
  @Column(name = "GPT_A_DATA")
  private Date gptAData;

  @Temporal(TemporalType.DATE)
  @Column(name = "GPT_B_DATA")
  private Date gptBData;

  public DstbParametro() {
    super();
  }

  @Override
  public int compareTo(DstbParametro o) {
    return this.getPk().compareTo(o.getPk());
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
    DstbParametro other = (DstbParametro) obj;
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

  public Byte getEscolaridade() {
    return this.escolaridade;
  }

  public void setEscolaridade(Byte escolaridade) {
    this.escolaridade = escolaridade;
  }

  public Short getAltura() {
    return this.altura;
  }

  public void setAltura(Short altura) {
    this.altura = altura;
  }

  public Short getTipoFisico() {
    return this.tipoFisico;
  }

  public void setTipoFisico(Short tipoFisico) {
    this.tipoFisico = tipoFisico;
  }

  public String getExcluirAnalfabeto() {
    return this.excluirAnalfabeto;
  }

  public void setExcluirAnalfabeto(String excluirAnalfabeto) {
    this.excluirAnalfabeto = excluirAnalfabeto;
  }

  public String getExcluirUniversitario() {
    return this.excluirUniversitario;
  }

  public void setExcluirUniversitario(String excluirUniversitario) {
    this.excluirUniversitario = excluirUniversitario;
  }

  public Short getMajMenor150() {
    return this.majMenor150;
  }

  public void setMajMenor150(Short majMenor150) {
    this.majMenor150 = majMenor150;
  }

  public Short getMajMaior150() {
    return this.majMaior150;
  }

  public void setMajMaior150(Short majMaior150) {
    this.majMaior150 = majMaior150;
  }

  public Short getMajB() {
    return this.majB;
  }

  public void setMajB(Short majB) {
    this.majB = majB;
  }

  public Date getGptAData() {
    return this.gptAData;
  }

  public void setGptAData(Date gptAData) {
    this.gptAData = gptAData;
  }

  public Date getGptBData() {
    return this.gptBData;
  }

  public void setGptBData(Date gptBData) {
    this.gptBData = gptBData;
  }

  /** Chave primária (PK) de DstbParametro.
   * @author Abreu Lopes
   * @since 4.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<DstbParametro.PK>, Serializable {
    
    private static final long serialVersionUID = -5351741405715798235L;

    @Column(name="RM_CODIGO")
    private Byte rmCodigo;

    @Column(name = "OM_TIPO")
    private Byte omTipo;

    public PK() {
      super();
    }
    
    public PK(final Byte rmCodigo, final Byte omTipo) {
      super();
      this.setRmCodigo(rmCodigo);
      this.setOmTipo(omTipo);
    }
    
    @Override
    public String toString() {
      return new StringBuilder(this.getRmCodigo() == null ? "RM" : this.getRmCodigo().toString())
                 .append("ª RM - OM Tipo ")
                 .append(this.getOmTipo() == null ? "TIPO" : this.getOmTipo())
                 .toString();
    }

    @Override
    public int compareTo(PK o) {
      int status = this.getRmCodigo().compareTo(o.getRmCodigo());
      if (status == 0) {
        status = this.getOmTipo().compareTo(o.getOmTipo());
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.omTipo == null) ? 0 : this.omTipo.hashCode());
      result = prime * result + ((this.rmCodigo == null) ? 0 : this.rmCodigo.hashCode());
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
      if (this.omTipo == null) {
        if (other.omTipo != null)
          return false;
      } else if (!this.omTipo.equals(other.omTipo))
        return false;
      if (this.rmCodigo == null) {
        if (other.rmCodigo != null)
          return false;
      } else if (!this.rmCodigo.equals(other.rmCodigo))
        return false;
      return true;
    }

    public Byte getRmCodigo() {
      return this.rmCodigo;
    }

    public void setRmCodigo(Byte rmCodigo) {
      this.rmCodigo = rmCodigo;
    }

    public Byte getOmTipo() {
      return this.omTipo;
    }

    public void setOmTipo(Byte omTipo) {
      this.omTipo = omTipo;
    }
    
  }
  
}
