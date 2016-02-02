package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Organiza��o Militar (OM).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Om.java 2481 2014-06-18 15:35:45Z wlopes $
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Om.listarMob", query = "SELECT o FROM Om o WHERE o.rm.codigo = ?1 AND o.omTipo = 9 ORDER BY o.sigla"),
  @NamedQuery(name = "Om.listarPorRm", query = "SELECT o FROM Om o WHERE o.rm.codigo = ?1 AND o.omTipo BETWEEN 1 AND 9 ORDER BY o.sigla"),
  @NamedQuery(name = "Om.listarPorCriterio", query = "SELECT o FROM Om o WHERE o.omTipo BETWEEN 1 AND 9 AND o.sigla LIKE CONCAT(?1,'%') AND o.municipio.descricao LIKE CONCAT(?2,'%') ORDER BY o.sigla"),
  @NamedQuery(name = "Om.listarPorDescricao", query = "SELECT o FROM Om o WHERE o.omTipo BETWEEN 1 AND 9 AND o.descricao LIKE CONCAT(?1,'%')")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Om implements Comparable<Om>, Serializable {

  private static final long serialVersionUID = 270615520257544542L;

  @Id
  private Integer codigo;

  private String descricao;

  private String endereco;

  private String bairro;

  private String cep;

  @Column(name = "CAIXA_POSTAL")
  private Integer caixaPostal;

  @ManyToOne
  @JoinColumn(name="MUNICIPIO_CODIGO", referencedColumnName="CODIGO")
  private Municipio municipio;

  @ManyToOne
  @JoinColumn(name="RM_CODIGO", referencedColumnName="CODIGO")
  private Rm rm;

  @OneToOne(mappedBy="om", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
  private OmCabecalho omCabecalho;

  @Column(name = "OM_TIPO")
  private Byte omTipo;

  private Byte prioridade;

  @Column(name = "QE_TIPO")
  private Byte qeTipo;

  @Column(name = "MOB_RESP")
  private Integer mobResp;

  private String sigla;

  public Om() {
  }

  @Override
  public int compareTo(Om o) {
    return this.getSigla().compareTo(o.getSigla());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.codigo == null) ? 0 : this.codigo.hashCode());
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
    Om other = (Om) obj;
    if (this.codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!this.codigo.equals(other.codigo))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODOM" : this.getCodigo().toString())
      .append(" - ")
      .append(this.getSigla() == null ? "SIGLA" : this.getSigla())
      .toString();
  }

  public String getBairro() {
    return this.bairro;
  }

  public Integer getCaixaPostal() {
    return this.caixaPostal;
  }

  public String getCep() {
    return this.cep;
  }

  public Integer getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public Integer getMobResp() {
    return this.mobResp;
  }

  public Municipio getMunicipio() {
    return this.municipio;
  }

  public Byte getOmTipo() {
    return this.omTipo;
  }

  public Byte getPrioridade() {
    return this.prioridade;
  }

  public Byte getQeTipo() {
    return this.qeTipo;
  }

  public Rm getRm() {
    return this.rm;
  }

  public String getSigla() {
    return this.sigla;
  }

  public Om(final Integer codigo) {
    this.setCodigo(codigo);
  }

  public void setBairro(String bairro) {
    this.bairro = (bairro == null || bairro.trim().isEmpty() ? null : bairro.trim().toUpperCase());
  }

  public void setCaixaPostal(Integer caixaPostal) {
    this.caixaPostal = caixaPostal;
  }

  public void setCep(String cep) {
    this.cep = (cep == null || cep.trim().isEmpty() ? null : cep.trim());
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public void setDescricao(String descricao) {
    this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : descricao.trim().toUpperCase());
  }

  public void setEndereco(String endereco) {
    this.endereco = (endereco == null || endereco.trim().isEmpty() ? null : endereco.trim().toUpperCase());
  }

  public void setMobResp(Integer mobResp) {
    this.mobResp = mobResp;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public void setOmTipo(Byte omTipo) {
    this.omTipo = omTipo;
  }

  public void setPrioridade(Byte prioridade) {
    this.prioridade = prioridade;
  }

  public void setQeTipo(Byte qeTipo) {
    this.qeTipo = qeTipo;
  }

  public void setRm(Rm rm) {
    this.rm = rm;
  }

  public void setSigla(String sigla) {
    this.sigla = (sigla == null || sigla.trim().isEmpty() ? null : sigla.trim().toUpperCase());
  }

  public OmCabecalho getOmCabecalho() {
    return omCabecalho;
  }

  public void setOmCabecalho(OmCabecalho omCabecalho) {
    this.omCabecalho = omCabecalho;
  }

}