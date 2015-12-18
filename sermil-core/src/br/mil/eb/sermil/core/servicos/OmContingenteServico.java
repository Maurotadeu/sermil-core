package br.mil.eb.sermil.core.servicos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.OmBoletimDao;
import br.mil.eb.sermil.core.dao.PostoGraduacaoDao;
import br.mil.eb.sermil.core.dao.QmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.OmBoletim;
import br.mil.eb.sermil.modelo.OmBoletimCidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Gerenciamento do contingente incorporado em uma OM.
 * @author Abreu Lopes, Gardino
 * @since 4.0
 * @version 5.2.6
 */
@Named("omEfetivoServico")
public class OmContingenteServico {

    protected static final Logger logger = LoggerFactory.getLogger(OmContingenteServico.class);

    @Inject
    private CidadaoDao cidadaoDao;

    @Inject
    private PostoGraduacaoDao pgDao;

    @Inject
    private QmDao qmDao;

    @Inject
    private OmBoletimDao omBoletimDao;

    public OmContingenteServico() {
        logger.debug("OmContingenteServico iniciado");
    }

    /** Listagem do efetivo da OM.
     * @param codom C�digo da OM
     * @param ano Ano da distribui��o
     * @param situacao Situa��o Militar (7 = distribu�do ou 12 = incorporado)
     * @param data data default para o evento a ser gerado
     * @param porNome true se listar por nome
     * @return Lista de eventos (CID_EVENTO) para cada cidad�o relacionado
     * @throws SermilException erro na consulta
     */
    public List<CidEvento> listarEfetivo(final Integer codom, final Integer ano, final Integer situacao, final Date data, final String biAbiNr, final boolean porNome) throws SermilException {
        List<CidEvento> listaEvt = null;
        final List<Object[]> listaEfetivo = this.cidadaoDao.findBySQL("SELECT c.ra, c.nome, c.padrao_Codigo, c.gpt_Incorp FROM Cidadao c, Cid_evento e WHERE c.ra = e.cidadao_ra and c.om_codigo = ? AND c.situacao_Militar = ? AND e.codigo = 7 AND extract(year from e.data) = ?  order by c.gpt_Incorp", codom, situacao, ano);
        // final List<Object[]> listaCid = this.cidadaoDao.findByNamedQueryArray("Cidadao.listarPorOmSituacao", codom, ano.substring(2, 4), situacao);
        final Date dataEvt = (data != null ? data : new Date());
        final int codigo = (situacao.equals(Short.valueOf("7")) ? Byte.valueOf("9") : Byte.valueOf("15"));
        if (listaEfetivo != null && !listaEfetivo.isEmpty()) {
            listaEvt = new ArrayList<CidEvento>(listaEfetivo.size());
            for (Object[] c : listaEfetivo) {
                final CidEvento e = new CidEvento(((BigDecimal) c[0]).longValue(), codigo, dataEvt);
                e.setBiAbiNr(biAbiNr);
                e.setAnotacao((String) c[1]);
                final Cidadao cid = new Cidadao();
                cid.setRa(((BigDecimal) c[0]).longValue());
                cid.setNome((String) c[1]);
                cid.setPadraoCodigo((String) c[2]);
                cid.setGptIncorp((String) c[3]);
                cid.addCidEvento(e);
                listaEvt.add(e);
            }
            if (porNome) {
                Collections.sort(listaEvt, new OmContingenteServico.OrdenaNome());
            }
        }
        return listaEvt;
    }

    public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom) throws SermilException {
        return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimOm", ano, codom);
    }

    public List<OmBoletim> listarBoletim(final Integer ano, final Integer codom, final Integer codigo) throws SermilException {
        return this.omBoletimDao.findByNamedQuery("OmBoletim.listarBoletimCod", ano, codom, codigo);
    }

    /** Processamento de incorpora��o de OM.
     * @param lista lista de eventos de cidad�os a serem incorporados
     * @param usr usu�rio respons�vel pela informa��o
     * @throws SermilException erro no processamento das informa��es
     */
    @Transactional
    public void incorporar(final List<CidEvento> lista, final Usuario usr, final List<OmBoletim> listaBoletim) throws SermilException {
        final Calendar cal = Calendar.getInstance();
        for (int i = 0; i < lista.size(); i++) {
            final CidEvento e = lista.get(i);
            if (e != null && e.getPk() != null && e.getPk().getCodigo() != -1) {
                int sitMil = TipoSituacaoMilitar.DISTRIBUIDO.ordinal();
                switch (e.getPk().getCodigo()) {
                case 6:
                    sitMil = TipoSituacaoMilitar.EXCESSO_OM.ordinal();
                    break;
                case 8:
                    sitMil = TipoSituacaoMilitar.INSUBMISSO.ordinal();
                    break;
                case 9:
                    sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
                    break;
                case 23:
                    sitMil = TipoSituacaoMilitar.REFRATARIO.ordinal();
                    break;
                }
                e.setAnotacao("USR: " + usr.getCpf());
                final Cidadao c = this.cidadaoDao.findById(e.getPk().getCidadaoRa());
                c.addCidEvento(e);
                c.setSituacaoMilitar(sitMil);
                final CidAuditoria aud = new CidAuditoria(c.getRa(), new Date(), "EVENTO " + e.getPk().getCodigo(), usr.getAcessoIp(), usr.getCpf());
                c.addCidAuditoria(aud);
                this.cidadaoDao.save(c);
                // Salva Boletim Incorp
                if (sitMil == 12) {
                    if (listaBoletim.get(i).getPk().getCodigo() != -1) {
                        final OmBoletim o = listaBoletim.get(i);
                        final List<OmBoletim> listaBol = this.listarBoletim(cal.get(Calendar.YEAR), c.getOm().getCodigo(), o.getPk().getCodigo());
                        final OmBoletim bol = listaBol.get(0);
                        final OmBoletimCidadao obc = new OmBoletimCidadao();
                        obc.setAno(bol.getPk().getAno());
                        obc.setBoletimCod(bol.getPk().getCodigo());
                        obc.setOmCodigo(bol.getPk().getOmCodigo());
                        obc.getPk().setCidadaoRa(Long.valueOf(c.getRa()));
                        obc.setCidadaoNome(c.getNome());
                        obc.getPk().setGptIncorp(bol.getPk().getGptIncorp());
                        bol.getOmBoletimCidadao().add(obc);
                        this.omBoletimDao.save(bol);
                    }
                }
            }
        }
    }

    /** Averba��o de eventos na vida militar do cidad�o incorporado.
     * @param lista lista de eventos para averba��o
     * @param qm lista de QM
     * @param pg lista de postos ou gradua��es
     * @param usr usu�rio respons�vel pela informa��o
     * @throws SermilException erro no processamento das informa��es
     */
    @Transactional
    public void averbarSituacoes(final List<CidEvento> lista, final List<String> qm, final List<String> pg, final Usuario usr) throws SermilException {
        if (lista == null || lista.isEmpty()) {
            throw new SermilException("Lista de eventos est� vazia.");
        }
        for (int i = 0; i < lista.size(); i++) {
            final CidEvento e = lista.get(i);
            if (e != null && e.getPk() != null && e.getPk().getCodigo() != -1) {
                int sitMil = 12; // Incorporado
                switch (e.getPk().getCodigo()) {
                case 10:
                    sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
                    break; // Qualifica��o
                case 11:
                    sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
                    break; // Engajamento
                case 12:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Licenciamento
                case 13:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Anula��o de Incorpora��o/Matr�cula
                case 14:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Desincorpora��o
                case 15:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Exclus�o a bem da Disciplina
                case 16:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Deser��o
                case 17:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Trancamento de Matr�cula
                case 18:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Reforma
                case 19:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Desaparecimento
                case 20:
                    sitMil = TipoSituacaoMilitar.LICENCIADO.ordinal();
                    break; // Extravio
                case 21:
                    sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
                    break; // Reinclus�o
                case 24:
                    sitMil = TipoSituacaoMilitar.INCORPORADO.ordinal();
                    break; // Reabilita��o
                case 25:
                    sitMil = TipoSituacaoMilitar.EXCLUIDO.ordinal();
                    break; // Falecido
                }
                e.setAnotacao("USR: " + usr.getCpf());
                final Cidadao c = this.cidadaoDao.findById(e.getPk().getCidadaoRa());
                c.addCidEvento(e);
                c.setSituacaoMilitar(sitMil);
                c.setDispensa(Byte.valueOf("0"));
                if (!"-1".equals(qm.get(i))) {
                    c.setQm(this.qmDao.findById(qm.get(i)));
                }
                if (!"-1".equals(pg.get(i))) {
                    c.setPostoGraduacao(this.pgDao.findById(pg.get(i)));
                }
                // for�a armada
                byte forca = 3;
                switch (c.getOm().getOmTipo()) {
                case 7:
                    forca = 1;
                    break;
                case 8:
                    forca = 2;
                    break;
                default:
                    forca = 3;
                }
                c.setForcaArmada(forca);
                // informa��es de mobiliza��o
                c.setMobSituacao(Byte.valueOf("1"));
                if (sitMil == 15) {
                    c.setMobDestino(Byte.valueOf("2"));
                } else {
                    c.setMobDestino(Byte.valueOf("1"));
                }
                if (c.getPostoGraduacao() != null && c.getQm() != null) {
                    int cpg = Integer.parseInt(c.getPostoGraduacao().getCodigo());
                    if (cpg < 20) { // Oficial
                        if (c.getQm().getCodigo().startsWith("6") || c.getQm().getCodigo().startsWith("7") || c.getQm().getCodigo().startsWith("8") || c.getQm().getCodigo().startsWith("9")) {
                            c.setClasseReserva(Byte.valueOf("1")); // R1
                        } else if (c.getQm().getCodigo().startsWith("T")) {
                            c.setClasseReserva(Byte.valueOf("2")); // R2
                        }
                    } else { // Pra�a
                        if (cpg == 24 && c.getQm().getCodigo().startsWith("S")) { // Sgt Tmpr
                            c.setClasseReserva(Byte.valueOf("4")); // Pra�a 2 Cat
                        } else if (cpg == 21) { // ST
                            c.setClasseReserva(Byte.valueOf("5")); // Reserva Remunerada
                        } else if (cpg > 21 && (!c.getQm().getCodigo().equals("2000") || !c.getQm().getCodigo().equals("2100"))) {
                            c.setClasseReserva(Byte.valueOf("3")); // Pra�a 1 Cat
                        } else {
                            c.setClasseReserva(Byte.valueOf("4")); // Pra�a 2 Cat
                        }
                    }
                }
                final CidAuditoria aud = new CidAuditoria(c.getRa(), new Date(), "EVENTO " + e.getPk().getCodigo(), usr.getAcessoIp(), usr.getCpf());
                c.addCidAuditoria(aud);
                this.cidadaoDao.save(c);
            }
        }
    }

    /** Implementa um comparator de eventos por data e c�digo.
     * @author AbreuLopes
     */
    private class OrdenaNome implements Comparator<CidEvento> {

        @Override
        public int compare(CidEvento e1, CidEvento e2) {
           //TODO: Arrumar substituto pra este c�digo
           //int status = e1.getCidadao().getNome().compareTo(e2.getCidadao().getNome());
           int status = e1.getPk().compareTo(e2.getPk());
            if (status == 0) { // mesmo nome
                if (e1.getPk().getCodigo() < e2.getPk().getCodigo()) {
                    status = -1;
                } else if (e1.getPk().getCodigo() > e2.getPk().getCodigo()) {
                    status = 1;
                } else { // mesmo c�digo
                    if (e1.getPk().getData().before(e2.getPk().getData())) {
                        status = -1;
                    } else if (e1.getPk().getData().after(e2.getPk().getData())) {
                        status = 1;
                    }
                }
            }
            return status;
        }

    }

}
