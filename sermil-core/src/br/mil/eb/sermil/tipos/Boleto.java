package br.mil.eb.sermil.tipos;

import java.io.Serializable;
import java.math.BigDecimal;

/** Guia de Recolhimento para CEF ou EBCT.
 * @author Abreu Lopes
 * @since 5.1
 * @version $Id$
 */
public class Boleto implements Serializable {

  /** serialVersionUID.*/
  private static final long serialVersionUID = 8512230610277315227L;

  /** 8 = Arrecadação, 6 = Órgão identificado pelo CNPJ */
  private static final String SEQ = "86";

  /** Valor Efetivo ou Referência (VER) 6 = valor em reais (DV módulo 10) */
  private static final String VER_EBCT = "6";

  /** Valor Efetivo ou Referência (VER) 7 = Quantidade de moeda (DV Módulo 10) */
  private static final String VER_CEF = "7";
  
  /** 00.394.411 = CNPJ Casa Civil da Presidência da República.*/
  private static final String CNPJ_MULTA = "00394411";

  /** 00.894.356 = CNPJ Fundo do Serviço Militar.*/
  private static final String CNPJ_TAXA = "00894356";

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
  
  public enum Tipo {MULTA, TAXA};

  public enum Banco {CEF, EBCT};
  
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
    final String VER = (banco == Banco.CEF ? VER_CEF : VER_EBCT);
    final String CNPJ = (tipo == Tipo.TAXA ? CNPJ_TAXA : CNPJ_MULTA);
    final BigDecimal total = new BigDecimal(valor.replace(",",".")).add(new BigDecimal(tarifa.replace(",",".")));
    final String vlr = String.format("%011d", Integer.valueOf(total.toString().replace(".","")));
    final String dv = String.valueOf(Utils.calculaModulo10(SEQ + VER + vlr + CNPJ + this.cpf));
    this.bloco1 = SEQ + VER + dv + vlr.substring(0,7) + "-" + Utils.calculaModulo10(SEQ + VER + dv + vlr.substring(0,7));
    this.bloco2 = vlr.substring(7,11) + CNPJ.substring(0,7) + "-" + Utils.calculaModulo10(vlr.substring(7,11) + CNPJ.substring(0,7));
    this.bloco3 = CNPJ.substring(7) + "0000000000" + "-" + Utils.calculaModulo10(CNPJ.substring(7) + "0000000000");
    this.bloco4 = this.cpf + "-" + Utils.calculaModulo10(this.cpf);
    this.blocoGeral = SEQ + VER + dv + vlr + CNPJ + "0000000000" + this.cpf;
  }

  @Override
  public String toString() {
    return new StringBuilder(this.bloco1).append(" ").append(this.bloco2).append(" ").append(this.bloco3).append(" ").append(this.bloco4).toString();
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
      Boleto b = new Boleto("98106546772","Teste","1,38","2,04",Tipo.MULTA,Banco.CEF);
      System.out.println(b);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
