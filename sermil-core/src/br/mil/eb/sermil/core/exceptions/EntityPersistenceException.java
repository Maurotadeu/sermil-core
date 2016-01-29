package br.mil.eb.sermil.core.exceptions;

public class EntityPersistenceException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -3964963574648096797L;

    /** Construtor. */
    public EntityPersistenceException() {
        super();
    }

    /** Altera a mensagem padr�o de erro.
     * @param msg mensagem de erro
     */
    public EntityPersistenceException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exce��o que gerou o erro
     */
    public EntityPersistenceException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exce��o que causou o erro.
     * @param causa exce��o que causou o erro
     */
    public EntityPersistenceException(Throwable causa) {
        super(causa);
    }

}