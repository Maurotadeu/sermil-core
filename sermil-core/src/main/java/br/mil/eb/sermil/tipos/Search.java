package br.mil.eb.sermil.tipos;

import br.mil.eb.sermil.modelo.Csm;
import br.mil.eb.sermil.modelo.RaPedido;

public class Search {

   private RaPedido pedido;

   private Csm csm;

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

}
