package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Registro de transferência de Região Militar.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidTransferido.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "CID_TRANSFERIDO")
@Deprecated
public final class CidTransferido implements Serializable {

  private static final long serialVersionUID = -4133855184397971291L;

  @EmbeddedId
  private CidTransferido.PK pk;

  private String bairro;

  private Integer cep;

  @Column(name = "CSM_CODIGO")
  private Byte csmCodigo;

  private String endereco;

  @Column(name = "JSM_CODIGO")
  private Short jsmCodigo;

  @Column(name = "MUNICIPIO_RESIDENCIA_CODIGO")
  private Integer municipioResidenciaCodigo;

  @Column(name = "OM_CODIGO")
  private Integer omCodigo;

  @Column(name = "PAIS_RESIDENCIA_CODIGO")
  private Short paisResidenciaCodigo;

  private String responsavel;

  @Column(name = "RM_CODIGO")
  private Byte rmCodigo;

  private String status;

  private Integer telefone;

  @Column(name = "ZONA_RESIDENCIAL")
  private Byte zonaResidencial;

  public CidTransferido() {
    this.setPk(new CidTransferido.PK());
  }

  public String toString() {
    return new StringBuilder(this.getRmCodigo() == null ? "RM" : this.getRmCodigo().toString())
    .append(" - ")
    .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
    .append(" - ")
    .append(this.getStatus() == null ? "STATUS" : this.getStatus())
    .toString();
  }

  public String getBairro() {
    return this.bairro;
  }

  public String getBanner() {
    return this.toString();
  }

  public Integer getCep() {
    return this.cep;
  }

  public Byte getCsmCodigo() {
    return this.csmCodigo;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public Short getJsmCodigo() {
    return this.jsmCodigo;
  }

  public Integer getMunicipioResidenciaCodigo() {
    return this.municipioResidenciaCodigo;
  }

  public Integer getOmCodigo() {
    return this.omCodigo;
  }

  public Short getPaisResidenciaCodigo() {
    return this.paisResidenciaCodigo;
  }

  public CidTransferido.PK getPk() {
    return this.pk;
  }

  public String getResponsavel() {
    return this.responsavel;
  }

  public Byte getRmCodigo() {
    return this.rmCodigo;
  }

  public String getStatus() {
    return this.status;
  }

  public Integer getTelefone() {
    return this.telefone;
  }

  public Byte getZonaResidencial() {
    return this.zonaResidencial;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public void setCep(Integer cep) {
    this.cep = cep;
  }

  public void setCsmCodigo(Byte csmCodigo) {
    this.csmCodigo = csmCodigo;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public void setJsmCodigo(Short jsmCodigo) {
    this.jsmCodigo = jsmCodigo;
  }

  public void setMunicipioResidenciaCodigo(Integer municipioResidenciaCodigo) {
    this.municipioResidenciaCodigo = municipioResidenciaCodigo;
  }

  public void setOmCodigo(Integer omCodigo) {
    this.omCodigo = omCodigo;
  }

  public void setPaisResidenciaCodigo(Short paisResidenciaCodigo) {
    this.paisResidenciaCodigo = paisResidenciaCodigo;
  }

  public void setPk(CidTransferido.PK pk) {
    this.pk = pk;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public void setRmCodigo(Byte rmCodigo) {
    this.rmCodigo = rmCodigo;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setTelefone(Integer telefone) {
    this.telefone = telefone;
  }

  public void setZonaResidencial(Byte zonaResidencial) {
    this.zonaResidencial = zonaResidencial;
  }

  /** Chave primária (PK) de CidTransferido.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidTransferido.java 1637 2011-11-25 13:52:11Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 7723927011624044138L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA")
    private Date data;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Date data) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setData(data);
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result
          + ((this.data == null) ? 0 : this.data.hashCode());
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
      if (this.cidadaoRa == null) {
        if (other.cidadaoRa != null)
          return false;
      } else if (!this.cidadaoRa.equals(other.cidadaoRa))
        return false;
      if (this.data == null) {
        if (other.data != null)
          return false;
      } else if (!this.data.equals(other.data))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setData(Date data) {
      this.data = data;
    }

  }

}
