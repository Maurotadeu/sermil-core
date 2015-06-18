package br.mil.eb.sermil.core.servicos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.core.utils.ZlibHelper;
import br.mil.eb.sermil.modelo.Jsm;

/** Serviço de Averbações de JSM.
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: ArquivoAverbacoesJsmServico.java 2495 2014-07-31 18:29:36Z wlopes $
 */
@Named("arquivoAverbacoesJsmServico")
public class ArquivoAverbacoesJsmServico {

  protected static final Logger logger = LoggerFactory.getLogger(ArquivoAverbacoesJsmServico.class);

  private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

  public static final String EOF = ("\r\n");

  public static final String SQL_SELECIONADOS = "SELECT TO_CHAR(C.CSM_CODIGO,'FM00')||TO_CHAR(C.JSM_CODIGO,'FM000')||TO_CHAR(C.RA,'FM000000000000')||TO_CHAR(E.DATA,'DDMMYYYY') DETALHE " + "FROM CIDADAO C, CID_EVENTO E "
      + "WHERE C.RA = E.CIDADAO_RA AND E.CODIGO IN (3,6) AND EXTRACT (YEAR FROM E.DATA) = ? AND C.CSM_CODIGO = ? AND C.JSM_CODIGO = ? AND SITUACAO_MILITAR = 3 " + "ORDER BY C.CSM_CODIGO, C.JSM_CODIGO, C.RA";

  public static final String SQL_DISTRIBUIDOS = "SELECT TO_CHAR(C.CSM_CODIGO,'FM00')||TO_CHAR(C.JSM_CODIGO,'FM000')||TO_CHAR(C.RA,'FM000000000000')||DECODE(TO_CHAR(c.om_codigo,'FM000000000'),null,'000000000',TO_CHAR(c.om_codigo,'FM000000000'))||TO_CHAR(E.DATA,'DDMMYYYY') DETALHE "
      + "FROM CIDADAO C, CID_EVENTO E " + "WHERE C.RA = E.CIDADAO_RA AND E.CODIGO = 7 AND EXTRACT (YEAR FROM E.DATA) = ? AND C.CSM_CODIGO = ? AND C.JSM_CODIGO = ? AND SITUACAO_MILITAR = 7 " + "ORDER BY C.CSM_CODIGO, C.JSM_CODIGO, C.RA";

  public static final String SQL_EXCESSO = "SELECT TO_CHAR(C.CSM_CODIGO,'FM00')||TO_CHAR(C.JSM_CODIGO,'FM000')||TO_CHAR(C.RA,'FM000000000000')||DECODE(TO_CHAR(c.om_codigo,'FM000000000'),null,TO_CHAR(c.om_codigo,'FM000000000'),TO_CHAR(c.om_codigo,'FM000000000'))||TO_CHAR(E.DATA,'DDMMYYYY') DETALHE "
      + "FROM CIDADAO C, CID_EVENTO E "
      + "WHERE C.RA = E.CIDADAO_RA AND C.OM_CODIGO IS NULL AND E.CODIGO = 6 AND EXTRACT(YEAR FROM E.DATA) = ? AND C.CSM_CODIGO = ? AND C.JSM_CODIGO = ? AND C.SITUACAO_MILITAR = 8 "
      + "ORDER BY C.CSM_CODIGO, C.JSM_CODIGO, C.RA";
  @Inject
  private JsmDao jsmDao;

  public ArquivoAverbacoesJsmServico() {
    logger.debug("ArquivoAverbacoesJsmServico iniciado");
  }

  public Path gerarArquivo(final Jsm jsm, final Integer ano, final Integer tipo) throws SermilException {
    final Path tempDir = Paths.get(Configurador.getInstance().getConfiguracao("temp.dir"));
    String CAB = null;
    String CMD = null;
    try {
      switch (tipo) {
      case 1:
        CAB = "SD";
        CMD = SQL_SELECIONADOS;
        break;
      case 2:
        CAB = "DD";
        CMD = SQL_DISTRIBUIDOS;
        break;
      case 3:
        CAB = "DE";
        CMD = SQL_EXCESSO;
        break;
      default:
        break;
      }
      final List<Object[]> lista = this.jsmDao.findBySQL(CMD, ano, jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo());
      if (lista == null || lista.isEmpty()) {
        throw new Exception("Não há dados para gerar o arquivo de averbação do SASM.");
      }
      final StringBuilder nome = new StringBuilder(CAB).append(String.format("%02d", jsm.getPk().getCsmCodigo())).append(String.format("%03d", jsm.getPk().getCodigo()));
      logger.debug("Arquivo Averbações JSM: {}", nome.toString());
      final Path arquivoTexto = Paths.get(tempDir.toString(), nome.append(".txt").toString());
      try (BufferedWriter bw = Files.newBufferedWriter(arquivoTexto, Charset.forName("UTF-8"))) {
        bw.write(new StringBuilder(CAB).append(String.format("%02d", jsm.getPk().getCsmCodigo())).append(String.format("%03d", jsm.getPk().getCodigo())).append(".dat").append(this.sdf.format(new Date())).append("000")
            .append(String.format("%07d", lista.size())).append("00003").append(EOF).toString());
        for (Object o : lista) {
          bw.write(new StringBuilder((String) o).append(EOF).toString());
        }
        bw.flush();
      } catch (IOException e) {
        throw new SermilException(e);
      }
      final Path arquivoCripto = Paths.get(tempDir.toString(), arquivoTexto.getFileName().toString().replace(".txt", ".cta"));
      CriptoSermil.executar(arquivoTexto.toFile(), arquivoCripto.toFile(), Integer.parseInt(String.valueOf(String.format("%02d", jsm.getPk().getCsmCodigo())) + String.valueOf(String.format("%03d", jsm.getPk().getCodigo()))));
      final Path zip = ZlibHelper.compactar(arquivoCripto);
      Files.delete(arquivoTexto);
      Files.delete(arquivoCripto);
      return zip;
    } catch (Exception e) {
      throw new SermilException(e.getMessage());
    }
  }

}
