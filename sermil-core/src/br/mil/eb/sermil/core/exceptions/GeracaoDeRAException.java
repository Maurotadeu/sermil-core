package br.mil.eb.sermil.core.exceptions;

/** Exce��o padr�o para pesquisa de informa��es quando n�o s�o encontradas informa��es.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: NoDataFoundException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class GeracaoDeRAException extends ConsultaException {

  /** serialVersionUID */
  private static final long serialVersionUID = -8208929587338035576L;

  /** Construtor. */
  public GeracaoDeRAException() {
    super("Consulta n�o retornou nenhum resultado.");
  }

  /** Define a mensagem de erro.
   * @param msg mensagem de erro
   */
  public GeracaoDeRAException(String msg) {
    super("Consulta n�o retornou nenhum resultado. [" + msg + "]");
  }

}
