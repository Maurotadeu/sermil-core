package br.mil.eb.sermil.tipos;

import java.io.Serializable;
import java.math.BigDecimal;

/** Guia de Recolhimento para CEF ou EBCT.
 * @author Abreu Lopes
 * @since 5.1
 * @version 5.2.3
 */
@SuppressWarnings("unused")
public class Boleto implements Serializable {

  /** serialVersionUID.*/
  private static final long serialVersionUID = 8512230610277315227L;

  /** 8 = Arrecadação, 6 = Órgão identificado pelo CNPJ */
  private static final String SEQ = "86";

  /** Valor Efetivo ou Referência (VER) 6 = valor em reais (DV módulo 10) */
  private static final String VER_EFETIVO = "6";

  /** Valor Efetivo ou Referência (VER) 7 = Quantidade de moeda (DV Módulo 10) */
  private static final String VER_REF = "7";
  
  /** 00.394.411 = CNPJ Casa Civil da Presidência da República. Foi mandado passar para CNPJ FSM (05/05/2015) */
  private static final String CNPJ_PR = "00394411";

  /** 00.894.356 = CNPJ Fundo do Serviço Militar.*/
  private static final String CNPJ_FSM = "00894356";

  private String cpf;

  private String nome;

  private String artigo;

  private String valor;

  private String tarifa;
  
  private String bloco1;

  private String bloco2;

  private String bloco3;

  private String bloco4;

  private String blocoGeral;

  private Tipo tipo;

  private Banco banco;
  
  public enum Tipo {
      
      MULTA("1"), TAXA("2");
      
      private final String codigo;

      Tipo(String cod) {
          codigo = cod;
      };

      public String getCodigo() {
          return codigo;
      }
      
  }  

  public enum Banco {
      
      CEF("1"), ECT("2");
      
      private final String codigo;

      Banco(String cod) {
          codigo = cod;
      };

      public String getCodigo() {
          return codigo;
      }
      
  };
  
  public Boleto() {
    super();
  }

  public Boleto(final String cpf, final String nome, final String valor, final String tarifa, final Tipo tipo, final Banco banco) throws Exception {
    this.banco = banco;
    this.tipo = tipo;
    this.cpf = cpf;
    this.nome = nome;
    this.valor = valor;
    this.tarifa = tarifa;
    final BigDecimal total = new BigDecimal(valor.replace(",",".")).add(new BigDecimal(tarifa.replace(",",".")));
    final String VER = (banco == Banco.CEF ? VER_REF : VER_EFETIVO);
    //Usando mesmo CNPJ para taxas/multas.
    //final String CNPJ = (tipo == Tipo.TAXA ? CNPJ_FSM : CNPJ_PR);
    final String CNPJ = CNPJ_FSM;
    final String CAMPO_LIVRE = new StringBuilder(this.tipo.getCodigo()).append(this.banco.getCodigo()).append("00000000").toString();
    final String VALOR = String.format("%011d", Integer.valueOf(total.toString().replace(".","")));
    final String DV = String.valueOf(Utils.calculaModulo10(SEQ + VER + VALOR + CNPJ + CAMPO_LIVRE + this.cpf));
    this.bloco1 = new StringBuilder(SEQ).append(VER).append(DV).append(VALOR.substring(0,7)).toString();
    this.bloco1 = adicionaDv(this.bloco1);
    this.bloco2 = new StringBuilder(VALOR.substring(7,11)).append(CNPJ.substring(0,7)).toString();
    this.bloco2 = adicionaDv(this.bloco2);
    this.bloco3 = new StringBuilder(CNPJ.substring(7)).append(CAMPO_LIVRE).toString();
    this.bloco3 = adicionaDv(this.bloco3);
    this.bloco4 = new StringBuilder(this.cpf).toString();
    this.bloco4 = adicionaDv(this.bloco4);
    this.blocoGeral = new StringBuilder(SEQ).append(VER).append(DV).append(VALOR).append(CNPJ).append(CAMPO_LIVRE).append(this.cpf).toString();
  }

  private String adicionaDv(final String bloco) {
    return new StringBuilder(bloco).append("-").append(Utils.calculaModulo10(bloco)).append(" ").toString();
  }

  @Override
  public String toString() {
    return new StringBuilder(this.bloco1).append(" ")
                     .append(this.bloco2).append(" ")
                     .append(this.bloco3).append(" ")
                     .append(this.bloco4).append(" ")
                     .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bloco1 == null) ? 0 : bloco1.hashCode());
    result = prime * result + ((bloco2 == null) ? 0 : bloco2.hashCode());
    result = prime * result + ((bloco3 == null) ? 0 : bloco3.hashCode());
    result = prime * result + ((bloco4 == null) ? 0 : bloco4.hashCode());
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
    Boleto other = (Boleto) obj;
    if (bloco1 == null) {
      if (other.bloco1 != null)
        return false;
    } else if (!bloco1.equals(other.bloco1))
      return false;
    if (bloco2 == null) {
      if (other.bloco2 != null)
        return false;
    } else if (!bloco2.equals(other.bloco2))
      return false;
    if (bloco3 == null) {
      if (other.bloco3 != null)
        return false;
    } else if (!bloco3.equals(other.bloco3))
      return false;
    if (bloco4 == null) {
      if (other.bloco4 != null)
        return false;
    } else if (!bloco4.equals(other.bloco4))
      return false;
    return true;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }
  
  public String getArtigo() {
    return artigo;
  }

  public void setArtigo(String artigo) {
    this.artigo = artigo;
  }

  public String getTarifa() {
    return tarifa;
  }

  public void setTarifa(String tarifa) {
    this.tarifa = tarifa;
  }
  
  public Tipo getTipo() {
    return tipo;
  }

  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
  }

  public Banco getBanco() {
    return banco;
  }

  public void setBanco(Banco banco) {
    this.banco = banco;
  }

  public String getBloco1() {
    return bloco1;
  }

  public void setBloco1(String bloco1) {
    this.bloco1 = bloco1;
  }

  public String getBloco2() {
    return bloco2;
  }

  public void setBloco2(String bloco2) {
    this.bloco2 = bloco2;
  }

  public String getBloco3() {
    return bloco3;
  }

  public void setBloco3(String bloco3) {
    this.bloco3 = bloco3;
  }

  public String getBloco4() {
    return bloco4;
  }

  public void setBloco4(String bloco4) {
    this.bloco4 = bloco4;
  }

  public String getBlocoGeral() {
    return blocoGeral;
  }

  public void setBlocoGeral(String blocoGeral) {
    this.blocoGeral = blocoGeral;
  }

  public static void main(String[] args) {
    try {
      Boleto b = new Boleto("00001443710", "Teste", "0,00", "0,00", Tipo.TAXA, Banco.ECT);
      System.out.println(b);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
