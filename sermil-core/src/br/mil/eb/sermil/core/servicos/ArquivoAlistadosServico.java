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
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.Jsm;

/** Geração do arquivo de carga de alistados para entrada no SASM.
 * @author Abreu Lopes, Gardino
 * @since 3.4
 * @version 5.2.3
 */
@Named("arquivoAlistadosServico")
public class ArquivoAlistadosServico {

   protected static final Logger logger = LoggerFactory.getLogger(ArquivoAlistadosServico.class);

   private static final String SQL =
         "SELECT TO_CHAR(ra,'FM000000000000')||RPAD(nome,70)||RPAD(pai,70)||RPAD(mae,70)||TO_CHAR(nascimento_data,'DDMMYYYY')||" +
         "LPAD(municipio_nascimento_codigo,8,'0')||LPAD(pais_nascimento_codigo,4,'0')||" +
         "estado_civil||sexo||RPAD(escolaridade,2)||'X1010'||DECODE(dispensa,1,1,2,2,3,3,4,4,5,5,15,6,0)||zona_residencial||" +
         "LPAD(municipio_residencia_codigo,8,'0')||LPAD(pais_residencia_codigo,4,'0')||" +
         "RPAD(NVL(endereco,' '),70)||RPAD(NVL(bairro,' '),70)||LPAD(NVL(cep,' '),8,'0')||SUBSTR(m.ddd,1,2)||" +
         "RPAD(SUBSTR(NVL(telefone,' '),1,8),8)||RPAD(NVL(cpf,' '),11)||RPAD(SUBSTR(NVL(rg,' '),1,12),12)||RPAD(NVL(email,' '),70)||TO_CHAR(e.data,'DDMMYYYY')||deseja_servir|| " +
         "TO_CHAR(d.emissao_data,'DDMMYYYY')||d.tipo||RPAD(NVL(d.numero,' '),32)||RPAD(NVL(d.livro,' '),10)||RPAD(NVL(d.folha,' '),10)||RPAD(NVL(d.cartorio,' '),30)||LPAD(municipio_codigo,8,'0') " +
         "FROM cidadao c JOIN cid_evento e ON c.ra = e.cidadao_ra JOIN municipio m ON c.municipio_residencia_codigo = m.codigo LEFT JOIN cid_doc_apres d ON c.ra = d.cidadao_ra WHERE c.csm_codigo = ? AND c.jsm_codigo = ? " +
         "AND e.codigo = 1 AND e.data BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY')";

   @Inject
   private JsmDao jsmDao;

   public ArquivoAlistadosServico() {
      logger.debug("ArquivoAlistadosServico iniciado.");
   }

   public Path gerarArquivo(final Jsm jsm, final Date dataInicial, final Date dataFinal) throws SermilException {
      final Path tempDir = Paths.get(Configurador.getInstance().getConfiguracao("temp.dir"));
      List<Object[]> lista = null;
      try {
         if (dataInicial == null && dataFinal == null) {
            throw new CriterioException("Informe a data inicial e final de alistamento.");
         } else {
            lista = this.jsmDao.findBySQL(SQL, jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo(), new SimpleDateFormat("dd/MM/yyyy").format(dataInicial), new SimpleDateFormat("dd/MM/yyyy").format(dataFinal));
         }
         if (lista == null || lista.size() == 0) {
            throw new SermilException("Não há alistados no período informado.");
         }
         final Jsm j = this.jsmDao.findById(jsm.getPk());
         final StringBuilder arqId = new StringBuilder("1")                   // TIPO ARQ
               .append(String.format("%02d", j.getCsm().getRm().getCodigo())) // RM
               .append(String.format("%02d", j.getPk().getCsmCodigo()))       // CSM
               .append(String.format("%03d", j.getPk().getCodigo()))          // JSM
               .append("000");                                                // NR ARQ

         final Path arquivoTexto = Paths.get(tempDir.toString(), new StringBuilder(arqId).append(".dat").toString());
         logger.debug("Arquivo MóduloJSM: {}", arquivoTexto.toString());
         try (BufferedWriter bw = Files.newBufferedWriter(arquivoTexto, Charset.forName("UTF-8"))) {
            final StringBuilder cab = new StringBuilder(arqId)
                  .append(new SimpleDateFormat("ddMMyyyy").format(new Date())) // DATA ATUAL
                  .append("000")                                               // NUMERO GR
                  .append(String.format("%07d", lista.size()) + "00004");      // VERSAO
            logger.debug("cab: {}", cab);
            bw.write(cab.toString());
            bw.newLine();
            for (Object item : lista) {
               logger.debug("info: {}", item);
               bw.write((String) item);
               bw.newLine();
            }
            bw.flush();
         } catch (IOException e) {
            throw e;
         }
         final Path arquivoCripto = Paths.get(tempDir.toString(), arquivoTexto.getFileName().toString().replace(".dat", ".cta"));
         CriptoSermil.executar(arquivoTexto.toFile(), arquivoCripto.toFile(), 2007);
         Files.delete(arquivoTexto);
         return arquivoCripto;
      } catch (Exception e) {
         throw new SermilException(e.getMessage());
      }
   }

}
