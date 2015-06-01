package br.mil.eb.sermil.core.exceptions;

/** Cidadao ja cadastrado no sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id$
 */
public class CidadaoNotFoundException extends ConsultaException {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8628253173169176401L;

    /** Construtor. */
    public CidadaoNotFoundException() {
        super("Cidadão não encontrado.");
    }

}
