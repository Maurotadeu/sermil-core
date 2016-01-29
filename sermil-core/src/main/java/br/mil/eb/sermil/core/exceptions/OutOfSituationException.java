package br.mil.eb.sermil.core.exceptions;


public class OutOfSituationException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 7814661297071543690L;

    /** Construtor. */
    public OutOfSituationException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public OutOfSituationException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public OutOfSituationException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public OutOfSituationException(Throwable causa) {
        super(causa);
    }

}
