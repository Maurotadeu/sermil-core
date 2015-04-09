package br.mil.eb.sermil.core.exceptions;

import javax.servlet.ServletException;

/** Exceção verificada padrão da aplicação.
 * Estende ServletExcpetion para facilitar o retorno de erros nas páginas JSP.
 * Todas as demais exceções descendem desta exceção.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: SermilException.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class UsuarioSalvarException extends ServletException {

  /** serialVersionUID */
  private static final long serialVersionUID = 3292209787804893422L;

  /** Construtor. */
  public UsuarioSalvarException() {
    super();
  }

  /** Altera a mensagem padrão de erro.
   * @param msg mensagem de erro
   */
  public UsuarioSalvarException(String msg) {
    super(msg);
  }

  /** Altera a mensagem de erro e define uma causa para o erro.
   * @param msg mensagem de erro
   * @param causa exceção que gerou o erro
   */
  public UsuarioSalvarException(String msg, Throwable causa) {
    super(msg, causa);
  }

  /** Define uma exceção que causou o erro.
   * @param causa exceção que causou o erro
   */
  public UsuarioSalvarException(Throwable causa) {
    super(causa);
  }

}
