package br.mil.eb.sermil.core.exceptions;

public class RaMestreException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 363373619060616283L;

    /** Construtor. */
    public RaMestreException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public RaMestreException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public RaMestreException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public RaMestreException(Throwable causa) {
        super(causa);
    }

}
