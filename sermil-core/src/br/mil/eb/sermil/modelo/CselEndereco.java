package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.FuncionamentoJaExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoNaoExisteException;

/**
 * Periodos e Locais de funcionamento das CSs.
 * 
 * @author Anselmo S Ribeiro
 * @since 5.2.3
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name = "CSEL_ENDERECO")
@NamedQueries({ 
   @NamedQuery(name = "Endereco.listarPorMunicipio", query = "select e from CselEndereco e where e.municipio.codigo = ?1 ") 
})
@NamedNativeQueries({
      @NamedNativeQuery(name = "listarEnderecosDeCselNative", query = "SELECT e.* from csel inner join CSEL_FUNCIONAMENTO f on f.csel_codigo = csel.codigo inner join CSEL_ENDERECO e on f.CSEL_ENDERECO_CODIGO = e.CODIGO where csel.CODIGO = ?1 ") 
})
public final class CselEndereco implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -1323553766132510978L;

   @Id
   private Integer codigo;

   @Column(length = 100, nullable = false)
   private String endereco;

   @Column(length = 100)
   private String bairro;

   @Column(length = 8)
   private String cep;

   @ManyToOne
   @JoinColumn(name = "municipio_codigo", referencedColumnName = "codigo", insertable = false, updatable = false, nullable = false)
   private Municipio municipio;

   public CselEndereco() {

   }

   public String toString() {
      return new StringBuilder().append(endereco).append(", ").append(bairro).append(", ").append(municipio.getSigla()).append(", ").append(municipio.getUf().getSigla()).append(", CEP: ").append(cep).toString();
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
    * FUNCIONAMENTOS
    */
   @OneToMany(mappedBy = "endereco", fetch = FetchType.LAZY)
   private List<CselFuncionamento> funcionamentos;

   public List<CselFuncionamento> getFuncionamentos() {
      return funcionamentos;
   }

   public void setFuncionamentos(List<CselFuncionamento> funcionamentos) {
      this.funcionamentos = funcionamentos;
   }

   public void addFuncionamento(CselFuncionamento funcionamento) throws FuncionamentoJaExisteException {
      if (this.funcionamentos.contains(funcionamento))
         throw new FuncionamentoJaExisteException();
      if (this.funcionamentos == null)
         this.funcionamentos = new ArrayList<CselFuncionamento>();
      this.funcionamentos.add(funcionamento);
      if (funcionamento.getEndereco() == null || funcionamento.getEndereco() != this)
         funcionamento.setEndereco(this);
   }

   public void removeFuncionamento(CselFuncionamento funcionamento) throws FuncionamentoNaoExisteException {
      if (!this.funcionamentos.contains(funcionamento))
         throw new FuncionamentoNaoExisteException();
      this.funcionamentos.remove(funcionamento);
      funcionamento.setEndereco(null);
   }
}
