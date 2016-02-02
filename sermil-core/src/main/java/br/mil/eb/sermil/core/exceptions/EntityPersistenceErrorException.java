package br.mil.eb.sermil.core.exceptions;

public class EntityPersistenceErrorException extends SermilException {

    /** serialVersionUID */
    private static final long serialVersionUID = -3964963574648096797L;

    /** Construtor. */
    public EntityPersistenceErrorException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public EntityPersistenceErrorException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public EntityPersistenceErrorException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public EntityPersistenceErrorException(Throwable causa) {
        super(causa);
    }

}
