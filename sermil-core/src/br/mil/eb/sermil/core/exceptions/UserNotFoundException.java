package br.mil.eb.sermil.core.exceptions;

import javax.servlet.ServletException;

/** Exce��o verificada padr�o da aplica��o.
 * Estende ServletExcpetion para facilitar o retorno de erros nas p�ginas JSP.
 * Todas as demais exce��es descendem desta exce��o.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: SermilException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class UserNotFoundException extends ServletException {

  /** serialVersionUID.*/
  private static final long serialVersionUID = -5700557977281739191L;

  /** Construtor. */
  public UserNotFoundException() {
    super("Usuario n�o encontrado.");
  }

  /** Altera a mensagem padr�o de erro.
   * @param msg mensagem de erro
   */
  public UserNotFoundException(String msg) {
    super(msg);
  }

  /** Altera a mensagem de erro e define uma causa para o erro.
   * @param msg mensagem de erro
   * @param causa exce��o que gerou o erro
   */
  public UserNotFoundException(String msg, Throwable causa) {
    super(msg, causa);
  }

  /** Define uma exce��o que causou o erro.
   * @param causa exce��o que causou o erro
   */
  public UserNotFoundException(Throwable causa) {
    super(causa);
  }

}
