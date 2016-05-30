package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.tipos.Utils;

/** Registro de BCC/IAP.
 * @author Abreu Lopes, Gardino
 * @since 5.0
 * @version 5.4
 */
@Entity
@Table(name="SEL_BCC")
@PrimaryKey(validation=IdValidation.NULL)
public final class SelBcc implements Comparable<SelBcc>, Serializable {

  private static final long serialVersionUID = 8952096911373023444L;

  @EmbeddedId
  private PK pk;

  private String respostas;

  @Column(name="RM_CODIGO")
  private Integer rmCodigo;

  private String status;

  public SelBcc() {
    this.setPk(new SelBcc.PK());
  }

  @Override
  public int compareTo(SelBcc o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return this.getRespostas() == null ? "BCC NULA" : new StringBuilder("RA=").append(this.getPk().getRa()).append(" RM=").append(this.getRmCodigo()).append(" FL=").append(this.getPk().getFolha()).append(" RSP=").append(this.getRespostas()).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
    SelBcc other = (SelBcc) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  public void decode(final String linha) throws Exception {
    this.getPk().setFolha(Byte.valueOf(linha.substring(0,2).replaceAll(" ","")));
    this.setRmCodigo(Integer.valueOf(linha.substring(2,4).replaceAll(" ","")));
    this.getPk().setRa(Long.valueOf(Utils.limpaAcento(linha.toUpperCase().substring(4,16).replaceAll(" ",""))));
    this.setRespostas(Utils.limpaAcento(linha.substring(16,linha.length()-1)));
    this.setStatus("C");
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String getRespostas() {
    return this.respostas;
  }

  public void setRespostas(String respostas) {
    this.respostas = respostas;
  }

  public Integer getRmCodigo() {
    return this.rmCodigo;
  }

  public void setRmCodigo(Integer rmCodigo) {
    this.rmCodigo = rmCodigo;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /** Chave primária (PK) de SelBcc.
   * @author Abreu Lopes
   * @since 5.0
   * @version 5.4
   */
  @Embeddable
  public static class PK implements Comparable<SelBcc.PK>, Serializable {

    private static final long serialVersionUID = -6443077337580980246L;

    private Byte folha;

    private Long ra;

    public PK() {
      super();
    }

    public PK(final Byte folha, final Long ra) {
    }

    @Override
    public int compareTo(PK o) {
      return this.getRa().compareTo(o.getRa()) == 0 ? this.getFolha().compareTo(o.getFolha()) : this.getRa().compareTo(o.getRa());
    }

    @Override
    public String toString() {
      return new StringBuilder("FL ")
        .append(this.getFolha() == null ? "0" : this.getFolha())
        .append(" - RA ")
        .append(this.getRa() == null ? "000000000000" : String.format("%012d",this.getRa()))
        .toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((folha == null) ? 0 : folha.hashCode());
      result = prime * result + ((ra == null) ? 0 : ra.hashCode());
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
      if (folha == null) {
        if (other.folha != null)
          return false;
      } else if (!folha.equals(other.folha))
        return false;
      if (ra == null) {
        if (other.ra != null)
          return false;
      } else if (!ra.equals(other.ra))
        return false;
      return true;
    }

    public Byte getFolha() {
      return folha;
    }

    public void setFolha(byte folha) {
      this.folha = folha;
    }

    public Long getRa() {
      return ra;
    }

    public void setRa(Long ra) {
      this.ra = ra;
    }

  }

}
