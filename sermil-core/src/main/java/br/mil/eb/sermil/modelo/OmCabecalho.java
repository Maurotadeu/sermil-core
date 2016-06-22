package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Informações de Organização Militar (OM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4.2
 */
@Entity
@Cache(type=CacheType.SOFT, size=1000, expiry=360000)
@Table(name="OM_CABECALHO")
@NamedQueries({
  @NamedQuery(name = "Cabecalho.listarPorOm", query = "SELECT c FROM OmCabecalho c where c.om.codigo = ?1"),
  @NamedQuery(name = "Cabecalho.listar", query = "SELECT DISTINCT c.om FROM OmCabecalho c")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class OmCabecalho implements Serializable {

  private static final long serialVersionUID = 6379245964099719225L;

  @OneToOne
  @Id
  @JoinColumn(name="OM_CODIGO", referencedColumnName="CODIGO")  
  private Om om;

  private String csm;

  private String email;

  private String telefone;

  private String ministerio;

  @Column(name = "FORCA_ARMADA")
  private String forcaArmada;

  @Column(name = "CMDO_MIL")
  private String cmdoMil;

  private String rm;

  @Column(name = "OM_DESCRICAO")
  private String omDescricao;

  @Column(name = "DESCRICAO_HISTORICA")
  private String descricaoHistorica;

  @Column(name = "NOME_AUTORIDADE")
  private String nomeAutoridade;

  private String funcao;

  private String sitio;

  @Lob
  @Column(name="ASS_DIGITAL")
  private byte[] assDigital;

  public OmCabecalho(){
    super();
  }

  public OmCabecalho(Om om){
    this.om = om;
    this.setOmDescricao("DESCRICAO DA OM");
    this.setRm("REGIAO MILITAR");
    this.setCmdoMil("CMDO MIL AREA");
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getOm() == null ? "CODOM" : this.getOm().getCodigo().toString())
        .append(" - ")
        .append(this.getOmDescricao() == null ? "OM CABECALHO" : this.getOmDescricao())
        .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((om == null) ? 0 : om.hashCode());
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
    OmCabecalho other = (OmCabecalho) obj;
    if (om == null) {
      if (other.om != null)
        return false;
    } else if (!om.equals(other.om))
      return false;
    return true;
  }

  public String getCsm() {
    return csm;
  }

  public void setCsm(String csm) {
    this.csm = csm;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getMinisterio() {
    return ministerio;
  }

  public void setMinisterio(String ministerio) {
    this.ministerio = ministerio;
  }

  public String getForcaArmada() {
    return forcaArmada;
  }

  public void setForcaArmada(String forcaArmada) {
    this.forcaArmada = forcaArmada;
  }

  public String getCmdoMil() {
    return cmdoMil;
  }

  public void setCmdoMil(String cmdoMil) {
    this.cmdoMil = cmdoMil;
  }

  public String getRm() {
    return rm;
  }

  public void setRm(String rm) {
    this.rm = rm;
  }

  public String getOmDescricao() {
    return omDescricao;
  }

  public void setOmDescricao(String omDescricao) {
    this.omDescricao = omDescricao;
  }

  public String getDescricaoHistorica() {
    return descricaoHistorica;
  }

  public void setDescricaoHistorica(String descricaoHistorica) {
    this.descricaoHistorica = descricaoHistorica;
  }

  public String getNomeAutoridade() {
    return nomeAutoridade;
  }

  public void setNomeAutoridade(String nomeAutoridade) {
    this.nomeAutoridade = nomeAutoridade;
  }

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  public String getSitio() {
    return sitio;
  }

  public void setSitio(String sitio) {
    this.sitio = sitio;
  }

  public byte[] getAssDigital() {
    return assDigital;
  }

  public void setAssDigital(byte[] assDigital) {
    this.assDigital = assDigital;
  }

  public Om getOm() {
    return om;
  }

  public void setOm(Om om) {
    this.om = om;
  }

}
