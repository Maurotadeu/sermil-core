package br.mil.eb.sermil.tipos;

/** Tipo de Situação Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.2.6
 */
public enum TipoSituacaoMilitar {

   EXCLUIDO("Excluído do Sistema"),
   ALISTADO("Alistado"),
   SELECAO("Seleção Geral"),
   DISPENSADO("Dispensado de Seleção"),
   APTO("Apto na Seleção"),
   INAPTO("Inapto na Seleção"),
   ADIAMENTO("Adiamento de Incorporação"),
   DISTRIBUIDO("Distribuído"),
   EXCESSO("Excesso de Contingente"),
   EXCESSO_OM("Excesso de Contingente (OM)"),
   INSUBMISSO("Insubmisso"),
   REFRATARIO("Refratário LSM"),
   INCORPORADO("Incorporado"),
   SERV_ALT("Serviço Militar Alternativo"),
   EXIMIDO("Eximido"),
   LICENCIADO("Licenciado/Desligado"),
   DISP_SERV_ALT("Dispensado do Serviço Alternativo"),
   PREST_SV_ALT("Prestou Serviço Alternativo"),
   DESERTOR("Desertor"),
   REFORMADO("Reformado")
   ;

   private String descricao;

   private TipoSituacaoMilitar(String descricao) { 
      this.descricao = descricao;
   }

   @Override 
   public String toString(){ 
      return this.descricao; 
   }

   public static void main(String[] args) {
      for(TipoSituacaoMilitar v : TipoSituacaoMilitar.values()){
         System.out.println(v.ordinal() + " - " + v);
      }
      int x = 1;
      if (x == TipoSituacaoMilitar.ALISTADO.ordinal()) {
         System.out.println(TipoSituacaoMilitar.ALISTADO);
      } else {
         System.out.println(TipoSituacaoMilitar.EXCLUIDO);
      }
   }
}
