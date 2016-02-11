package br.mil.eb.sermil.core.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Randomiza sequencia alfanumérica.
 * @author abreulopes
 * @version $Id: Randomize.java 2379 2014-03-17 13:48:55Z wlopes $
 */
public class Randomize {

  protected static final Logger logger = LoggerFactory.getLogger(Randomize.class);

  private static final String SENHA_REGEXP = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&*]).{8,8})";

  private static char[] ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$&*".toCharArray();

  public String execute() throws SermilException {
    try {
      final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      String aux;
      do {
        aux = this.gera(sr);
      } while (!aux.matches(SENHA_REGEXP));
      logger.debug("Sequência gerada: {}", aux);
      return aux;
    } catch(NoSuchAlgorithmException nae) {
      throw new SermilException(nae);
    }
  }

  private String gera(SecureRandom sr) {
    final StringBuilder sequencia = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      int ch = sr.nextInt(ALPHA.length);
      sequencia.append(ALPHA[ch]);
    }
    return sequencia.toString();
  }

}
