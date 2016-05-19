package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Junta de Serviço Militar - Informações complementares.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.4
 */
@Entity
@Table(name = "JSM_INFO")
@PrimaryKey(validation = IdValidation.NULL)
public final class JsmInfo implements Serializable {

  private static final long serialVersionUID = -1212315790442679823L;

  @EmbeddedId
  private PK pk;

  @Column(insertable=false, updatable=false, nullable=false)
  private String internet;

  private String endereco;

  private String bairro;

  private String cep;

  private String telefone;

  private String email;

  private String autoridade;

  @OneToOne
  @JoinColumns({
    @JoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO", insertable=false, updatable=false),
    @JoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false)
  })
  private Jsm jsm;
    
  public JsmInfo() {
    this.setPk(new JsmInfo.PK());
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
    JsmInfo other = (JsmInfo) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.getPk().toString();
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String getInternet() {
    return this.internet;
  }

  public void setInternet(String internet) {
    this.internet = internet;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getBairro() {
    return this.bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCep() {
    return this.cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Jsm getJsm() {
    return this.jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }
  
  public String getAutoridade() {
    return autoridade;
  }

  public void setAutoridade(String autoridade) {
    this.autoridade = autoridade;
  }
  
  /** Chave primária (PK) de JsmInfo.
   * @author Abreu Lopes
   * @since 3.4
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = -537844181563261148L;

    @Column(name = "CSM_CODIGO")
    Byte csmCodigo;

    @Column(name = "JSM_CODIGO")
    Short jsmCodigo;

    public PK() {
      super();
    }

    public PK(final Byte csmCodigo, final Short jsmCodigo) {
      this.setJsmCodigo(jsmCodigo);
      this.setCsmCodigo(csmCodigo);
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
        .append("/")
        .append(this.getJsmCodigo() == null ? "000" : new DecimalFormat("000").format(this.getJsmCodigo()))
        .toString();
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((csmCodigo == null) ? 0 : csmCodigo.hashCode());
      result = prime * result + ((jsmCodigo == null) ? 0 : jsmCodigo.hashCode());
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
      if (csmCodigo == null) {
        if (other.csmCodigo != null)
          return false;
      } else if (!csmCodigo.equals(other.csmCodigo))
        return false;
      if (jsmCodigo == null) {
        if (other.jsmCodigo != null)
          return false;
      } else if (!jsmCodigo.equals(other.jsmCodigo))
        return false;
      return true;
    }

    public Short getJsmCodigo() {
      return this.jsmCodigo;
    }

    public Byte getCsmCodigo() {
      return this.csmCodigo;
    }

    public void setJsmCodigo(Short jsmCodigo) {
      this.jsmCodigo = jsmCodigo;
    }

    public void setCsmCodigo(Byte csmCodigo) {
      this.csmCodigo = csmCodigo;
    }

  }

}
