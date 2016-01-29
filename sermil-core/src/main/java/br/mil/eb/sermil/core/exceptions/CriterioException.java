package br.mil.eb.sermil.core.exceptions;

/** Crit�rio de pesquisa inv�lido.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CriterioException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class CriterioException extends ConsultaException {

    /** serialVersionUID */
    private static final long serialVersionUID = 2333659629255424743L;

    /** Construtor. */
    public CriterioException() {
        super("Campos Obrigat�rios em Branco.");
    }

    /** Define a mensagem de erro.
     * @param msg mensagem de erro
     */
    public CriterioException(String msg) {
        super(msg);
    }

    /** Define a mensagem de erro e a exce��o que causou o erro.
     * @param msg mensagem de erro
     * @param causa causa do erro
     */
    public CriterioException(String msg, Throwable causa) {
        super(msg, causa);
    }

}
