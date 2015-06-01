package br.mil.eb.sermil.core.exceptions;

public class RaPedidoJaProcessadoException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 7016201617842588455L;

    /** Construtor. */
    public RaPedidoJaProcessadoException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public RaPedidoJaProcessadoException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public RaPedidoJaProcessadoException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public RaPedidoJaProcessadoException(Throwable causa) {
        super(causa);
    }

}
