package br.mil.eb.sermil.core.exceptions;

public class RaItemSalvarFalhaException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -6635512838970577945L;

    /** Construtor. */
    public RaItemSalvarFalhaException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public RaItemSalvarFalhaException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public RaItemSalvarFalhaException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public RaItemSalvarFalhaException(Throwable causa) {
        super(causa);
    }

}
