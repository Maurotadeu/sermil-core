package br.mil.eb.sermil.core.exceptions;

/** Cidadao n�o cadastrado no sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.0
 */
public class CidadaoNotFoundException extends ConsultaException {

   /** serialVersionUID. */
   private static final long serialVersionUID = 8628253173169176401L;

   /** Construtor. */
   public CidadaoNotFoundException() {
      super("N�o foi encontrado nenhum cidad�o que atenda aos crit�rios informados.");
   }

}
