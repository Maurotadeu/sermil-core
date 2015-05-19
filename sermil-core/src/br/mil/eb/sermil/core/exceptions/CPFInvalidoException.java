package br.mil.eb.sermil.core.exceptions;

public class CPFInvalidoException extends SermilException {

    /** serialVersionUID.*/
    private static final long serialVersionUID = 4683211342001166772L;

    /** Construtor. */
    public CPFInvalidoException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public CPFInvalidoException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public CPFInvalidoException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public CPFInvalidoException(Throwable causa) {
        super(causa);
    }

}
