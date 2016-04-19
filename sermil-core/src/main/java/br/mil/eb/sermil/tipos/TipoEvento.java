package br.mil.eb.sermil.tipos;

/** Tipo de Evento no Servi�o Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.3.2
 */
public enum TipoEvento {

   ALISTAMENTO(1, "Alistamento"),
   ADIAMENTO(2, "Adiamento de Incorpora��o"),
   DISPENSA(3, "Dispensa de Sele��o"),
   SELECAO(4, "Sele��o Geral"),
   RECURSO(5, "Grau de Recurso na Sele��o Geral"),
   EXCESSO(6, "Excesso de Contingente"),
   DISTRIBUICAO(7, "Distribu�do"),
   INSUBMISSAO(8, "Insubmisso"),
   INCORPORACAO(9, "Incorpora��o na OM"),
   QUALIFICACAO(10, "Qualifica��o na OM"),
   ENGAJAMENTO(11, "Engajamento na OM"),
   LICENCIAMENTO(12, "Licenciado/Desligado da OM"),
   ANULACAO(13, "Anula��o de Incorpora��o"),
   DESINCORPORACAO(14, "Desincorpora��o"),
   EXCLUSAO(15, "Exclus�o a Bem da Disciplina"),
   DESERCAO(16, "Deser��o"),
   INTERRUPCAO(17, "Interrup��o de curso"),
   REFORMA(18, "Reforma"),
   DESAPARECIMENTO(19, "Desaparecimento"),
   EXTRAVIADO(20, "Extraviado"),
   REINCLUSAO(21, "Reinclus�o na OM"),
   TRANSFERENCIA(22, "Transfer�ncia de OM"),
   REFRATARIO(23, "Refrat�rio LSM"),
   REABILITACAO(24, "Reabilita��o"),
   FALECIMENTO(25, "Falecimento");

   private Integer codigo;
   
   private String descricao;

   private TipoEvento(Integer codigo, String descricao) { 
     this.codigo = codigo;
     this.descricao = descricao;
   }

   @Override 
   public String toString(){ 
      return this.codigo + " - " + this.descricao; 
   }
   
   public Integer getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public static void main(String[] args) {
      for(TipoEvento v : TipoEvento.values()){
         System.out.println(v);
      }
      int x = 1;
      if (x == TipoEvento.ALISTAMENTO.getCodigo()) {
         System.out.println(TipoEvento.ALISTAMENTO);
      }
   }
   
}
