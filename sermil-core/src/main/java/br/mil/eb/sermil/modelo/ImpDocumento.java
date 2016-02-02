package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Documento da entrada de dados (IMP_DOCUMENTO).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: ImpDocumento.java 2441 2014-05-29 17:16:31Z wlopes $
 */
@Entity
@Table(name = "IMP_DOCUMENTO")
public final class ImpDocumento implements Serializable {

  private static final long serialVersionUID = -8477865526394457022L;

  @EmbeddedId
  private PK pk;

  @Column(name = "DOC_TIPO")
  private Integer docTipo;

  @Column(name = "MENSAGEM_CODIGO")
  private Byte mensagemCodigo;

  private String informacao;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "SERVICO_CODIGO", referencedColumnName = "CODIGO", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "SERVICO_ANO", referencedColumnName = "ANO", insertable = false, updatable = false, nullable = false)
  })
  private ImpServico impServico;

  public ImpDocumento() {
    this.setPk(new PK());
  }

  public ImpDocumento(String codigo, Integer ano, Integer tarefa, Integer doc) {
    this.setPk(new PK(codigo, ano, tarefa, doc));
  }

  @Override
  public String toString() {
    return this.getPk().toString();
  } 

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Integer getDocTipo() {
    return this.docTipo;
  }

  public void setDocTipo(Integer docTipo) {
    this.docTipo = docTipo;
  }

  public Byte getMensagemCodigo() {
    return this.mensagemCodigo;
  }

  public void setMensagemCodigo(Byte mensagemCodigo) {
    this.mensagemCodigo = mensagemCodigo;
  }

  public String getInformacao() {
    return this.informacao;
  }

  public void setInformacao(String informacao) {
    this.informacao = informacao;
  }

  public ImpServico getImpServico() {
    return this.impServico;
  }

  public void setImpServico(ImpServico impServico) {
    this.impServico = impServico;
  }

  public void decode(String linha) {
    this.setInformacao(linha);
  }
  
  /** Chave primária (PK) de ImpDocumento.
   * @author Abreu Lopes
   * @since 3.0
   * @version $Id: ImpDocumento.java 2441 2014-05-29 17:16:31Z wlopes $
   */
  @Embeddable
  public static class PK implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 3286581881983292244L;

    @Column(name = "SERVICO_CODIGO")
    private String servicoCodigo;

    @Column(name = "SERVICO_ANO")
    private Integer servicoAno;

    private Integer tarefa;

    @Column(name = "DOC_NR")
    private Integer docNr;

    public PK() {
      super();
    }
    
    public PK(final String servicoCodigo, final Integer servicoAno, final Integer tarefa, final Integer docNr) {
      super();
      this.setServicoCodigo(servicoCodigo);
      this.setServicoAno(servicoAno);
      this.setTarefa(tarefa);
      this.setDocNr(docNr);
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getServicoCodigo() == null ? "NULO" : this.getServicoCodigo())
                 .append(" - ")
                 .append(this.getServicoAno() == null ? "NULO" : this.getServicoAno())
                 .append(" - ")
                 .append(this.getTarefa() == null ? "NULO" : this.getTarefa())
                 .append(" - ")
                 .append(this.getDocNr() == null ? "NULO" : this.getDocNr())
                 .toString();
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
      return this.servicoCodigo.equals(other.servicoCodigo) &&
             this.servicoAno == other.servicoAno &&
             this.tarefa == other.tarefa &&
             this.docNr == other.docNr;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int hash = 17;
      hash = hash * prime + this.servicoCodigo.hashCode();
      hash = hash * prime + ((int) (this.servicoAno ^ (this.servicoAno >>> 32)));
      hash = hash * prime + ((int) (this.tarefa ^ (this.tarefa >>> 32)));
      hash = hash * prime + ((int) (this.docNr ^ (this.docNr >>> 32)));
      return hash;
    }

    public String getServicoCodigo() {
      return this.servicoCodigo;
    }

    public void setServicoCodigo(String servicoCodigo) {
      this.servicoCodigo = servicoCodigo;
    }

    public Integer getServicoAno() {
      return this.servicoAno;
    }

    public void setServicoAno(Integer servicoAno) {
      this.servicoAno = servicoAno;
    }

    public Integer getTarefa() {
      return this.tarefa;
    }

    public void setTarefa(Integer tarefa) {
      this.tarefa = tarefa;
    }

    public Integer getDocNr() {
      return this.docNr;
    }

    public void setDocNr(Integer docNr) {
      this.docNr = docNr;
    }
    
  }

}
