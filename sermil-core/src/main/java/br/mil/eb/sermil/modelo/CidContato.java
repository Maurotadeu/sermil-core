package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Informações de pessoas de contatos do cidadão.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidContato.java 2426 2014-05-14 15:01:41Z wlopes $
 */
@Entity
@Table(name = "CID_CONTATO")
public final class CidContato implements Comparable<CidContato>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3052283970947553631L;

  @EmbeddedId
  private CidContato.PK pk;

  private String bairro;

  private String cep;

  private String endereco;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name = "MUNICIPIO_CODIGO")
  private Municipio municipio;

  @Column(name = "NOME_CONTATO")
  private String nomeContato;

  private Byte parentesco;

  private String telefone;

  public CidContato() {
  }

  public CidContato(final Long ra, final Byte prioridade) {
    this.setPk(new CidContato.PK(ra, prioridade));
  }

  @Override
  public int compareTo(CidContato o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().getPrioridade() == null ? "PRIO" : this.getPk().getPrioridade().toString())
      .append(" - ")
      .append(this.getNomeContato() == null ? "NOME" : this.getNomeContato())
      .append(": ")
      .append(this.getEndereco() == null ? "ENDEREÇO" : this.getEndereco())
      .append(", ")
      .append(this.getBairro() == null ? "BAIRRO" : this.getBairro())
      .append(", ")
      .append(this.getCep() == null ? "CEP" : this.getCep())
      .append(", ")
      .append(this.getMunicipio() == null ? "MUNICIPIO" : this.getMunicipio())
      .append(", TEL: ")
      .append(this.getTelefone() == null ? "TELEFONE" : this.getTelefone())
      .toString();
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
    CidContato other = (CidContato) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public String getBairro() {
    return this.bairro;
  }

  public String getCep() {
    return this.cep;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public Municipio getMunicipio() {
    return this.municipio;
  }

  public String getNomeContato() {
    return this.nomeContato;
  }

  public Byte getParentesco() {
    return this.parentesco;
  }

  public CidContato.PK getPk() {
    return this.pk;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidContatoCollection().contains(this)) {
      cid.getCidContatoCollection().add(this);
    }
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public void setNomeContato(String nomeContato) {
    this.nomeContato = nomeContato;
  }

  public void setParentesco(Byte parentesco) {
    this.parentesco = parentesco;
  }

  public void setPk(CidContato.PK pk) {
    this.pk = pk;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  /** Chave primária (PK) de CidContato.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidContato.java 2426 2014-05-14 15:01:41Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidContato.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -4872493189199867881L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name = "PRIORIDADE")
    private Byte prioridade;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Byte prioridade) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setPrioridade(prioridade);
    }

    @Override
    public int compareTo(CidContato.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getPrioridade().compareTo(o.getPrioridade());
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result
          + ((this.prioridade == null) ? 0 : this.prioridade.hashCode());
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
      if (this.prioridade == null) {
        if (other.prioridade != null)
          return false;
      } else if (!this.prioridade.equals(other.prioridade))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Byte getPrioridade() {
      return this.prioridade;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setPrioridade(Byte prioridade) {
      this.prioridade = prioridade;
    }

  }

}
