package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 * Periodos e Locais de funcionamento das CSs.
 * 
 * @author Anselmo S Ribeiro
 * @since 5.2.3
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name = "CSEL_FERIADO")

@NamedQueries({ @NamedQuery(name = "Endereco.listarPorFuncionamento", query = "select f from CselFeriado f where f.funcionamento.codigo = ?1 ") })

public final class CselFeriado implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 2174684503038866912L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "CSEL_FERIADO")
   @TableGenerator(name = "CSEL_FERIADO", allocationSize = 1)
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "csel_funcionamento_codigo", referencedColumnName = "codigo", insertable = true, updatable = true, nullable = false )
   private CselFuncionamento funcionamento;

   @Column(nullable = false, name = "FER_DATA")
   @Temporal(TemporalType.DATE)
   private Date feriadoData;

   public CselFeriado() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(new SimpleDateFormat("dd/mm/yyyy").format(feriadoData)).toString();
   }

   public CselFuncionamento getFuncionamento() {
      return funcionamento;
   }

   public void setFuncionamento(CselFuncionamento funcionamento) {
      this.funcionamento = funcionamento;
   }

   public Integer getCodigo() {
      return codigo;
   }

   public Date getFeriadoData() {
      return feriadoData;
   }

   public void setFeriadoData(Date feriadoData) {
      this.feriadoData = feriadoData;
   }

}
