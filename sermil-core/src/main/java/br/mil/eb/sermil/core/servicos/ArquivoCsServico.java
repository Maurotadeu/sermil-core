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
import org.springframework.core.env.Environment;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Constantes;
import br.mil.eb.sermil.core.utils.ZlibHelper;
import br.mil.eb.sermil.modelo.Cs;

/** Geração do arquivo de seleção para carregamento no Módulo CS.
 * @author Abreu Lopes, Daniel Gardino
 * @since 4.0
 * @version 5.4.5
 */
@Named("arquivoCsServico")
public class ArquivoCsServico {

  protected static final Logger logger = LoggerFactory.getLogger(ArquivoCsServico.class);

  public static final String EOF = ("\r\n");

  private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

  private static final String SQL_SELECIONADOS = "SELECT DISTINCT TO_CHAR(c.ra,'FM000000000000') " +
  "||RPAD(c.nome,70)||RPAD(c.pai,70)||RPAD(c.mae,70)||TO_CHAR(c.nascimento_data,'DDMMYYYY')||TO_CHAR(c.municipio_nascimento_codigo,'FM00000000')" +
  "||TO_CHAR(c.pais_nascimento_codigo,'FM0000')||TO_CHAR(c.estado_civil,'FM0')||TO_CHAR(c.sexo,'FM0')||TO_CHAR(c.escolaridade,'FM00')"+
  "||'X1010'||TO_CHAR(c.csm_codigo,'FM00')||TO_CHAR(c.jsm_codigo,'FM000')||TO_CHAR(c.zona_residencial,'FM0')" +
  // Manter enquanto Módulo Csel não usar tabela de ocupações nova, enviando código antigo X1010.
  //"||RPAD(NVL(c.ocupacao_codigo,CHR(32)),6)||TO_CHAR(c.csm_codigo,'FM00')||TO_CHAR(c.jsm_codigo,'FM000')||TO_CHAR(c.zona_residencial,'FM0')" +
  "||TO_CHAR(c.municipio_residencia_codigo,'FM00000000')||TO_CHAR(c.pais_residencia_codigo,'FM0000')||RPAD(NVL(c.endereco,CHR(32)),70)" +
  "||RPAD(NVL(c.bairro,CHR(32)),70)||RPAD(NVL(c.cep,CHR(32)),8)||RPAD(NVL(c.telefone,CHR(32)),9)||RPAD(NVL(c.cpf,CHR(32)),11)" +
  "||RPAD(NVL(c.rg,CHR(32)),30)||RPAD(NVL(c.email,CHR(32)),70)||TO_CHAR(e.data,'DDMMYYYY') " +
  "FROM cidadao c, cid_evento e, jsm j, csm s " +
  "WHERE c.ra = e.cidadao_ra AND c.vinculacao_ano = EXTRACT(YEAR FROM SYSDATE) AND e.data BETWEEN TO_DATE('0107'||(EXTRACT(YEAR FROM SYSDATE)-1),'DDMMYYYY') AND TO_DATE('3006'||EXTRACT(YEAR FROM SYSDATE),'DDMMYYYY') "+
  "AND e.codigo = 1 AND situacao_militar = 2 AND c.csm_codigo = j.csm_codigo AND c.jsm_codigo = j.codigo AND s.codigo = j.csm_codigo AND s.rm_codigo= ? AND j.cs= ? ";

  /* Excluído do Módulo Csel por não ter utilidade
  private static final String SQL_REFRATARIOS = "SELECT DISTINCT TO_CHAR(c.ra,'FM000000000000')||RPAD(c.nome,70)||RPAD(c.pai,70)||RPAD(c.mae,70)||TO_CHAR(c.nascimento_data,'DDMMYYYY')" +
  "||TO_CHAR(c.municipio_nascimento_codigo,'FM00000000')||TO_CHAR(c.pais_nascimento_codigo,'FM0000')||TO_CHAR(c.estado_civil,'FM0')||TO_CHAR(c.sexo,'FM0')||TO_CHAR(c.escolaridade,'FM00')" +
  "||RPAD(NVL(c.ocupacao_codigo,CHR(32)),6)||TO_CHAR(c.csm_codigo,'FM00')||TO_CHAR(c.jsm_codigo,'FM000')||TO_CHAR(c.zona_residencial,'FM0')||TO_CHAR(c.municipio_residencia_codigo,'FM00000000')" +
  "||TO_CHAR(c.pais_residencia_codigo,'FM0000')||RPAD(NVL(c.endereco,CHR(32)),70)||RPAD(NVL(c.bairro,CHR(32)),70)||RPAD(NVL(c.cep,CHR(32)),8)" +
  "||RPAD(NVL(c.telefone,CHR(32)),9)||RPAD(NVL(c.cpf,CHR(32)),11)||RPAD(NVL(c.rg,CHR(32)),30)||RPAD(NVL(c.email,CHR(32)),70)||TO_CHAR(e.data,'DDMMYYYY') " +
  "FROM cidadao c, cid_evento e, jsm j, csm s " +
  "WHERE c.ra = e.cidadao_ra AND e.codigo = 1 AND c.situacao_militar = 11 "+
  "AND c.vinculacao_ano IN (EXTRACT(YEAR FROM SYSDATE)-2,EXTRACT(YEAR FROM SYSDATE)-1) "+
  "AND c.csm_codigo = j.csm_codigo AND c.jsm_codigo = j.codigo AND s.codigo = j.csm_codigo AND s.rm_codigo= ? AND j.cs= ?";
  */

  private static final String SQL_DISPENSADOS = "SELECT DISTINCT TO_CHAR(c.ra,'FM000000000000')||RPAD(c.nome,70)||TO_CHAR(c.situacao_militar,'FM00') " +
  "FROM cidadao c, cid_evento e, jsm j, csm s " +
  "WHERE c.ra = e.cidadao_ra AND c.vinculacao_ano = EXTRACT(YEAR FROM SYSDATE) " +
  "AND e.data BETWEEN TO_DATE('0107'||(EXTRACT(YEAR FROM SYSDATE)-1),'DDMMYYYY') AND TO_DATE('3006'||EXTRACT(YEAR FROM SYSDATE),'DDMMYYYY') " +
  "AND e.codigo = 1 AND c.situacao_militar = 3 AND c.csm_codigo = j.csm_codigo AND c.jsm_codigo = j.codigo AND s.codigo = j.csm_codigo AND s.rm_codigo = ? AND j.cs = ?";

  private static final String SQL_DISTRIBUIDOS = "SELECT DISTINCT TO_CHAR(c.ra,'FM000000000000')||RPAD(c.nome,70)||RPAD(c.pai,70)||RPAD(c.mae,70)||TO_CHAR(c.nascimento_data,'DDMMYYYY')||" +
  "TO_CHAR(c.csm_codigo,'FM00')||TO_CHAR(c.jsm_codigo,'FM000')||RPAD(NVL(TO_CHAR(c.situacao_militar,'FM00'),CHR(32)),2)||RPAD(NVL(TO_CHAR(c.om_codigo,'FM000000000'),CHR(32)),9)||c.gpt_incorp " +
  "FROM cidadao c ,jsm j, csm s " +
  "WHERE c.vinculacao_ano = ? AND situacao_militar in (7,8) AND c.csm_codigo = j.csm_codigo AND c.jsm_codigo = j.codigo AND s.codigo = j.csm_codigo AND s.rm_codigo = ? AND j.cs = ?";

  @Inject
  private JsmDao jsmDao;

  @Inject
  private CsDao csDao;

  @Inject
  private Environment env;

  public ArquivoCsServico() {
    logger.debug("ArquivoCsServico iniciado");
  }

  public Path gerarArquivo(final Byte rm, final Integer csCodigo, final Integer ano, final Short tipo) throws SermilException {
    final Path tempDir = Paths.get(this.env.getRequiredProperty(Constantes.TEMP_DIR));
    try {
      final Cs cs = this.csDao.findById(csCodigo);
      List<Object[]> lista = null;
      //List<Object[]> listaRefratarios = null;
      List<Object[]> listaDispensados = null;
      String CAB = null;
      switch(tipo) {
      case 1: // Seleção
        lista = this.jsmDao.findBySQL(SQL_SELECIONADOS, rm, cs.getCodigo());
        if (lista == null || lista.isEmpty()) {
          throw new Exception("Não há dados para gerar o arquivo de seleção do Módulo CS.");
        } else {
          //listaRefratarios = this.jsmDao.findBySQL(SQL_REFRATARIOS, rm, csel);
          listaDispensados = this.jsmDao.findBySQL(SQL_DISPENSADOS, rm, cs.getCodigo());
          CAB = "2";
        }
        break;
      case 2: // Distribuição
        lista = this.jsmDao.findBySQL(SQL_DISTRIBUIDOS, ano, rm, cs.getCodigo());
        if (lista == null || lista.isEmpty()) {
          throw new Exception("Não há dados para gerar o arquivo de distribuição do Módulo CS.");
        }
        CAB = "7";
        break;
      default:
        break;
      }
      final StringBuilder nome = new StringBuilder(CAB).append(String.format("%02d",rm)).append(new SimpleDateFormat("yy").format(new Date())).append(String.format("%03d", cs.getNumero())).append("000");
      logger.debug("Arquivo MóduloCS: {}", nome.toString());
      final Path arquivoTexto = Paths.get(tempDir.toString(), nome.append(".txt").toString());
      try (BufferedWriter bw = Files.newBufferedWriter(arquivoTexto, Charset.forName("UTF-8"))) {
        // Cabeçalho
        bw.write((nome.delete(11, 15).append(this.sdf.format(new Date())).append("000").append(String.format("%03d",lista.size())).append("00004").append(EOF).toString()));
        switch (tipo) {
        case 1:
          // Detalhes Seleção
          for(Object o: lista) {
            logger.debug("Registro: {}", (String)o);
            bw.write(new StringBuilder((String)o).append(EOF).toString());
          }
          /* Lista de refratarios nao é mais enviada
          bw.write(new StringBuilder("#").append(EOF).toString());
          if (listaRefratarios == null || listaRefratarios.size() == 0) {
            bw.write(EOF);
          } else {
            for(Object o: listaRefratarios) {
              bw.write(new StringBuilder((String)o).append(EOF).toString());
            }
          }
          */
          bw.write(new StringBuilder("#").append(EOF).toString());
          if (listaDispensados == null || listaDispensados.size() == 0) {
            bw.write(EOF);
          } else {
            for(Object o: listaDispensados) {
              bw.write(new StringBuilder((String)o).append(EOF).toString());
            }
          }
          break;
        case 2:
          // Detalhes Distribuição
          for(Object o: lista) {
            logger.debug("Registro: {}", (String)o);
            bw.write(new StringBuilder((String)o).append(EOF).toString());
          }
          break;
        default:
          break;
        }
        bw.flush();
      } catch (IOException e) {
        throw new SermilException(e);
      }
      final Path arquivoCripto = Paths.get(tempDir.toString(), arquivoTexto.getFileName().toString().replace(".txt", ".cta"));
      CriptoSermil.executar(arquivoTexto.toFile(), arquivoCripto.toFile(), 2007);
      final Path zip1 = Paths.get(arquivoCripto.toString().substring(0, arquivoCripto.toString().lastIndexOf(".")).concat(".zip"));
      ZlibHelper.compactar(zip1, arquivoCripto);
      Files.delete(arquivoTexto);
      Files.delete(arquivoCripto);
      // Gambiarra enquanto Módulo CS não aceitar arquivo com extensão diferente de .cta
      final Path zip = Paths.get(arquivoCripto.toString().substring(0, arquivoCripto.toString().lastIndexOf(".")).concat(".cta"));
      Files.move(zip1, zip);
      return zip;
    } catch (Exception e) {
      throw new SermilException(e.getMessage());
    }
  }

}
