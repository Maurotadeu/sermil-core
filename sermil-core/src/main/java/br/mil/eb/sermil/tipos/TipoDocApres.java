package br.mil.eb.sermil.tipos;

/** Tipo de Documento Apresentado no Alistamento.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.2.6
 */
public enum TipoDocApres {

   CERT_NASC("Certid�o de Nascimento"),
   CERT_CAS("Certid�o de Casamento"),
   CERT_NAT("Certid�o de Naturaliza��o"),
   RG("Registro Geral (Identidade)"),
   CART_PROF("Carteira Profissional/Funcional"),
   PASS("Passaporte"),
   CNH("Carteira Nacional de Habilita��o (CNH)"),
   CART_TRAB("Carteira de Trabalho");

   private String descricao;

   private TipoDocApres(String descricao) {
      this.descricao = descricao;
   }

   @Override 
   public String toString(){
      return this.descricao; 
   }

   public static void main(String[] args) {
      for(TipoDocApres v : TipoDocApres.values()){
         System.out.println(v.ordinal() + " - " + v);
      }
      int x = 0;
      if (x == TipoDocApres.CERT_NASC.ordinal()) {
         System.out.println(TipoDocApres.CERT_NASC);
      } else {
         System.out.println("N�o possui");
      }
   }
}
