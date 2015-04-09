package br.mil.eb.sermil.tipos;

import java.io.Serializable;

/** Guia de Recolhimento da União Simples.
 * @author Abreu Lopes
 * @since 5.0
 * @version $Id$
 */
public class Gru implements Serializable {

  private static final long serialVersionUID = 1160064862406299624L;

  /** 8 = código da arrecadação, 5 = orgãos do govervo, 8 = reais ou 9 = referência. */
  private static final String SEQ = "858";

  /** 0254 = código da STN. */
  private static final String ORGAO = "0254";

  /** 00216 = apelido da UG/Gestão. */
  private static final String UG = "00216";

  /** Tipo de contribuinte: 1 = CPF ou 2 = CNPJ. */
  private static final String TIPO_CONTRIBUINTE = "1";

  private String cpf;

  private String nome;

  private String valor;

  private String receita;

  private String bloco1;

  private String bloco2;

  private String bloco3;

  private String bloco4;

  private String blocoGeral;

  public Gru() {
    super();
  }

  public Gru(final String cpf, final String nome, final String valor, final String receita) throws Exception {
    this.cpf = "000" + cpf;
    this.nome = nome;
    this.valor = valor;
    this.receita = receita;
    final String vlr = String.format("%011d", Integer.valueOf(valor.replace(",","")));
    final String dv = String.valueOf(this.getMod11(SEQ + vlr + ORGAO + this.receita.substring(0,5) + UG + TIPO_CONTRIBUINTE + this.cpf));
    this.bloco1 = SEQ + dv + vlr.substring(0,7) + "-" + this.getMod11(SEQ + dv + vlr.substring(0,7));
    this.bloco2 = vlr.substring(7,11) + ORGAO + this.receita.substring(0,3) + "-" + this.getMod11(vlr.substring(7,11) + ORGAO + this.receita.substring(0,3));
    this.bloco3 = this.receita.substring(3,5) + UG + TIPO_CONTRIBUINTE + this.cpf.substring(0,3) + "-" + this.getMod11(this.receita.substring(3,5) + UG + TIPO_CONTRIBUINTE + this.cpf.substring(0,3));
    this.bloco4 = this.cpf.substring(3,14) + "-" + getMod11(this.cpf.substring(3,14));
    this.blocoGeral = SEQ + dv + vlr + ORGAO + this.receita.substring(0,5) + UG + TIPO_CONTRIBUINTE + this.cpf;
  }

  @Override
  public String toString() {
    return this.bloco1 + " " + this.bloco2 + " " + this.bloco3 + " " + this.bloco4;
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
    Gru other = (Gru) obj;
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

  public String getReceita() {
    return receita;
  }

  public void setReceita(String receita) {
    this.receita = receita;
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

  private int getMod11(String sequencia) throws Exception {
    int soma = 0;
    int fator = 2;
    for (int i = sequencia.length(); i > 0; i--) {
      soma += Integer.parseInt(sequencia.substring(i-1,i)) * fator;
      if (fator == 9) {
        fator = 1;
      }
      fator++;
    }
    soma *= 10;
    return soma%11 == 10 ? 0 : soma%11;
  }

}
