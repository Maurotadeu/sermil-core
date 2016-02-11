package br.mil.eb.sermil.tipos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ArquivoCabecalho implements Serializable {

  private static final long serialVersionUID = 5641737273088623987L;

  private String tipo;

  private String servico;

  private Date data;

  private String guiaRemessa;

  private Integer registros;

  public ArquivoCabecalho() {
    super();
  }

  public ArquivoCabecalho(String linha) {
    final Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DATE, Integer.parseInt(linha.substring(11, 13)));
    cal.set(Calendar.MONTH, Integer.parseInt(linha.substring(13, 15))-1);
    cal.set(Calendar.YEAR, Integer.parseInt(linha.substring(15, 19)));
    this.setTipo(linha.substring(0, 1));
    this.setServico(linha.substring(1, 11));
    this.setData(cal.getTime());
    this.setGuiaRemessa(linha.substring(19, 22));
    this.setRegistros(Integer.parseInt(linha.substring(22, 29)));
  }

  @Override
  public String toString() {
    return new StringBuilder("Tipo: ").append(this.getTipo())
                     .append(" Sv: ").append(this.getServico())
                     .append(" Data: ").append(new SimpleDateFormat("dd/MM/yyyy").format(this.getData()))
                     .append(" GR: ").append(this.getGuiaRemessa())
                     .append(" Reg: ").append(this.getRegistros())
                     .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result
        + ((guiaRemessa == null) ? 0 : guiaRemessa.hashCode());
    result = prime * result + ((registros == null) ? 0 : registros.hashCode());
    result = prime * result + ((servico == null) ? 0 : servico.hashCode());
    result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
    ArquivoCabecalho other = (ArquivoCabecalho) obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    if (guiaRemessa == null) {
      if (other.guiaRemessa != null)
        return false;
    } else if (!guiaRemessa.equals(other.guiaRemessa))
      return false;
    if (registros == null) {
      if (other.registros != null)
        return false;
    } else if (!registros.equals(other.registros))
      return false;
    if (servico == null) {
      if (other.servico != null)
        return false;
    } else if (!servico.equals(other.servico))
      return false;
    if (tipo == null) {
      if (other.tipo != null)
        return false;
    } else if (!tipo.equals(other.tipo))
      return false;
    return true;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getServico() {
    return servico;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public String getGuiaRemessa() {
    return guiaRemessa;
  }

  public void setGuiaRemessa(String guiaRemessa) {
    this.guiaRemessa = guiaRemessa;
  }

  public Integer getRegistros() {
    return registros;
  }

  public void setRegistros(Integer registros) {
    this.registros = registros;
  }

  /** Classe construtora interna (Fluent Interface).
   * @author wlopes
   * @since 5.0
   * @version $Id: Ra.java 2186 2013-03-13 13:20:38Z gardino $
   */
  public static class Builder {

    private ArquivoCabecalho _temp;

    public Builder() {
      this._temp = new ArquivoCabecalho();
    }

    public Builder tipo(String tipo) {
      if ("1".equals(tipo) || "2".equals(tipo) || "6".equals(tipo) || "9".equals(tipo)) {
        this._temp.tipo = tipo;
      } else {
        throw new IllegalArgumentException("Tipo de arquivo inválido.");
      }
      return this;
    }

    public Builder servico(String servico) {
      if (servico.length() == 11) {
        this._temp.servico = servico;
      } else {
        throw new IllegalArgumentException("Serviço inválido.");
      }
      return this;
    }

    public Builder data(String data) {
      final Calendar cal = Calendar.getInstance();
      cal.set(Integer.parseInt(data.substring(4,8)), Integer.parseInt(data.substring(2, 4))-1, Integer.parseInt(data.substring(0,2)));
      this._temp.data = cal.getTime();
      return this;
    }

    public Builder gr(int gr) {
      this._temp.guiaRemessa = String.format("%03d", gr);
      return this;
    }

    public Builder reg(int reg) {
      this._temp.registros = reg;
      return this;
    }

    public ArquivoCabecalho build() {
      if(StringUtils.isEmpty(_temp.tipo) || StringUtils.isEmpty(_temp.servico) || _temp.data == null || StringUtils.isEmpty(_temp.guiaRemessa) || _temp.registros == null) {
        throw new IllegalArgumentException("Cabeçalho inválido: 90099000999999");
      }
      return _temp;
    }

  }

}
