package br.mil.eb.sermil.core.exceptions;

public class DocumentoJaExisteException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -3937033397797989715L;


    /** Construtor. */
    public DocumentoJaExisteException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public DocumentoJaExisteException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public DocumentoJaExisteException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public DocumentoJaExisteException(Throwable causa) {
        super(causa);
    }

}
