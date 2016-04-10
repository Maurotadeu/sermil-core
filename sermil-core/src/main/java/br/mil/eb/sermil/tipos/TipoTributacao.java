package br.mil.eb.sermil.tipos;

/** Tipo de Tributação de JSM.
 * @author Abreu Lopes
 * @since 5.3.2
 * @version 5.3.2
 */
public enum TipoTributacao {

   MNT("Não tributária"),
   OMA_OFOR("OMA/OFOR"),
   OMA_TG("OMA/TG"),
   TG("TG"),
   OMA("OMA"),
   DESATIVADA("Desativada");
  
   private String descricao;

   private TipoTributacao(String descricao) {
      this.descricao = descricao;
   }

   @Override 
   public String toString(){
      return this.descricao; 
   }

   public static void main(String[] args) {
      for(TipoTributacao v : TipoTributacao.values()){
         System.out.println(v.ordinal() + " - " + v);
      }
      int x = 0;
      if (x == TipoTributacao.MNT.ordinal()) {
         System.out.println(TipoTributacao.MNT);
      } else {
         System.out.println("Não possui");
      }
   }
}
