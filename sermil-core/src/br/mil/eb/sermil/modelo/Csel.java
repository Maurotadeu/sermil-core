package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import br.mil.eb.sermil.core.exceptions.FuncionamentoJaExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoNaoExisteException;

/**
 * Comissao de Selecao
 * 
 * @author Anselmo Ribeiro
 * @since 5.2.3
 */
@Entity
@Table(name = "CSEL")
@NamedQueries({
   @NamedQuery(name = "Csel.listarPorRM", query = "select c from Csel c where c.rm.codigo = ?1 "),
      @NamedQuery(name = "Csel.listarPorNome", query = "select c from Csel c where c.nome = ?1 ") 
      })

public final class Csel implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -6654214842787703523L;

   /** TRIBUTACAO */
   public static final String TRIBUTACAO_EB = "EB";
   public static final String TRIBUTACAO_MAR = "MAR";
   public static final String TRIBUTACAO_FAB = "FAB";
   public static final String TRIBUTACAO_TG = "TG";

   @Id
   @GeneratedValue(strategy=GenerationType.TABLE, generator="CSEL")
   @TableGenerator(name="CSEL", allocationSize=1)
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "rm_codigo", referencedColumnName = "codigo", insertable = true, updatable = true, nullable = false)
   private Rm rm;
   
   @Column(nullable=false)
   private Integer numero;

   @Column(length = 60, nullable = false)
   private String nome;

   @Column(nullable = false)
   private String tributacao;

   @Column(nullable = false)
   private Integer atendimentos;

   public Csel() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(nome).append(" (").append("CS").append(numero).append("/").append(rm.getCodigo()).append("RM)").toString();
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

   public String getTributacao() {
      return this.tributacao;
   }

   public void setTributacao(String tributacao) {
      this.tributacao = tributacao;
   }

   public Integer getAtendimentos() {
      return atendimentos;
   }

   public void setAtendimentos(Integer atendimentos) {
      this.atendimentos = atendimentos;
   }

   public Integer getNumero() {
      return numero;
   }

   public void setNumero(Integer numero) {
      this.numero = numero;
   }

   /*
    * FUNCIONAMENTOS
    */
   @OneToMany(mappedBy = "csel", fetch = FetchType.LAZY)
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
      if (funcionamento.getCsel() == null || funcionamento.getCsel() != this)
         funcionamento.setCsel(this);
   }

   public void removeFuncionamento(CselFuncionamento funcionamento) throws FuncionamentoNaoExisteException {
      if (!this.funcionamentos.contains(funcionamento))
         throw new FuncionamentoNaoExisteException();
      this.funcionamentos.remove(funcionamento);
      funcionamento.setCsel(null);
   }

}
