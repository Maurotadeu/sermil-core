package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 * Comando Militar de Área.
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Cma.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name="CS")
@NamedQueries({ 
   @NamedQuery(name = "cs.listarPorRM", query = "select c from cs c where c.rm.codigo = :rm_codigo "), 
   @NamedQuery(name = "cs.listarPorNome", query = "select c from cs c where c.nome = :nome ") })
public final class CS implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -6654214842787703523L;

   /** TRIBUTACAO */
   public static final String TRIBUTACAO_EB = "EB";
   public static final String TRIBUTACAO_MAR = "MAR";
   public static final String TRIBUTACAO_FAB = "FAB";
   public static final String TRIBUTACAO_TG = "TG";

   @Id
   private Integer codigo;

   @Column(name = "nome", length = 30)
   private String nome;

   @Column(name = "rm_codigo")
   @ManyToOne
   @JoinColumn(name = "RM_CODIGO", nullable = false)
   private Rm rm;

   @Column(name = "tributacao", length = 30)
   private String tributacao;

   @Column
   private int atendimentos;

   public CS() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(codigo).append(" CS").toString();
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public Rm getRm() {
      return rm;
   }

   public void setRm(Rm rm) {
      this.rm = rm;
   }

   public String getTributacao() {
      return tributacao;
   }

   public void setTributacao(String tributacao) {
      this.tributacao = tributacao;
   }

   public int getAtendimentos() {
      return atendimentos;
   }

   public void setAtendimentos(int atendimentos) {
      this.atendimentos = atendimentos;
   }

}
