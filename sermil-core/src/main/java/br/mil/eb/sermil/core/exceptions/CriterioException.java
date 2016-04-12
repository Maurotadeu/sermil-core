package br.mil.eb.sermil.core.exceptions;

/** Crit�rio de pesquisa inv�lido.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.0
 */
public class CriterioException extends ConsultaException {

   private static final long serialVersionUID = 91742039272905031L;

   public CriterioException() {
      super("Informe pelo menos um crit�rio de pesquisa.");
   }

   public CriterioException(String msg) {
      super(msg);
   }

   public CriterioException(String msg, Throwable causa) {
      super(msg, causa);
   }

}
