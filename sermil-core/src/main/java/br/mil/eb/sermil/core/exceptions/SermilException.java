package br.mil.eb.sermil.core.exceptions;

import javax.servlet.ServletException;

/** Exce��o verificada padr�o da aplica��o.
 * Estende ServletExcpetion para facilitar o retorno de erros nas p�ginas JSP.
 * Todas as demais exce��es descendem desta exce��o.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.0
 */
public class SermilException extends ServletException {

    /** serialVersionUID */
    private static final long serialVersionUID = 5803746300766943321L;

    /** Construtor. */
    public SermilException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public SermilException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public SermilException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public SermilException(Throwable causa) {
        super(causa);
    }

}
