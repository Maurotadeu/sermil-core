package br.mil.eb.sermil.core.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/** Criptografia fraca (rotação de bits) padrão dos arquivos do SERMIL.
 * @author wlopes
 * @version $Id$
 */
public class CriptoSermil {

  CriptoSermil() {
    // Construtor protegido
  }

  public static void executar(File entrada, File saida, int chave) throws IOException {
    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entrada)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saida))) {
      for (int i = 0; i < entrada.length(); i++) {
        bos.write((bis.read() ^ ~ ((char) ((byte) (chave >> i)))) & 0x000000FF);
      }
    }
  }

}
