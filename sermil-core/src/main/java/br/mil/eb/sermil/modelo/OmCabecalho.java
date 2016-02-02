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

/** Cabeçalho de relatório de OM.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: OmCabecalho.java 2421 2014-05-12 16:12:00Z wlopes $
 */
@Entity
@Table(name="OM_CABECALHO")
@NamedQueries({
  @NamedQuery(name = "Cabecalho.listarPorOm", query = "SELECT c FROM OmCabecalho c where c.om.codigo= ?1 " ),
  @NamedQuery(name = "Cabecalho.listar", query = "SELECT DISTINCT c.om FROM OmCabecalho c " )
})
public final class OmCabecalho implements Serializable {

  private static final long serialVersionUID = -455600186915817511L;

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
  
  @Override
  public String toString() {
    return new StringBuilder(this.getOm() == null ? "CODOM" : this.getOm().getCodigo().toString())
      .append(" - ")
      .append(this.getOmDescricao() == null ? "DESCRICAO" : this.getOmDescricao())
      .toString();
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
