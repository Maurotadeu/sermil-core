package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Documento de entrada de informação do cidadão.
 * @author Abreu Lopes
 * @since 2.0
 * @version $Id: CidDocumento.java 2423 2014-05-13 17:00:54Z wlopes $
 */
@Entity
@Table(name = "CID_DOCUMENTO")
public final class CidDocumento implements Comparable<CidDocumento>, Serializable {

  private static final long serialVersionUID = 3262879606032007532L;

  @EmbeddedId
  private CidDocumento.PK pk;

  private Byte documento;

  private String servico;

  private Short tarefa;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false)
  private Cidadao cidadao;

  public CidDocumento() {
    this.setPk(new CidDocumento.PK());
  }

  public CidDocumento(final Long ra, final Date data, final Byte tipo ) {
    this.setPk(new CidDocumento.PK(ra, data, tipo));
  }

  @Override
  public int compareTo(CidDocumento o) {
    return this.getPk().compareTo(o.getPk());
  }
  
  @Override
  public String toString() {
    return new StringBuilder("Tipo: ")
      .append(this.getPk().getTipo())
      .append(" Data: ")
      .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getPk().getData()))
      .append(" Svr: ")
      .append(this.getServico() == null ? "00000000000" : this.getServico())
      .append(" Tar: ")
      .append(this.getTarefa() == null ? "000" : this.getTarefa())
      .append(" Doc: ")
      .append(this.getDocumento() == null ? "00" : this.getDocumento())
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
    CidDocumento other = (CidDocumento) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Cidadao getCidadao() {
    return this.cidadao;
  }

  public Byte getDocumento() {
    return this.documento;
  }

  public CidDocumento.PK getPk() {
    return this.pk;
  }

  public String getServico() {
    return this.servico;
  }

  public Short getTarefa() {
    return this.tarefa;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidDocumentoCollection().contains(this)) {
      cid.getCidDocumentoCollection().add(this);
    }
  }

  public void setDocumento(Byte documento) {
    this.documento = documento;
  }

  public void setPk(CidDocumento.PK pk) {
    this.pk = pk;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public void setTarefa(Short tarefa) {
    this.tarefa = tarefa;
  }

  /** Chave primária (PK) de CidDocumento.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: CidDocumento.java 2423 2014-05-13 17:00:54Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidDocumento.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 4482404487121916555L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA")
    private Date data;

    @Column(name = "TIPO")
    private Byte tipo;

    public PK() {
      super();
    }
    
    public PK(final Long cidadaoRa, final Date data, final Byte tipo) {
      super();
      this.setCidadaoRa(cidadaoRa);
      this.setData(data);
      this.setTipo(tipo);
    }

    @Override
    public int compareTo(CidDocumento.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0 ) {
        status = this.getData().compareTo(o.getData());
        if (status == 0) {
          status = this.getTipo().compareTo(o.getTipo());
        }
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
          + ((this.data == null) ? 0 : this.data.hashCode());
      result = prime * result
          + ((this.tipo == null) ? 0 : this.tipo.hashCode());
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
      if (this.tipo == null) {
        if (other.tipo != null)
          return false;
      } else if (!this.tipo.equals(other.tipo))
        return false;
      return true;
    }

    public Long getCidadaoRa() {
      return this.cidadaoRa;
    }

    public Date getData() {
      return this.data;
    }

    public Byte getTipo() {
      return this.tipo;
    }

    public void setCidadaoRa(Long cidadaoRa) {
      this.cidadaoRa = cidadaoRa;
    }

    public void setData(Date data) {
      this.data = data;
    }

    public void setTipo(Byte tipo) {
      this.tipo = tipo;
    }

  }

}
