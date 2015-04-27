package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.CidCertificadoDao;
import br.mil.eb.sermil.core.dao.CidEventoDao;
import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.CertificateNotFoundException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.EventNotFoundException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.OutOfSituationException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.CidQualidadeReserva;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/**
 * Serviço de Cidadão. (Tabelas CIDADAO e CID_AUDITORIA)
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: CidadaoServico.java 2426 2014-05-14 15:01:41Z wlopes $
 */
@Named("cidadaoServico")
public class CidadaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(CidadaoServico.class);

  @Inject
  private CidadaoDao cidadaoDao;

  @Inject
  private CidAuditoriaDao cidAuditoriaDao;

  @Inject
  private CidCertificadoDao cidCertificadoDao;

  @Inject
  private CidEventoDao cidEventoDao;

  public CidadaoServico() {
    logger.debug("CidadaoServico iniciado");
  }

  public boolean isCidadaoCadastrado(final Cidadao cidadao)  {
    boolean status = false;
    if (!this.cidadaoDao.findByNamedQuery("Cidadao.listarUnico", cidadao.getNome(), cidadao.getMae(), cidadao.getNascimentoData()).isEmpty()) {
      status = true;
    }
    if (!StringUtils.isEmpty(cidadao.getCpf()) && !this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", cidadao.getCpf()).isEmpty()) {
      status = true;
    }
    return status;
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public void excluir(final Cidadao cid, final Usuario usr, final String msg) throws SermilException {
    final Cidadao c = this.cidadaoDao.findById(cid.getRa());
    this.cidadaoDao.delete(c);
    this.cidAuditoriaDao.save(new CidAuditoria(cid.getRa(), new Date(), msg.substring(0, msg.length()), usr.getAcessoIp(), usr.getCpf()));
  }

  public Cidadao recuperar(final Long ra) throws SermilException {
    return this.cidadaoDao.findById(ra);
  }

  public Cidadao recuperar(final String cpf) throws SermilException {
    List<Cidadao> lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", cpf);
    return lista.isEmpty() ? null : lista.get(0);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
  @Transactional
  public Cidadao salvar(final Cidadao cid, final Usuario usr, final String msg) throws SermilException {
    final CidAuditoria aud = new CidAuditoria(cid.getRa(), new Date(), msg.substring(0, msg.length() > 500 ? 500 : msg.length()), usr.getAcessoIp(), usr.getCpf());
    cid.addCidAuditoria(aud);
    return this.cidadaoDao.save(cid);
  }

  public Map<String, String> listarAtributos() throws SermilException {
    final List<Object[]> cols = this.cidadaoDao.findBySQL("SELECT COLUMN_NAME, SUBSTR(COMMENTS,1,40) FROM all_col_comments WHERE table_name = 'CIDADAO' ORDER BY 2");
    final Map<String, String> res = new TreeMap<String, String>();
    for (Object[] item : cols) {
      res.put((String) item[0], (String) item[1]);
    }
    return res;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','om','mob')")
  public List<CidAuditoria> listarAuditoria(final Long ra) throws SermilException {
    final List<CidAuditoria> lista = this.cidAuditoriaDao.findByNamedQuery("CidAuditoria.listarPorRa", ra);
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md','convidado')")
  public List<Object[]> listar(final Cidadao cidadao) throws SermilException {
    if (cidadao == null) {
      throw new CriterioException("Informe ao menos um critério de pesquisa de cidadão.");
    }
    try {
      final CriteriaBuilder builder = this.cidadaoDao.getEntityManager().getCriteriaBuilder();
      final CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
      final Root<Cidadao> root = query.from(Cidadao.class);
      query.multiselect(root.<String> get("ra"), root.<String> get("nome"), root.<String> get("mae"), root.<String> get("nascimentoData"));
      Predicate condicao = null;
      if (cidadao.getRa() != null) {
        condicao = builder.equal(root.<String> get("ra"), cidadao.getRa());
      } else if (cidadao.getCpf() != null) {
        condicao = builder.equal(root.<String> get("cpf"), cidadao.getCpf());
      } else if (cidadao.getIdtMilitar() != null) {
        condicao = builder.equal(root.<String> get("idtMilitar"), cidadao.getIdtMilitar());
      } else {
        if (cidadao.getNome() != null) {
          condicao = builder.like(root.<String> get("nome"), cidadao.getNome().concat("%"));
        }
        if (cidadao.getMae() != null) {
          if (condicao == null) {
            condicao = builder.like(root.<String> get("mae"), cidadao.getMae().concat("%"));
          } else {
            condicao = builder.and(condicao, builder.like(root.<String> get("mae"), cidadao.getMae().concat("%")));
          }
        }
        if (cidadao.getNascimentoData() != null) {
          if (condicao == null) {
            condicao = builder.equal(root.get("nascimentoData"), cidadao.getNascimentoData());
          } else {
            condicao = builder.and(condicao, builder.equal(root.get("nascimentoData"), cidadao.getNascimentoData()));
          }
        }
      }
      query.where(condicao);
      return this.cidadaoDao.getEntityManager().createQuery(query).getResultList();
    } catch (Exception e) {
      throw new CriterioException();
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
  @Transactional
  public Cidadao adicionarApresentacao(final Cidadao cidadao, final CidExar apresentacao, final Usuario usr, String msg) throws SermilException {
    if (cidadao.getSituacaoMilitar() != 15) {
      throw new SermilException("ERRO: Para cadastrar uma apresentação o cidadão deve estar na situação LICENCIADO.");
    }
    final List<CidExar> lista = new ArrayList<CidExar>(apresentacao.getPk().getApresentacaoQtd());
    for (int i = 1; i <= apresentacao.getPk().getApresentacaoQtd(); i++) {
      final CidExar apr = new CidExar();
      apr.getPk().setApresentacaoQtd((byte) i);
      apr.getPk().setCidadaoRa(apresentacao.getPk().getCidadaoRa());
      apr.setApresentacaoData(apresentacao.getApresentacaoData());
      apr.setApresentacaoForma(apresentacao.getApresentacaoTipo());
      apr.setApresentacaoTipo(apresentacao.getApresentacaoTipo());
      apr.setMunicipio(apresentacao.getMunicipio());
      apr.setOm(apresentacao.getOm());
      apr.setPais(apresentacao.getPais());
      apr.setIp(apresentacao.getIp());
      lista.add(apr);
    }
    if (cidadao.getCidExarCollection().size() > 0) {
      for (int i = 0; i < cidadao.getCidExarCollection().size(); i++) {
        lista.remove(cidadao.getCidExarCollection().get(i));
      }
    }
    for (CidExar ce : lista) {
      cidadao.addCidExar(ce);
    }
    return this.salvar(cidadao, usr, msg);
  }

  public void pesquisaQualidadeReserva(final Cidadao cidadao) throws SermilException {
    final Calendar cal = Calendar.getInstance();
    final CidQualidadeReserva cqr = new CidQualidadeReserva(cidadao.getRa(), cal.get(Calendar.YEAR));
    cqr.setAreaAtividade((byte) 1);
    cqr.setEmpregoNivel((byte) 1);
    cqr.setEmpregoTipo((byte) 1);
    cqr.setEscolaridade(cidadao.getEscolaridade());
    cqr.setMissaoPaz("2");
    cqr.setRenda((byte) 1);
    cidadao.addCidQualidadeReserva(cqr);
  }

  
     /**
    * 
    * O cidadao tem que ter Pelo menos um evento do tipo 3,6,13,14 ou 24 Pelo um certificado do tipo
    * 3,4 ou 6 E ele tem que estar em uma das situacoes militares: 3,8, ou 9
    * 
    * @param cidadao
    * @throws EventNotFoundException
    * @throws CertificateNotFoundException
    * @throws OutOfSituationException
    * @throws CidadaoNaoTemEventoException
    */
   public void cidadaoPodeImprimirCdi(Cidadao cidadao) throws EventNotFoundException, CertificateNotFoundException, OutOfSituationException {
      if (!cidadaoTemEvento(cidadao, CidEvento.EXCESSO_DE_CONTINGENTE_CODIGO)) {
         throw new EventNotFoundException();
      }
      if (!cidadaoTemPeloMenosUmCertificado(cidadao, new Byte[] { CidCertificado.DISPENSA_DE_INCORPORACAO_COMPUTADOR, CidCertificado.DISPENSA_DE_INCORPORACAO_INFOR, CidCertificado.DISPENSA_DE_INCORPORACAO_PLANO })) {
         throw new CertificateNotFoundException();
      }
      if (!StringUtils.containsAny(cidadao.getSituacaoMilitar().toString(), "389")) {
         throw new OutOfSituationException();
      }
   }

 
   public boolean cidadaoTemCertificado(Cidadao cidadao, Byte tipo) {
      List<CidCertificado> certificados = cidadao.getCidCertificadoCollection();
      if (certificados != null && certificados.size() > 0) {
         for (CidCertificado certificado : certificados) {
            if (certificado.getPk().getTipo() == tipo) {
               return true;
            }
         }
      }
      return false;
   }

   public boolean cidadaoTemPeloMenosUmCertificado(Cidadao cidadao, Byte[] tipos) {
      for (Byte tipo : tipos) {
         if (cidadaoTemCertificado(cidadao, tipo)) {
            return true;
         }
      }
      return false;
   }

   /**
    * cidadaoJaTemCdi.
    * 
    * @param cidadao
    * @return Boolean
    * @author Anselmo Ribeiro
    */
   public Boolean cidadaoJaTemCdi(Cidadao cidadao) {
      List<CidCertificado> certificados = cidCertificadoDao.findByNamedQuery("Certificado.cidadaoTemCdi", cidadao.getRa());
      if (certificados.isEmpty()) {
         return false;
      }
      return true;
   }


   public Boolean cidadaoJaFezEntrevista(Cidadao cidadao) {
      // TODO: implementar assim que a tabela entrevista for criada
      return false;
   }

   public boolean cidadaoTemEvento(Cidadao cidadao, Byte eventoCodigo) {
      List<CidEvento> eventos = cidadao.getCidEventoCollection();
      if (eventos != null && eventos.size() > 0) {
         for (CidEvento evento : eventos) {
            if (evento.getPk().getCodigo() == eventoCodigo) {
               return true;
            }
         }
      }
      return false;
   }

}
