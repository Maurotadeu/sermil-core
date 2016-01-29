package br.mil.eb.sermil.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** Compacta e descompacta arquivos padrão ZLIB.
 * @author Abreu Lopes
 * @since 3.4
 * @version $Id$
 */
public final class ZlibHelper {

    private static final int BUFSIZ = 4096;

    public static final Path descompactar(final Path arquivo) throws IOException {
        if (arquivo == null) {
            throw new IOException("Nome do arquivo não foi informado.");
        }
        final Path cripto = Paths.get(arquivo.toString().substring(0, arquivo.toString().lastIndexOf(".")).concat(".cripto"));
        long zeCrc = 0;
        try (OutputStream os = Files.newOutputStream(cripto); ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(arquivo.toFile())))) {
            final ZipEntry ze = zis.getNextEntry();
            if (ze == null) {
                throw new IllegalArgumentException(new StringBuilder("Erro: ").append("arquivo compactado inválido - ").append(arquivo).toString());
            } else {
                zeCrc = ze.getCrc();
            }
            byte inbuf[] = new byte[BUFSIZ];
            int n;
            final CRC32 crc = new CRC32();
            crc.reset();
            final BufferedOutputStream bos = new BufferedOutputStream(os);
            while ((n = zis.read(inbuf, 0, BUFSIZ)) != -1) {
                bos.write(inbuf, 0, n);
                crc.update(inbuf, 0, n);
            }
            bos.flush();
            zis.closeEntry();
            if (crc.getValue() != zeCrc) {
                throw new ZipException("Arquivo compactado corrompido. (CRC32 inválido)");
            }
        }
        return cripto;
    }

    public static final Path compactar(final Path saida, final Path entrada) throws IOException {
        if (saida == null) {
            throw new IOException("Nome do arquivo de saída não foi informado.");
        }
        if (entrada == null) {
            throw new IOException("Nome do arquivo de entrada não foi informado.");
        }
        try (InputStream fis = Files.newInputStream(entrada); ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(saida.toFile()))) {
            zos.putNextEntry(new ZipEntry(entrada.getFileName().toString()));
            byte[] buf = new byte[BUFSIZ];
            int len = 0;
            while((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            return saida;
        }
    }

}
