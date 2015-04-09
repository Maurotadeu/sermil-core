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

/** Necessidades do Boletim de Necessidade de OM.
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: DstbNecessidade.java 2433 2014-05-22 19:16:47Z wlopes $
 */
@Entity
@Table(name = "DSTB_NECESSIDADE")
public final class DstbNecessidade implements Comparable<DstbNecessidade>, Serializable {

  private static final long serialVersionUID = -5462112339938354892L;

  @EmbeddedId
  private PK pk;

  private Short nec;

  private Short maj;

  @Column(name="NEC_HIST")
  private Short necHist;

  @Column(name="MAJ_HIST")
  private Short majHist;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(name = "OM_CODIGO", referencedColumnName = "OM_CODIGO", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "GPT_INCORP", referencedColumnName = "GPT_INCORP", insertable = false, updatable = false, nullable = false),
    @JoinColumn(name = "BOLNEC_NR", referencedColumnName = "NUMERO", insertable = false, updatable = false, nullable = false)
  })
  private DstbBolNec bolnec;

  public DstbNecessidade() {
    this.setPk(new PK());
  }

  public DstbNecessidade(final Integer codom, final String gpt, final Short nr, final String padrao) {
    this();
    this.getPk().setOmCodigo(codom);
    this.getPk().setGptIncorp(gpt);
    this.getPk().setBolnecNr(nr);
    this.getPk().setPadraoCodigo(padrao);
  }

  @Override
  public int compareTo(DstbNecessidade o) {
    return this.getPk().compareTo(o.getPk());
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
    DstbNecessidade other = (DstbNecessidade) obj;
    if (pk == null) {
      if (other.pk != null)
        return false;
    } else if (!pk.equals(other.pk))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getPk() == null || this.getPk().getPadraoCodigo() == null? "PADRAO" : this.getPk().getPadraoCodigo())
    .append(": NEC=")
    .append(this.getNecHist() == null ? "0" : this.getNecHist())
    .append(" - MAJ=")
    .append(this.getMajHist()== null ? "0" : this.getMajHist())
    .toString();
  }

  public PK getPk() {
    return this.pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public Short getNec() {
    return this.nec;
  }

  public void setNec(Short nec) {
    this.nec = nec;
  }

  public Short getMaj() {
    return this.maj;
  }

  public void setMaj(Short maj) {
    this.maj = maj;
  }

  public Short getNecHist() {
    return this.necHist;
  }

  public void setNecHist(Short necHist) {
    this.necHist = necHist;
  }

  public Short getMajHist() {
    return this.majHist;
  }

  public void setMajHist(Short majHist) {
    this.majHist = majHist;
  }

  public DstbBolNec getBolnec() {
    return bolnec;
  }

  public void setBolnec(DstbBolNec bolnec) {
    this.bolnec = bolnec;
    this.getPk().setOmCodigo(bolnec.getPk().getOmCodigo());
    this.getPk().setGptIncorp(bolnec.getPk().getGptIncorp());
    this.getPk().setBolnecNr(bolnec.getPk().getNumero());
  }

  /** Chave primária (PK) de DstbNecessidade.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: DstbNecessidade.java 2433 2014-05-22 19:16:47Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<DstbNecessidade.PK>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -8332077063488585338L;

    @Column(name = "OM_CODIGO")
    private Integer omCodigo;

    @Column(name = "GPT_INCORP")
    private String gptIncorp;

    @Column(name="BOLNEC_NR")
    private Short bolnecNr;

    @Column(name="PADRAO_CODIGO")
    private String padraoCodigo;

    /** Construtor. */
    public PK() {
      super();
    }
    
    /** Construtor inicializando atributos com os parâmetros informados.
     * @param omCodigo CODOM
     * @param gptIncorp Grupamento de incorporação
     * @param bolnecNr Número do Bol Nec
     * @param padraoCodigo Padrão
     */
    public PK(final Integer omCodigo, final String gptIncorp, final Short bolnecNr, final String padraoCodigo) {
      super();
      this.setOmCodigo(omCodigo);
      this.setGptIncorp(gptIncorp);
      this.setBolnecNr(bolnecNr);
      this.setPadraoCodigo(padraoCodigo);
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((bolnecNr == null) ? 0 : bolnecNr.hashCode());
      result = prime * result + ((gptIncorp == null) ? 0 : gptIncorp.hashCode());
      result = prime * result + ((omCodigo == null) ? 0 : omCodigo.hashCode());
      result = prime * result + ((padraoCodigo == null) ? 0 : padraoCodigo.hashCode());
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
      if (bolnecNr == null) {
        if (other.bolnecNr != null)
          return false;
      } else if (!bolnecNr.equals(other.bolnecNr))
        return false;
      if (gptIncorp == null) {
        if (other.gptIncorp != null)
          return false;
      } else if (!gptIncorp.equals(other.gptIncorp))
        return false;
      if (omCodigo == null) {
        if (other.omCodigo != null)
          return false;
      } else if (!omCodigo.equals(other.omCodigo))
        return false;
      if (padraoCodigo == null) {
        if (other.padraoCodigo != null)
          return false;
      } else if (!padraoCodigo.equals(other.padraoCodigo))
        return false;
      return true;
    }

    @Override
    public int compareTo(PK o) {
      int status = this.getOmCodigo().compareTo(o.getOmCodigo());
      if (status == 0) {
        status = this.getGptIncorp().compareTo(o.getGptIncorp());
        if (status == 0) {
          status = this.getBolnecNr().compareTo(o.getBolnecNr());
          if (status == 0) {
            status = this.getPadraoCodigo().compareTo(o.getPadraoCodigo());
          }
        }
      }
      return status;
    }
    
    @Override
    public String toString() {
      return new StringBuilder("OM: ")
      .append(this.getOmCodigo() == null ? "CODOM" : this.getOmCodigo())
      .append(" - Gpt: ")
      .append(this.getGptIncorp() == null ? "GPT" : this.getGptIncorp())
      .append(" - Nr: ")
      .append(this.getBolnecNr() == null ? "NR" : this.getBolnecNr())
      .append(" - PAD: ")
      .append(this.getPadraoCodigo() == null ? "PAD" : this.getPadraoCodigo())
      .toString();
    }

    public Integer getOmCodigo() {
      return this.omCodigo;
    }

    public void setOmCodigo(Integer omCodigo) {
      this.omCodigo = omCodigo;
    }

    public String getGptIncorp() {
      return this.gptIncorp;
    }

    public void setGptIncorp(String gptIncorp) {
      this.gptIncorp = gptIncorp;
    }


    public Short getBolnecNr() {
      return this.bolnecNr;
    }

    public void setBolnecNr(Short bolnecNr) {
      this.bolnecNr = bolnecNr;
    }

    public String getPadraoCodigo() {
      return this.padraoCodigo;
    }

    public void setPadraoCodigo(String padraoCodigo) {
      this.padraoCodigo = padraoCodigo;
    }

  }

}
