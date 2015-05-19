package br.mil.eb.sermil.core.exceptions;

/** Erro na consulta.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: ConsultaException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class ConsultaException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = 604564842605410161L;

    /** Construtor. */
    public ConsultaException() {
        super("Consulta não implementada.");
    }

    /** Define a mensagem de erro.
     * @param msg
     */
    public ConsultaException(String msg) {
        super(msg);
    }

    /** Define a mensagem de erro e a exceção que causou o erro.
     * @param msg mensagem de erro
     * @param causa causa do erro
     */
    public ConsultaException(String msg, Throwable causa) {
        super(msg, causa);
    }

}
