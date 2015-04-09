package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.mil.eb.sermil.core.exceptions.SermilException;

/**
 * Serviço da entrada de dados (IMP_SERVICO).
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: ImpServico.java 2441 2014-05-29 17:16:31Z wlopes $
 */
@Entity
@Table(name = "IMP_SERVICO")
public final class ImpServico implements Comparable<ImpServico>, Serializable {

  private static final long serialVersionUID = -8735022797336621747L;

  /** Indicador da versão do arquivo. */
  private static final String VERSAO = "00004";

  @EmbeddedId
  private PK pk;

  @Column(name = "GRAVACAO_DATA")
  @Temporal(TemporalType.DATE)
  private Date gravacaoData;

  @Column(name = "GR_NR")
  private String grNr;

  @Column(name = "GR_EMISSAO_DATA")
  private String grEmissaoData;

  @Column(name = "GR_ENTRADA_DATA")
  private String grEntradaData;

  @Column(name = "NOME_ARQUIVO")
  private String nomeArquivo;

  @Column(name = "REGISTROS_QTD")
  private Integer registrosQtd;

  @ManyToOne
  @JoinColumn(name = "USUARIO", referencedColumnName = "CPF", updatable = false, nullable = false)
  private Usuario usuario;

  @Column(name = "USUARIO_DATA")
  @Temporal(TemporalType.DATE)
  private Date usuarioData;

  @OneToMany(mappedBy = "impServico", fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<ImpDocumento> impDocumentoCollection;

  @Transient
  private Integer tipo;

  @Transient
  private String versao;

  @Transient
  private Integer rm;

  public ImpServico() {
    this.setPk(new PK());
  }

  public ImpServico(String linha) throws SermilException {
    this();
    this.decode(linha);
  }

  @Override
  public int compareTo(ImpServico o) {
    return this.getPk().compareTo(o.getPk());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk().toString()).append(" - DATA: ").append(this.getGravacaoData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getGravacaoData())).append(" - REG: ")
        .append(this.getRegistrosQtd() == null ? "REGISTROS" : this.getRegistrosQtd()).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.pk == null) ? 0 : this.pk.hashCode());
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
    ImpServico other = (ImpServico) obj;
    if (this.pk == null) {
      if (other.pk != null)
        return false;
    } else if (!this.pk.equals(other.pk))
      return false;
    return true;
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Date getGravacaoData() {
    return this.gravacaoData;
  }

  public void setGravacaoData(Date gravacaoData) {
    this.gravacaoData = gravacaoData;
  }

  public String getGrNr() {
    return this.grNr;
  }

  public void setGrNr(String grNr) {
    this.grNr = grNr;
  }

  public String getGrEmissaoData() {
    return this.grEmissaoData;
  }

  public void setGrEmissaoData(String grEmissaoData) {
    this.grEmissaoData = grEmissaoData;
  }

  public String getGrEntradaData() {
    return this.grEntradaData;
  }

  public void setGrEntradaData(String grEntradaData) {
    this.grEntradaData = grEntradaData;
  }

  public String getNomeArquivo() {
    return this.nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public Integer getRegistrosQtd() {
    return this.registrosQtd;
  }

  public void setRegistrosQtd(Integer registrosQtd) {
    this.registrosQtd = registrosQtd;
  }

  public Set<ImpDocumento> getImpDocumentoCollection() {
    return this.impDocumentoCollection;
  }

  public void setImpDocumentoCollection(Set<ImpDocumento> impDocumentoCollection) {
    this.impDocumentoCollection = impDocumentoCollection;
  }

  public Integer getTipo() {
    return this.tipo;
  }

  public void setTipo(Integer tipo) throws SermilException {
    if (tipo != 1 && tipo != 2 && tipo != 6) {
      throw new SermilException(new StringBuilder("ERRO: tipo de arquivo inválido (tipo=").append(tipo).append(", aceitos 1 = FAMCO, 2 = FS ou 6 = FAMSEL).").toString());
    }
    this.tipo = tipo;
  }

  public String getVersao() {
    return this.versao;
  }

  public void setVersao(String versao) throws SermilException {
    if (!VERSAO.equals(versao)) {
      throw new SermilException("ERRO: versão do arquivo inválida, atualize a versão do SASM ou Módulo CS.");
    }
    this.versao = versao;
  }

  public Integer getRm() {
    return this.rm;
  }

  public void setRm(Integer rm) {
    this.rm = rm;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Date getUsuarioData() {
    return usuarioData;
  }

  public void setUsuarioData(Date usuarioData) {
    this.usuarioData = usuarioData;
  }

  public void addImpDocumento(final ImpDocumento id) throws SermilException {
    if (this.getImpDocumentoCollection() == null) {
      this.setImpDocumentoCollection(new HashSet<ImpDocumento>(1));
    }
    if (this.getImpDocumentoCollection().contains(id)) {
      throw new SermilException("Documento já existe");
    }
    this.getImpDocumentoCollection().add(id);
    if (id.getImpServico() != this) {
      id.setImpServico(this);
    }
  }

  public void decode(final String linha) throws NumberFormatException, SermilException {
    final Calendar cal = Calendar.getInstance();
    Integer n1;
    Integer n2;
    Integer n3;
    try {
      n1 = Integer.parseInt(linha.substring(15, 19));
      n2 = Integer.parseInt(linha.substring(13, 15)) - 1;
      n3 = Integer.parseInt(linha.substring(11, 13));
    } catch (Exception e) {
      throw new SermilException("Cabecalho do arquivo nao reconhecido. Confira se sua versao de SASM é a mais atual.");
    }
    cal.set(n1, n2, n3);
    this.setTipo(Integer.valueOf(linha.substring(0, 1)));
    this.setRm(Integer.valueOf(linha.substring(1, 3)));
    this.getPk().setCodigo(linha.substring(0, 11));
    this.getPk().setAno(Integer.parseInt(linha.substring(15, 19)));
    this.setNomeArquivo(linha.substring(0, 11) + ".dat");
    this.setGrNr(linha.substring(19, 22));
    this.setRegistrosQtd(Integer.parseInt(linha.substring(22, 29)));
    this.setGrEntradaData(DateFormat.getDateInstance().format(cal.getTime()).substring(0, 5));
    this.setGravacaoData(cal.getTime());
    this.setGrEmissaoData(DateFormat.getDateInstance().format(cal.getTime()).substring(0, 5));
    this.setVersao(linha.substring(29).trim());
  }

  /**
   * Chave primária (PK) de ImpServico.
   * 
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: ImpServico.java 2441 2014-05-29 17:16:31Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<ImpServico.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -1402002723620654401L;

    private String codigo;

    private Integer ano;

    public PK() {
      super();
    }

    public PK(final String codigo, final Integer ano) {
      super();
      this.setCodigo(codigo);
      this.setAno(ano);
    }

    @Override
    public int compareTo(PK o) {
      return this.getAno().compareTo(o.getAno() == 0 ? this.getCodigo().compareTo(o.getCodigo()) : this.getAno().compareTo(o.getAno()));
    }

    public String getCodigo() {
      return this.codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    public Integer getAno() {
      return this.ano;
    }

    public void setAno(Integer ano) {
      this.ano = ano;
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
      return this.codigo.equals(other.codigo) && (this.ano == other.ano);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + this.codigo.hashCode();
      hash = hash * prime + ((int) (this.ano ^ (this.ano >>> 32)));
      return hash;
    }

    @Override
    public String toString() {
      return new StringBuilder("COD: ").append(this.getCodigo() == null ? "NULO" : this.getCodigo()).append(" - ANO: ").append(this.getAno() == null ? "NULO" : this.getAno()).toString();
    }

  }

}
