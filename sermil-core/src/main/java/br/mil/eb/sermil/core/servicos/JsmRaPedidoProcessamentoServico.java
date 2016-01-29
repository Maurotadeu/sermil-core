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
import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.RaItensDao;
import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.dao.RaPedidoDao;
import br.mil.eb.sermil.core.exceptions.RaMestreException;
import br.mil.eb.sermil.core.exceptions.RaPedidoJaProcessadoException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.RaItens;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.modelo.RaPedido;
import br.mil.eb.sermil.tipos.Ra;

/** Serviços do processo de Pedido de RA.
 * @author Anselmo, Abreu Lopes
 * @since 5.0
 * @version 5.2.7
 */
@Named("jsmRaPedidoProcessamentoServico")
public class JsmRaPedidoProcessamentoServico {

    protected static final Logger logger = LoggerFactory.getLogger(JsmRaPedidoProcessamentoServico.class);

    @Inject
    private RaMestreDao raMestreDao;

    @Inject
    private RaPedidoDao raPedidoDao;

    @Inject
    private RaItensDao itemDao;

    private InputStream arqStream;

    public JsmRaPedidoProcessamentoServico() {
        logger.debug("JsmRaPedidoProcessamentoServico iniciado");
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
    @Transactional
    public void processarRaPedido(final Integer raPedidoNumero) throws SermilException  {
        if (raPedidoNumero == null) {
          throw new SermilException("Nr do Pedido de RA deve ser informado");
        }
       
        // Verificar Pedido de RA
        final RaPedido raPedido = this.raPedidoDao.findById(raPedidoNumero);
        if ("S".equals(raPedido.getProcessado())) {
            throw new RaPedidoJaProcessadoException();
        }
        this.raPedidoDao.getEntityManager().lock(raPedido, LockModeType.PESSIMISTIC_WRITE);
        
        // Alterar e salvar Pedido de RA
        raPedido.setProcessado("S");
        this.raPedidoDao.save(raPedido);
        for (RaItens raItem : raPedido.getRaItensCollection()) {
            // Incrementar Sequencial (RaMestre)
            final RaMestre raMestre = this.raMestreDao.findById(new RaMestre.PK(raItem.getPk().getCsmCodigo(), raItem.getPk().getJsmCodigo()));
            this.raMestreDao.getEntityManager().lock(raMestre, LockModeType.PESSIMISTIC_WRITE);
            this.raMestreDao.getEntityManager().lock(raItem, LockModeType.PESSIMISTIC_WRITE);
            final Integer sequencial = raMestre.getSequencial() + raItem.getQuantidade();
            raMestre.setSequencial(sequencial);
            this.raMestreDao.save(raMestre);
            // Definir RA inicial e final do RaItem
            raItem.setRaInicial(sequencial - raItem.getQuantidade() + 1);
            raItem.setRaFinal(sequencial);
            this.itemDao.save(raItem);
            logger.debug("Faixa gerada: {} - {} (JSM = {})", raItem.getRaInicial(), raItem.getRaFinal(), raMestre.getPk());
        }
        this.raMestreDao.getEntityManager().flush();
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
    public Path createTxtFile(final RaItens item) throws SermilException {
        String title = new StringBuilder("Faixa de RA Emergencial - ").append(item.getPk().getJsmCodigo()).append("ª JSM.txt").toString();
        String caminho = Configurador.getInstance().getConfiguracao("temp.dir").toString();
        final Path file = Paths.get(caminho, title);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(file.toString()), Charset.forName("UTF-8"))) {
            String title2 = new StringBuilder("Pedido Nr ").append(this.raPedidoDao.findById(item.getPk().getRaPedidoNumero()).toString()).toString();
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
            throw new SermilException("Falha ao gerar o arquivo de faixa emergencial de RA");
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

}
