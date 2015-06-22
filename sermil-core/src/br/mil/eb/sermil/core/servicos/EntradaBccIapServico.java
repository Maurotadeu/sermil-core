package br.mil.eb.sermil.core.servicos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.SelBccDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.SelBcc;
import br.mil.eb.sermil.modelo.Usuario;

/** Serviço de carga de arquivo de BCC/IAP.
 * @author Abreu Lopes, Gardino
 * @since 4.0
 * @version $Id: EntradaBccIapServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("entradaBccIapServico")
public class EntradaBccIapServico {

  protected static final Logger logger = LoggerFactory.getLogger(EntradaBccIapServico.class);

  @Inject
  private SelBccDao dao;

  public EntradaBccIapServico() {
    logger.debug("EntradaBccIapServico iniciado");
  }

  @Transactional
  public int processar(final File arquivo, final Usuario usr) throws SermilException {
    if (arquivo == null || arquivo.getName().isEmpty()) {
      throw new SermilException("ERRO: informe o nome do arquivo a ser carregado.");
    }
    try (BufferedReader br = Files.newBufferedReader(arquivo.toPath(), Charset.forName("ISO-8859-1"))) {
      int doc = 0;
      while (br.ready()) {
        doc++;
        logger.debug("Linha {}", doc);
        final SelBcc bcc = new SelBcc();
        try {
          bcc.decode(br.readLine());
        } catch (Exception e) {
          logger.debug("BCC/IAP Linha:{} ERRO: {}", doc, e.getMessage());
          continue;
        }
        logger.debug("BCC/IAP: {}", bcc);
        if (bcc.getPk().getFolha() != 1 && bcc.getPk().getFolha() != 2 && bcc.getPk().getFolha() != 3 && bcc.getRmCodigo() < 1 && bcc.getRmCodigo() > 12 ) {
          logger.debug("BCC/IAP ERRO: {}", bcc);
          continue;
        }
        this.dao.save(bcc);
      }
      logger.debug("TOTAL={}", doc);
      return doc;
    } catch (IOException ioe) {
      throw new SermilException(ioe);
    }
  }

}
