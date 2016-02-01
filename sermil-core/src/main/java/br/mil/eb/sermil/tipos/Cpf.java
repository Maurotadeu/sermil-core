package br.mil.eb.sermil.tipos;

import java.io.Serializable;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Tipo de dado CPF.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Cpf.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class Cpf implements Serializable {

  private static final long serialVersionUID = 5661701389859805211L;

  private String valor;

  public Cpf() {
    super();
  }

  public Cpf(String valor) throws SermilException {
    if (valor.length() != 11 || !Cpf.isCpf(valor)) {
      throw new SermilException("CPF inválido.");
    } else {
      this.valor = valor;
    }
  }

  @Override
  public String toString() {
    return this.getValor().substring(0,3) + "." + this.getValor().substring(3,6) + "." + this.getValor().substring(6,9) + "-" + this.getValor().substring(9);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((valor == null) ? 0 : valor.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cpf other = (Cpf) obj;
    if (valor == null) {
      if (other.valor != null)
        return false;
    } else if (!valor.equals(other.valor))
      return false;
    return true;
  }

  public static boolean isCpf(final String cpf) {
    boolean status = false;
    final String aux = cpf.replaceAll("\\D", "");
    if (aux != null && aux.length() == 11) {
      final String digito = Integer.toString(Utils.calculaModulo11(aux, 8)).concat(Integer.toString(Utils.calculaModulo11(aux, 9)));
      if (aux.substring(9).equals(digito)) {
        status = true;
      }
    }
    return status;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return this.valor;
  }

}
