package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Perfil de usuário.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: UsuarioPerfil.java 1860 2012-05-09 20:00:11Z gardino $
 */
@Entity
@Table(name="USUARIO_PERFIL")
@NamedQueries({
  @NamedQuery(name="UsuarioPerfil.listarPorCpf", query="SELECT p FROM UsuarioPerfil p WHERE p.pk.cpf = :cpf"),
  @NamedQuery(name="UsuarioPerfil.excluirPorCpf", query="DELETE FROM UsuarioPerfil p WHERE p.pk.cpf = ?1")
})
public final class UsuarioPerfil implements Serializable {

  private static final long serialVersionUID = -7601872565016893323L;

  @EmbeddedId
  private UsuarioPerfil.PK pk;

  @ManyToOne
  @JoinColumn(name="CPF", nullable=false, insertable=false, updatable=false)
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name="PERFIL", nullable=false, insertable=false, updatable=false)
  private Perfil perfil;

  public UsuarioPerfil() {
    this.setPk(new PK());
  }

  public UsuarioPerfil(final String cpf, final String perfil) {
    this.setPk(new PK(cpf, perfil));
  }

  @Override
  public String toString() {
    return this.getPk().toString();
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
    UsuarioPerfil other = (UsuarioPerfil) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public UsuarioPerfil.PK getPk() {
    return this.pk;
  }

  public void setPk(UsuarioPerfil.PK pk) {
    this.pk = pk;
  }

  public Usuario getUsuario() {
    return this.usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Perfil getPerfil() {
    return perfil;
  }

  public void setPerfil(Perfil perfil) {
    this.perfil = perfil;
  }

  //public String getAuthority() {
  //  return this.getPk().getPerfil();
  //}

  /** Chave primária (PK) de UsuarioPerfil.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: UsuarioPerfil.java 1860 2012-05-09 20:00:11Z gardino $
   */
  @Embeddable
  public static class PK implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpf;

    private String perfil;

    public PK() {
      super();
    }

    public PK(final String cpf, final String perfil) {
      this.setCpf(cpf);
      this.setPerfil(perfil);
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getCpf() == null ? "CPF" : this.getCpf()).append(" - ").append(this.getPerfil() == null ? "PERFIL" : this.getPerfil()).toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
      result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
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
      if (cpf == null) {
        if (other.cpf != null)
          return false;
      } else if (!cpf.equals(other.cpf))
        return false;
      if (perfil == null) {
        if (other.perfil != null)
          return false;
      } else if (!perfil.equals(other.perfil))
        return false;
      return true;
    }

    public String getCpf() {
      return this.cpf;
    }

    public void setCpf(String cpf) {
      this.cpf = (cpf == null || cpf.trim().isEmpty() ? null : cpf.trim());
    }

    public String getPerfil() {
      return this.perfil;
    }

    public void setPerfil(String perfil) {
      this.perfil = (perfil == null || perfil.trim().isEmpty() ? null : perfil.trim());
    }

  }

}
