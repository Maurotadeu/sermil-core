package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "CSEL_ENDERECO")
@NamedQueries({ @NamedQuery(name = "Endereco.listar", query = "select f from CselFuncionamento f where f.anoBase = ?1 and f.csel.codigo = ?2 ") })
public final class CselFeriado implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 2174684503038866912L;

   @Id
   private Integer codigo;

   @ManyToOne
   @JoinColumn(name = "csel_funcionamento_codigo", nullable = false, insertable = false, updatable = false)
   private CselFuncionamento funcionamento;

   @Temporal(TemporalType.DATE)
   private Date data;

   public CselFeriado() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(codigo).toString();
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public CselFuncionamento getFuncionamento() {
      return funcionamento;
   }

   public void setFuncionamento(CselFuncionamento funcionamento) {
      this.funcionamento = funcionamento;
   }

   public Date getData() {
      return data;
   }

   public void setData(Date data) {
      this.data = data;
   }

}
