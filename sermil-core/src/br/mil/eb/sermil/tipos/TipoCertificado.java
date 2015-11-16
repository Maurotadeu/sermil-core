package br.mil.eb.sermil.tipos;

public enum TipoCertificado {

   ND0("N/D","N/D"),
   CR1("Certificado de Reservista de 1a Categoria", "CR1"),
   CR2("Certificado de Reservista de 2a Categoria", "CR2"),
   CDI("Certificado de Dispensa de Incorpora��o", "CDI"),
   ND4("N/D", "N/D"),
   CI("Certificado de Isen��o", "CI"),
   ND6("N/D", "N/D"),
   CDSA("Certificado de Dispensa do Servi�o Alternativo", "CDSA"),
   CISA("Certificado de Isen��o do Servi�o Alternativo", "CISA"),
   CRSA("Certificado de Recusa do Servi�o Alternativo", "CRSA"),
   CAM("Certificado de Alistamento Militar", "CAM"),
   CPSA("Certificado de Presta��o do Servi�o Alternativo", "CPSA"),
   CSM("Certid�o de Situa��o Militar", "CSM");

   private String descricao;

   private String sigla;

   private TipoCertificado(String descricao, String sigla) { 
      this.descricao = descricao;
      this.sigla = sigla;
   }

   @Override 
   public String toString(){ 
      return this.descricao; 
   }

   public String getSigla() {
      return this.sigla;
   }

   public static void main(String[] args) {
      for(TipoCertificado v : TipoCertificado.values()){
         System.out.println(" Nr :"+ v.ordinal() + " - " + v);
      }
      int x = 1;
      if (x == TipoCertificado.CR1.ordinal()) {
         System.out.println(TipoCertificado.CR1);
         System.out.println(TipoCertificado.CR1.getSigla());
      } else if (x == TipoCertificado.CR2.ordinal()) {
         System.out.println(TipoCertificado.CR2);
         System.out.println(TipoCertificado.CR2.getSigla());
      } else {
         System.out.println(TipoCertificado.ND0);
         System.out.println(TipoCertificado.ND0.getSigla());
      }
   }
}
