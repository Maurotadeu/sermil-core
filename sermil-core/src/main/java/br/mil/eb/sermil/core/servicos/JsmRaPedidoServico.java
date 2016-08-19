package br.mil.eb.sermil.core.servicos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.dao.RaItensDao;
import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.dao.RaPedidoDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.RaPedidoException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Constantes;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.RaItens;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.modelo.RaPedido;
import br.mil.eb.sermil.tipos.Ra;

/** Serviço de Pedido de Faixa de RA.
 * @author Abreu Lopes, Anselmo Ribeiro
 * @since 3.0
 * @version 5.4.6
 */
@Named("jsmRaPedidoservico")
public class JsmRaPedidoServico {

  protected static final Logger logger = LoggerFactory.getLogger(JsmRaPedidoServico.class);

  @Inject
  private Environment env;
  
  @Inject
  private RaMestreDao raMestreDao;

  @Inject
  private RaPedidoDao raPedidoDao;

  @Inject
  private RaItensDao raItemDao;

  @Inject
  private JsmDao jsmDao;

  public JsmRaPedidoServico() {
    logger.debug("JsmRaPedidoServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
  public List<RaPedido> listar(final RaPedido pedido) throws SermilException {
    List<RaPedido> lista = null;
    if (pedido == null) {
      throw new CriterioException();
    } else {
      if (pedido.getNumero() != null) {
        final RaPedido ped = this.recuperar(pedido.getNumero());
        if (ped != null) {
          lista = new ArrayList<RaPedido>(1);
          lista.add(ped);
        }
      } else if (pedido.getCsm().getCodigo() != -1 && pedido.getCsm().getCodigo() != null) {
        lista = createYmd(this.raPedidoDao.findByNamedQuery("RaPedido.listarPorCsm", pedido.getCsm().getCodigo(), "false".equals(pedido.getAprovado()) ? "N" : "S"));
      } else if (pedido.getAprovado() != null) {
        lista = createYmd(this.raPedidoDao.findByNamedQuery("RaPedido.listarPorStatus", "false".equals(pedido.getAprovado()) ? "N" : "S"));
      } else {
        throw new CriterioException();
      }
    }
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public void aprovar(final Integer id) throws SermilException {
    final RaPedido pedido = this.raPedidoDao.findById(id);
    if (this.isProcessarOk(pedido)) {
      final EntityManager em = this.raPedidoDao.getEntityManager();
      em.lock(pedido, LockModeType.PESSIMISTIC_WRITE);
      // Processar Itens do Pedido
      for (RaItens item : pedido.getRaItensCollection()) {
        // Incrementar Sequencial (RaMestre)
        final RaMestre mestre = this.raMestreDao.findById(new RaMestre.PK(item.getPk().getCsmCodigo(), item.getPk().getJsmCodigo()));
        em.lock(mestre, LockModeType.PESSIMISTIC_WRITE);
        em.lock(item, LockModeType.PESSIMISTIC_WRITE);
        final Integer sequencial = mestre.getSequencial() + item.getQuantidade();
        mestre.setSequencial(sequencial);
        this.raMestreDao.save(mestre);
        // Definir RA inicial e final do RaItem
        item.setRaInicial(sequencial - item.getQuantidade() + 1);
        item.setRaFinal(sequencial);
        logger.info("Faixa gerada: {} - {} (JSM = {})", item.getRaInicial(), item.getRaFinal(), mestre.getPk());
      }
      // Salvar Pedido
      pedido.setAprovado("S");
      pedido.setProcessado("S");
      this.raPedidoDao.save(pedido);
      em.flush();
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public void excluir(final Integer id) throws SermilException {
    final RaPedido pedido = this.raPedidoDao.findById(id);
    if (this.isProcessarOk(pedido)) {
      this.raPedidoDao.delete(pedido);
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','csm')")
  @Transactional
  public RaPedido salvar(final RaPedido pedido) throws SermilException {

    // Verifica status de aprovado e processado do pedido.
    this.isProcessarOk(pedido);

    // Salvar o pedido antes dos itens,para obter o número do pedido
    final RaPedido novo = this.raPedidoDao.save(pedido);

    // Processar os itens do pedido
    for (RaItens item : pedido.getRaItensCollection()) {
      item.getPk().setRaPedidoNumero(novo.getNumero());
      item.setRaPedido(novo);
      item.setJsm(this.jsmDao.findById(new Jsm.PK(item.getPk().getCsmCodigo(), item.getPk().getJsmCodigo())));
      // Acertar tipo do pedido se a JSM for não-informatizada
      if ("N".equals(item.getJsm().getInfor())) {
        item.getPk().setTipo(Byte.valueOf("1"));
      }
      // Arredondar quantidade para multiplos de 10
      if (item.getQuantidade() != null && item.getQuantidade() > 0) {
        int qtd = item.getQuantidade();
        item.setQuantidade((qtd % 10 == 0 ? qtd : qtd + (10 - qtd%10)));
      }
    }
    return this.raPedidoDao.save(novo);
  }

  public RaPedido recuperar(final Integer id) throws SermilException {
    final RaPedido pedido = this.raPedidoDao.findById(id);
    if (pedido == null) {
      throw new SermilException("Pedido de RA não existe.");
    }
    return pedido;
  }

  public RaItens recuperarItem(final RaItens obj) throws SermilException {
    final RaItens item = this.raItemDao.findById(obj.getPk());
    if (item == null) {
      throw new SermilException("Item do pedido de RA não existe.");
    }
    return item;
  }

  public boolean isProcessarOk(final RaPedido pedido) throws RaPedidoException {
    if (pedido == null)
      throw new RaPedidoException("Pedido não foi informado.");
    if ("S".equals(pedido.getProcessado())) {
      throw new RaPedidoException(pedido, "Pedido já foi processado.");
    }
    if ("S".equals(pedido.getAprovado())) {
      throw new RaPedidoException(pedido, "Pedido já foi aprovado.");
    }
    return true;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
  public Path gerarArquivoEmergencial(final RaItens obj) throws SermilException {

    // Obter Item da Faixa
    final RaItens item = this.recuperarItem(obj);

    // Criar Arquivo
    final String caminho = this.env.getRequiredProperty(Constantes.TEMP_DIR).toString();
    final String nome = new StringBuilder("Faixa de RA Emergencial - ").append(item.getPk().getJsmCodigo()).append("ª JSM.txt").toString();
    final Path file = Paths.get(caminho, nome);

    // Gravar linhas no arquivo
    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(file.toString()), Charset.forName("UTF-8"))) {
      bw.write(nome.replace(".txt", "") + "\r\n\r\n");
      bw.write(new StringBuilder("Pedido Nr ").append(item.getPk().getRaPedidoNumero()).append("\r\n\r\n").toString());
      int i = 0;
      final Ra.Builder rb = new Ra.Builder();
      for (Integer num = item.getRaInicial(); num <= item.getRaFinal(); num++) {
        bw.write(rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(num).build().toString());
        if (++i % 4 == 0) {
          bw.write("\r\n\r\n");
        } else {
          bw.write("\t");
        }
      }
      bw.flush();
      return file;
    } catch (IOException ioe) {
      throw new SermilException(ioe.getMessage());
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm')")
  public Path gerarArquivoSasm(final RaItens obj) throws SermilException {

    // Obter Item da Faixa
    final RaItens item = this.recuperarItem(obj);

    // Criar Arquivo de Faixa de RA do SASM
    final String csm = String.format("%02d", item.getPk().getCsmCodigo());
    final String jsm = String.format("%03d", item.getPk().getJsmCodigo());
    final String caminho = this.env.getRequiredProperty(Constantes.TEMP_DIR).toString();
    final String nome = new StringBuilder("FAIXARA").append(item.getPk().getTipo() == 3 ? "E" : "A").append(csm).append(jsm).append(".txt").toString();
    final Path file = Paths.get(caminho, nome);

    // Grava linha no arquivo: CSM(00) + JSM(000) + RA Inicial + RA Final + QTD(000000) + TIPO(0) + DATA (DDMMYYYY)
    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(file.toString()), Charset.forName("UTF-8"))) {
      final Ra.Builder rb = new Ra.Builder();
      final String linha =  new StringBuilder(csm)
                                      .append(jsm)
                                      .append(rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(item.getRaInicial()).build().toString())
                                      .append(rb.csm(item.getPk().getCsmCodigo()).jsm(item.getPk().getJsmCodigo()).sequencial(item.getRaFinal()).build().toString())
                                      .append(String.format("%06d", item.getQuantidade()))
                                      .append(String.format("%01d", item.getPk().getTipo()))
                                      .append(new SimpleDateFormat("ddMMyyyy").format(new Date()))
                                      .toString();
      logger.info("LINHA: {}", linha);
      bw.write(linha);
      bw.flush();

      // Criptografar Arquivo de Faixa
      final Path criptoFile = Paths.get(caminho, nome.replace(".txt", ".cta"));
      Integer chave = Integer.parseInt(item.getPk().getCsmCodigo() + jsm);
      CriptoSermil.executar(file.toFile(), criptoFile.toFile(), chave);

      Files.delete(file);
      return criptoFile;
    } catch (IOException ioe) {
      throw new SermilException(ioe.getMessage());
    }
  }
  
  private List<RaPedido> createYmd(final List<RaPedido> lista) {
    for (RaPedido pedido : lista) {
      final Calendar cal = Calendar.getInstance();
      cal.setTime(pedido.getData());
      pedido.setYmd(new StringBuilder(cal.get(Calendar.YEAR)).append(String.format("%02d", cal.get(Calendar.MONTH))).append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))).toString());
    }
    return lista;
  }
  
}
