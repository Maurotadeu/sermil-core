package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/** Endereço de funcionamento de CS.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 5.3.2
 */
@Entity
@Table(name = "CS_ENDERECO")
@NamedQueries({
   @NamedQuery(name = "CsEndereco.listarPorMunicipio", query = "select e from CsEndereco e where e.municipio.codigo = ?1"),
   @NamedQuery(name = "CsEndereco.listarPorRm", query = "select distinct(e) from CsEndereco e join Jsm j on e.municipio.codigo = j.municipio.codigo where j.csm.codigo in (select c.codigo from Csm c where c.rm.codigo = ?1)")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class CsEndereco implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CS_ENDERECO")
   @TableGenerator(name = "CS_ENDERECO", allocationSize = 1)
   private Integer codigo;

   @Column(length = 250, nullable = false)
   private String endereco;

   @Column(length = 250, nullable = false)
   private String bairro;

   @Column(length = 8)
   private String cep;

   @ManyToOne
   @JoinColumn(name = "municipio_codigo", referencedColumnName = "codigo", insertable = true, updatable = true, nullable = false)
   private Municipio municipio;

   //@OneToMany(mappedBy = "csEndereco", fetch = FetchType.LAZY)
   //private List<CsFuncionamento> csFuncionamentos;

   public CsEndereco() {
      super();
   }

   public String toString() {
      StringBuilder end = new StringBuilder().append(endereco).append(", ").append(bairro).append(", ").append(municipio.getDescricao()).append(", ").append(municipio.getUf().getSigla());
      if (cep != null)
         end.append(", CEP: ").append(cep);
      return end.toString();
   }

   public Municipio getMunicipio() {
      return municipio;
   }

   public void setMunicipio(Municipio municipio) {
      this.municipio = municipio;
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public String getEndereco() {
      return endereco;
   }

   public void setEndereco(String endereco) {
      this.endereco = endereco;
   }

   public String getBairro() {
      return bairro;
   }

   public void setBairro(String bairro) {
      this.bairro = bairro;
   }

   public String getCep() {
      return cep;
   }

   public void setCep(String cep) {
      this.cep = cep;
   }
/*
   public List<CsFuncionamento> getCsFuncionamentos() {
      return csFuncionamentos;
   }

   public void setCsFuncionamentos(List<CsFuncionamento> csFuncionamentos) {
      this.csFuncionamentos = csFuncionamentos;
   }

   public void addFuncionamento(CsFuncionamento csFuncionamento) throws FuncionamentoJaExisteException {
      if (this.csFuncionamentos.contains(csFuncionamento)) {
         throw new FuncionamentoJaExisteException();
      }
      if (this.csFuncionamentos == null) {
         this.csFuncionamentos = new ArrayList<CsFuncionamento>();
      }
      this.csFuncionamentos.add(csFuncionamento);
      if (csFuncionamento.getCsEndereco() == null || csFuncionamento.getCsEndereco() != this) {
         csFuncionamento.setCsEndereco(this);
      }
   }

   public void removeFuncionamento(CsFuncionamento csFuncionamento) throws FuncionamentoNaoExisteException {
      if (!this.csFuncionamentos.contains(csFuncionamento)) {
         throw new FuncionamentoNaoExisteException();
      }
      this.csFuncionamentos.remove(csFuncionamento);
      csFuncionamento.setCsEndereco(null);
   }
*/   
}
