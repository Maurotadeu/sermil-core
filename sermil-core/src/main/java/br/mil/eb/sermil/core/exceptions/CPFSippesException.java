package br.mil.eb.sermil.core.exceptions;

import java.io.IOException;
import java.util.Map;

public class CPFSippesException extends IOException {

    /** serialVersionUID.*/
    private static final long serialVersionUID = 1L;

    private Map<String, Object> properties;
    
    /** Construtor. */
    public CPFSippesException() {
        super();
    }

    /** Altera a mensagem padrão de erro.
     * @param msg mensagem de erro
     */
    public CPFSippesException(String msg) {
        super(msg);
    }

    /** Altera a mensagem de erro e define uma causa para o erro.
     * @param msg mensagem de erro
     * @param causa exceção que gerou o erro
     */
    public CPFSippesException(String msg, Throwable causa) {
        super(msg, causa);
    }

    /** Define uma exceção que causou o erro.
     * @param causa exceção que causou o erro
     */
    public CPFSippesException(Throwable causa) {
        super(causa);
    }

   public Map<String, Object> getProperties() {
      return properties;
   }

   public void setProperties(Map<String, Object> properties) {
      this.properties = properties;
   }
    
}
