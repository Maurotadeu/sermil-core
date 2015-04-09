package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** Foto do cidadão.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidFoto.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "CID_FOTO")
public final class CidFoto implements Serializable {

  private static final long serialVersionUID = 6609814755675487400L;

  @Id
  @Column(name = "CIDADAO_RA")
  private Long cidadaoRa;

  @Lob
  @Column(name = "FOTO_IMG")
  private byte[] fotoImg;

  @OneToOne
  @JoinColumn(name="CIDADAO_RA", insertable=false, updatable=false)
  private Cidadao cidadao;

  public CidFoto() {
    super();
  }

  public Long getCidadaoRa() {
    return this.cidadaoRa;
  }

  public byte[] getFotoImg() {
    return this.fotoImg;
  }

  public void setCidadaoRa(Long cidadaoRa) {
    this.cidadaoRa = cidadaoRa;
  }

  public void setFotoImg(byte[] fotoImg) {
    this.fotoImg = fotoImg;
  }

  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

}
