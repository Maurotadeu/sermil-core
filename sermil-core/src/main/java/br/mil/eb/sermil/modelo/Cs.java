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
 * @version 5.4
 */
@Entity
@Table(name = "CS")
@NamedQueries({
  @NamedQuery(name = "Cs.listarCsPorRm", query = "SELECT DISTINCT c.codigo, c.nome FROM Cs c WHERE c.rm.codigo = ?1"),
  @NamedQuery(name = "Cs.listarPorRm", query = "select c from Cs c where c.rm.codigo = ?1"),
  @NamedQuery(name = "Cs.listarPorNome", query = "select c from Cs c where c.nome = ?1") 
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Cs implements Serializable {

   private static final long serialVersionUID = 8846493879279340834L;

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
   //@JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO") Anselmo
   private List<CsExclusaoData> csExclusaoDataCollection;

   @OneToMany(mappedBy = "cs", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
   //@JoinColumn(name = "CS_CODIGO", referencedColumnName = "CODIGO") Anselmo
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
      result = prime * result + ((numero == null) ? 0 : numero.hashCode());
      result = prime * result + ((rm == null) ? 0 : rm.hashCode());
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
      if (numero == null) {
         if (other.numero != null)
            return false;
      } else if (!numero.equals(other.numero))
         return false;
      if (rm == null) {
         if (other.rm != null)
            return false;
      } else if (!rm.equals(other.rm))
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
         throw new SermilException("Data de Exclusão já existe");
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
         throw new SermilException("Funcionamento de CS já existe");
      }
      this.csFuncionamentoCollection.add(csFuncionamento);
      if (csFuncionamento.getCs() != this) {
         csFuncionamento.setCs(this);
      }
   }

   public void removeCsFuncionamento(CsFuncionamento csFuncionamento) throws SermilException {
      if (!this.csFuncionamentoCollection.contains(csFuncionamento)) {
         throw new SermilException("Funcionamento de CS não existe");
      }
      this.csFuncionamentoCollection.remove(csFuncionamento);
      csFuncionamento.setCs(null);
   }

}
