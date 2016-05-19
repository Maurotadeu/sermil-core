package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;

import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.ObjectDuplicatedException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.tipos.Cpf;
import br.mil.eb.sermil.tipos.TipoEvento;
import br.mil.eb.sermil.tipos.Utils;

/** Entidade Cidadao. (TABELA CIDADAO)
 * @author Abreu Lopes
 * @since 2.0
 * @version 5.4
 */
@Entity
@Table(name="CIDADAO")
@Cache(type=CacheType.SOFT, size=64000, expiry=360000)
@NamedQueries({
  @NamedQuery(name = "Cidadao.listarPorCsmJsm", query = "SELECT c FROM Cidadao c WHERE c.jsm.pk.csmCodigo = ?1 AND c.jsm.pk.codigo = ?2"),
  @NamedQuery(name = "Cidadao.listarPorFracao", query = "SELECT c FROM Cidadao c WHERE c.qcp.pk.omCodigo = ?1 AND c.qcp.pk.fracaoId = ?2 ORDER BY c.nome"),
  @NamedQuery(name = "Cidadao.listarUnico", query = "SELECT c FROM Cidadao c WHERE c.nome = ?1 AND c.mae = ?2 AND c.nascimentoData = ?3 ORDER BY c.nome"),
  @NamedQuery(name = "Cidadao.listarPorCpf", query = "SELECT c FROM Cidadao c WHERE c.cpf = ?1"),
  @NamedQuery(name = "Cidadao.listarPorMaeNasc", query = "SELECT c FROM Cidadao c WHERE c.mae LIKE :mae AND c.nascimentoData = :nasc ORDER BY c.mae ASC"),
  @NamedQuery(name = "Cidadao.listarPorMaeNascNome", query = "SELECT c FROM Cidadao c WHERE c.mae = ?1 AND c.nascimentoData = ?2 AND c.nome = ?3 ORDER BY c.mae ASC"),
  @NamedQuery(name = "Cidadao.listarPorOmSituacao", query = "SELECT c.ra, c.nome, c.padraoCodigo, c.gptIncorp FROM Cidadao c JOIN c.cidEventoCollection e WHERE c.om.codigo = ?1 AND e.pk.codigo = 7 AND SUBSTRING(e.pk.data, 7, 8) = ?2  AND c.situacaoMilitar = ?3  order by c.gptIncorp"),
  @NamedQuery(name = "Cidadao.listarDistribuidos", query = "SELECT c.ra, c.nome, c.padraoCodigo, c.gptIncorp  FROM Cidadao c WHERE c.om.codigo = ?1 AND c.vinculacaoAno = ?2 AND c.situacaoMilitar = 7 ORDER BY c.gptIncorp"),
  @NamedQuery(name = "Cidadao.gruparPorSituacao", query = "SELECT c.situacaoMilitar, c.vinculacaoAno, COUNT(c) FROM Cidadao c WHERE c.vinculacaoAno BETWEEN ?1 AND ?2 GROUP BY c.situacaoMilitar, c.vinculacaoAno"),
  @NamedQuery(name = "Cidadao.gruparPorForca", query = "SELECT c.desejaServir, c.forcaArmada, COUNT(c) FROM Cidadao c JOIN c.cidEventoCollection e WHERE c.vinculacaoAno = ?1 AND c.jsm.csm.rm.codigo = ?2 AND e.pk.codigo = 7 AND e.pk.data BETWEEN ?3 AND ?4 AND c.desejaServir IS NOT NULL AND c.forcaArmada IS NOT NULL GROUP BY c.desejaServir, c.forcaArmada"),
  @NamedQuery(name = "Cidadao.contarPorMae", query = "SELECT COUNT(c.ra) FROM Cidadao c WHERE c.mae LIKE :mae"),
  @NamedQuery(name = "Cidadao.contarPorMaeNasc", query = "SELECT COUNT(c.ra) FROM Cidadao c WHERE c.mae LIKE :mae AND c.nascimentoData = :nasc"),
  @NamedQuery(name = "Cidadao.contarPorMaeNascNome", query = "SELECT COUNT(c.ra) FROM Cidadao c WHERE c.mae LIKE :mae AND c.nascimentoData = :nasc AND c.nome LIKE :nome"),
  @NamedQuery(name = "Cidadao.limpaEmail", query = "UPDATE Cidadao c SET c.email = null WHERE c.ra = ?1"),
  @NamedQuery(name = "Cidadao.SinpaWS1", query = "SELECT c.ra, c.nome, c.nascimentoData, c.mae, c.sexo, c.cpf, c.idtMilitar, c.situacaoMilitar FROM Cidadao c WHERE c.cpf = ?1"),
  @NamedQuery(name = "Cidadao.SinpaWS2", query = "SELECT c.ra, c.nome, c.nascimentoData, c.mae, c.sexo, c.cpf, c.idtMilitar, c.situacaoMilitar FROM Cidadao c WHERE c.mae = ?1 AND c.nascimentoData = ?2 AND c.nome = ?3")
})
public final class Cidadao implements Serializable {

  private static final long serialVersionUID = -2474008787553497225L;

  @Column(name = "ACUIDADE_AUDITIVA")
  private Byte acuidadeAuditiva;

  @Column(name = "ACUIDADE_VISUAL")
  private Byte acuidadeVisual;

  private Short altura;

  private String anotacoes;

  @Column(name = "ATUALIZACAO_DATA")
  @Temporal(TemporalType.TIMESTAMP)
  private Date atualizacaoData;

  private String bairro;

  private Byte cabeca;

  private Byte calcado;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "CARGO_MILITAR_CODIGO", referencedColumnName = "CODIGO")
  private CargoMilitar cargoMilitar;

  private String cep;

  private String cid;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidAdiamento> cidAdiamentoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidArrecadacao> cidArrecadacaoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER)
  private List<CidAuditoria> cidAuditoriaCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidAverbacao> cidAverbacaoCollection;

  @OneToOne(mappedBy = "cidadao", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private CidBcc cidBcc;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidCertificado> cidCertificadoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidContato> cidContatoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidDocApres> cidDocApresColletion;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER)
  private List<CidDocumento> cidDocumentoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidEvento> cidEventoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidExar> cidExarCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidEmpresa> cidEmpresaCollection;

  @OneToOne(mappedBy = "cidadao", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private CidEximido cidEximido;

  @OneToOne(mappedBy = "cidadao", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
  @PrimaryKeyJoinColumn
  private CidFoto cidFoto;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidHabilitacao> cidHabilitacaoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidMobilizacao> cidMobilizacaoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidMovimentacao> cidMovimentacaoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidPromocao> cidPromocaoCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidQualidadeReserva> cidQualidadeReservaCollection;

  @OneToMany(mappedBy = "cidadao", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<CidRequerimento> cidRequerimentoCollection;

  private Short cintura;

  @Column(name = "CLASSE_RESERVA")
  private Byte classeReserva;

  private Byte comportamento;

  private String cpf;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "CS", referencedColumnName = "CODIGO")
  private Cs cs;

  @Column(name = "CSE_INDICACAO")
  private Byte cseIndicacao;

  @Column(name = "CSE_RESULTADO")
  private Byte cseResultado;

  @Column(name = "DESEJA_SERVIR")
  private Byte desejaServir;

  private Byte diagnostico;

  private Byte dispensa;

  @Column(name = "DISTR_AJUSTAMENTO")
  private Byte distrAjustamento;

  @Column(name = "DISTR_GRUPO")
  private Byte distrGrupo;

  @Column(name = "DISTR_ORDEM")
  private Integer distrOrdem;

  @Column(name = "DISTR_TIPO")
  private Byte distrTipo;

  private String email;

  private String endereco;

  private Byte escolaridade;

  @Column(name = "ESTADO_CIVIL")
  private Byte estadoCivil;

  @Column(name = "EXPRESSAO_ORAL")
  private Byte expressaoOral;

  @Column(name = "FATOR_RH")
  private Byte fatorRh;

  @Column(name = "FORCA_ARMADA")
  private Byte forcaArmada;

  @Column(name = "FORCA_MUSCULAR")
  private Short forcaMuscular;

  @Column(name = "FS_NR")
  private Integer fsNr;

  @Column(name = "GPT_INCORP")
  private String gptIncorp;

  @Column(name = "IDT_MILITAR")
  private String idtMilitar;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumns({@JoinColumn(name = "CSM_CODIGO", referencedColumnName = "CSM_CODIGO"), @JoinColumn(name = "JSM_CODIGO", referencedColumnName = "CODIGO")})
  private Jsm jsm;

  private String mae;

  @Column(name = "MEDICO_CRM")
  private String medicoCrm;

  @Column(name = "MOB_DESTINO")
  private Byte mobDestino;

  @Column(name = "MOB_SETOR")
  private Integer mobSetor;

  @Column(name = "MOB_SITUACAO")
  private Byte mobSituacao;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "MUNICIPIO_NASCIMENTO_CODIGO", referencedColumnName = "CODIGO")
  private Municipio municipioNascimento;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "MUNICIPIO_RESIDENCIA_CODIGO", referencedColumnName = "CODIGO")
  private Municipio municipioResidencia;

  @Column(name = "NASCIMENTO_DATA")
  @Temporal(TemporalType.DATE)
  private Date nascimentoData;

  private String nome;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "OCUPACAO_CODIGO", referencedColumnName = "CODIGO")
  private Ocupacao ocupacao;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "OM_CODIGO", referencedColumnName = "CODIGO")
  private Om om;

  @Column(name = "PADRAO_CODIGO")
  private String padraoCodigo;

  @Column(name = "PADRAO_PQ1_CODIGO")
  private String padraoPq1Codigo;

  @Column(name = "PADRAO_PQ2_CODIGO")
  private String padraoPq2Codigo;

  private String pai;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "PAIS_NASCIMENTO_CODIGO", referencedColumnName = "CODIGO")
  private Pais paisNascimento;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "PAIS_RESIDENCIA_CODIGO", referencedColumnName = "CODIGO")
  private Pais paisResidencia;

  @Column(name = "PARECER_CMT_PROMOCAO")
  private Byte parecerCmtPromocao;

  private Short peso;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "POSTO_GRADUACAO_CODIGO", referencedColumnName = "CODIGO")
  private PostoGraduacao postoGraduacao;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumns({ @JoinColumn(name = "QCP_OM_CODIGO", referencedColumnName = "OM_CODIGO"), @JoinColumn(name = "QCP_FRACAO_ID", referencedColumnName = "FRACAO_ID") })
  private Qcp qcp;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "QM_CODIGO", referencedColumnName = "CODIGO")
  private Qm qm;

  @Id
  private Long ra;

  private String rg;

  @Column(name = "SABE_NADAR")
  private Byte sabeNadar;

  private Byte sexo;

  @Column(name = "SITUACAO_MILITAR")
  private Integer situacaoMilitar;

  private String telefone;

  @Column(name = "TIPO_FISICO")
  private Byte tipoFisico;

  @Column(name = "TIPO_SANGUINEO")
  private Byte tipoSanguineo;

  private String tsc;

  @Column(name = "TSI_I")
  private Byte tsiI;

  @Column(name = "TSI_P")
  private Byte tsiP;

  private Byte uniforme;

  @Column(name = "VINCULACAO_ANO")
  private Integer vinculacaoAno;

  @Column(name = "ZONA_RESIDENCIAL")
  private Byte zonaResidencial;

  @Column(name = "RELIGIAO")
  private Short religiao;

  public Cidadao() {
    super();
  }

  public Cidadao(final Long ra) {
    this.setRa(ra);
  }

  @Override
  public String toString() {
    return new StringBuilder((this.getRa() == null ? "RA" : this.getRa()).toString()).append(" - ").append(this.getNome() == null ? "NOME" : this.getNome()).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    Cidadao other = (Cidadao) obj;
    if (ra == null) {
      if (other.ra != null)
        return false;
    } else if (!ra.equals(other.ra))
      return false;
    return true;
  }

  public Byte getAcuidadeAuditiva() {
    return this.acuidadeAuditiva;
  }

  public Byte getAcuidadeVisual() {
    return this.acuidadeVisual;
  }

  public Short getAltura() {
    return this.altura;
  }

  public String getAnotacoes() {
    return this.anotacoes;
  }

  public Date getAtualizacaoData() {
    return this.atualizacaoData;
  }

  public String getBairro() {
    return this.bairro;
  }

  public Byte getCabeca() {
    return this.cabeca;
  }

  public Byte getCalcado() {
    return this.calcado;
  }

  public CargoMilitar getCargoMilitar() {
    return this.cargoMilitar;
  }

  public String getCep() {
    return this.cep;
  }

  public String getCid() {
    return this.cid;
  }

  public List<CidAdiamento> getCidAdiamentoCollection() {
    if (this.cidAdiamentoCollection != null) {
      Collections.sort(this.cidAdiamentoCollection);
    }
    return this.cidAdiamentoCollection;
  }

  public List<CidArrecadacao> getCidArrecadacaoCollection() {
    if (this.cidArrecadacaoCollection != null) {
      Collections.sort(this.cidArrecadacaoCollection);
    }
    return cidArrecadacaoCollection;
  }

  public List<CidAuditoria> getCidAuditoriaCollection() {
    if (this.cidAuditoriaCollection != null) {
      Collections.sort(this.cidAuditoriaCollection);
    }
    return this.cidAuditoriaCollection;
  }

  public List<CidAverbacao> getCidAverbacaoCollection() {
    if (this.cidAverbacaoCollection != null) {
      Collections.sort(this.cidAverbacaoCollection);
    }
    return this.cidAverbacaoCollection;
  }

  public CidBcc getCidBcc() {
    return cidBcc;
  }

  public List<CidCertificado> getCidCertificadoCollection() {
    if (this.cidCertificadoCollection != null) {
      Collections.sort(this.cidCertificadoCollection);
    }
    return this.cidCertificadoCollection;
  }

  public List<CidContato> getCidContatoCollection() {
    if (this.cidContatoCollection != null) {
      Collections.sort(this.cidContatoCollection);
    }
    return this.cidContatoCollection;
  }

  public List<CidDocApres> getCidDocApresColletion() {
    if (this.cidDocApresColletion != null) {
      Collections.sort(this.cidDocApresColletion);
    }
    return this.cidDocApresColletion;
  }

  public List<CidDocumento> getCidDocumentoCollection() {
    if (this.cidDocumentoCollection != null) {
      Collections.sort(this.cidDocumentoCollection);
    }
    return this.cidDocumentoCollection;
  }

  public List<CidEvento> getCidEventoCollection() {
    if (this.cidEventoCollection != null) {
      Collections.sort(this.cidEventoCollection);
    }
    return this.cidEventoCollection;
  }

  public List<CidExar> getCidExarCollection() {
    if (this.cidExarCollection != null) {
      Collections.sort(this.cidExarCollection);
    }
    return this.cidExarCollection;
  }

  public CidEximido getCidEximido() {
    return this.cidEximido;
  }

  public List<CidEmpresa> getCidEmpresaCollection() {
    if (this.cidEmpresaCollection != null) {
      Collections.sort(this.cidEmpresaCollection);
    }
    return cidEmpresaCollection;
  }

  public void setCidEmpresaCollection(List<CidEmpresa> cidEmpresaCollection) {
    this.cidEmpresaCollection = cidEmpresaCollection;
  }

  public List<CidHabilitacao> getCidHabilitacaoCollection() {
    if (this.cidHabilitacaoCollection != null) {
      Collections.sort(this.cidHabilitacaoCollection);
    }
    return this.cidHabilitacaoCollection;
  }

  public List<CidMobilizacao> getCidMobilizacaoCollection() {
    if (this.cidMobilizacaoCollection != null) {
      Collections.sort(this.cidMobilizacaoCollection);
    }
    return this.cidMobilizacaoCollection;
  }

  public List<CidMovimentacao> getCidMovimentacaoCollection() {
    if (this.cidMovimentacaoCollection != null) {
      Collections.sort(this.cidMovimentacaoCollection);
    }
    return this.cidMovimentacaoCollection;
  }

  public List<CidPromocao> getCidPromocaoCollection() {
    if (this.cidPromocaoCollection != null) {
      Collections.sort(this.cidPromocaoCollection);
    }
    return this.cidPromocaoCollection;
  }

  public List<CidQualidadeReserva> getCidQualidadeReservaCollection() {
    return cidQualidadeReservaCollection;
  }

  public List<CidRequerimento> getCidRequerimentoCollection() {
    if (this.cidRequerimentoCollection != null) {
      Collections.sort(this.cidRequerimentoCollection);
    }
    return this.cidRequerimentoCollection;
  }

  public Short getCintura() {
    return this.cintura;
  }

  public Byte getClasseReserva() {
    return this.classeReserva;
  }

  public Byte getComportamento() {
    return this.comportamento;
  }

  public String getCpf() {
    return this.cpf;
  }

  public Cs getCs() {
    return this.cs;
  }

  public Byte getCseIndicacao() {
    return this.cseIndicacao;
  }

  public Byte getCseResultado() {
    return this.cseResultado;
  }

  public Byte getDesejaServir() {
    return this.desejaServir;
  }

  public Byte getDiagnostico() {
    return this.diagnostico;
  }

  public Byte getDispensa() {
    return this.dispensa;
  }

  public Byte getDistrAjustamento() {
    return this.distrAjustamento;
  }

  public Byte getDistrGrupo() {
    return this.distrGrupo;
  }

  public Integer getDistrOrdem() {
    return this.distrOrdem;
  }

  public Byte getDistrTipo() {
    return this.distrTipo;
  }

  public String getEmail() {
    return this.email;
  }

  public String getEndereco() {
    return this.endereco;
  }

  public Byte getEscolaridade() {
    return this.escolaridade;
  }

  public Byte getEstadoCivil() {
    return this.estadoCivil;
  }

  public Byte getExpressaoOral() {
    return this.expressaoOral;
  }

  public Byte getFatorRh() {
    return this.fatorRh;
  }

  public Byte getForcaArmada() {
    return this.forcaArmada;
  }

  public Short getForcaMuscular() {
    return this.forcaMuscular;
  }

  public Integer getFsNr() {
    return this.fsNr;
  }

  public String getGptIncorp() {
    return this.gptIncorp;
  }

  public String getIdtMilitar() {
    return this.idtMilitar;
  }

  public Jsm getJsm() {
    return this.jsm;
  }

  public String getMae() {
    return this.mae;
  }

  public String getMedicoCrm() {
    return this.medicoCrm;
  }

  public Byte getMobDestino() {
    return this.mobDestino;
  }

  public Integer getMobSetor() {
    return this.mobSetor;
  }

  public Byte getMobSituacao() {
    return this.mobSituacao;
  }

  public Municipio getMunicipioNascimento() {
    return this.municipioNascimento;
  }

  public Municipio getMunicipioResidencia() {
    return this.municipioResidencia;
  }

  public Date getNascimentoData() {
    return this.nascimentoData;
  }

  public String getNome() {
    return this.nome;
  }

  public Ocupacao getOcupacao() {
    return this.ocupacao;
  }

  public Om getOm() {
    return this.om;
  }

  public String getPadraoCodigo() {
    return this.padraoCodigo;
  }

  public String getPadraoPq1Codigo() {
    return this.padraoPq1Codigo;
  }

  public String getPadraoPq2Codigo() {
    return this.padraoPq2Codigo;
  }

  public String getPai() {
    return this.pai;
  }

  public Pais getPaisNascimento() {
    return this.paisNascimento;
  }

  public Pais getPaisResidencia() {
    return this.paisResidencia;
  }

  public Byte getParecerCmtPromocao() {
    return this.parecerCmtPromocao;
  }

  public Short getPeso() {
    return this.peso;
  }

  public PostoGraduacao getPostoGraduacao() {
    return this.postoGraduacao;
  }

  public Qcp getQcp() {
    return this.qcp;
  }

  public Qm getQm() {
    return this.qm;
  }

  public Long getRa() {
    return this.ra;
  }

  public String getRg() {
    return this.rg;
  }

  public Byte getSabeNadar() {
    return this.sabeNadar;
  }

  public Byte getSexo() {
    return this.sexo;
  }

  public Integer getSituacaoMilitar() {
    return this.situacaoMilitar;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public Byte getTipoFisico() {
    return this.tipoFisico;
  }

  public Byte getTipoSanguineo() {
    return this.tipoSanguineo;
  }

  public String getTsc() {
    return this.tsc;
  }

  public Byte getTsiI() {
    return this.tsiI;
  }

  public Byte getTsiP() {
    return this.tsiP;
  }

  public Byte getUniforme() {
    return this.uniforme;
  }

  public Integer getVinculacaoAno() {
    return this.vinculacaoAno;
  }

  public Byte getZonaResidencial() {
    return this.zonaResidencial;
  }

  public void setAcuidadeAuditiva(Byte acuidadeAuditiva) {
    this.acuidadeAuditiva = acuidadeAuditiva;
  }

  public void setAcuidadeVisual(Byte acuidadeVisual) {
    this.acuidadeVisual = acuidadeVisual;
  }

  public void setAltura(Short altura) {
    this.altura = altura;
  }

  public void setAnotacoes(String anotacoes) {
    this.anotacoes = anotacoes;
  }

  public void setAtualizacaoData(Date atualizacaoData) {
    this.atualizacaoData = atualizacaoData;
  }

  public void setBairro(String bairro) {
    this.bairro = (bairro == null || bairro.trim().isEmpty() ? null : Utils.limpaAcento(bairro.trim().toUpperCase()));
  }

  public void setCabeca(Byte cabeca) {
    this.cabeca = cabeca;
  }

  public void setCalcado(Byte calcado) {
    this.calcado = calcado;
  }

  public void setCargoMilitar(CargoMilitar cargoMilitar) {
    this.cargoMilitar = cargoMilitar;
  }

  public void setCep(String cep) {
    this.cep = (cep == null || cep.trim().isEmpty() ? null : cep.trim().replaceAll("\\D", ""));
  }

  public void setCid(String cid) {
    this.cid = (cid == null || cid.trim().isEmpty() ? null : cid.trim());
  }

  public void setCidBcc(CidBcc cidBcc) {
    this.cidBcc = cidBcc;
  }

  public void setCidAdiamentoCollection(List<CidAdiamento> cidAdiamentoCollection) {
    this.cidAdiamentoCollection = cidAdiamentoCollection;
  }

  public void setCidArrecadacaoCollection(List<CidArrecadacao> cidArrecadacaoCollection) {
    this.cidArrecadacaoCollection = cidArrecadacaoCollection;
  }

  public void setCidAuditoriaCollection(List<CidAuditoria> cidAuditoriaCollection) {
    this.cidAuditoriaCollection = cidAuditoriaCollection;
  }

  public void setCidAverbacaoCollection(List<CidAverbacao> cidAverbacaoCollection) {
    this.cidAverbacaoCollection = cidAverbacaoCollection;
  }

  public void setCidCertificadoCollection(List<CidCertificado> cidCertificadoCollection) {
    this.cidCertificadoCollection = cidCertificadoCollection;
  }

  public void setCidContatoCollection(List<CidContato> cidContatoCollection) {
    this.cidContatoCollection = cidContatoCollection;
  }

  public void setCidDocApresColletion(List<CidDocApres> cidDocApresColletion) {
    this.cidDocApresColletion = cidDocApresColletion;
  }

  public void setCidDocumentoCollection(List<CidDocumento> cidDocumentoCollection) {
    this.cidDocumentoCollection = cidDocumentoCollection;
  }

  public void setCidEventoCollection(List<CidEvento> cidEventoCollection) {
    this.cidEventoCollection = cidEventoCollection;
  }

  public void setCidExarCollection(List<CidExar> cidExarCollection) {
    this.cidExarCollection = cidExarCollection;
  }

  public void setCidEximido(CidEximido cidEximido) {
    this.cidEximido = cidEximido;
  }

  public void setCidHabilitacaoCollection(List<CidHabilitacao> cidHabilitacaoCollection) {
    this.cidHabilitacaoCollection = cidHabilitacaoCollection;
  }

  public void setCidMobilizacaoCollection(List<CidMobilizacao> cidMobilizacaoCollection) {
    this.cidMobilizacaoCollection = cidMobilizacaoCollection;
  }

  public void setCidMovimentacaoCollection(List<CidMovimentacao> cidMovimentacaoCollection) {
    this.cidMovimentacaoCollection = cidMovimentacaoCollection;
  }

  public void setCidPromocaoCollection(List<CidPromocao> cidPromocaoCollection) {
    this.cidPromocaoCollection = cidPromocaoCollection;
  }

  public void setCidQualidadeReservaCollection(List<CidQualidadeReserva> cidQualidadeReservaCollection) {
    this.cidQualidadeReservaCollection = cidQualidadeReservaCollection;
  }

  public void setCidRequerimentoCollection(List<CidRequerimento> cidRequerimentoCollection) {
    this.cidRequerimentoCollection = cidRequerimentoCollection;
  }

  public void setCintura(Short cintura) {
    this.cintura = cintura;
  }

  public void setClasseReserva(Byte classeReserva) {
    this.classeReserva = classeReserva;
  }

  public void setComportamento(Byte comportamento) {
    this.comportamento = comportamento;
  }

  public void setCpf(String cpf) {
    if (!StringUtils.isBlank(cpf) && Cpf.isCpf(cpf)) {
      this.cpf = cpf;
    }
  }

  public void setCs(Cs cs) {
    this.cs = cs;
  }

  public void setCseIndicacao(Byte cseIndicacao) {
    this.cseIndicacao = cseIndicacao;
  }

  public void setCseResultado(Byte cseResultado) {
    this.cseResultado = cseResultado;
  }

  public void setDesejaServir(Byte desejaServir) {
    this.desejaServir = desejaServir;
  }

  public void setDiagnostico(Byte diagnostico) {
    this.diagnostico = diagnostico;
  }

  public void setDispensa(Byte dispensa) {
    this.dispensa = dispensa;
  }

  public void setDistrAjustamento(Byte distrAjustamento) {
    this.distrAjustamento = distrAjustamento;
  }

  public void setDistrGrupo(Byte distrGrupo) {
    this.distrGrupo = distrGrupo;
  }

  public void setDistrOrdem(Integer distrOrdem) {
    this.distrOrdem = distrOrdem;
  }

  public void setDistrTipo(Byte distrTipo) {
    this.distrTipo = distrTipo;
  }

  public void setEmail(String email) {
    this.email = (StringUtils.isBlank(email) ? null : email.trim().toLowerCase());
  }

  public void setEndereco(String endereco) {
    this.endereco = (StringUtils.isBlank(endereco) ? null : Utils.limpaAcento(endereco.trim().toUpperCase()));
  }

  public void setEscolaridade(Byte escolaridade) {
    this.escolaridade = escolaridade;
  }

  public void setEstadoCivil(Byte estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public void setExpressaoOral(Byte expressaoOral) {
    this.expressaoOral = expressaoOral;
  }

  public void setFatorRh(Byte fatorRh) {
    this.fatorRh = fatorRh;
  }

  public void setForcaArmada(Byte forcaArmada) {
    this.forcaArmada = forcaArmada;
  }

  public void setForcaMuscular(Short forcaMuscular) {
    this.forcaMuscular = forcaMuscular;
  }

  public void setFsNr(Integer fsNr) {
    this.fsNr = fsNr;
  }

  public void setGptIncorp(String gptIncorp) {
    this.gptIncorp = gptIncorp;
  }

  public void setIdtMilitar(String idtMilitar) {
    this.idtMilitar = (StringUtils.isBlank(idtMilitar) ? null : idtMilitar.replaceAll("\\D", "").trim());
  }

  public void setJsm(Jsm jsm) {
    this.jsm = jsm;
  }

  public void setMae(String mae) {
    this.mae = (StringUtils.isBlank(mae) ? null : Utils.limpaAcento(mae.toUpperCase()));
  }

  public void setMedicoCrm(String medicoCrm) {
    this.medicoCrm = medicoCrm;
  }

  public void setMobDestino(Byte mobDestino) {
    this.mobDestino = mobDestino;
  }

  public void setMobSetor(Integer mobSetor) {
    this.mobSetor = mobSetor;
  }

  public void setMobSituacao(Byte mobSituacao) {
    this.mobSituacao = mobSituacao;
  }

  public void setMunicipioNascimento(Municipio municipioNascimento) {
    this.municipioNascimento = municipioNascimento;
  }

  public void setMunicipioResidencia(Municipio municipioResidencia) {
    this.municipioResidencia = municipioResidencia;
  }

  public void setNascimentoData(Date data) throws SermilException {
    if (data != null) {
      final Calendar cal = Calendar.getInstance();
      if (cal.getTime().before(data)) {
        throw new SermilException("Data de nascimento maior que a data atual.");
      } else {
        cal.set(1900, 0, 1); // 01-01-1900
        if (cal.getTime().after(data)) {
          throw new SermilException("Data de nascimento menor que 01/01/1900.");
        }
      }
      this.nascimentoData = data;
    }
  }

  public void setNome(String nome) {
    this.nome = (StringUtils.isBlank(nome) ? null : Utils.limpaAcento(nome.toUpperCase()));
  }

  public void setOcupacao(Ocupacao ocupacao) {
    this.ocupacao = ocupacao;
  }

  public void setOm(Om om) {
    this.om = om;
  }

  public void setPadraoCodigo(String padraoCodigo) {
    this.padraoCodigo = padraoCodigo;
  }

  public void setPadraoPq1Codigo(String padraoPq1Codigo) {
    this.padraoPq1Codigo = padraoPq1Codigo;
  }

  public void setPadraoPq2Codigo(String padraoPq2Codigo) {
    this.padraoPq2Codigo = padraoPq2Codigo;
  }

  public void setPai(String pai) {
    this.pai = (StringUtils.isBlank(pai) ? null : Utils.limpaAcento(pai.toUpperCase()));
  }

  public void setPaisNascimento(Pais paisNascimento) {
    this.paisNascimento = paisNascimento;
  }

  public void setPaisResidencia(Pais paisResidencia) {
    this.paisResidencia = paisResidencia;
  }

  public void setParecerCmtPromocao(Byte parecerCmtPromocao) {
    this.parecerCmtPromocao = parecerCmtPromocao;
  }

  public void setPeso(Short peso) {
    this.peso = peso;
  }

  public void setPostoGraduacao(PostoGraduacao postoGraduacao) {
    this.postoGraduacao = postoGraduacao;
  }

  public void setQcp(Qcp qcp) {
    this.qcp = qcp;
  }

  public void setQm(Qm qm) {
    this.qm = qm;
  }

  public void setRa(Long ra) {
    this.ra = ra;
  }

  public void setRg(String rg) {
    this.rg = (StringUtils.isBlank(rg) ? null : rg.trim().toUpperCase());
  }

  public void setSabeNadar(Byte sabeNadar) {
    this.sabeNadar = sabeNadar;
  }

  public void setSexo(Byte sexo) {
    this.sexo = sexo;
  }

  public void setSituacaoMilitar(Integer situacaoMilitar) {
    this.situacaoMilitar = situacaoMilitar;
  }

  public void setTelefone(String telefone) {
    this.telefone = (StringUtils.isBlank(telefone) ? null : telefone.replaceAll("\\D", "").trim());
  }

  public void setTipoFisico(Byte tipoFisico) {
    this.tipoFisico = tipoFisico;
  }

  public void setTipoSanguineo(Byte tipoSanguineo) {
    this.tipoSanguineo = tipoSanguineo;
  }

  public void setTsc(String tsc) {
    this.tsc = (StringUtils.isBlank(tsc) ? null : tsc.replaceAll("\\D", "").trim());
  }

  public void setTsiI(Byte tsiI) {
    this.tsiI = tsiI;
  }

  public void setTsiP(Byte tsiP) {
    this.tsiP = tsiP;
  }

  public void setUniforme(Byte uniforme) {
    this.uniforme = uniforme;
  }

  public void setVinculacaoAno(Integer vinculacaoAno) {
    this.vinculacaoAno = vinculacaoAno;
  }

  public void setZonaResidencial(Byte zonaResidencial) {
    this.zonaResidencial = zonaResidencial;
  }

  public CidFoto getCidFoto() {
    return this.cidFoto;
  }

  public void setCidFoto(CidFoto cidFoto) {
    this.cidFoto = cidFoto;
  }

  public Short getReligiao() {
    return religiao;
  }

  public void setReligiao(Short religiao) {
    this.religiao = religiao;
  }

  public void addCidAdiamento(final CidAdiamento ca) throws SermilException {
    if (this.getCidAdiamentoCollection() == null) {
      this.setCidAdiamentoCollection(new ArrayList<CidAdiamento>(1));
    }
    if (this.getCidAdiamentoCollection().contains(ca)) {
      throw new ObjectDuplicatedException("Adiamento já existe", ca);
    }
    this.getCidAdiamentoCollection().add(ca);
    if (ca.getCidadao() != this) {
      ca.setCidadao(this);
    }
  }

  public void addCidArrecadacao(final CidArrecadacao ca) throws SermilException {
    if (this.getCidArrecadacaoCollection() == null) {
      this.setCidArrecadacaoCollection(new ArrayList<CidArrecadacao>(1));
    }
    if (this.getCidArrecadacaoCollection().contains(ca)) {
      throw new ObjectDuplicatedException("Arrecadação já existe", ca);
    }
    this.getCidArrecadacaoCollection().add(ca);
    if (ca.getCidadao() != this) {
      ca.setCidadao(this);
    }
  }

  public void addCidAuditoria(final CidAuditoria ca) throws SermilException {
    if (this.getCidAuditoriaCollection() == null) {
      this.setCidAuditoriaCollection(new ArrayList<CidAuditoria>(1));
    }
    if (this.getCidAuditoriaCollection().contains(ca)) {
      throw new ObjectDuplicatedException("Auditoria já existe", ca);
    }
    this.getCidAuditoriaCollection().add(ca);
    if (ca.getCidadao() != this) {
      ca.setCidadao(this);
    }
  }

  public void addCidAverbacao(final CidAverbacao cv) throws SermilException {
    if (this.getCidAverbacaoCollection() == null) {
      this.setCidAverbacaoCollection(new ArrayList<CidAverbacao>(1));
    }
    if (this.getCidAverbacaoCollection().contains(cv)) {
      throw new ObjectDuplicatedException("Averbação já existe", cv);
    }
    this.getCidAverbacaoCollection().add(cv);
    if (cv.getCidadao() != this) {
      cv.setCidadao(this);
    }
  }

  public void addCidCertificado(final CidCertificado cc) throws SermilException {
    if (this.getCidCertificadoCollection() == null) {
      this.setCidCertificadoCollection(new ArrayList<CidCertificado>(1));
    }
    if (this.getCidCertificadoCollection().contains(cc)) {
      throw new ObjectDuplicatedException("Certificado já existe", cc);
    }
    this.getCidCertificadoCollection().add(cc);
    if (cc.getCidadao() != this) {
      cc.setCidadao(this);
    }
  }

  public void addCidContato(final CidContato cc) throws SermilException {
    if (this.getCidContatoCollection() == null) {
      this.setCidContatoCollection(new ArrayList<CidContato>(1));
    }
    if (this.getCidContatoCollection().contains(cc)) {
      throw new ObjectDuplicatedException("Contato já existe", cc);
    }
    this.getCidContatoCollection().add(cc);
    if (cc.getCidadao() != this) {
      cc.setCidadao(this);
    }
  }

  public void addCidDocApres(final CidDocApres cda) throws SermilException {
    if (this.getCidDocApresColletion() == null) {
      this.setCidDocApresColletion(new ArrayList<CidDocApres>(1));
    }
    if (this.getCidDocApresColletion().contains(cda)) {
      throw new ObjectDuplicatedException("Documento já existe", cda);
    }
    this.getCidDocApresColletion().add(cda);
    if (cda.getCidadao() != this) {
      cda.setCidadao(this);
    }
  }

  public void addCidDocumento(final CidDocumento cd) throws SermilException {
    if (this.getCidDocumentoCollection() == null) {
      this.setCidDocumentoCollection(new ArrayList<CidDocumento>(1));
    }
    if (this.getCidDocumentoCollection().contains(cd)) {
      throw new ObjectDuplicatedException("Documento já existe", cd);
    }
    this.getCidDocumentoCollection().add(cd);
    if (cd.getCidadao() != this) {
      cd.setCidadao(this);
    }
  }

  public void addCidEmpresa(final CidEmpresa ce) throws SermilException {
    if (this.getCidEmpresaCollection() == null) {
      this.setCidEmpresaCollection(new ArrayList<CidEmpresa>(1));
    }
    if (this.getCidEmpresaCollection().contains(ce)) {
      throw new ObjectDuplicatedException("Empresa já existe", ce);
    }
    this.getCidEmpresaCollection().add(ce);
    if (ce.getCidadao() != this) {
      ce.setCidadao(this);
    }
  }

  public void addCidEvento(final CidEvento ce) throws SermilException {
    if (this.getCidEventoCollection() == null) {
      this.setCidEventoCollection(new ArrayList<CidEvento>(1));
    }
    if (this.getCidEventoCollection().contains(ce)) {
      throw new ObjectDuplicatedException("Evento já existe", ce);
    }
    this.getCidEventoCollection().add(ce);
    if (ce.getCidadao() != this) {
      ce.setCidadao(this);
    }
  }

  public void addCidExar(final CidExar cx) throws SermilException {
    if (this.getCidExarCollection() == null) {
      this.setCidExarCollection(new ArrayList<CidExar>(1));
    }
    if (this.getCidExarCollection().contains(cx)) {
      throw new ObjectDuplicatedException("Apresentação já existe", cx);
    }
    this.getCidExarCollection().add(cx);
    if (cx.getCidadao() != this) {
      cx.setCidadao(this);
    }
  }

  public void addCidHabilitacao(final CidHabilitacao ch) throws SermilException {
    if (this.getCidHabilitacaoCollection() == null) {
      this.setCidHabilitacaoCollection(new ArrayList<CidHabilitacao>(1));
    }
    if (this.getCidHabilitacaoCollection().contains(ch)) {
      throw new ObjectDuplicatedException("Habilitação já existe", ch);
    }
    this.getCidHabilitacaoCollection().add(ch);
    if (ch.getCidadao() != this) {
      ch.setCidadao(this);
    }
  }

  public void addCidMobilizacao(final CidMobilizacao cm) throws SermilException {
    if (this.getCidMobilizacaoCollection() == null) {
      this.setCidMobilizacaoCollection(new ArrayList<CidMobilizacao>(1));
    }
    if (this.getCidMobilizacaoCollection().contains(cm)) {
      throw new ObjectDuplicatedException("Mobilização já existe", cm);
    }
    this.getCidMobilizacaoCollection().add(cm);
    if (cm.getCidadao() != this) {
      cm.setCidadao(this);
    }
  }

  public void addCidMovimentacao(final CidMovimentacao cm) throws SermilException {
    if (this.getCidMovimentacaoCollection() == null) {
      this.setCidMovimentacaoCollection(new ArrayList<CidMovimentacao>(1));
    }
    if (this.getCidMovimentacaoCollection().contains(cm)) {
      throw new ObjectDuplicatedException("Movimentação já existe", cm);
    }
    this.getCidMovimentacaoCollection().add(cm);
    if (cm.getCidadao() != this) {
      cm.setCidadao(this);
    }
  }

  public void addCidPromocao(final CidPromocao cp) throws SermilException {
    if (this.getCidPromocaoCollection() == null) {
      this.setCidPromocaoCollection(new ArrayList<CidPromocao>(1));
    }
    if (this.getCidPromocaoCollection().contains(cp)) {
      throw new ObjectDuplicatedException("Promoção já existe", cp);
    }
    this.getCidPromocaoCollection().add(cp);
    if (cp.getCidadao() != this) {
      cp.setCidadao(this);
    }
  }

  public void addCidQualidadeReserva(final CidQualidadeReserva cqr) throws SermilException {
    if (this.getCidQualidadeReservaCollection() == null) {
      this.setCidQualidadeReservaCollection(new ArrayList<CidQualidadeReserva>(1));
    }
    if (this.getCidQualidadeReservaCollection().contains(cqr)) {
      throw new ObjectDuplicatedException("Qualidade da Reserva já existe", cqr);
    }
    this.getCidQualidadeReservaCollection().add(cqr);
    if (cqr.getCidadao() != this) {
      cqr.setCidadao(this);
    }
  }

  public void addCidRequerimento(final CidRequerimento cr) throws SermilException {
    if (this.getCidRequerimentoCollection() == null) {
      this.setCidRequerimentoCollection(new ArrayList<CidRequerimento>(1));
    }
    if (this.getCidRequerimentoCollection().contains(cr)) {
      throw new ObjectDuplicatedException("Requerimento já existe", cr);
    }
    this.getCidRequerimentoCollection().add(cr);
    if (cr.getCidadao() != this) {
      cr.setCidadao(this);
    }
  }

  public boolean hasCertificado(final int tipoCertificado) throws CriterioException {
    if (CollectionUtils.isEmpty(this.getCidCertificadoCollection())) {
      throw new CriterioException("Cidadão sem lista de certificados.");
    }
    return this.getCidCertificadoCollection().stream().anyMatch(c -> c.getPk().getTipo() == tipoCertificado);
  }

  public boolean hasEvento(final int eventoCodigo) throws CriterioException {
    if (CollectionUtils.isEmpty(this.getCidEventoCollection())) {
      throw new CriterioException("Cidadão sem lista de eventos.");
    }
    return this.getCidEventoCollection().stream().anyMatch(e -> e.getPk().getCodigo() == eventoCodigo);
  }

  public Date getAlistamentoData() throws SermilException {
    final Optional<CidEvento> alist =  this.getCidEventoCollection().stream().filter(e -> e.getPk().getCodigo() == TipoEvento.ALISTAMENTO.getCodigo()).findFirst();
    if (!alist.isPresent()) {
      throw new SermilException("Cidadão não tem Data de Alistamento.");
    }
    return alist.get().getPk().getData();
  }

  public boolean isClasseConvocada(final int anoBase) throws CriterioException {
    if (this.getNascimentoData() == null) {
      throw new CriterioException("Cidadão sem data de nascimento.");
    }
    int anoNasc =  new java.sql.Date(this.getNascimentoData().getTime()).toLocalDate().getYear();
    return (anoNasc+18 == anoBase) ? true : false;
  }

  public boolean isForaPrazo() throws SermilException {
    if (this.getNascimentoData() == null) {
      throw new CriterioException("Cidadão sem data de nascimento");
    }
    final LocalDate dtAlist =  new java.sql.Date(this.getAlistamentoData().getTime()).toLocalDate();
    final LocalDate dtNasc =  new java.sql.Date(this.getNascimentoData().getTime()).toLocalDate();
    return ((dtNasc.getYear()+18 < dtAlist.getYear()) || (this.isClasseConvocada(dtAlist.getYear()) && dtAlist.getMonthValue() > 6)) ? true : false;
  }

  public boolean isForaLimiteIdade() throws CriterioException {
    if (this.getNascimentoData() == null) {
      throw new CriterioException("Cidadão sem data de nascimento");
    }
    int anoNasc =  new java.sql.Date(this.getNascimentoData().getTime()).toLocalDate().getYear();
    int anoAtual = LocalDate.now().getYear();
    return (anoNasc >= anoAtual - 45 && anoNasc <= anoAtual - 17) ? false : true;
  }

  public boolean isAlistadoInternet() {
    // Usando atributo mobSetor como flag de alistamento internet
    return (this.getMobSetor() == null || this.getMobSetor() != 1) ? false : true;
  }

}
