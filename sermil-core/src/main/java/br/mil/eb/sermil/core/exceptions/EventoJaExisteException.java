package br.mil.eb.sermil.core.exceptions;

public class EventoJaExisteException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -7650668811104558337L;

    /** Construtor. */
    public EventoJaExisteException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public EventoJaExisteException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public EventoJaExisteException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public EventoJaExisteException(Throwable causa) {
        super(causa);
    }

}
