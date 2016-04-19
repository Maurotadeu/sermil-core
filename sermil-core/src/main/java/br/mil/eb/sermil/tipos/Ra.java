package br.mil.eb.sermil.tipos;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Registro de Alistamento.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.2
 */
public final class Ra implements Serializable {

  private static final long serialVersionUID = -1301493889827126156L;

  private Long valor;

  private String csm;

  private String jsm;

  private String sequencial;

  private String dv;

  /** Contrutor privado: usar Builder para instanciar um RA. */
  private Ra() {
    super();
  }

  public Ra(final Long valor) throws SermilException {
    final String aux = String.format("%012d", valor);
    final Ra ra = new Ra.Builder().csm(Integer.parseInt(aux.substring(0, 2))).jsm(Integer.parseInt(aux.substring(2, 5))).sequencial(Integer.parseInt(aux.substring(5, 11))).build();
    if (!aux.substring(11).equals(ra.dv)) {
      throw new SermilException("RA inválido, dígito verificador incorreto.");
    } else {
      this.csm = ra.csm;
      this.jsm = ra.jsm;
      this.sequencial = ra.sequencial;
      this.dv = ra.dv;
      this.valor = valor;
    }
  }

  @Override
  public String toString() {
    return new StringBuilder(this.csm).append(".").append(this.jsm).append(".").append(this.sequencial).append("-").append(this.dv).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((csm == null) ? 0 : csm.hashCode());
    result = prime * result + ((dv == null) ? 0 : dv.hashCode());
    result = prime * result + ((jsm == null) ? 0 : jsm.hashCode());
    result = prime * result + ((sequencial == null) ? 0 : sequencial.hashCode());
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
    Ra other = (Ra) obj;
    if (csm == null) {
      if (other.csm != null)
        return false;
    } else if (!csm.equals(other.csm))
      return false;
    if (dv == null) {
      if (other.dv != null)
        return false;
    } else if (!dv.equals(other.dv))
      return false;
    if (jsm == null) {
      if (other.jsm != null)
        return false;
    } else if (!jsm.equals(other.jsm))
      return false;
    if (sequencial == null) {
      if (other.sequencial != null)
        return false;
    } else if (!sequencial.equals(other.sequencial))
      return false;
    return true;
  }

  public String getCsm() {
    return csm;
  }

  public String getJsm() {
    return jsm;
  }

  public String getSequencial() {
    return sequencial;
  }

  public String getDv() {
    return dv;
  }

  public Long getValor() {
    return valor;
  }

  public void setValor(Long valor) {
    this.valor = valor;
  }

  public static boolean isRa(final String ra) {
    boolean status = false;
    if (ra != null && ra.length() == 12) {
      status = Utils.calculaModulo10(ra) == 0;
    }
    return status;
  }

  /** Classe construtora interna (Fluent Interface).
   * @author wlopes
   * @since 5.0
   * @version 5.3.2
   */
  public static class Builder {

    private Ra _temp;

    public Builder() {
      this._temp = new Ra();
    }

    public Builder csm(int csm) {
      this._temp.csm = String.format("%02d", csm);
      return this;
    }

    public Builder jsm(int jsm) {
      this._temp.jsm = String.format("%03d", jsm);
      return this;
    }

    public Builder sequencial(int seq) {
      this._temp.sequencial = String.format("%06d", seq);
      return this;
    }

    public Ra build() throws SermilException {
      if (StringUtils.isEmpty(_temp.csm) || StringUtils.isEmpty(_temp.jsm) || StringUtils.isEmpty(_temp.sequencial)) {
        throw new SermilException("RA é composto de CSM/JSM/SEQUENCIAL");
      }
      final StringBuilder sb = new StringBuilder(_temp.csm).append(_temp.jsm).append(_temp.sequencial);
      _temp.dv = Integer.toString(Utils.calculaModulo10(sb.toString()));
      sb.append(_temp.dv);
      _temp.setValor(Long.valueOf(sb.toString()));
      return _temp;
    }

  }

}
