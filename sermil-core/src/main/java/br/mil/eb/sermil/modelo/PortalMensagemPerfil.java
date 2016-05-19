package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** Entidade PortalMensagemPerfil.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
 */
@Entity
@Table(name = "PORTAL_MENSAGEM_PERFIL")
@NamedQueries({
	@NamedQuery(name = "PortalMensagemPerfil.listarPorMensagemCodigo", query = "SELECT p FROM PortalMensagemPerfil p WHERE p.pk.mensagemCodigo = :mensagemCodigo"),
	@NamedQuery(name = "PortalMensagemPerfil.excluirPorMensagemCodigo", query = "DELETE FROM PortalMensagemPerfil p WHERE p.pk.mensagemCodigo = ?1")
})
public final class PortalMensagemPerfil implements Serializable {

  private static final long serialVersionUID = -315693799535151020L;

  @EmbeddedId
	private PortalMensagemPerfil.PK pk;

	@ManyToOne
	@JoinColumn(name = "MENSAGEM_CODIGO", nullable = false, insertable = false, updatable = false)
	private PortalMensagem portalMensagem;

	public PortalMensagem getPortalMensagem() {
		return portalMensagem;
	}

	public void setPortalMensagem(PortalMensagem portalMensagem) {
		this.portalMensagem = portalMensagem;
	}

	public PortalMensagemPerfil() {
		this.setPk(new PK());
	}

	public PortalMensagemPerfil(final Integer mensagemCodigo, final String mensagemPerfil) {
		this.setPk(new PK(mensagemCodigo, mensagemPerfil));
	}

	public PortalMensagemPerfil.PK getPk() {
		return this.pk;
	}

	public void setPk(PortalMensagemPerfil.PK pk) {
		this.pk = pk;
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
		PortalMensagemPerfil other = (PortalMensagemPerfil) obj;
		if (this.pk == null) {
			if (other.pk != null)
				return false;
		} else if (!this.pk.equals(other.pk))
			return false;
		return true;
	}

  /** Chave primária (PK) PortalMensagemPerfil.
   * @author Abreu Lopes
   * @since 4.0
   * @version 5.4
   */
	@Embeddable
	public static class PK implements Serializable {

    private static final long serialVersionUID = 4053086107772554739L;

    @Column(name="MENSAGEM_CODIGO")
		private Integer mensagemCodigo;

		@Column(name="PERFIL_CODIGO")
		private String perfilCodigo;

		public PK() {
			super();
		}

		public PK(final Integer mensagemCodigo, final String mensagemPerfil) {
			this.setMensagemCodigo(mensagemCodigo);
			this.setPerfilCodigo(mensagemPerfil);

		}

    @Override
    public String toString() {
      return new StringBuilder((this.getMensagemCodigo() == null ? "MENSAGEM" : this.getMensagemCodigo().toString()))
      .append(" - ")
      .append(this.getPerfilCodigo() == null ? "PERFIL" : this.getPerfilCodigo())
      .toString();
    }
		
		@Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
          + ((mensagemCodigo == null) ? 0 : mensagemCodigo.hashCode());
      result = prime * result
          + ((perfilCodigo == null) ? 0 : perfilCodigo.hashCode());
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
      if (mensagemCodigo == null) {
        if (other.mensagemCodigo != null)
          return false;
      } else if (!mensagemCodigo.equals(other.mensagemCodigo))
        return false;
      if (perfilCodigo == null) {
        if (other.perfilCodigo != null)
          return false;
      } else if (!perfilCodigo.equals(other.perfilCodigo))
        return false;
      return true;
    }

    public Integer getMensagemCodigo() {
			return mensagemCodigo;
		}

		public void setMensagemCodigo(Integer mensagemCodigo) {
			this.mensagemCodigo = mensagemCodigo;
		}

		public String getPerfilCodigo() {
			return perfilCodigo;
		}

		public void setPerfilCodigo(String perfilCodigo) {
			this.perfilCodigo = perfilCodigo;
		}

	}

}
