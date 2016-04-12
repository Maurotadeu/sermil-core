package br.mil.eb.sermil.core.exceptions;

public class NoDataFoundException extends ConsultaException {

   private static final long serialVersionUID = 7868874855754529599L;

   public NoDataFoundException() {
      super("Pesquisa n�o retornou nenhum resultado.");
   }

   public NoDataFoundException(String msg) {
      super("Pesquisa n�o retornou nenhum resultado. [" + msg + "]");
   }

}
