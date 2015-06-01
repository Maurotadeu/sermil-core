package br.mil.eb.sermil.core.exceptions;

public class EventNotFoundException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -2211471714352647672L;

    /** Construtor. */
    public EventNotFoundException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public EventNotFoundException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public EventNotFoundException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public EventNotFoundException(Throwable causa) {
        super(causa);
    }

}
