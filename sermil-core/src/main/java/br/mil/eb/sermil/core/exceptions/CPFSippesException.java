package br.mil.eb.sermil.core.exceptions;

import java.io.IOException;
import java.util.Map;

/** Exceção Web Service CPFSippes.
 * @author Abreu Lopes
 * @since 5.1.0
 * @version 5.4.6
 */
public class CPFSippesException extends IOException {

  private static final long serialVersionUID = -4916543629786601730L;

  private Map<String, Object> properties;

  public CPFSippesException() {
    super();
  }

  public CPFSippesException(String msg) {
    super(msg);
  }

  public CPFSippesException(String msg, Throwable causa) {
    super(msg, causa);
  }

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
