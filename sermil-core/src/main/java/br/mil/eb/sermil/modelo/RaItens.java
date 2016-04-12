package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Itens do Pedido de RA.
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: RaItens.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@Table(name = "RA_ITENS")
public final class RaItens implements Serializable {

   private static final long serialVersionUID = -2094341422409056895L;

   public static final Byte TIPO_EMERGENCIAL = new Byte("3");
   public static final Byte TIPO_NORMAL = new Byte("2");
   public static final Byte TIPO_1 = new Byte("1");

   @EmbeddedId
   private RaItens.PK pk;

   private Integer quantidade;

   @Column(name = "RA_INICIAL")
   private Integer raInicial;

   @Column(name = "RA_FINAL")
   private Integer raFinal;

   @ManyToOne
   @JoinColumns({
         @JoinColumn(name = "CSM_CODIGO", referencedColumnName = "CSM_CODIGO", insertable = false, updatable = false, nullable = false),
         @JoinColumn(name = "JSM_CODIGO", referencedColumnName = "CODIGO", insertable = false, updatable = false, nullable = false) })
   private Jsm jsm;

   @ManyToOne
   @JoinColumn(name = "RA_PEDIDO_NUMERO", insertable = false, updatable = false, nullable = false)
   private RaPedido raPedido;

   public RaItens() {
      this.setPk(new RaItens.PK());
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.pk == null) ? 0 : this.pk.hashCode());
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
      RaItens other = (RaItens) obj;
      if (this.pk == null) {
         if (other.pk != null)
            return false;
      } else if (!this.pk.equals(other.pk))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getPk() == null ? "ITEM" : this.getPk().toString()).append(" - ")
            .append(this.getQuantidade() == null ? "QTD" : this.getQuantidade()).toString();
   }

   public RaItens.PK getPk() {
      return this.pk;
   }

   public void setPk(RaItens.PK pk) {
      this.pk = pk;
   }

   public Integer getQuantidade() {
      return this.quantidade;
   }

   public void setQuantidade(Integer quantidade) {
      this.quantidade = quantidade;
   }

   public RaPedido getRaPedido() {
      return this.raPedido;
   }

   public void setRaPedido(RaPedido raPedido) {
      this.raPedido = raPedido;
   }

   public Jsm getJsm() {
      return this.jsm;
   }

   public void setJsm(Jsm jsm) {
      this.jsm = jsm;
   }

   public Integer getRaInicial() {
      return raInicial;
   }

   public void setRaInicial(Integer raInicial) {
      this.raInicial = raInicial;
   }

   public Integer getRaFinal() {
      return raFinal;
   }

   public void setRaFinal(Integer raFinal) {
      this.raFinal = raFinal;
   }

   /** Chave primária (PK) de RaItens. */
   @Embeddable
   public static class PK implements Serializable {

      /** serialVersionUID. */
      private static final long serialVersionUID = 5640857077466571589L;

      @Column(name = "RA_PEDIDO_NUMERO")
      private Integer raPedidoNumero;

      @Column(name = "CSM_CODIGO")
      private Byte csmCodigo;

      @Column(name = "JSM_CODIGO")
      private Short jsmCodigo;

      private Byte tipo;

      public PK() {
         super();
      }

      @Override
      public boolean equals(Object o) {
         if (o == this) {
            return true;
         }
         if (!(o instanceof PK)) {
            return false;
         }
         PK other = (PK) o;
         return this.raPedidoNumero.equals(other.raPedidoNumero) && this.csmCodigo.equals(other.csmCodigo)
               && this.jsmCodigo.equals(other.jsmCodigo) && this.tipo.equals(other.tipo);
      }

      @Override
      public int hashCode() {
         return this.raPedidoNumero.hashCode() ^ this.csmCodigo.hashCode() ^ this.jsmCodigo.hashCode()
               ^ this.tipo.hashCode();
      }

      @Override
      public String toString() {
         return new StringBuilder()
               .append(this.getRaPedidoNumero() == null ? "NR" : this.getRaPedidoNumero().toString()).append(" - ")
               .append(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
               .append("/")
               .append(this.getJsmCodigo() == null ? "000" : new DecimalFormat("000").format(this.getJsmCodigo()))
               .append(" - ").append(this.getTipo() == null ? "TIPO" : this.getTipo()).toString();
      }

      public Integer getRaPedidoNumero() {
         return this.raPedidoNumero;
      }

      public void setRaPedidoNumero(Integer numero) {
         this.raPedidoNumero = numero;
      }

      public Byte getCsmCodigo() {
         return this.csmCodigo;
      }

      public void setCsmCodigo(Byte codigo) {
         this.csmCodigo = codigo;
      }

      public Short getJsmCodigo() {
         return this.jsmCodigo;
      }

      public void setJsmCodigo(Short codigo) {
         this.jsmCodigo = codigo;
      }

      public Byte getTipo() {
         return this.tipo;
      }

      public void setTipo(Byte tipo) {
         this.tipo = tipo;
      }

   }

}
