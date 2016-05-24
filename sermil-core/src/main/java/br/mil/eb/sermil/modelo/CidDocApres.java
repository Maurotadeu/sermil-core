package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.mil.eb.sermil.tipos.Utils;

/** Documento de apresentação no alistamento.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name="CID_DOC_APRES")
@NamedQuery(name = "CidDocApres.listarUnico", query = "SELECT c FROM CidDocApres c WHERE c.pk.cidadaoRa = :ra")
public final class CidDocApres implements Comparable<CidDocApres>, Serializable {

  private static final long serialVersionUID = -7912788647684371606L;

  @EmbeddedId
  private CidDocApres.PK pk;

  private Byte tipo;

  @Column(name="EMISSAO_DATA")
  @Temporal(TemporalType.DATE)
  private Date emissaoData;

  private String livro;

  private String folha;

  private String cartorio;

  @Column(name="MUNICIPIO_CODIGO")
  private Integer municipioCodigo;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  public CidDocApres() {
    this.setPk(new CidDocApres.PK());
  }

  public CidDocApres(final Long ra, final String numero) {
    this.setPk(new CidDocApres.PK(ra, numero));
  }

  @Override
  public int compareTo(CidDocApres o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getTipo() == null ? "TIPO" : this.getTipo().toString())
    .append(" - ")
    .append(this.getEmissaoData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getEmissaoData()))
    .append(" - ")
    .append(this.getPk().getNumero() == null ? "NR DOC APRES" : this.getPk().getNumero())
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
    CidDocApres other = (CidDocApres) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Byte getTipo() {
    return this.tipo;
  }

  public void setTipo(Byte tipo) {
    this.tipo = tipo;
  }

  public Date getEmissaoData() {
    return this.emissaoData;
  }

  public void setEmissaoData(Date emissaoData) {
    this.emissaoData = emissaoData;
  }

  public String getLivro() {
    return this.livro;
  }

  public void setLivro(String livro) {
    this.livro = livro;
  }

  public String getFolha() {
    return this.folha;
  }

  public void setFolha(String folha) {
    this.folha = folha;
  }

  public String getCartorio() {
    return this.cartorio;
  }

  public void setCartorio(String cartorio) {
    this.cartorio =  Utils.limpaAcento(cartorio);
  }

  public Integer getMunicipioCodigo() {
    return this.municipioCodigo;
  }

  public void setMunicipioCodigo(Integer municipioCodigo) {
    this.municipioCodigo = municipioCodigo;
  }

  public void setPk(CidDocApres.PK pk) {
    this.pk = pk;
  }

  public CidDocApres.PK getPk() {
    return pk;
  }

  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidDocApresColletion().contains(this)) {
      cid.getCidDocApresColletion().add(this);
    }
  }

  /** Chave primária (PK) de CidDocApres.
   * @author Abreu Lopes
   * @since 3.4
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<CidDocApres.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 3880540457788902526L;

    @Column(name="CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name="NUMERO")
    private String numero;

    public PK() {
      super();
    }

    public PK(final Long cidadaoRa, final String numero) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setNumero(numero);
    }

    public int compareTo(CidDocApres.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getNumero().compareTo(o.getNumero()); 

      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
      result = prime * result + ((this.numero == null) ? 0 : this.numero.hashCode());
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
      if (this.numero == null) {
        if (other.numero != null)
          return false;
      } else if (!this.numero.equals(other.numero))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public String getNumero() {
      return this.numero;
    }

    public void setNumero(String numero) {
      this.numero = numero;
    }

  }

}
