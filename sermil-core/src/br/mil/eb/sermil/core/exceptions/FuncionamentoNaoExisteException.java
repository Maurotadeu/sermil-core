package br.mil.eb.sermil.core.exceptions;

public class FuncionamentoNaoExisteException extends SermilException {

    /** serialVersionUID.*/
   private static final long serialVersionUID = -3188889118171720094L;

   /** Construtor. */
    public FuncionamentoNaoExisteException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public FuncionamentoNaoExisteException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public FuncionamentoNaoExisteException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public FuncionamentoNaoExisteException(Throwable causa) {
        super(causa);
    }

}
