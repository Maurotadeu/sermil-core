package br.mil.eb.sermil.modelo;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Search {

   private RaPedido pedido;

   private Csm csm;

   @Inject
   CS cs;
   
   @Inject
   Rm rm ;

   public RaPedido getPedido() {
      return pedido;
   }

   public void setPedido(RaPedido pedido) {
      this.pedido = pedido;
   }

   public Csm getCsm() {
      return csm;
   }

   public void setCsm(Csm csm) {
      this.csm = csm;
   }

   public CS getCs() {
      return cs;
   }

   public void setCs(CS cs) {
      this.cs = cs;
   }

   public Rm getRm() {
      return rm;
   }

   public void setRm(Rm rm) {
      this.rm = rm;
   }

}
