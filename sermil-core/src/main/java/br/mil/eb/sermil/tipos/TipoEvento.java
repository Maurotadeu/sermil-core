package br.mil.eb.sermil.tipos;

/** Tipo de Evento no Serviço Militar.
 * @author Abreu Lopes
 * @since 5.2.6
 * @version 5.3.2
 */
public enum TipoEvento {

   ALISTAMENTO(1, "Alistamento"),
   ADIAMENTO(2, "Adiamento de Incorporação"),
   DISPENSA(3, "Dispensa de Seleção"),
   SELECAO(4, "Seleção Geral"),
   RECURSO(5, "Grau de Recurso na Seleção Geral"),
   EXCESSO(6, "Excesso de Contingente"),
   DISTRIBUICAO(7, "Distribuído"),
   INSUBMISSAO(8, "Insubmisso"),
   INCORPORACAO(9, "Incorporação na OM"),
   QUALIFICACAO(10, "Qualificação na OM"),
   ENGAJAMENTO(11, "Engajamento na OM"),
   LICENCIAMENTO(12, "Licenciado/Desligado da OM"),
   ANULACAO(13, "Anulação de Incorporação"),
   DESINCORPORACAO(14, "Desincorporação"),
   EXCLUSAO(15, "Exclusão a Bem da Disciplina"),
   DESERCAO(16, "Deserção"),
   INTERRUPCAO(17, "Interrupção de curso"),
   REFORMA(18, "Reforma"),
   DESAPARECIMENTO(19, "Desaparecimento"),
   EXTRAVIADO(20, "Extraviado"),
   REINCLUSAO(21, "Reinclusão na OM"),
   TRANSFERENCIA(22, "Transferência de OM"),
   REFRATARIO(23, "Refratário LSM"),
   REABILITACAO(24, "Reabilitação"),
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
