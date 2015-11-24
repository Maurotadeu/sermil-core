package br.mil.eb.sermil.tipos;

/** Tipo de Evento no Serviço Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.2.6
 */
public enum TipoEvento {

   ND("N/D"),
   ALISTAMENTO("Alistamento"),
   ADIAMENTO("Adiamento de Incorporação"),
   DISPENSA("Dispensa de Seleção"),
   SELECAO("Seleção Geral"),
   RECURSO("Grau de Recurso na Seleção Geral"),
   EXCESSO("Excesso de Contingente"),
   DISTRIBUICAO("Distribuído"),
   INSUBMISSAO("Insubmisso"),
   INCORPORACAO("Incorporação na OM"),
   QUALIFICACAO("Qualificação na OM"),
   ENGAJAMENTO("Engajamento na OM"),
   LICENCIAMENTO("Licenciado/Desligado da OM"),
   ANULACAO("Anulação de Incorporação"),
   DESINCORPORACAO("Desincorporação"),
   EXCLUSAO("Exclusão a Bem da Disciplina"),
   DESERCAO("Deserção"),
   INTERRUPCAO("Interrupção de curso"),
   REFORMA("Reforma"),
   DESAPARECIMENTO("Desaparecimento"),
   EXTRAVIADO("Extraviado"),
   REINCLUSAO("Reinclusão na OM"),
   TRANSFERENCIA("transferência de OM"),
   REFRATARIO("Refratário LSM"),
   REABILITACAO("Reabilitação"),
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
