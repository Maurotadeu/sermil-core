package br.mil.eb.sermil.modelo.old;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Mapa de situação de informações de Sec Mob.
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: MapaSitImpSecmob.java 2441 2014-05-29 17:16:31Z wlopes $
 */
@Entity
@Table(name = "MAPA_SIT_IMP_SECMOB")
@Deprecated
public final class MapaSitImpSecmob implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PK pk;

  private Long rm;

  private Long csm;

  private Long origem;

  private Long registros;

  public MapaSitImpSecmob() {
    this.setPk(new PK());
  }

  public Long getRm() {
    return this.rm;
  }

  public void cabecalho(String linha) {
    this.getPk().setCdOm(Long.valueOf(linha.substring(8, 17)));
    final Calendar cal = Calendar.getInstance();
    cal.set(Integer.parseInt((linha.substring(48, 52))),
            Integer.parseInt(linha.substring(45, 47)) - 1,
            Integer.parseInt(linha.substring(42, 44)));
    this.getPk().setData(cal.getTime());
    this.setRm(Long.valueOf(linha.substring(4, 6)));
    this.setCsm(Long.valueOf(linha.substring(6, 8)));
    this.setOrigem(Long.valueOf(linha.substring(2, 4)));
    this.setRegistros(Long.valueOf(linha.substring(34, 42)));
  }

  public void setRm(Long rm) {
    this.rm = rm;
  }

  public Long getCsm() {
    return this.csm;
  }

  public void setCsm(Long csm) {
    this.csm = csm;
  }

  public Long getOrigem() {
    return this.origem;
  }

  public void setOrigem(Long origem) {
    this.origem = origem;
  }

  public Long getRegistros() {
    return this.registros;
  }

  public void setRegistros(Long registros) {
    this.registros = registros;
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  /** Chave primária (PK) de MapaSitImpSecmob.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: MapaSitImpSecmob.java 2441 2014-05-29 17:16:31Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = 6140695282263931333L;

    @Column(name = "CD_OM")
    private Long cdOm;

    @Temporal(TemporalType.DATE)
    private Date data;

    public PK() {
      super();
    }
    
    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof PK)) {
        return false;
      }
      PK other = (PK) o;
      return (this.cdOm == other.cdOm) && this.data.equals(other.data);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + ((int) (this.cdOm ^ (this.cdOm >>> 32)));
      hash = hash * prime + this.data.hashCode();
      return hash;
    }

    public Long getCdOm() {
      return this.cdOm;
    }

    public void setCdOm(Long cdOm) {
      this.cdOm = cdOm;
    }

    public Date getData() {
      return this.data;
    }

    public void setData(Date data) {
      this.data = data;
    }

  }

}
