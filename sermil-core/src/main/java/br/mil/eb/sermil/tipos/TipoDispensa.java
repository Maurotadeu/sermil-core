package br.mil.eb.sermil.tipos;

/** Tipo de Tributação de JSM.
 * @author Abreu Lopes
 * @since 5.3.2
 * @version 5.3.2
 */
public enum TipoDispensa {

   SEM_DISPENSA(Byte.parseByte("0"), "Sem dispensa"),
   JSM_NT(Byte.parseByte("1") ,"JSM Não-Tributária"),
   NOT_INCAPAZ(Byte.parseByte("2"), "Notoriamente incapaz"),
   MAIOR_30(Byte.parseByte("3"), "Maior de 30 anos"),
   ZONA_RURAL(Byte.parseByte("4"), "Zona Rural de OFR"),
   INC_MORAL(Byte.parseByte("5"), "Incapaz Moral"),
   JSM_DISP_TOTAL(Byte.parseByte("6"), "JSM Dispensa Total"),
   JSM_DISP_PARCIAL(Byte.parseByte("7"), "JSM Dispensa Parcial"),
   DISP_RM(Byte.parseByte("8"), "Cmt Região Militar (RM)"),
   DISP_DSM(Byte.parseByte("9"), "Dir Serviço Militar (DSM)"),
   ARRIMO(Byte.parseByte("10"), "Arrimo de Família"),
   PROB_SOCIAL(Byte.parseByte("11"), "Problema Social"),
   INAPTO_K(Byte.parseByte("12"), "Inapto K"),
   CALCADO_45(Byte.parseByte("13"), "Calçado maior que 45"),
   CINTURA_104(Byte.parseByte("14"), "Cintura maior que 104 cm"),
   FALECIDO(Byte.parseByte("15"), "Falecimento"),
   MATRICULADO_ENS_MIL(Byte.parseByte("16"), "Matriculado em Estabelecimento de Ensino Militar"),
   EMPREGADO_SEG_NAC(Byte.parseByte("17"), "Empregado em Empresa Diretamente Relacionada à Segurança Nacional)"),
   MAIOR_45(Byte.parseByte("18"), "Maior de 45 anos"),
   EXC_DISCIPLINAR(Byte.parseByte("19"), "Excluído a bem da disciplina"),
   BRE(Byte.parseByte("20"), "Brasileiro Residente no Exterior (BRE)"),
   IMP_COSNCIENCIA(Byte.parseByte("21"), "Imperativo de Consciência");
  
   private Byte codigo;

   private String descricao;

   private TipoDispensa(Byte codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
   }

   public Byte getCodigo() {
      return this.codigo;
   }
   
   @Override 
   public String toString(){
      return this.descricao + " (" + this.codigo + ")"; 
   }

   public static void main(String[] args) {
      for(TipoDispensa v : TipoDispensa.values()){
         System.out.println(v.ordinal() + " - " + v);
      }
      int x = 0;
      if (x == TipoDispensa.SEM_DISPENSA.ordinal()) {
         System.out.println(TipoDispensa.SEM_DISPENSA.getCodigo());
      } else {
         System.out.println("Não possui");
      }
   }
}
