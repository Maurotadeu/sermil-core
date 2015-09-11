package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 * Periodos e Locais de funcionamento das CSs.
 * 
 * @author Anselmo S Ribeiro
 * @since 5.2.0
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name = "CSEL_FUNCIONAMENTO")
@NamedQueries({ @NamedQuery(name = "Funcionamento.listarAnoBaseECselCodigo", query = "select f from CselFuncionamento f where f.anoBase = ?1 and f.csel.codigo = ?2 ") })
public final class CselFuncionamento implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -4504490870669249972L;

   @Id
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "cs_codigo", insertable = false, updatable = false, nullable = false)
   private Csel csel;

   @Column(name = "ANO_BASE")
   @Temporal(TemporalType.DATE)
   private Date anoBase;

   @Column(name = "INICIO_DATA")
   @Temporal(TemporalType.DATE)
   private Date inicioData;

   @Column(name = "TERMINO_DATA")
   @Temporal(TemporalType.DATE)
   private Date ternimoData;

   @Column(length = 100, nullable = false)
   private String endereco;

   @Column(length = 100, nullable = true)
   private String bairro;

   @Column(length = 8)
   private String cep;

   @ManyToOne
   @JoinColumn(name = "municipio_id", nullable = false, insertable = false, updatable = false)
   private Municipio municipio;

   @ManyToOne
   @JoinColumn(name = "pais_codigo", nullable = false, insertable = false, updatable = false)
   private Pais pais;

   @OneToMany(mappedBy = "cselFeriado", fetch = FetchType.EAGER, orphanRemoval = true)
   private List<CselFeriado> feriados;

   public CselFuncionamento() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(" Ano Base: ").append(anoBase).append(" Inicio: ").append(new SimpleDateFormat("dd/mm/yyyy").format(inicioData)).append(" Termino: ").append(new SimpleDateFormat("dd/mm/yyyy").format(ternimoData)).toString();
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

   public Date getAnoBase() {
      return anoBase;
   }

   public void setAnoBase(Date anoBase) {
      this.anoBase = anoBase;
   }

   public Date getInicioData() {
      return inicioData;
   }

   public void setInicioData(Date inicioData) {
      this.inicioData = inicioData;
   }

   public Date getTernimoData() {
      return ternimoData;
   }

   public void setTernimoData(Date ternimoData) {
      this.ternimoData = ternimoData;
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

   public Municipio getMunicipio() {
      return municipio;
   }

   public void setMunicipio(Municipio municipio) {
      this.municipio = municipio;
   }

   public Pais getPais() {
      return pais;
   }

   public void setPais(Pais pais) {
      this.pais = pais;
   }

   public List<CselFeriado> getFeriados() {
      return feriados;
   }

   public void setFeriados(List<CselFeriado> feriados) {
      this.feriados = feriados;
   }

}
