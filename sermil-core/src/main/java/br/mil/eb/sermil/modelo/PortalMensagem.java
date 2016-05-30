package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Mensagem do Portal.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "PORTAL_MENSAGEM")
public final class PortalMensagem implements Comparable<PortalMensagem>, Serializable {

  private static final long serialVersionUID = -5037900885209477967L;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="PORTAL_MENSAGEM")
  @TableGenerator(name="PORTAL_MENSAGEM", allocationSize=1)
  private Integer codigo;

  private String titulo;

  private String icone;

  private String texto;

  @ManyToOne
  private Usuario usuario;

  @Temporal(TemporalType.TIMESTAMP)
  private Date data;

  @OneToMany(mappedBy="portalMensagem", fetch=FetchType.EAGER, orphanRemoval=true)
  private List<PortalMensagemPerfil> portalMensagemPerfilCollection;

  public PortalMensagem() {
    super();
  }

  @Override
  public String toString() {
    return this.getCodigo() == null ? "CODIGO" : new StringBuilder(this.getCodigo().toString()).append(" - ").append(this.getTitulo()).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
    PortalMensagem other = (PortalMensagem) obj;
    if (this.codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!this.codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Integer getCodigo() {
    return this.codigo;
  }

  public Date getData() {
    return this.data;
  }

  public String getTexto() {
    return this.texto;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public String getIcone() {
    return this.icone;
  }

  public Usuario getUsuario() {
    return this.usuario;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setIcone(String icone) {
    this.icone = icone;
  }

  public void setUsuario(Usuario usr) {
    this.usuario = usr;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public List<PortalMensagemPerfil> getPortalMensagemPerfilCollection() {
    return portalMensagemPerfilCollection;
  }

  public void setPortalMensagemPerfilCollection(List<PortalMensagemPerfil> portalMensagemPerfilCollection) {
    this.portalMensagemPerfilCollection = portalMensagemPerfilCollection;
  }

  @Override
  public int compareTo(PortalMensagem o) {
    // Ordenando msg mais nova para mais antiga
    return  -1 * this.getData().compareTo(o.getData());
  }

}
