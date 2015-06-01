package br.mil.eb.sermil.core.exceptions;

public class CPFDuplicadoException extends SermilException {

    /** serialVersionUID.*/
    private static final long serialVersionUID = 5454250690780305153L;


    /** Construtor. */
    public CPFDuplicadoException() {
        super("CPF duplicado na base de dados.");
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public CPFDuplicadoException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public CPFDuplicadoException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public CPFDuplicadoException(Throwable causa) {
        super(causa);
    }

}
