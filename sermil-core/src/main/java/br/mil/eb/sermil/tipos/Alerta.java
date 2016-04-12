package br.mil.eb.sermil.tipos;

import java.util.ArrayList;
import java.util.List;

public class Alerta {

   private String titulo;

   private List<String> mensagens;

   private int tipo;

   public final static int TIPO_OK = 1;
   public final static int TIPO_WARNING = 2;
   public final static int TIPO_ERROR = 3;

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public List<String> getMensagens() {
      return mensagens;
   }

   public void setMensagens(List<String> mensagens) {
      this.mensagens = mensagens;
   }

   public void addMessage(String message) {
      if (this.mensagens == null) {
         this.mensagens = new ArrayList<>();
      }
      this.mensagens.add(message);
   }

   public int getTipo() {
      return tipo;
   }

   public void setTipo(int tipo) {
      this.tipo = tipo;
   }

}
