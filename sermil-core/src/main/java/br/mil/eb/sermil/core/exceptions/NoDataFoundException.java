package br.mil.eb.sermil.core.exceptions;

public class NoDataFoundException extends ConsultaException {

    /** serialVersionUID */
    private static final long serialVersionUID = -8208929587338035576L;

    /** Construtor. */
    public NoDataFoundException() {
        super("Consulta n�o retornou nenhum resultado.");
    }

    /** Define a mensagem de erro.
     * @param msg mensagem de erro
     */
    public NoDataFoundException(String msg) {
        super("Consulta n�o retornou nenhum resultado. [" + msg + "]");
    }

}
