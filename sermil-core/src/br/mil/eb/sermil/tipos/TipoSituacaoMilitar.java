package br.mil.eb.sermil.tipos;

/** Tipo de Situa��o Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.2.6
 */
public enum TipoSituacaoMilitar {

   EXCLUIDO("Exclu�do do Sistema"),
   ALISTADO("Alistado"),
   SELECAO("Sele��o Geral"),
   DISPENSADO("Dispensado de Sele��o"),
   APTO("Apto na Sele��o"),
   INAPTO("Inapto na Sele��o"),
   ADIAMENTO("Adiamento de Incorpora��o"),
   DISTRIBUIDO("Distribu�do"),
   EXCESSO("Excesso de Contingente"),
   EXCESSO_OM("Excesso de Contingente (OM)"),
   INSUBMISSO("Insubmisso"),
   REFRATARIO("Refrat�rio LSM"),
   INCORPORADO("Incorporado"),
   SERV_ALT("Servi�o Militar Alternativo"),
   EXIMIDO("Eximido"),
   LICENCIADO("Licenciado/Desligado"),
   DISP_SERV_ALT("Dispensado do Servi�o Alternativo"),
   PREST_SV_ALT("Prestou Servi�o Alternativo"),
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
