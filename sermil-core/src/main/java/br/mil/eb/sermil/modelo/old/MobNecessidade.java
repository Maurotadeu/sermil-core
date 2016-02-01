package br.mil.eb.sermil.modelo.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/** Necessidade existente no QC da Sec Mob.
 * @author Abreu Lopes, Neckel
 * @since 3.5
 * @version $Id: MobNecessidade.java 984 2008-09-23 11:13:52Z wlopes  - Neckel$
 */
@Entity
@Table(name = "MOB_NECESSIDADE")
@Deprecated
public final class MobNecessidade implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** NOP = Necessidade de Oficiais e Praças. */
  private static final String NOP = "NOP";

  /** 03 = Arquivo do módulo SecMob. */
  private static final String ASM = "03";

  @EmbeddedId
  private MobNecessidade.PK pk;

  private Long secMob;

  @Column(name="POSTO_CODIGO")
  private String postoCodigo;

  @Column(name="QM_CODIGO")
  private String qmCodigo;

  @Column(name="HABILITACAO_1_CODIGO")
  private String habilitacao1Codigo;

  @Column(name="HABILITACAO_2_CODIGO")
  private String habilitacao2Codigo;

  private Integer quantidade;

  public MobNecessidade() {
    this.setPk(new PK());
  }

  public MobNecessidade.PK getPk() {
    return this.pk;
  }

  public void setPk(MobNecessidade.PK pk) {
    this.pk = pk;
  }

  public long getSecMob() {
    return this.secMob;
  }

  public void setSecMob(long secMob) {
    this.secMob = secMob;
  }

  public String getPostoCodigo() {
    return this.postoCodigo;
  }

  public void setPostoCodigo(String postoCodigo) {
    this.postoCodigo = postoCodigo;
  }

  public String getQmCodigo() {
    return this.qmCodigo;
  }

  public void setQmCodigo(String qmCodigo) {
    this.qmCodigo = qmCodigo;
  }

  public String getHabilitacao1Codigo() {
    return this.habilitacao1Codigo;
  }

  public void setHabilitacao1Codigo(String habilitacao1Codigo) {
    this.habilitacao1Codigo = habilitacao1Codigo;
  }

  public String getHabilitacao2Codigo() {
    return this.habilitacao2Codigo;
  }

  public void setHabilitacao2Codigo(String habilitacao2Codigo) {
    this.habilitacao2Codigo = habilitacao2Codigo;
  }

  public Integer getQuantidade() {
    return this.quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public void trim(String linha) {
    this.setSecMob(Long.parseLong(linha.substring(0, 9)));
    this.getPk().setOmCodigo(Long.parseLong(linha.substring(9, 18)));
    this.getPk().setFracaoId(linha.substring(18, 33));
    this.setPostoCodigo(linha.substring(33, 35));
    this.setQmCodigo(linha.substring(35, 39));
    this.setHabilitacao1Codigo(linha.substring(39, 42));
    this.setHabilitacao2Codigo(linha.substring(42, 45));
    this.setQuantidade(Integer.valueOf(linha.substring(45, 48))); 
  }

  public boolean arquivoValido(String cabecalho) {
    boolean status = false;
    if (ASM.equals(cabecalho.substring(2, 4)) || NOP.equals(cabecalho.substring(32, 35))) {
      status = true;
    }
    return status;
  }

  /** Chave primária (PK) de MobNecessidade.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: MobNecessidade.java 2441 2014-05-29 17:16:31Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 5091805363313907063L;

    @Column(name="OM_CODIGO")
    private long omCodigo;

    @Column(name="FRACAO_ID")
    private String fracaoId;

    public PK() {
      super();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((fracaoId == null) ? 0 : fracaoId.hashCode());
      result = prime * result + (int) (omCodigo ^ (omCodigo >>> 32));
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
      if (fracaoId == null) {
        if (other.fracaoId != null)
          return false;
      } else if (!fracaoId.equals(other.fracaoId))
        return false;
      if (omCodigo != other.omCodigo)
        return false;
      return true;
    }

    public long getOmCodigo() {
      return omCodigo;
    }

    public void setOmCodigo(long omCodigo) {
      this.omCodigo = omCodigo;
    }

    public String getFracaoId() {
      return fracaoId;
    }

    public void setFracaoId(String fracaoId) {
      this.fracaoId = fracaoId;
    }

  }

}
