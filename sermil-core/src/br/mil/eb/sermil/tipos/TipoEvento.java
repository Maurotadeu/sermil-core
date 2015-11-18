package br.mil.eb.sermil.tipos;

/** Tipo de Evento no Servi�o Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.2.6
 */
public enum TipoEvento {

   ND("N/D"),
   ALISTAMENTO("Alistamento"),
   ADIAMENTO("Adiamento de Incorpora��o"),
   DISPENSA("Dispensa de Sele��o"),
   SELECAO("Sele��o Geral"),
   RECURSO("Grau de Recurso na Sele��o Geral"),
   EXCESSO("Excesso de Contingente"),
   DISTRIBUICAO("Distribu�do"),
   INSUBMISSAO("Insubmisso"),
   INCORPORACAO("Incorpora��o na OM"),
   QUALIFICACAO("Qualifica��o na OM"),
   ENGAJAMENTO("Engajamento na OM"),
   LICENCIAMENTO("Licenciado/Desligado da OM"),
   ANULACAO("Anula��o de Incorpora��o"),
   DESINCORPORACAO("Desincorpora��o"),
   EXCLUSAO("Exclus�o a Bem da Disciplina"),
   DESERCAO("Deser��o"),
   INTERRUPCAO("Interrup��o de curso"),
   REFORMA("Reforma"),
   DESAPARECIMENTO("Desaparecimento"),
   EXTRAVIADO("Extraviado"),
   REINCLUSAO("Reinclus�o na OM"),
   TRANSFERENCIA("transfer�ncia de OM"),
   REFRATARIO("Refrat�rio LSM"),
   REABILITACAO("Reabilita��o"),
   FALECIMENTO("Falecimento");

   private String descricao;

   private TipoEvento(String descricao) { 
      this.descricao = descricao;
   }

   @Override 
   public String toString(){ 
      return this.descricao; 
   }

   public static void main(String[] args) {
      for(TipoEvento v : TipoEvento.values()){
         System.out.println(v.ordinal() + " - " + v);
      }
      int x = 1;
      if (x == TipoEvento.ALISTAMENTO.ordinal()) {
         System.out.println(TipoEvento.ALISTAMENTO);
      } else {
         System.out.println(TipoEvento.ND);
      }
   }
}
