package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/** Sequência de RA.
 * @author Abreu Lopes
 * @since 3.3
 * @version $Id: RaMestre.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name="RA_MESTRE")
public final class RaMestre implements Serializable {
	
  private static final long serialVersionUID = 3538898577883823486L;

  @EmbeddedId
	private PK pk;

	private Integer sequencial;

	public RaMestre() {
		this.setPk(new RaMestre.PK());
	}

	public PK getPk() {
		return this.pk;
	}

	public void setPk(PK pk) {
		this.pk = pk;
	}

	public Integer getSequencial() {
		return this.sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}

  /** Chave primária (PK) de RaMestre.
   * @author Abreu Lopes
   * @since 3.3
   * @version $Id: RaMestre.java 1637 2011-11-25 13:52:11Z wlopes $
   */
	@Embeddable
	public static class PK implements Serializable {
		
		/** serialVersionUID. */
    private static final long serialVersionUID = 7686725811239638906L;

    @Column(name="CSM_CODIGO")
		private Byte csmCodigo;

		@Column(name="JSM_CODIGO")
		private Short jsmCodigo;

		public PK() {
			super();
		}

		public PK(final Byte csm, final Short jsm) {
			this.setCsmCodigo(csm);
			this.setJsmCodigo(jsm);
		}
		
		public Byte getCsmCodigo() {
			return this.csmCodigo;
		}

		public void setCsmCodigo(Byte csmCodigo) {
			this.csmCodigo = csmCodigo;
		}

		public Short getJsmCodigo() {
			return this.jsmCodigo;
		}

		public void setJsmCodigo(Short jsmCodigo) {
			this.jsmCodigo = jsmCodigo;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if ( ! (o instanceof PK)) {
				return false;
			}
			PK other = (PK) o;
			return (this.csmCodigo == other.csmCodigo)
				&& (this.jsmCodigo == other.jsmCodigo);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int hash = 17;
			hash = hash * prime + ((int) (this.csmCodigo ^ (this.csmCodigo >>> 32)));
			hash = hash * prime + ((int) (this.jsmCodigo ^ (this.jsmCodigo >>> 32)));
			return hash;
		}
	}
	
}
