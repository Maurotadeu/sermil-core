package br.mil.eb.sermil.core.exceptions;

public class CPFDuplicadoException extends SermilException {

   /** serialVersionUID.*/
   private static final long serialVersionUID = 1279395783532222794L;

   /** Construtor. */
   public CPFDuplicadoException(final String cpf) {
      super(String.format("CPF = %s já consta da base de dados.", cpf));
   }

}
