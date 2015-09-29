package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.FeriadoJaExisteException;

/**
 * Periodos e Locais de funcionamento das CSs.
 * 
 * @author Anselmo S Ribeiro
 * @since 5.2.0
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name = "CSEL_FUNCIONAMENTO")

@NamedQueries({ @NamedQuery(name = "Funcionamento.listarFuncionamentosDeCsel", query = "select f from CselFuncionamento f where f.csel.codigo = ?1 "),
/*      @NamedQuery(name = "listarFuncionamentosDeCsel", query = "select f from CselFuncionamento f, Csel cs where cs.codigo = ?1 "),*/
      @NamedQuery(name = "Funcionamento.listarAnoBaseECselCodigo", query = "select f from CselFuncionamento f where f.anoBase = ?1 and f.csel.codigo = ?2 ") })
@NamedNativeQueries({
   @NamedNativeQuery( resultClass=CselFuncionamento.class, name="listarFuncionamentosDeCsel", query="select f.* from csel cs inner join CSEL_FUNCIONAMENTO f on f.CSEL_CODIGO = cs.CODIGO where cs.codigo = ?1")
   
})

public final class CselFuncionamento implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -4504490870669249972L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CSEL_FUNCIONAMENTO")
   @TableGenerator(name = "CSEL_FUNCIONAMENTO", allocationSize = 1)
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "csel_codigo", referencedColumnName = "codigo", insertable = true, updatable = true, nullable = false)
   private Csel csel;

   @Column(name = "ano_base", nullable = false, length = 4)
   private String anoBase;

   @Column(name = "inicio_data", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date inicioData;

   @Column(name = "termino_data", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date terminoData;

   @ManyToOne
   @JoinColumn(name = "csel_endereco_codigo", referencedColumnName = "codigo", insertable = true, updatable = true, nullable = false)
   private CselEndereco endereco;

   public CselFuncionamento() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder("Ano Base: ").append(anoBase).toString();
   }

   public CselEndereco getEndereco() {
      return endereco;
   }

   public void setEndereco(CselEndereco endereco) {
      this.endereco = endereco;
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public Csel getCsel() {
      return csel;
   }

   public void setCsel(Csel csel) {
      this.csel = csel;
   }

   public String getAnoBase() {
      return anoBase;
   }

   public void setAnoBase(String anoBase) {
      this.anoBase = anoBase;
   }

   public Date getInicioData() {
      return inicioData;
   }

   public void setInicioData(Date inicioData) {
      this.inicioData = inicioData;
   }

   public Date getTerminoData() {
      return terminoData;
   }

   public void setTerminoData(Date terminoData) {
      this.terminoData = terminoData;
   }

   /*
    * FERIADOS
    */
   @OneToMany(mappedBy = "funcionamento", fetch = FetchType.EAGER, orphanRemoval=true)
   private List<CselFeriado> feriados;

   public List<CselFeriado> getFeriados() {
      return feriados;
   }

   public void setFeriados(List<CselFeriado> feriados) {
      this.feriados = feriados;
   }

   public void addFeriado(CselFeriado feriado) throws FeriadoJaExisteException {
      if (this.feriados.contains(feriado))
         throw new FeriadoJaExisteException();
      if (this.feriados == null)
         this.feriados = new ArrayList<CselFeriado>();
      this.feriados.add(feriado);
      if (feriado.getFuncionamento() == null || feriado.getFuncionamento() != this)
         feriado.setFuncionamento(this);
   }

}
