package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** OM Boletim de Incorporação Cidadão (Tabela OM_BOLETIM_CIDADAO).
 * @author Abreu Lopes, gardino
 * @since 4.6
 * @version 5.4
 */
@Entity
@Table(name="OM_BOLETIM_CIDADAO")
@PrimaryKey(validation=IdValidation.NULL)
public final class OmBoletimCidadao implements Comparable<OmBoletimCidadao>, Serializable {

  private static final long serialVersionUID = -8866204469565305785L;

  @EmbeddedId
  private OmBoletimCidadao.PK pk;

  private Integer ano;

  @Column(name="CIDADAO_NOME")
  private String cidadaoNome;

  @Column(name="OM_CODIGO")
  private Integer omCodigo;

  @Column(name="BOLETIM_COD")
  private Integer boletimCod;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name="ANO", referencedColumnName="ANO",insertable = false, updatable = false, nullable = false),
    @JoinColumn(name="BOLETIM_COD", referencedColumnName="CODIGO",insertable = false, updatable = false, nullable = false),
    @JoinColumn(name="OM_CODIGO", referencedColumnName="OM_CODIGO",insertable = false, updatable = false, nullable = false),
    @JoinColumn(name="GPT_INCORP", referencedColumnName="GPT_INCORP",insertable = false, updatable = false, nullable = false),
  })
  private OmBoletim omBoletim;

  public OmBoletimCidadao() {
    this.setPk(new OmBoletimCidadao.PK());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.pk == null) ? 0 : this.pk.hashCode());
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
    OmBoletimCidadao other = (OmBoletimCidadao) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }
  
  public OmBoletimCidadao.PK getPk() {
    return this.pk;
  }

  public void setPk(OmBoletimCidadao.PK pk) {
    this.pk = pk;
  }

  public Integer getBoletimCod() {
    return boletimCod;
  }

  public void setBoletimCod(Integer boletimCod) {
    this.boletimCod = boletimCod;
  }

  public String getCidadaoNome() {
    return this.cidadaoNome;
  }

  public void setCidadaoNome(String cidadaoNome) {
    this.cidadaoNome = cidadaoNome;
  }


  public OmBoletim getOmBoletim() {
    return this.omBoletim;
  }

  public void setOmBoletim(OmBoletim omBoletim) {
    this.omBoletim = omBoletim;
  }

  public Integer getAno() {
    return ano;
  }

  public void setAno(Integer ano) {
    this.ano = ano;
  }

  public Integer getOmCodigo() {
    return omCodigo;
  }

  public void setOmCodigo(Integer omCodigo) {
    this.omCodigo = omCodigo;
  }

  @Override
  public int compareTo(OmBoletimCidadao o) {
    return this.getPk().compareTo(o.getPk());
  }

  /** Chave primária (PK) OmBoletimCidadao.
   * @author Abreu Lopes
   * @since 4.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<OmBoletimCidadao.PK>, Serializable {

    private static final long serialVersionUID = -694771974282602825L;

    @Column(name="CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name="GPT_INCORP")
    private String gptIncorp;

    public PK() {
      super();
    }

    public PK(final String gptIncorp,final Long cidadaoRa) {
      super();
      this.setGptIncorp(gptIncorp);
      this.setCidadaoRa(cidadaoRa);
    }

    public int compareTo(OmBoletimCidadao.PK o) {
      int status = this.getGptIncorp().compareTo(o.getGptIncorp());
      if (status == 0 ) {
        status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.gptIncorp == null) ? 0 : this.gptIncorp.hashCode());
      result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
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
      if (this.gptIncorp == null) {
        if (other.gptIncorp != null)
          return false;
      } else if (!this.gptIncorp.equals(other.gptIncorp))
        return false;
      if (this.cidadaoRa == null) {
        if (other.cidadaoRa != null)
          return false;
      } else if (!this.cidadaoRa.equals(other.cidadaoRa))
        return false;
      return true;
    }

    public String getGptIncorp() {
      return gptIncorp;
    }

    public void setGptIncorp(String gptIncorp) {
      this.gptIncorp = gptIncorp;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }
    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

  } 

}
