package br.mil.eb.sermil.core.exceptions;

public class RaPedidoSalvarFalhaException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -207820811407638650L;

    /** Construtor. */
    public RaPedidoSalvarFalhaException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public RaPedidoSalvarFalhaException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public RaPedidoSalvarFalhaException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public RaPedidoSalvarFalhaException(Throwable causa) {
        super(causa);
    }

}
