package br.mil.eb.sermil.core.servicos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.RaItensDao;
import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.dao.RaPedidoDao;
import br.mil.eb.sermil.core.exceptions.RaItemSalvarFalhaException;
import br.mil.eb.sermil.core.exceptions.RaMestreException;
import br.mil.eb.sermil.core.exceptions.RaPedidoJaProcessadoException;
import br.mil.eb.sermil.core.exceptions.RaPedidoSalvarFalhaException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.RaItens;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.modelo.RaMestre.PK;
import br.mil.eb.sermil.modelo.RaPedido;
import br.mil.eb.sermil.tipos.Ra;

/**
 * Servi�os do processo de Pedido de RA.
 * 
 * @author dsmanselmo
 * @since 5.0
 * @version $Id: JsmRaPedidoServico.java 2511 2014-08-20 11:08:50Z anselmo $
 */
@Named("jsmRaPedidoProcessamentoServico")
public class JsmRaPedidoProcessamentoServico {

    protected static final Logger logger = LoggerFactory.getLogger(JsmRaPedidoProcessamentoServico.class);

    @Inject
    private RaPedidoDao pedidoDao;

    @Inject
    private RaPedidoDao raPedidoDao;

    @Inject
    private RaMestreDao raMestreDao;

    @Inject
    private RaItensDao RaItensDao;

    private InputStream arqStream;

    public JsmRaPedidoProcessamentoServico() {
        logger.debug("JsmRaPedidoProcessamentoServico iniciado");
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
    @Transactional
    public void processarRaPedido(final Integer raPedidoNumero) throws RaPedidoJaProcessadoException, RaPedidoSalvarFalhaException, RaItemSalvarFalhaException, RaMestreException  {

        final RaPedido raPedido = this.pedidoDao.findById(raPedidoNumero);

        if ("S".equals(raPedido.getProcessado())) {
            throw new RaPedidoJaProcessadoException();
        }
        // Alterar e salvar Pedido
        raPedido.setProcessado("S");
        try {
            this.raPedidoDao.save(raPedido); 
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RaPedidoSalvarFalhaException();
        }

        for (RaItens raItem : raPedido.getRaItensCollection()) {
            // Incrementar Sequencial do Ra Mestre
            final PK raMestrePk = new PK();
            raMestrePk.setCsmCodigo(raItem.getPk().getCsmCodigo());
            raMestrePk.setJsmCodigo(raItem.getPk().getJsmCodigo());
            final RaMestre raMestre = this.raMestreDao.findById(raMestrePk);
            int sequencial = raMestre.getSequencial() + raItem.getQuantidade();
            raMestre.setSequencial(sequencial);
            try {
                this.raMestreDao.save(raMestre);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RaMestreException();
            }

            // Definir ra inicial e final de RaItem
            raItem.setRaInicial(sequencial - raItem.getQuantidade() + 1);
            raItem.setRaFinal(sequencial);
            try {
                RaItensDao.save(raItem);
            } catch (Exception e1) {
                logger.error(e1.getMessage());
                throw new RaItemSalvarFalhaException();
            }
        }
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
    public Path createTxtFile(final RaItens item) throws SermilException {
        String title = new StringBuilder("Faixa de RA Emergencial - ").append(item.getPk().getJsmCodigo()).append("� JSM.txt").toString();
        String caminho = Configurador.getInstance().getConfiguracao("temp.dir").toString();
        final Path file = Paths.get(caminho, title);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(file.toString()), Charset.forName("UTF-8"))) {
            String title2 = new StringBuilder("Pedido Nr ").append(pedidoDao.findById(item.getPk().getRaPedidoNumero()).toString()).toString();
            bw.write(title.replace(".txt", "") + "\r\n\r\n");
            bw.write(title2 + "\r\n\r\n");
            Integer raIinicial = item.getRaInicial();
            Integer raFinal = item.getRaFinal();
            Integer i = 0;
            final Ra.Builder rb = new Ra.Builder();
            for (Integer num = raIinicial; num <= raFinal; num++) {
                bw.write(rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(num).build().toString());
                if (++i % 4 == 0) {
                    bw.write("\r\n\r\n");
                } else {
                    bw.write("\t");
                }
            }
            bw.flush();
            return file;
        } catch (Exception e1) {
            logger.error(e1.getMessage());
            throw new SermilException("Arquivo n�o p�de ser gerado.");
        }
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
    public Path gerarArquivo(final RaItens item) throws SermilException, IOException, RaMestreException {
        // Criando arquivo
        final String faixaTipo = Byte.toString(item.getPk().getTipo()).equals("3") ? "E" : "A";
        final String csm = String.format("%02d", item.getPk().getCsmCodigo());
        final String jsm = String.format("%03d", item.getPk().getJsmCodigo());
        String caminho = Configurador.getInstance().getConfiguracao("temp.dir").toString();
        String title = "FAIXARA" + faixaTipo + csm + jsm + ".txt";
        final Path file = Paths.get(caminho, title);

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(file.toString()), Charset.forName("UTF-8"))) {
            // Criando as os valores que vao compor as linhas do arquivo:
            final Ra.Builder rb = new Ra.Builder();
            final String raInicial = rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(item.getRaInicial()).build().toString();
            final String raFinal = rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(item.getRaFinal()).build().toString();
            final String qtd = String.format("%06d", item.getQuantidade());
            final String tipo = String.format("%01d", item.getPk().getTipo());
            final Format formatter = new SimpleDateFormat("ddMMyyyy");
            final String data = formatter.format(new Date());

            // Escrevendo no arquivo
            bw.write(csm + jsm + raInicial + raFinal + qtd + tipo + data);
            bw.flush();

            // Criptografar
            final Path criptoFile = Paths.get(caminho, title.replace(".txt", ".cta"));
            Integer chave = Integer.parseInt(item.getPk().getCsmCodigo() + jsm);
            CriptoSermil.executar(file.toFile(), criptoFile.toFile(), chave);

            Files.delete(file);
            return criptoFile;

        }
    }

    public InputStream getArqStream() {
        return arqStream;
    }

    public void setArqStream(InputStream arqStream) {
        this.arqStream = arqStream;
    }

    public RaMestreDao getRaMestreDao() {
        return raMestreDao;
    }

    public void setRaMestreDao(RaMestreDao raMestreDao) {
        this.raMestreDao = raMestreDao;
    }

    public RaItensDao getRaItensDao() {
        return RaItensDao;
    }

    public void setRaItensDao(RaItensDao raItensDao) {
        RaItensDao = raItensDao;
    }

}
