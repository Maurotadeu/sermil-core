package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.Ra;

/** Serviço de Mobilização de Pessoal.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: MobilizacaoServico.java 2466 2014-06-12 14:17:33Z wlopes $
 */
@Named("mobilizacaoServico")
public class MobilizacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(MobilizacaoServico.class);

  @Inject
  private CidadaoDao cidadaoDao;

  @Inject
  private RaServico raServico;

  public MobilizacaoServico() {
    logger.debug("MobilizacaoServico iniciado");
  }

  @Transactional
  public Cidadao cadastrar(final Cidadao cid, final Usuario usr, final String msg) throws SermilException {
    // CSM = 99, JSM = 900
    if (cid.getRa() == null) {
      cid.setRa(this.raServico.gerar(Byte.valueOf("99"), Short.valueOf("900")));
    } else {
      new Ra(cid.getRa());
      if (this.cidadaoDao.findById(cid.getRa()) != null) {
        throw new SermilException("RA já está cadastrado.");
      }
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
    if(this.isCidadaoCadastrado(cid)) {
      throw new CidadaoCadastradoException();
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
      evento.getPk().setCodigo(Byte.parseByte("1"));
      evento.getPk().setData(cal.getTime());
      evento.setAnotacao("Cadastro de Mobilização");
      cid.addCidEvento(evento);
    }
    final CidAuditoria aud = new CidAuditoria(cid.getRa(), new Date(), msg.substring(0, msg.length() > 500 ? 500 : msg.length()), usr.getAcessoIp(), usr.getCpf());
    if (cid.getCidAuditoriaCollection() == null) {
      cid.setCidAuditoriaCollection(new ArrayList<CidAuditoria>(1));
    }
    cid.getCidAuditoriaCollection().add(aud);
    aud.setCidadao(cid);
    return this.cidadaoDao.save(cid);
  }

  public List<Object[]> pesquisar(final Cidadao cidadao, final String dataLic) throws SermilException {
	if (cidadao == null) {
      throw new CriterioException();
    }
    try {
    /*
      final CriteriaBuilder builder = this.cidadaoDao.getEntityManager().getCriteriaBuilder();
      final CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
      final Root<Cidadao> from = query.from(Cidadao.class);
      // final Join<Cidadao, CidEvento> cidEvento =from.joinList("cidEventoCollection");
      query.multiselect(from.<String>get("ra"),
                        from.<String>get("postoGraduacao"),
                        from.<String>get("nome"),
                        from.<String>get("qm"),
                        from.<String>get("nascimentoData"),
                        from.<String>get("om"),
                        from.<String>get("endereco"),
                        from.<String>get("bairro"),
                        from.<String>get("cep"),
                        from.<String>get("telefone"),
                        from.<String>get("municipioResidencia"),
                        from.<String>get("cidEventoCollection"));
      Predicate condicao = null;
      if (cidadao.getMobDestino() != null) {
        condicao = builder.equal(from.<String>get("mobDestino"), cidadao.getMobDestino());
      }
      if (cidadao.getMunicipioResidencia().getCodigo() != null) {
        condicao = builder.and(condicao, builder.equal(from.<String>get("municipioResidencia"), cidadao.getMunicipioResidencia()));
      }
      if (cidadao.getPostoGraduacao().getCodigo() != null) {
        condicao = builder.and(condicao, builder.equal(from.<String>get("postoGraduacao"), cidadao.getPostoGraduacao()));
      }
      if (cidadao.getQm().getCodigo() != null) {
        condicao = builder.and(condicao, builder.equal(from.<String>get("qm"), cidadao.getQm()));
      }
      if (!dataLic.equals("")) {
        condicao = builder.and(condicao, builder.equal(from.<String>get("cidEventoCollection.pk.codigo"), 12));
      }
      query.where(condicao);
      query.orderBy(builder.asc(from.<String>get("nome")));
      return cidadaoDao.getEntityManager().createQuery(query).getResultList();*/

      String sql = "SELECT C.RA,P.SIGLA,C.NOME,C.QM_CODIGO,C.NASCIMENTO_DATA,O.SIGLA,C.ENDERECO,C.BAIRRO,"
    	    	+"C.CEP,C.TELEFONE,M.DESCRICAO,E.APRESENTACAO_QTD,E.APRESENTACAO_DATA FROM CIDADAO C,"
				+"(SELECT MAX(APRESENTACAO_DATA)APRESENTACAO_DATA,MAX(APRESENTACAO_QTD)APRESENTACAO_QTD,"
				+ " CIDADAO_RA FROM CID_EXAR GROUP BY CIDADAO_RA) E,"
				+" MUNICIPIO M,"
				+" OM O, "
				+" POSTO_GRADUACAO P ";

    	    if (!dataLic.equals("")) {
    	    	sql +=",CID_EVENTO EV WHERE C.RA=E.CIDADAO_RA(+) "
    	    		+" AND C.RA=EV.CIDADAO_RA"
    	    		+" AND E.CIDADAO_RA=EV.CIDADAO_RA"
    	    		+" AND M.CODIGO=C.MUNICIPIO_RESIDENCIA_CODIGO"
    	    		+" AND O.CODIGO=C.OM_CODIGO"
    	    		+" AND P.CODIGO=C.POSTO_GRADUACAO_CODIGO"
    	    		+" AND EV.CODIGO=12"
    	    		+" AND EXTRACT(YEAR FROM DATA)='"+dataLic+"'"
    	    		+" AND C.mob_destino= '" +cidadao.getMobDestino()+"'";
  	      }
    	    if (cidadao.getMobDestino() != null && dataLic.equals("") ) {
    	    		sql +=" WHERE C.RA=E.CIDADAO_RA(+)  "
    	    			+" AND M.CODIGO=C.MUNICIPIO_RESIDENCIA_CODIGO"
    	    	    	+" AND O.CODIGO=C.OM_CODIGO"
    	    	    	+" AND P.CODIGO=C.POSTO_GRADUACAO_CODIGO"
    	    			+" AND C.mob_destino= '" +cidadao.getMobDestino()+"'";
    	    }
    	      if (cidadao.getMunicipioResidencia().getCodigo() != null) {
    	    	  sql +=" AND C.MUNICIPIO_RESIDENCIA_CODIGO= '" +cidadao.getMunicipioResidencia().getCodigo()+"'";
    	      }
    	      if (cidadao.getPostoGraduacao().getCodigo() != null) {
    	    	  sql +=" AND C.POSTO_GRADUACAO_CODIGO= '" +cidadao.getPostoGraduacao().getCodigo() +"'";
    	      }
    	      if (cidadao.getQm().getCodigo() != null) {
    	    	  sql +=" AND C.QM_CODIGO= '" +cidadao.getQm().getCodigo()+"'";
    	      }
      return cidadaoDao.findBySQL(sql);
    } catch (Exception e) {
      throw new SermilException(e);
    }
  }

  private boolean isCidadaoCadastrado(final Cidadao cid) throws SermilException {
    if (cid.getNome() == null || cid.getNome().isEmpty() || cid.getMae() == null || cid.getMae().isEmpty() || cid.getNascimentoData() == null) {
      throw new CriterioException("Informe o nome, nome da mãe e data de nascimento do cidadão.");
    }
    final List<Cidadao> lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarUnico", cid.getNome(), cid.getMae(), cid.getNascimentoData());
    return lista.isEmpty() ? false : true;
  }
  
}
