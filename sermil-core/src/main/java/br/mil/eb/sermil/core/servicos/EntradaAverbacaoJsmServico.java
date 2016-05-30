package br.mil.eb.sermil.core.servicos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.Constantes;
import br.mil.eb.sermil.core.dao.CidCertificadoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.ZlibHelper;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.ArquivoCabecalho;

/** Processamento de carga do arquivo de averbações do Módulo JSM (SASM).
 * @author Abreu Lopes, Gardino
 * @since 4.0
 * @version 5.4
 */
@Named("entradaAverbacaoJsmServico")
public class EntradaAverbacaoJsmServico {

  protected static final Logger logger = LoggerFactory.getLogger(EntradaAverbacaoJsmServico.class);

  @Inject
  private Environment env;

  @Inject
  private CidCertificadoDao crtDao;

  public EntradaAverbacaoJsmServico() {
    logger.debug("EntradaAverbacaoJsmServico iniciado");
  }

  @Transactional
  public ArquivoCabecalho processar(final File arquivo, final Usuario usr) throws SermilException {
    if (arquivo == null || arquivo.getName().isEmpty()) {
      throw new SermilException("ERRO: informe o nome do arquivo a ser carregado.");
    }
    final Path tempDir = Paths.get(this.env.getRequiredProperty(Constantes.TEMP_DIR));
    final Path txt = Paths.get(tempDir.toString(), arquivo.getName().concat(".txt"));
    Connection con = null;
    ArquivoCabecalho cab = null;
    try {
      CriptoSermil.executar(ZlibHelper.descompactar(arquivo.toPath()).toFile(), txt.toFile(), 2007);
    } catch (Throwable e) {
      throw new SermilException(e.getMessage());
    }
    try (BufferedReader br = Files.newBufferedReader(txt, Charset.forName("ISO-8859-1"))) {
      String linha = null;
      if ((linha = br.readLine()) != null) {
        cab = new ArquivoCabecalho(linha);
      } else {
        throw new SermilException("ERRO: Arquivo inválido, sem cabeçalho.");
      }
      if(!"9".equals(cab.getTipo())) {
        throw new SermilException("ERRO: tipo de arquivo inválido para averbação do Módulo JSM.");
      }
      con = this.crtDao.getConnection();
      PreparedStatement stmt1 = null;
      PreparedStatement stmt2 = null;
      int doc = 0;
      while (br.ready()) {
        doc++;
        linha = br.readLine();
        switch (linha.substring(0, 2)) {
        case "01": // Certificado
          try {
            long ra = Long.valueOf(linha.substring(2, 14));
            int tipo = this.converteTipoCertificado(Integer.parseInt(linha.substring(14, 16)));
            final Date data = java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(linha.substring(20, 24)), Month.of(Integer.parseInt(linha.substring(18, 20))), Integer.parseInt(linha.substring(16, 18))));
            stmt1 = con.prepareStatement("DELETE FROM CID_CERTIFICADO WHERE CIDADAO_RA = ? AND TIPO = ? AND DATA = ?");
            stmt1.setLong(1, ra);
            stmt1.setInt(2, tipo);
            stmt1.setDate(3, data);
            stmt1.execute();
            stmt2 = con.prepareStatement("INSERT INTO CID_CERTIFICADO (CIDADAO_RA,TIPO,DATA,NUMERO,SERIE,RESPONSAVEL) VALUES(?, ?, ?, ?, ?, ?)");
            stmt2.setLong(1, ra);
            stmt2.setInt(2, tipo);
            stmt2.setDate(3, data);
            stmt2.setLong(4, Integer.valueOf(linha.substring(24, 35).replaceAll("[^0-9 ]", "").trim()));
            stmt2.setString(5, linha.substring(24, 35).replaceAll("[^A-Z ]", "").trim());
            stmt2.setString(6, "Módulo JSM");
            stmt2.execute();
          } catch (Exception e) {
            logger.debug("CRT Linha:{} ERRO: {}", doc, e.getMessage());
            continue;
          } finally {
            if (stmt1 != null) {
              stmt1.close();
            }
            if (stmt2 != null) {
              stmt2.close();
            }
          }
          break;
        case "02": // Arrecadação
          try {
            stmt1 = con.prepareStatement("INSERT INTO CID_ARRECADACAO VALUES(?, ?, ?, ?, ?)");
            stmt1.setLong(1, Long.valueOf(linha.substring(2, 14)));
            stmt1.setInt(2, Integer.parseInt(linha.substring(14, 17)));
            stmt1.setLong(3, Long.parseLong(linha.substring(17, 20)));
            final Date data = java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(linha.substring(24, 28)), Month.of(Integer.parseInt(linha.substring(22, 24))), Integer.parseInt(linha.substring(20, 22))));
            //data.set(Integer.parseInt(linha.substring(24, 28)), Integer.parseInt(linha.substring(22, 24))-1, Integer.parseInt(linha.substring(20, 22)));
            stmt1.setDate(4, data);
            stmt1.setDouble(5, Double.parseDouble(linha.substring(28, 32).replaceAll(",", ".")));
            stmt1.execute();
          } catch (Exception e) {
            logger.debug("ARR Linha:{} ERRO: {}", doc, e.getMessage());
            continue;
          } finally {
            if (stmt1 != null) {
              stmt1.close();
            }
          }
          break;
        case "03": // Outras Averbações
          try {
            stmt1 = con.prepareStatement("INSERT INTO CID_AVERBACAO (CIDADAO_RA,DATA,DESCRICAO,RESPONSAVEL) VALUES(?, ?, ?, ? )");
            stmt1.setLong(1, Long.valueOf(linha.substring(2, 14)));
            final Date data = java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(linha.substring(18, 22)), Month.of(Integer.parseInt(linha.substring(16, 18))), Integer.parseInt(linha.substring(14, 16))));
            //data.set(Integer.parseInt(linha.substring(18, 22)), Integer.parseInt(linha.substring(16, 18))-1, Integer.parseInt(linha.substring(14, 16)));
            stmt1.setDate(2, data);
            stmt1.setString(3, linha.substring(22, 262).replaceAll("\\n|" + System.lineSeparator(), ""));
            stmt1.setString(4, "Módulo JSM");
            stmt1.execute();
          } catch (Exception e) {
            logger.debug("AVB Linha:{} ERRO: {}", doc, e.getMessage());
            continue;
          } finally {
            if (stmt1 != null) {
              stmt1.close();
            }
          }
          break;
        default:
          logger.debug("ERR: {}", linha);
          break;
        }
      }
    } catch (IOException ioe) {
      throw new SermilException(ioe);
    } catch (SQLException e) {
      logger.error("Erro = {}", e.getMessage());
    } finally {
      try {
        con.commit();
      } catch (SQLException e) {
        logger.error("Erro = {}", e.getMessage());
      }
    }
    return cab;
  }
  
  private Integer converteTipoCertificado(final Integer tipo) {
     switch(tipo) {
       case 4: // CDI Computador => Novo Tipo 3 (CDI) 
       case 6: // CDI JSM Infor  => Novo Tipo 3 (CDI)
         return 3;
       default: return tipo;   
     }
  }
  
}
