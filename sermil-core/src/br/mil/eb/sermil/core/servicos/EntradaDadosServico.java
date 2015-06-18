package br.mil.eb.sermil.core.servicos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.Constantes;
import br.mil.eb.sermil.core.dao.ImpServicoDao;
import br.mil.eb.sermil.core.exceptions.ConsultaException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.core.utils.ZlibHelper;
import br.mil.eb.sermil.modelo.ImpDocumento;
import br.mil.eb.sermil.modelo.ImpServico;
import br.mil.eb.sermil.modelo.Usuario;

/** Processamento da entrada de dados de arquivos do Módulo JSM (FAMCO) e Módulo CS (FS e FAMSEL).
 * As informações dos arquivos são armazenadas nas tabelas IMP_SERVICO (cabeçalho do arquivo) e IMP_DOCUMENTO (linhas).
 * Os arquivos devem estar criptografados e compactados com a extensão .cta antes de serem processados.
 * Somente são aceitos arquivos com o identificador de versão 00003 na linha de cabeçalho (primeira linha).
 * O layout dos arquivos está descrito na documentação do sistema.
 * @author Abreu Lopes, Gardino
 * @since 3.4
 * @version $Id: EntradaDadosServico.java 2509 2014-08-19 18:36:30Z wlopes $
 */
@Named("entradaDadosServico")
public class EntradaDadosServico {

  private static final String SELECT_1 = "SELECT s.codigo, s.ano, s.gravacao_data, s.gr_nr, s.gr_emissao_data, s.gr_entrada_data, s.nome_arquivo, s.registros_qtd, u.cpf||' - '||u.nome, s.usuario_data FROM imp_servico s, usuario u WHERE u.cpf = s.usuario AND SUBSTR(CODIGO,1,1) = ? AND TO_NUMBER(SUBSTR(CODIGO,4,2)) = ? and ano = ?";

  private static final String SELECT_2 = "SELECT s.codigo, s.ano, s.gravacao_data, s.gr_nr, s.gr_emissao_data, s.gr_entrada_data, s.nome_arquivo, s.registros_qtd, u.cpf||' - '||u.nome, s.usuario_data FROM imp_servico s, cs c, usuario u WHERE u.cpf = s.usuario AND SUBSTR(s.CODIGO,2,2) = c.rm and SUBSTR(s.codigo,6,3) = c.cs AND SUBSTR(s.CODIGO,1,1) in (2,6) AND c.csm = ? AND ano = ?";

  protected static final Logger logger = LoggerFactory.getLogger(EntradaDadosServico.class);

  @Inject
  private ImpServicoDao dao;

  public EntradaDadosServico() {
    logger.debug("EntradaDadosServico iniciado");
  }

  public List<Object[]> listar(final Integer csm, final Integer ano, final Integer tipo) throws SermilException {
    if (csm == null || ano == null || tipo == null) {
      throw new CriterioException();
    }
    switch (tipo) {
    case 1:
      final List<Object[]> lista = this.dao.findBySQL(SELECT_1, String.valueOf(tipo), csm, ano);
      return lista;
    case 2:
      final List<Object[]> lista2 = this.dao.findBySQL(SELECT_2, csm, ano);
      return lista2;
    default:
      throw new ConsultaException();
    }
   }

  public ImpServico recuperar(final ImpServico.PK id) throws SermilException {
    return this.dao.findById(id);
  }

  @Transactional
  public ImpServico processar(final File arquivo, final Usuario usr) throws SermilException {
    if (arquivo == null || arquivo.getName().isEmpty()) {
      throw new SermilException("ERRO: informe o nome do arquivo a ser carregado.");
    }
    final Path tempDir = Paths.get(Configurador.getInstance().getConfiguracao(Constantes.TEMP_DIR));
    final Path txt = Paths.get(tempDir.toString(), arquivo.getName().concat(".txt"));
    try {
      CriptoSermil.executar(ZlibHelper.descompactar(arquivo.toPath()).toFile(), txt.toFile(), 2007);
    } catch (Throwable e) {
      throw new SermilException(e.getMessage());
    }
    ImpServico impSrv = null;
    try (BufferedReader br = Files.newBufferedReader(txt, Charset.forName("ISO-8859-1"))) {
      String linha = null;
      if ((linha = br.readLine()) != null) {
        impSrv = new ImpServico(linha);
      } else {
        throw new SermilException("ERRO: Arquivo inválido, sem cabeçalho.");
      }
      try {
        if (this.dao.findById(impSrv.getPk()) != null) {
          throw new SermilException("Arquivo " + arquivo.getName() + " já foi carregado.");
        }
      } catch (NoDataFoundException e) {
        logger.debug("Sem dados = {}", e);
      }
      int documento = 0;
      while (br.ready()) {
        documento++;
        final ImpDocumento doc = new ImpDocumento(impSrv.getPk().getCodigo(), impSrv.getPk().getAno(), 1, documento);
        doc.setDocTipo(impSrv.getTipo());
        doc.setMensagemCodigo(Byte.valueOf("0"));
        doc.decode(br.readLine());
        impSrv.addImpDocumento(doc);
      }
      impSrv.setUsuario(usr);
      impSrv.setUsuarioData(new Date());
      this.dao.save(impSrv);
      if (documento != impSrv.getRegistrosQtd()) {
        throw new IOException("ERRO: Arquivo não foi carregado, tente novamente. [Qtd registros inconsistente: " + documento + " de " + impSrv.getRegistrosQtd() + "]");
      }
      return impSrv;
    } catch (IOException ioe) {
      ioe.printStackTrace();
      throw new SermilException(ioe);
    }
  }

}
