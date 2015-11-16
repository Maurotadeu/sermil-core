package br.mil.eb.sermil.core.servicos;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoEvento;

/** Serviço de Mobilização de Pessoal.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.6
 */
@Named("mobilizacaoServico")
public class MobilizacaoServico {

    protected static final Logger logger = LoggerFactory.getLogger(MobilizacaoServico.class);

    @Inject
    private CidadaoServico cidadaoServico;

    @Inject
    private CidadaoDao cidadaoDao;

    @Inject
    private RaServico raServico;

    public MobilizacaoServico() {
        logger.debug("MobilizacaoServico iniciado");
    }

    @Transactional
    public Cidadao cadastrar(final Cidadao cid, final Usuario usr, final String msg) throws SermilException {
        if (cid.getRa() == null) {
            // CSM = 99, JSM = 900
            cid.setRa(this.raServico.gerar(Byte.valueOf("99"), Short.valueOf("900")));
        }
        if(this.cidadaoServico.isCidadaoCadastrado(cid)) {
            throw new CidadaoCadastradoException(cid.getNome(), cid.getMae(), cid.getNascimentoData());
        }
        if (cid.getNascimentoData() == null) {
            throw new SermilException("Data de nascimento é obrigatória.");
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 16);
            if (cid.getNascimentoData().after(cal.getTime())) {
                throw new SermilException("Data de nascimento é muito recente.");
            }
        }
        if (cid.getVinculacaoAno()== null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(cid.getNascimentoData());
            cid.setVinculacaoAno(cal.get(Calendar.YEAR) + 18);
        }
        if (cid.getDispensa() == null) {
            cid.setDispensa(Byte.valueOf("0"));
        }
        if (cid.getPostoGraduacao() != null && cid.getPostoGraduacao().getCodigo() == null) {
            cid.setPostoGraduacao(null);
        }
        if (cid.getOm() != null && cid.getOm().getCodigo() == null) {
            cid.setOm(null);
        }
        if (cid.getSituacaoMilitar() == 1) {
            final Calendar cal = Calendar.getInstance();
            final CidEvento evento = new CidEvento();
            evento.getPk().setCidadaoRa(cid.getRa());
            evento.getPk().setCodigo(TipoEvento.ALISTAMENTO.ordinal());
            evento.getPk().setData(cal.getTime());
            evento.setAnotacao("Cadastro de Mobilização");
            cid.addCidEvento(evento);
        }
        return this.cidadaoServico.salvar(cid, usr, msg);
    }

    public List<Object[]> pesquisar(final Cidadao cidadao, final String dataLic) throws SermilException {
        if (cidadao == null) {
            throw new CriterioException();
        }
        try {
            final EntityManager em = this.cidadaoDao.getEntityManager();
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            final Root<Cidadao> cid = cq.from(Cidadao.class);
            final Join<Cidadao, CidEvento> evt = cid.join("cidEventoCollection");
            cq.multiselect(cid.get("ra"),
                           cid.get("nome"),
                           cid.get("postoGraduacao"),
                           cid.get("qm"),
                           cid.get("cidHabilitacaoCollection").as(List.class),
                           cid.get("om"),
                           cid.get("endereco"), 
                           cid.get("bairro"),
                           cid.get("cep"),
                           cid.get("municipioResidencia"),
                           cid.get("email"),
                           cid.get("telefone"),
                           evt.get("pk").get("data"),
                           cid.get("mobSituacao"),
                           cid.get("mobDestino"));
            Predicate condicao = cb.equal(evt.get("pk").get("codigo"), 12);
            if (cidadao.getMobDestino() != null) {
                condicao = cb.and(condicao,cb.equal(cid.<String>get("mobDestino"), cidadao.getMobDestino()));
            }
            if (cidadao.getMunicipioResidencia().getCodigo() != null) {
                condicao = cb.and(condicao, cb.equal(cid.<String>get("municipioResidencia"), cidadao.getMunicipioResidencia()));
            }
            if (cidadao.getPostoGraduacao().getCodigo() != null) {
                condicao = cb.and(condicao, cb.equal(cid.<String>get("postoGraduacao"), cidadao.getPostoGraduacao()));
            }
            if (cidadao.getQm().getCodigo() != null) {
                condicao = cb.and(condicao, cb.equal(cid.<String>get("qm"), cidadao.getQm()));
            }
            if (!dataLic.equals("")) {
                condicao = cb.and(condicao, cb.equal(cid.<String>get("cidEventoCollection.pk.codigo"), 12));
            }
            cq.where(condicao);
            cq.orderBy(cb.asc(cid.<String>get("nome")));
            final List<Object[]> lista = em.createQuery(cq).getResultList();
            return lista;
        } catch (Exception e) {
            throw new SermilException(e);
        }
    }

}
