package br.mil.eb.sermil.core.exceptions;

import br.mil.eb.sermil.modelo.RaPedido;

/** Exceção no processamento pedido de faixa de RA.
 * @author Abreu Lopes
 * @since 5.0
 * @version 5.4.6
 */
public class RaPedidoException extends SermilException {

  private static final long serialVersionUID = 1L;

  private RaPedido pedido;
  
  public RaPedidoException() {
    super();
  }

  public RaPedidoException(final String msg) {
    super(msg);
  }

  public RaPedidoException(final RaPedido pedido, final String msg) {
    super(msg);
    this.pedido = pedido;
  }

  public RaPedido getPedido() {
    return pedido;
  }

  public void setPedido(RaPedido pedido) {
    this.pedido = pedido;
  }
  
}
