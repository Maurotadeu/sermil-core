package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.NamedStoredFunctionQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Boletim de Necessidade de OM (DSTB_BOLNEC).
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id: DstbBolNec.java 2433 2014-05-22 19:16:47Z wlopes $
 */
@Entity
@Table(name = "DSTB_BOLNEC")
@NamedQueries({
  @NamedQuery(name = "DstbBolNec.listarOm", query = "SELECT o FROM Om o WHERE EXISTS (SELECT DISTINCT b FROM DstbBolNec b WHERE b.pk.omCodigo = o.codigo) ORDER BY o.descricao"),
  @NamedQuery(name = "DstbBolNec.listarPorRm", query = "SELECT b FROM DstbBolNec b WHERE b.om.rm.codigo = ?1 order BY b.om.codigo"),
  @NamedQuery(name = "DstbBolNec.listarPorOm", query = "SELECT b FROM DstbBolNec b WHERE b.pk.omCodigo = ?1 ORDER BY b.pk.numero")
})
@NamedStoredFunctionQuery(name="DstbBolNec.majorar", functionName="dis_bolnec.majorar", returnParameter=@StoredProcedureParameter(queryParameter="MSG"))
//@NamedPLSQLStoredFunctionQuery(name="DstbBolNec.majorar", functionName="dis_bolnec.majorar", returnParameter=@PLSQLParameter(name="RESULT", databaseType="VARCHAR2(2000)"))
public final class DstbBolNec implements Comparable<DstbBolNec>, Serializable {

  private static final long serialVersionUID = 2079976557988778936L;

  @EmbeddedId
  private PK pk;
  
  private Short nec;
  
  private Short maj;

  @Column(name="CRIACAO_DATA", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date criacaoData;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "USUARIO_CPF", nullable = false)
  private Usuario usuario;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "OM_CODIGO", nullable = false, insertable = false, updatable = false)
  private Om om;

  @OneToMany(mappedBy="bolnec", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
  private List<DstbNecessidade> dstbNecessidadeCollection;

  public DstbBolNec() {
    this.setPk(new PK());
  }

  public DstbBolNec(final Integer codom, final String gpt, final Short numero) {
    this();
    this.getPk().setOmCodigo(codom);
    this.getPk().setGptIncorp(gpt);
    this.getPk().setNumero(numero);
  }

  @Override
  public String toString() {
    return this.getPk().toString();
  }
  
  @Override
  public int compareTo(DstbBolNec o) {
    return this.getPk().compareTo(o.getPk());
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
    DstbBolNec other = (DstbBolNec) obj;
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
  
  public Om getOm() {
    return this.om;
  }

  public void setOm(Om om) {
    this.om = om;
  }
  
  public Usuario getUsuario() {
    return this.usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
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
  
  public Date getCriacaoData() {
    return this.criacaoData;
  }

  public void setCriacaoData(Date criacaoData) {
    this.criacaoData = criacaoData;
  }

  public List<DstbNecessidade> getDstbNecessidadeCollection() {
    return this.dstbNecessidadeCollection;
  }

  public void setDstbNecessidadeCollection(List<DstbNecessidade> dstbNecessidadeCollection) {
    this.dstbNecessidadeCollection = dstbNecessidadeCollection;
  }

  public void addDstbNecessidade(final DstbNecessidade nec) throws SermilException {
    if (this.getDstbNecessidadeCollection() == null) {
      this.setDstbNecessidadeCollection(new ArrayList<DstbNecessidade>(1));
    }
    if (this.getDstbNecessidadeCollection().contains(nec)) {
      this.getDstbNecessidadeCollection().get(this.getDstbNecessidadeCollection().indexOf(nec)).setNecHist(nec.getNecHist());;
    } else {
      this.getDstbNecessidadeCollection().add(nec);
    }
    if (nec.getBolnec() != this) {
      nec.setBolnec(this);
    }
  }  
  
  /** Chave primária (PK) de DstbBolNec.
   * @author Abreu Lopes
   * @since 3.4
   * @version $Id: DstbBolNec.java 2433 2014-05-22 19:16:47Z wlopes $
   */
  @Embeddable
  public static class PK implements Comparable<DstbBolNec.PK>, Serializable {

    private static final long serialVersionUID = -5068221055143857096L;

    @Column(name = "OM_CODIGO")
    private Integer omCodigo;

    @Column(name = "GPT_INCORP")
    private String gptIncorp;

    private Short numero;

    public PK() {
      super();
    }
    
    public PK(final Integer omCodigo, final String gptIncorp, final Short numero) {
      super();
      this.setOmCodigo(omCodigo);
      this.setGptIncorp(gptIncorp);
      this.setNumero(numero);
    }

    @Override
    public String toString() {
      return new StringBuilder()
        .append(this.getOmCodigo() == null ? "OM" : this.getOmCodigo())
        .append(" - Gpt ")
        .append(this.getGptIncorp() == null ? "GPT" : this.getGptIncorp())
        .append(" - Bol Nr ")
        .append(this.getNumero() == null ? "NR" : this.getNumero())
        .toString();
    }

    @Override
    public int compareTo(PK o) {
      int status = this.getOmCodigo().compareTo(o.getOmCodigo());
      if (status == 0) {
        status = this.getGptIncorp().compareTo(o.getGptIncorp());
        if (status == 0) {
          status = this.getNumero().compareTo(o.getNumero());
        }
      }
      return status;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.gptIncorp == null) ? 0 : this.gptIncorp.hashCode());
      result = prime * result + ((this.numero == null) ? 0 : this.numero.hashCode());
      result = prime * result + ((this.omCodigo == null) ? 0 : this.omCodigo.hashCode());
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
      if (this.gptIncorp == null) {
        if (other.gptIncorp != null)
          return false;
      } else if (!this.gptIncorp.equals(other.gptIncorp))
        return false;
      if (this.numero == null) {
        if (other.numero != null)
          return false;
      } else if (!this.numero.equals(other.numero))
        return false;
      if (this.omCodigo == null) {
        if (other.omCodigo != null)
          return false;
      } else if (!this.omCodigo.equals(other.omCodigo))
        return false;
      return true;
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

    public Short getNumero() {
      return this.numero;
    }

    public void setNumero(Short numero) {
      this.numero = numero;
    }

  }

}
