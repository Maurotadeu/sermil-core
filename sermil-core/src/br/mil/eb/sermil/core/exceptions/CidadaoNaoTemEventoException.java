package br.mil.eb.sermil.core.exceptions;

public class CidadaoNaoTemEventoException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -7134188388498699443L;

    /** Construtor. */
    public CidadaoNaoTemEventoException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public CidadaoNaoTemEventoException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public CidadaoNaoTemEventoException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public CidadaoNaoTemEventoException(Throwable causa) {
        super(causa);
    }

}
