package br.mil.eb.sermil.core.exceptions;

public class UserNotFoundException extends SermilException {

    /** serialVersionUID.*/
    private static final long serialVersionUID = -5700557977281739191L;

    /** Construtor. */
    public UserNotFoundException() {
        super("Usuario não encontrado.");
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public UserNotFoundException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public UserNotFoundException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public UserNotFoundException(Throwable causa) {
        super(causa);
    }

}
