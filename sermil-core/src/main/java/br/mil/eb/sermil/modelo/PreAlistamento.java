package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.mil.eb.sermil.tipos.Utils;

/** Alistamento Online.
 * @author Abreu Lopes
 * @since 3.4
 * @version 5.2.7
 */
@Entity
@Table(name="PRE_ALISTAMENTO")
@NamedQueries({
  @NamedQuery(name = "PreAlistamento.listarUnico", query = "SELECT p FROM PreAlistamento p WHERE p.nome = ?1 AND p.mae = ?2 AND p.nascimentoData = ?3"),
  @NamedQuery(name = "PreAlistamento.listarPorNome", query = "SELECT p FROM PreAlistamento p WHERE p.nome LIKE ?1"),
  @NamedQuery(name = "PreAlistamento.listarPorCpf", query = "SELECT p FROM PreAlistamento p WHERE p.cpf LIKE ?1")
})
public final class PreAlistamento implements Comparable<PreAlistamento>, Serializable {

  /** serialVersionUID.*/
  private static final long serialVersionUID = 3685757311148877664L;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="PRE_ALISTAMENTO")
  @TableGenerator(name="PRE_ALISTAMENTO", allocationSize=1)
  private Long codigo;

  private String nome;

  private String pai;

  private String mae;

  @Column(name="RG_NR")
  private String rgNr;

  @Column(name="RG_UF")
  private String rgUf;

  private String cpf;

  @Column(name="ESTADO_CIVIL")
  private Byte estadoCivil;

  private Byte escolaridade;

  @ManyToOne
  @JoinColumn(name="OCUPACAO_CODIGO", referencedColumnName="CODIGO")
  private Ocupacao ocupacao;

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumns({
    @JoinColumn(name="CSM_CODIGO", referencedColumnName="CSM_CODIGO"),
    @JoinColumn(name="JSM_CODIGO", referencedColumnName="CODIGO")
  })
  private Jsm jsm;

  @ManyToOne
  @JoinColumn(name="MUNICIPIO_NASCIMENTO_CODIGO", referencedColumnName="CODIGO")
  private Municipio municipioNascimento;

  @ManyToOne
  @JoinColumn(name="MUNICIPIO_RESIDENCIA_CODIGO", referencedColumnName="CODIGO")
  private Municipio municipioResidencia;

  @ManyToOne
  @JoinColumn(name="PAIS_NASCIMENTO_CODIGO", referencedColumnName="CODIGO")
  private Pais paisNascimento;

  @ManyToOne
  @JoinColumn(name="PAIS_RESIDENCIA_CODIGO", referencedColumnName="CODIGO")
  private Pais paisResidencia;

  @Column(name="ZONA_RESIDENCIAL")
  private Byte zonaResidencial;

  private String endereco;

  private String bairro;

  private String cep;

  private String email;

  private String telefone;

  @Column(name="DESEJA_SERVIR")
  private Byte desejaServir;

  @Column(name="PROTOCOLO_DATA")
  @Temporal(TemporalType.TIMESTAMP)
  private Date protocoloData;

  @Column(name="NASCIMENTO_DATA")
  @Temporal(TemporalType.DATE)
  private Date nascimentoData;

  @Column(name="PESSOA_FAMILIA_QTD")
  private Byte pessoaFamiliaQtd;

  @Column(name="RENDA_FAMILIAR")
  private Byte rendaFamiliar;

  private Byte sexo;

  @Transient
  private Byte tipo;

  @Column(name="DOC_APRES_EMISSAO_DATA")
  @Temporal(TemporalType.DATE)
  private Date docApresEmissaoData;

  @Column(name="DOC_APRES_TIPO")
  private Byte docApresTipo;

  @Column(name="DOC_APRES_NR")
  private String docApresNr;

  @Column(name="DOC_APRES_LIVRO")
  private String docApresLivro;

  @Column(name="DOC_APRES_FOLHA")
  private String docApresFolha;

  @Column(name="DOC_APRES_CARTORIO")
  private String docApresCartorio;

  @ManyToOne
  @JoinColumn(name="DOC_APRES_MUNICIPIO_CODIGO", referencedColumnName="CODIGO")
  private Municipio docApresMunicipio;

  public PreAlistamento() {
    super();
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getCodigo() == null ? "CODIGO" : this.getCodigo().toString())
    .append(" - ")
    .append(this.getProtocoloData() == null ? "DATA" : this.getProtocoloData())
    .toString();
  }

  @Override
  public int compareTo(PreAlistamento o) {
    return this.getCodigo().compareTo(o.getCodigo());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
    PreAlistamento other = (PreAlistamento) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Long getCodigo() {
    return codigo;
  }

  public void setCodigo(Long codigo) {
    this.codigo = codigo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = (nome == null || nome.trim().isEmpty() ? null : Utils.limpaCharEsp(Utils.limpaAcento(nome.toUpperCase())))  ;
  }

  public String getPai() {
    return pai;
  }

  public void setPai(String pai) {
    this.pai = (pai == null || pai.trim().isEmpty() ? null : Utils.limpaAcento(pai.toUpperCase()));
  }

  public String getMae() {
    return mae;
  }

  public void setMae(String mae) {
    this.mae = (mae == null || mae.trim().isEmpty() ? null : Utils.limpaAcento(mae.toUpperCase()));
  }

  public String getRgNr() {
    return rgNr;
  }

  public void setRgNr(String rgNr) {
    this.rgNr = (rgNr == null || rgNr.trim().isEmpty() ? null : rgNr.replaceAll("\\W", "").trim().toUpperCase());;
  }

  public String getRgUf() {
    return rgUf;
  }

  public void setRgUf(String rgUf) {
    this.rgUf = rgUf;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = (cpf == null || cpf.trim().isEmpty() ? null : cpf.replaceAll("\\D", "").trim());
  }

  public Byte getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(Byte estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public Byte getEscolaridade() {
    return escolaridade;
  }

  public void setEscolaridade(Byte escolaridade) {
    this.escolaridade = escolaridade;
  }

  public Ocupacao getOcupacao() {
    return ocupacao;
  }

  public void setOcupacao(Ocupacao ocupacao) {
    this.ocupacao = ocupacao;
  }

  public Jsm getJsm() {
    return jsm;
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  public Municipio getMunicipioNascimento() {
    return municipioNascimento;
  }

  public void setMunicipioNascimento(Municipio municipioNascimento) {
    this.municipioNascimento = municipioNascimento;
  }

  public Municipio getMunicipioResidencia() {
    return municipioResidencia;
  }

  public void setMunicipioResidencia(Municipio municipioResidencia) {
    this.municipioResidencia = municipioResidencia;
  }

  public Pais getPaisNascimento() {
    return paisNascimento;
  }

  public void setPaisNascimento(Pais paisNascimento) {
    this.paisNascimento = paisNascimento;
  }

  public Pais getPaisResidencia() {
    return paisResidencia;
  }

  public void setPaisResidencia(Pais paisResidencia) {
    this.paisResidencia = paisResidencia;
  }

  public Byte getZonaResidencial() {
    return zonaResidencial;
  }

  public void setZonaResidencial(Byte zonaResidencial) {
    this.zonaResidencial = zonaResidencial;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = (endereco == null || endereco.trim().isEmpty() ? null : endereco.trim().toUpperCase());
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = (bairro == null || bairro.trim().isEmpty() ? null : bairro.trim().toUpperCase());
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = (cep == null || cep.trim().isEmpty() ? null : cep.trim().replaceAll("\\D", ""));
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = (email == null || email.trim().isEmpty() ? null : email.trim().toLowerCase());
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = (telefone == null || telefone.trim().isEmpty() ? null : telefone.replaceAll("\\D", "").trim());;
  }

  public Byte getDesejaServir() {
    return desejaServir;
  }

  public void setDesejaServir(Byte desejaServir) {
    this.desejaServir = desejaServir;
  }

  public Date getProtocoloData() {
    return protocoloData;
  }

  public void setProtocoloData(Date protocoloData) {
    this.protocoloData = protocoloData;
  }

  public Date getNascimentoData() {
    return nascimentoData;
  }

  public void setNascimentoData(Date nascimentoData) {
    this.nascimentoData = nascimentoData;
  }

  public Byte getPessoaFamiliaQtd() {
    return this.pessoaFamiliaQtd;
  }

  public void setPessoaFamiliaQtd(Byte pessoaFamiliaQtd) {
    this.pessoaFamiliaQtd = pessoaFamiliaQtd;
  }

  public Byte getRendaFamiliar() {
    return this.rendaFamiliar;
  }

  public void setRendaFamiliar(Byte rendaFamiliar) {
    this.rendaFamiliar = rendaFamiliar;
  }

  public Byte getSexo() {
    return sexo;
  }

  public void setSexo(Byte sexo) {
    this.sexo = sexo;
  }
  
  public Byte getTipo() {
    return this.tipo;
  }

  public void setTipo(Byte tipo) {
    this.tipo = tipo;
  }

  public Date getDocApresEmissaoData() {
    return this.docApresEmissaoData;
  }

  public void setDocApresEmissaoData(Date docApresEmissaoData) {
    this.docApresEmissaoData = docApresEmissaoData;
  }

  public Byte getDocApresTipo() {
    return this.docApresTipo;
  }

  public void setDocApresTipo(Byte docApresTipo) {
    this.docApresTipo = docApresTipo;
  }

  public String getDocApresNr() {
    return this.docApresNr;
  }

  public void setDocApresNr(String docApresNr) {
    this.docApresNr = docApresNr;
  }

  public String getDocApresLivro() {
    return docApresLivro;
  }

  public void setDocApresLivro(String docApresLivro) {
    this.docApresLivro = docApresLivro;
  }

  public String getDocApresFolha() {
    return this.docApresFolha;
  }

  public void setDocApresFolha(String docApresFolha) {
    this.docApresFolha = docApresFolha;
  }

  public String getDocApresCartorio() {
    return docApresCartorio;
  }

  public void setDocApresCartorio(String docApresCartorio) {
    this.docApresCartorio = docApresCartorio;
  }

  public Municipio getDocApresMunicipio() {
    return docApresMunicipio;
  }

  public void setDocApresMunicipio(Municipio docApresMunicipio) {
    this.docApresMunicipio = docApresMunicipio;
  }

}
