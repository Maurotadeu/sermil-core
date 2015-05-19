package br.mil.eb.sermil.core.exceptions;

public class CertificateNotFoundException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 7902537829242816832L;


    /** Construtor. */
    public CertificateNotFoundException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public CertificateNotFoundException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public CertificateNotFoundException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public CertificateNotFoundException(Throwable causa) {
        super(causa);
    }

}
