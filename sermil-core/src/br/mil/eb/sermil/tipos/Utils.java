package br.mil.eb.sermil.tipos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitários.
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Utils.java 2416 2014-05-08 18:06:10Z wlopes $
 */
public final class Utils {

   private Utils() {
      // Classe utilitária, contrutor protegido.
   }

   /**
    * Remove acentuação de uma string.
    * 
    * @param texto string
    * @return string sem acentuação
    */
   public static String limpaAcento(String texto) {
      return texto.trim().replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A").replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I")
            .replaceAll("[ÕÔÒÓÖ]", "O").replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N');
   }

   public static String limpaCharEsp(String texto) {
      return texto.trim().replace("-", "").replace("+", "").replace(".", "").replace(",", "").replace("!", "").replace("?", "").replace("/", "").replace("$", "").replace("%", "").replace("&", "").replace("*", "").replace("(", "").replace(")", "")
            .replace("_", "").replace("=", "").replace(";", "").replace("*", "").replace(":", "");

   }

   /**
    * Algoritmo de verificação módulo 10 (fórmula de Luhn).
    * 
    * @param sequencia sequência de dígitos
    * @return dígito verificador
    */
   public static int calculaModulo10(final String sequencia) {
      int soma = 0;
      final char[] c = sequencia.toCharArray();
      for (int i = 0; i < c.length; i++) {
         int d = Character.digit(c[i], 10);
         if (i % 2 == 0) {
            d *= 2;
            if (d > 9) {
               soma += Integer.valueOf(Integer.toString(d).substring(0, 1)) + Integer.valueOf(Integer.toString(d).substring(1));
            } else {
               soma += d;
            }
         } else {
            soma += d;
         }
      }
      return soma % 10 == 0 ? 0 : 10 - soma % 10;
   }

   /**
    * Algoritmo de verificação módulo 11.
    * 
    * @param digitos sequência de dígitos
    * @param posicao posição na sequência onde iniciar o cálculo
    * @return dígito verificador
    */
   public static int calculaModulo11(final String digitos, int posicao) {
      int p = 0;
      for (int i = 2; posicao >= 0; i++, posicao--) {
         p = p + Integer.parseInt(digitos.substring(posicao, posicao + 1)) * i;
      }
      p = p % 11;
      return (p > 1 ? (11 - p) : 0);
   }

   /**
    * @param Path file
    * @return List<String>
    * @throws IOException
    * @author Anselmo S Ribeiro (anselmo.sr@gmail.com)
    */
   public static List<String> readFile(Path file) throws IOException {
      ArrayList<String> lines = new ArrayList<String>();
      try (BufferedReader br = Files.newBufferedReader(file, Charset.forName("ISO-8859-1"))) {
         while (br.ready()) {
            lines.add(br.readLine());
         }
         br.close();
      }
      return lines.isEmpty() ? null : lines;
   }
   
      

}
