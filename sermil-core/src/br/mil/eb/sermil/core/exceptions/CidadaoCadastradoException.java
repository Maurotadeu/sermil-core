package br.mil.eb.sermil.core.exceptions;

/** Cidad�o j� cadastrado no sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidadaoCadastradoException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class CidadaoCadastradoException extends ConsultaException {

    /** serialVersionUID */
    private static final long serialVersionUID = 4260391301257943255L;

    /** Construtor. */
    public CidadaoCadastradoException() {
        super("Cidad�o j� cadastrado.");
    }

}
