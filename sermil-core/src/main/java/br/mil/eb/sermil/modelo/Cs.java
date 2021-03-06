package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Entidade CS (Comissao de Selecao).
 * @author Anselmo Ribeiro, Abreu lopes
 * @since 5.2.3
 * @version 5.4.6
 */
@Entity
@Table(name = "CS")
@NamedQueries({
  @NamedQuery(name = "Cs.listarCsPorRm",  query = "SELECT DISTINCT c.codigo, c.nome FROM Cs c WHERE c.rm.codigo = ?1 ORDER BY c.numero"),
  @NamedQuery(name = "Cs.listarPorRm",    query = "select c from Cs c where c.rm.codigo = ?1 ORDER BY c.numero"),
  @NamedQuery(name = "Cs.listarPorNome",  query = "select c from Cs c where c.nome = ?1 ORDER BY c.nome"),
  @NamedQuery(name = "Cs.listarPorRmEnd", query = "SELECT c.codigo, c.numero, c.nome, c.atende, f.inicioData, f.terminoData, CONCAT(e.endereco,', ',e.bairro,', ',e.cep), CONCAT(m.descricao,' - ',m.uf.sigla) FROM Cs c JOIN c.csFuncionamentoCollection f JOIN f.csEndereco e JOIN e.municipio m WHERE c.rm.codigo = ?1 ORDER BY c.numero, f.inicioData")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Cs implements Comparable<Cs>, Serializable {

  private static final long serialVersionUID = -5282034949837118064L;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CS")
  @TableGenerator(name="CS", allocationSize=1)
  private Integer codigo;

  @Column
  private Integer atende;

  @Column
  private String nome;

  @Column
  private Integer numero;

  @ManyToOne(cascade=CascadeType.REFRESH)
  @JoinColumn(name = "RM_CODIGO", referencedColumnName = "CODIGO", nullable = false)
  private Rm rm;

  @OneToMany(mappedBy = "cs", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
  @JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO")
  private List<CsExclusaoData> csExclusaoDataCollection;

  @OneToMany(mappedBy = "cs", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
  @JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO")
  private List<CsFuncionamento> csFuncionamentoCollection;

  public Cs() {
    super();
  }

  @Override
  public String toString() {
    return new StringBuilder("CS ").append(new DecimalFormat("000").format(this.getNumero()))
        .append("/").append(new DecimalFormat("00").format(this.getRm().getCodigo())).toString();
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
    Cs other = (Cs) obj;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public Rm getRm() {
    return rm;
  }

  public void setRm(Rm rm) {
    this.rm = rm;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getAtende() {
    return atende;
  }

  public void setAtende(Integer atende) {
    this.atende = atende;
  }

  public Integer getNumero() {
    return numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public List<CsExclusaoData> getCsExclusaoDataCollection() {
    return csExclusaoDataCollection;
  }

  public void setCsExclusaoDataCollection(List<CsExclusaoData> csExclusaoDataCollection) {
    this.csExclusaoDataCollection = csExclusaoDataCollection;
  }

  public void addCsExclusaoData(final CsExclusaoData csExclusaoData) throws SermilException {
    if (this.csExclusaoDataCollection == null) {
      this.csExclusaoDataCollection = new ArrayList<CsExclusaoData>();
    }
    if (this.csExclusaoDataCollection.contains(csExclusaoData)) {
      throw new SermilException("Data de Exclus�o j� existe");
    }
    this.csExclusaoDataCollection.add(csExclusaoData);
    if (csExclusaoData.getCs() != this) {
      csExclusaoData.setCs(this);
    }
  }

  public List<CsFuncionamento> getCsFuncionamentoCollection() {
    return csFuncionamentoCollection;
  }

  public void setCsFuncionamentoCollection(List<CsFuncionamento> csFuncionamentoCollection) {
    this.csFuncionamentoCollection = csFuncionamentoCollection;
  }

  public void addCsFuncionamento(CsFuncionamento csFuncionamento) throws SermilException {
    if (this.csFuncionamentoCollection == null) {
      this.csFuncionamentoCollection = new ArrayList<CsFuncionamento>();
    }
    if (this.csFuncionamentoCollection.contains(csFuncionamento)) {
      throw new SermilException("Funcionamento de CS j� existe");
    }
    this.csFuncionamentoCollection.add(csFuncionamento);
    if (csFuncionamento.getCs() != this) {
      csFuncionamento.setCs(this);
    }
  }

  public void removeCsFuncionamento(CsFuncionamento csFuncionamento) throws SermilException {
    if (!this.csFuncionamentoCollection.contains(csFuncionamento)) {
      throw new SermilException("Funcionamento de CS n�o existe");
    }
    this.csFuncionamentoCollection.remove(csFuncionamento);
    csFuncionamento.setCs(null);
  }

  @Override
  public int compareTo(Cs o) {
    return this.getNumero() != null ? this.getNumero().compareTo(o.getNumero()) : this.getCodigo().compareTo(o.getCodigo());
  }

}
