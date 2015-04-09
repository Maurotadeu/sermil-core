package br.mil.eb.sermil.tipos;

import java.util.Date;

/** Pesquisa utilizada nas consultas gerenciais.
 * @author Abreu Lopes
 * @version $Id: Pesquisa.java 1637 2011-11-25 13:52:11Z wlopes $
 */
public class Pesquisa {

  private String ano;
  
  private String grupo1;
  
  private String grupo2;
  
  private String grupo3;

  private Integer evento;

  private Date dataInicial;

  private Date dataFinal;

  private String restricao1;

  private String restricao2;

  private String restricao3;

  private String restricao4;

  private String restricao5;

  private String operador1;

  private String operador2;

  private String operador3;

  private String operador4;

  private String operador5;

  private String valor1;

  private String valor2;

  private String valor3;

  private String valor4;

  private String valor5;

  public Pesquisa() {
    // Objeto pesquisa
  }
  
  public String getGrupo1() {
    return grupo1;
  }

  public void setGrupo1(String grupo1) {
    this.grupo1 = grupo1;
  }

  public String getGrupo2() {
    return grupo2;
  }

  public void setGrupo2(String grupo2) {
    this.grupo2 = grupo2;
  }

  public String getGrupo3() {
    return grupo3;
  }

  public void setGrupo3(String grupo3) {
    this.grupo3 = grupo3;
  }

  public Integer getEvento() {
    return evento;
  }

  public void setEvento(Integer evento) {
    this.evento = evento;
  }

  public Date getDataInicial() {
    return dataInicial;
  }

  public void setDataInicial(Date dataInicial) {
    this.dataInicial = dataInicial;
  }

  public Date getDataFinal() {
    return dataFinal;
  }

  public void setDataFinal(Date dataFinal) {
    this.dataFinal = dataFinal;
  }

  public String getRestricao1() {
    return restricao1;
  }

  public void setRestricao1(String restricao1) {
    this.restricao1 = restricao1;
  }

  public String getRestricao2() {
    return restricao2;
  }

  public void setRestricao2(String restricao2) {
    this.restricao2 = restricao2;
  }

  public String getRestricao3() {
    return restricao3;
  }

  public void setRestricao3(String restricao3) {
    this.restricao3 = restricao3;
  }

  public String getRestricao4() {
    return restricao4;
  }

  public void setRestricao4(String restricao4) {
    this.restricao4 = restricao4;
  }

  public String getRestricao5() {
    return restricao5;
  }

  public void setRestricao5(String restricao5) {
    this.restricao5 = restricao5;
  }

  public String getOperador1() {
    return operador1;
  }

  public void setOperador1(String operador1) {
    this.operador1 = operador1;
  }

  public String getOperador2() {
    return operador2;
  }

  public void setOperador2(String operador2) {
    this.operador2 = operador2;
  }

  public String getOperador3() {
    return operador3;
  }

  public void setOperador3(String operador3) {
    this.operador3 = operador3;
  }

  public String getOperador4() {
    return operador4;
  }

  public void setOperador4(String operador4) {
    this.operador4 = operador4;
  }

  public String getOperador5() {
    return operador5;
  }

  public void setOperador5(String operador5) {
    this.operador5 = operador5;
  }

  public String getValor1() {
    return valor1;
  }

  public void setValor1(String valor1) {
    this.valor1 = valor1;
  }

  public String getValor2() {
    return valor2;
  }

  public void setValor2(String valor2) {
    this.valor2 = valor2;
  }

  public String getValor3() {
    return valor3;
  }

  public void setValor3(String valor3) {
    this.valor3 = valor3;
  }

  public String getValor4() {
    return valor4;
  }

  public void setValor4(String valor4) {
    this.valor4 = valor4;
  }

  public String getValor5() {
    return valor5;
  }

  public void setValor5(String valor5) {
    this.valor5 = valor5;
  }

  public String getAno() {
    return ano;
  }

  public void setAno(String ano) {
    this.ano = ano;
  }

  
}
