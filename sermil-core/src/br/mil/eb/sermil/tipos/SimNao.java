package br.mil.eb.sermil.tipos;

public enum SimNao {

    S("Sim"), N("N�o");

    private String descricao; 

    private SimNao(String descricao) { 
      this.descricao = descricao; 
    }
        
    @Override 
    public String toString(){ 
      return this.descricao; 
    }
    
}
