package br.mil.eb.sermil.core.exceptions;

public class JsmException extends SermilException {

    /** serialVersionUID.*/
    private static final long serialVersionUID = 5631661435145163198L;

    /** Construtor. */
    public JsmException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public JsmException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public JsmException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public JsmException(Throwable causa) {
        super(causa);
    }

}
