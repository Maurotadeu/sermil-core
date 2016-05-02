package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Delegacia de Serviço Militar.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Del.listarPorCsm", query = "SELECT d FROM Delegacia d WHERE d.pk.csmCodigo = ?1"),
  @NamedQuery(name = "Del.listarPorMun", query = "SELECT d FROM Delegacia d WHERE d.om.municipio.codigo = ?1")
})
@PrimaryKey(validation=IdValidation.NULL)
public class Delegacia implements Serializable {

  private static final long serialVersionUID = 6148473059170164840L;

  @EmbeddedId
  private PK pk;

  private String email;

  private String telefone;

  private Float latitude;
  
  private Float longitude;
  
  @OneToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "DELEGADO", referencedColumnName="CPF")
  private Usuario delegado;

  @OneToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name="OM_CODIGO", referencedColumnName="CODIGO")
  private Om om;

  public Delegacia() {
    this.setPk(new Delegacia.PK());
  }
  
  public Delegacia(final Byte csm, final Byte del) {
    this();
    this.getPk().setCodigo(del);
    this.getPk().setCsmCodigo(csm);
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString())
      .append(" - ")
      .append(this.getOm() == null ? "DELEGACIA" : this.getOm().getDescricao())
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
    Delegacia other = (Delegacia) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public Delegacia.PK getPk() {
    return this.pk;
  }

  public void setPk(Delegacia.PK id) {
    this.pk = id;
  }

  public Usuario getDelegado() {
    return delegado;
  }

  public void setDelegado(Usuario delegado) {
    this.delegado = delegado;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Om getOm() {
    return this.om;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }
  
  public Float getLatitude() {
     return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
     return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }
  
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID.*/
    private static final long serialVersionUID = 6501995752848718479L;

    @Column(name="CSM_CODIGO")
    private Byte csmCodigo;

    private Byte codigo;

    public PK() {
    }
    
    public PK(final Byte csmCodigo, final Byte codigo) {
      this.csmCodigo = csmCodigo;
      this.codigo = codigo;
    }
    
    @Override
    public String toString() {
      return new StringBuilder()
        .append(getCsmCodigo())
        .append("/")
        .append(getCodigo())
        .toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
      result = prime * result
          + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
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
      if (codigo == null) {
        if (other.codigo != null)
          return false;
      } else if (!codigo.equals(other.codigo))
        return false;
      if (csmCodigo == null) {
        if (other.csmCodigo != null)
          return false;
      } else if (!csmCodigo.equals(other.csmCodigo))
        return false;
      return true;
    }

    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

    public Byte getCodigo() {
      return this.codigo;
    }

    public void setCodigo(Byte codigo) {
      this.codigo = codigo;
    }

  }

}
