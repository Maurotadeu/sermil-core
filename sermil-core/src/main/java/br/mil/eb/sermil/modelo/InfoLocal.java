package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** InfoLocal.
 * @author Abreu Lopes
 * @since 3.5
 * @version $Id: InfoLocal.java 1929 2012-05-24 18:16:36Z wlopes $
 */
@Entity
@Table(name="INFO_LOCAL")
@NamedQuery(name = "InfoLocal.listar", query = "SELECT o FROM InfoLocal o ORDER BY o.rm")
public final class InfoLocal implements Serializable {

  private static final long serialVersionUID = -4363058900318285929L;

  @Id
  private Byte rm;

  private Byte cta;

  @Column(name = "SEL_STATUS")
  private Byte selStatus;
  
  @Column(name = "DISTR_STATUS")
  private Byte distrStatus;

  @Column(name="ALTERACAO_DATA", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date alteracaoData;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "USUARIO_CPF", nullable = false)
  private Usuario usuario;

  public InfoLocal() {
    super();
  }

  public Byte getRm() {
    return this.rm;
  }

  public void setRm(Byte rm) {
    this.rm = rm;
  }

  public Byte getCta() {
    return this.cta;
  }

  public void setCta(Byte cta) {
    this.cta = cta;
  }

  public Byte getSelStatus() {
    return this.selStatus;
  }

  public void setSelStatus(Byte selStatus) {
    this.selStatus = selStatus;
  }

  public Byte getDistrStatus() {
    return this.distrStatus;
  }

  public void setDistrStatus(Byte distrStatus) {
    this.distrStatus = distrStatus;
  }

  public Date getAlteracaoData() {
    return alteracaoData;
  }

  public void setAlteracaoData(Date alteracaoData) {
    this.alteracaoData = alteracaoData;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  
}
