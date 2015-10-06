package br.mil.eb.sermil.core.exceptions;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Cidadão já cadastrado no sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.5
 */
public class CidadaoCadastradoException extends ConsultaException {

   /** serialVersionUID */
   private static final long serialVersionUID = 238975662596173010L;

   /** Construtor. */
   public CidadaoCadastradoException(final String nome, final String mae, final Date nasc) {
      super(String.format("Cidadão já consta da base de dados. (%s, %s, %s)", nome, mae, new SimpleDateFormat("dd/MM/yyyy").format(nasc)));
   }

}
