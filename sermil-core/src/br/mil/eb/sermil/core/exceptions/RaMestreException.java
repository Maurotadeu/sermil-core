package br.mil.eb.sermil.core.exceptions;

public class RaMestreException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 363373619060616283L;

    /** Construtor. */
    public RaMestreException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public RaMestreException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public RaMestreException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public RaMestreException(Throwable causa) {
        super(causa);
    }

}
