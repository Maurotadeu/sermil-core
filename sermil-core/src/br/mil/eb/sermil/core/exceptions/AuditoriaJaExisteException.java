package br.mil.eb.sermil.core.exceptions;

public class AuditoriaJaExisteException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -574098603103626575L;

    /** Construtor. */
    public AuditoriaJaExisteException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public AuditoriaJaExisteException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public AuditoriaJaExisteException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public AuditoriaJaExisteException(Throwable causa) {
        super(causa);
    }

}
