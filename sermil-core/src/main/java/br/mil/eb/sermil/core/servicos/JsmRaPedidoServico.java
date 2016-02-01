package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.dao.RaItensDao;
import br.mil.eb.sermil.core.dao.RaPedidoDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.RaPedidoJaAprovadoException;
import br.mil.eb.sermil.core.exceptions.RaPedidoJaProcessadoException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.RaItens;
import br.mil.eb.sermil.modelo.RaPedido;

/** Serviços do processo de Pedido de RA.
 * @author Abreu Lopes, Anselmo
 * @since 3.0
 * @version $Id$
 */
@Named("jsmRaPedidoservico")
public class JsmRaPedidoServico {

    protected static final Logger logger = LoggerFactory.getLogger(JsmRaPedidoServico.class);

    @Inject
    private RaPedidoDao pedidoDao;

    @Inject
    private RaItensDao itemDao;

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
                final RaPedido ped = this.pedidoDao.findById(pedido.getNumero());
                if (ped != null) {
                    lista = new ArrayList<RaPedido>(1);
                    lista.add(ped);
                }
            } else if (pedido.getCsm().getCodigo() != -1 && pedido.getCsm().getCodigo() != null) {
                lista = createYmd(this.pedidoDao.findByNamedQuery("RaPedido.listarPorCsm", pedido.getCsm().getCodigo(), "false".equals(pedido.getAprovado()) ? "N" : "S"));
            } else if (pedido.getAprovado() != null) {
                lista = createYmd(this.pedidoDao.findByNamedQuery("RaPedido.listarPorStatus", "false".equals(pedido.getAprovado()) ? "N" : "S"));
            } else {
                throw new CriterioException();
            }
        }
        if (lista == null || lista.isEmpty()) {
            throw new NoDataFoundException();
        }
        return lista;
    }

    public List<RaPedido> createYmd(final List<RaPedido> lista) {
        for (RaPedido pedido : lista) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(pedido.getData());
            pedido.setYmd(new StringBuilder(cal.get(Calendar.YEAR)).append(String.format("%02d", cal.get(Calendar.MONTH))).append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))).toString());
        }
        return lista;
    }

    @PreAuthorize("hasAnyRole('adm','dsm')")
    @Transactional
    public void excluir(final Integer id) throws SermilException {
        final RaPedido pedido = this.pedidoDao.findById(id);
        if ("S".equals(pedido.getAprovado())) {
            throw new SermilException("Pedido já foi aprovado, não pode ser excluído.");
        }
        if ("S".equals(pedido.getProcessado())) {
            throw new SermilException("Pedido já foi processado, não pode ser excluído.");
        }
        this.pedidoDao.delete(pedido);
    }

    @PreAuthorize("hasAnyRole('adm','dsm')")
    @Transactional
    public void aprovar(final Integer id) throws SermilException {
        final RaPedido pedido = pedidoDao.findById(id);
        if ("S".equals(pedido.getProcessado())) {
            throw new RaPedidoJaProcessadoException();
        } else if ("S".equals(pedido.getAprovado())) {
            throw new RaPedidoJaAprovadoException();
        }
        pedido.setAprovado("S");
        this.pedidoDao.save(pedido);
    }

    @PreAuthorize("hasAnyRole('adm','dsm','csm')")
    @Transactional
    public RaPedido salvar(final RaPedido pedido) throws SermilException {
        // Verificar se pedido ja foi processado ou aprovado.
        if (pedido == null)
            throw new CriterioException();
        if ("S".equals(pedido.getProcessado())) {
            throw new RaPedidoJaProcessadoException();
        } else if ("S".equals(pedido.getAprovado())) {
            throw new RaPedidoJaAprovadoException();
        }
        // Salvar o pedido antes dos itens,para obter o número do pedido
        final RaPedido pedidoNovo = this.pedidoDao.save(pedido);

        // Processar os itens do pedido
        for (RaItens item : pedido.getRaItensCollection()) {
            item.getPk().setRaPedidoNumero(pedidoNovo.getNumero());
            item.setRaPedido(pedidoNovo);
            item.setJsm(this.jsmDao.findById(new Jsm.PK(item.getPk().getCsmCodigo(), item.getPk().getJsmCodigo())));
            // Acertar tipo do pedido se a JSM for não-informatizada
            if ("N".equals(item.getJsm().getInfor())) {
                item.getPk().setTipo(Byte.valueOf("1"));
            }
            // Arredondar quantidade para multiplos de 10
            if (item.getQuantidade() != null && item.getQuantidade() > 0) {
                int qtd = item.getQuantidade();
                item.setQuantidade((qtd % 10 == 0 ? qtd : qtd + (10 - qtd % 10)));
            }
            this.itemDao.save(item);
        }
        return pedidoNovo;
    }

    public RaPedido recuperar(final Integer id) throws SermilException {
        return this.pedidoDao.findById(id);
    }

    public RaItens findRaItemByPk(RaItens.PK pk) {
        return this.itemDao.findById(pk);
    }

}
