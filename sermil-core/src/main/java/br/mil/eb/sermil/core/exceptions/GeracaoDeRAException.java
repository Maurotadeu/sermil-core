package br.mil.eb.sermil.core.exceptions;

public class GeracaoDeRAException extends ConsultaException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1628337656084440462L;

    /** Construtor. */
    public GeracaoDeRAException() {
        super("Consulta não retornou nenhum resultado.");
    }

    /** Define a mensagem de erro.
     * @param msg mensagem de erro
     */
    public GeracaoDeRAException(String msg) {
        super("Consulta não retornou nenhum resultado. [" + msg + "]");
    }

}
