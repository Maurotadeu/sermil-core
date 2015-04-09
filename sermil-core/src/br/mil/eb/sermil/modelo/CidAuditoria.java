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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Auditoria de Cidadão.
 * @author Abreu Lopes
 * @since 2.0
 * @version $Id: CidAuditoria.java 2423 2014-05-13 17:00:54Z wlopes $
 */
@Entity
@Table(name = "CID_AUDITORIA")
@NamedQueries({
@NamedQuery(name = "CidAuditoria.listarPorRa", query = "SELECT a FROM CidAuditoria a WHERE a.pk.cidadaoRa = ?1"),
@NamedQuery(name = "CidAuditoria.listarPorCpf", query = "SELECT a FROM CidAuditoria a WHERE a.pk.usuarioBd = ?1")
})
public final class CidAuditoria implements Comparable<CidAuditoria>, Serializable {

  private static final long serialVersionUID = 6604205609811127675L;

  @EmbeddedId
  private CidAuditoria.PK pk;

  private String descricao;

  private String ip;

  private String terminal;

  @Column(name = "USUARIO_SO")
  private String usuarioSo;

  @ManyToOne
  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
  private Cidadao cidadao;

  @ManyToOne
  @JoinColumn(name = "USUARIO_BD", insertable = false, updatable = false, nullable = false)
  private Usuario usuario;

  public CidAuditoria() {
    this.setPk(new CidAuditoria.PK());
  }

  public CidAuditoria(final Long ra, final Date data, final String descricao, final String ip, final String usuario) {
    this();
    this.getPk().setCidadaoRa(ra);
    this.getPk().setData(data);
    this.getPk().setUsuarioBd(usuario);
    this.setDescricao(descricao);
    this.setIp(ip);
  }

  @Override
  public int compareTo(CidAuditoria o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getUsuario() == null ? "USUARIO" : this.getUsuario().getCpf())
      .append(" - ")
      .append(this.getPk().getData() == null ? "DATA" : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(this.getPk().getData()))
      .append(" - ")
      .append(this.getDescricao() == null ? "DESCRICAO" : this.getDescricao())
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
    CidAuditoria other = (CidAuditoria) obj;
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

  public String getDescricao() {
    return this.descricao;
  }

  public String getIp() {
    return this.ip;
  }

  public CidAuditoria.PK getPk() {
    return this.pk;
  }

  public String getTerminal() {
    return this.terminal;
  }

  public String getUsuarioSo() {
    return this.usuarioSo;
  }

  public void setCidadao(Cidadao cid) {
    this.cidadao = cid;
    if (!cid.getCidAuditoriaCollection().contains(this)) {
      cid.getCidAuditoriaCollection().add(this);
    }
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setPk(CidAuditoria.PK pk) {
    this.pk = pk;
  }

  public void setTerminal(String terminal) {
    this.terminal = terminal;
  }

  public void setUsuarioSo(String usuarioSo) {
    this.usuarioSo = usuarioSo;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  /** Chave primária (PK) de CidAuditoria. 
   * @author Abreu Lopes
   * @since 2.0
   * @version $Id: CidAuditoria.java 2423 2014-05-13 17:00:54Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<CidAuditoria.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    @Column(name = "USUARIO_BD")
    private String usuarioBd;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    public PK() {
      super();
    }

    @Override
    public int compareTo(CidAuditoria.PK o) {
      int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
      if (status == 0) {
        status = this.getData().compareTo(o.getData());
        if (status == 0) {
          status = this.getUsuarioBd().compareTo(o.getUsuarioBd());
        }
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((cidadaoRa == null) ? 0 : cidadaoRa.hashCode());
      result = prime * result + ((data == null) ? 0 : data.hashCode());
      result = prime * result
          + ((usuarioBd == null) ? 0 : usuarioBd.hashCode());
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
      if (cidadaoRa == null) {
        if (other.cidadaoRa != null)
          return false;
      } else if (!cidadaoRa.equals(other.cidadaoRa))
        return false;
      if (data == null) {
        if (other.data != null)
          return false;
      } else if (!data.equals(other.data))
        return false;
      if (usuarioBd == null) {
        if (other.usuarioBd != null)
          return false;
      } else if (!usuarioBd.equals(other.usuarioBd))
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

    public String getUsuarioBd() {
      return usuarioBd;
    }

    public void setUsuarioBd(String usuarioBd) {
      this.usuarioBd = usuarioBd;
    }
    
  }

}
