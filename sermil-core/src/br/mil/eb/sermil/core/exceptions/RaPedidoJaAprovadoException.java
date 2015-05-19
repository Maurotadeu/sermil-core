package br.mil.eb.sermil.core.exceptions;

public class RaPedidoJaAprovadoException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 8840258523078429246L;

    /** Construtor. */
    public RaPedidoJaAprovadoException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public RaPedidoJaAprovadoException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public RaPedidoJaAprovadoException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public RaPedidoJaAprovadoException(Throwable causa) {
        super(causa);
    }

}
