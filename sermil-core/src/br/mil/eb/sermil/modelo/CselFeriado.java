package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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

@NamedQueries({ 
   @NamedQuery(name = "Endereco.listarPorFuncionamento", query = "select f from CselFeriado f where f.funcionamento.codigo = ?1 ") 
   })

public final class CselFeriado implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 2174684503038866912L;

   @Id
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "csel_funcionamento_codigo", referencedColumnName = "codigo", insertable = false, updatable = false, nullable = false)
   private CselFuncionamento funcionamento;

   @Column(nullable = false)
   @Temporal(TemporalType.DATE)
   private Date data;

   public CselFeriado() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(new SimpleDateFormat("dd/mm/yyyy").format(data)).toString();
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

}
