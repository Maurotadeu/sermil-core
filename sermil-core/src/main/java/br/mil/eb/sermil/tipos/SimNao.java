package br.mil.eb.sermil.tipos;

public enum SimNao {

   ND("N/D"), S("Sim"), N("Não");

   private String descricao; 

   private SimNao(String descricao) { 
      this.descricao = descricao; 
   }

   @Override 
   public String toString(){ 
      return this.descricao; 
   }

   public static void main(String[] args) {
      for(SimNao v : SimNao.values()){
         System.out.println(" Nr :"+ v.ordinal() + " - " + v);
      }
      int x = 4;
      if (x == SimNao.N.ordinal())
         System.out.println(SimNao.N);
      else if (x == SimNao.S.ordinal())
         System.out.println(SimNao.S);
      else
         System.out.println(SimNao.ND);
   }
}
