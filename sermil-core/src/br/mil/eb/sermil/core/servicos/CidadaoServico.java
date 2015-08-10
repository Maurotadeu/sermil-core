package br.mil.eb.sermil.core.servicos;

import java.text.DecimalFormat;
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
import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.MunicipioDao;
import br.mil.eb.sermil.core.dao.PreAlistamentoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemDocApresException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.utils.EmailSender;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.CidDocumento;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.PreAlistamento;
import br.mil.eb.sermil.modelo.SituacaoMilitar;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.Ra;

/**
 * Gerenciamento de informa��es de Cidad�o.
 * 
 * @author Abreu Lopes, Anselmo
 * @since 3.0
 * @version 5.2.3
 */
@Named("cidadaoServico")
public class CidadaoServico {

   protected static final Logger logger = LoggerFactory.getLogger(CidadaoServico.class);

   @Inject
   private CidadaoDao cidadaoDao;

   @Inject
   private CidAuditoriaDao cidAuditoriaDao;

   @Inject
   private PreAlistamentoDao preAlistamentoDao;

   @Inject
   private RaServico raServico;

   @Inject
   private EmailSender emailSender;

   private static final int DOC_RG = 3;

   @Inject
   private MunicipioDao municipioDao;

   public CidadaoServico() {
      logger.debug("CidadaoServico iniciado");
   }

   public boolean isCidadaoCadastrado(final Cidadao cidadao) {
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
      if (ra == null) {
         throw new SermilException("Informe o RA do cidad�o.");
      }
      final Cidadao cid = this.cidadaoDao.findById(ra);
      if (cid == null) {
         throw new CidadaoNotFoundException();
      }
      return cid;
   }

   public Cidadao recuperar(final String cpf) throws SermilException {
      final List<Cidadao> lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", cpf);
      return lista.isEmpty() ? null : lista.get(0);
   }

   @PreAuthorize("hasAnyRole('adm','dsm','csm','del','jsm','mob','om','smr')")
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

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md','cs','convidado')")
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

   public boolean cidadaoTemEvento(final Cidadao cidadao, final Byte eventoCodigo) {
      final List<CidEvento> eventos = cidadao.getCidEventoCollection();
      if (eventos != null && eventos.size() > 0) {
         for (CidEvento evento : eventos) {
            if (evento.getPk().getCodigo() == eventoCodigo) {
               return true;
            }
         }
      }
      return false;
   }

   // Esse metodo alistar veio do PORTAL
   // TODO: melhorar o m�todo para receber Cidadao ao inv�s de PreAlistamento
   @Transactional
   public Cidadao alistar(final PreAlistamento alistamento, final Date dataAlist, final Long ra, final Byte situacaoMilitar, final Usuario usr, final String anotacoes) throws SermilException {
      final Cidadao cidadao = new Cidadao();
      // Verifica se j� foi alistado anteriormente
      cidadao.setNome(alistamento.getNome());
      cidadao.setMae(alistamento.getMae());
      cidadao.setNascimentoData(alistamento.getNascimentoData());
      if (isCidadaoCadastrado(cidadao)) {
         throw new CidadaoCadastradoException();
      }
      // Gerar novo RA
      if (ra == null) {
         cidadao.setRa(this.raServico.gerar(alistamento.getJsm().getPk().getCsmCodigo(), alistamento.getJsm().getPk().getCodigo()));
      } else {
         cidadao.setRa(new Ra(ra).getValor());
      }
      // Configurar informa��es
      cidadao.setPai(alistamento.getPai());
      cidadao.setDispensa(alistamento.getTipo());
      cidadao.setMunicipioNascimento(alistamento.getMunicipioNascimento());
      cidadao.setPaisNascimento(alistamento.getPaisNascimento());
      cidadao.setEstadoCivil(alistamento.getEstadoCivil());
      cidadao.setSexo(alistamento.getSexo());
      cidadao.setEscolaridade(alistamento.getEscolaridade());
      cidadao.setOcupacao(alistamento.getOcupacao());
      cidadao.setVinculacaoAno(Calendar.getInstance().get(Calendar.YEAR));
      cidadao.setJsm(alistamento.getJsm());
      cidadao.getJsm().setCsm(alistamento.getJsm().getCsm());
      cidadao.setZonaResidencial(alistamento.getZonaResidencial());
      cidadao.setMunicipioResidencia(alistamento.getMunicipioResidencia());
      cidadao.setPaisResidencia(alistamento.getPaisResidencia());
      cidadao.setEndereco(alistamento.getEndereco());
      cidadao.setBairro(alistamento.getBairro());
      cidadao.setCep(alistamento.getCep());
      cidadao.setRg(alistamento.getRgNr() == null ? null : new StringBuilder(alistamento.getRgUf()).append(alistamento.getRgNr()).toString());
      cidadao.setCpf(alistamento.getCpf());
      cidadao.setEmail(alistamento.getEmail());
      cidadao.setTelefone(alistamento.getTelefone());
      cidadao.setSituacaoMilitar(situacaoMilitar);
      cidadao.setDesejaServir(alistamento.getDesejaServir());
      cidadao.setAtualizacaoData(new Date());
      // Documento apresentado
      final CidDocApres cda = new CidDocApres();
      cda.getPk().setCidadaoRa(cidadao.getRa());
      cda.getPk().setNumero(alistamento.getDocApresNr());
      cda.setTipo(alistamento.getDocApresTipo());
      cda.setEmissaoData(alistamento.getDocApresEmissaoData());
      cda.setLivro(alistamento.getDocApresLivro());
      cda.setFolha(alistamento.getDocApresFolha());
      cda.setMunicipioCodigo(alistamento.getDocApresMunicipio() == null ? null : alistamento.getDocApresMunicipio().getCodigo());
      cda.setCartorio(alistamento.getDocApresCartorio());
      cidadao.addCidDocApres(cda);
      // Documento do sistema (FAMCO)
      final CidDocumento cd = new CidDocumento();
      cd.setServico(new StringBuilder("100").append(new DecimalFormat("00").format(alistamento.getJsm().getCsmCodigo())).append(new DecimalFormat("000").format(alistamento.getJsm().getCodigo())).append("888").toString());
      cd.setTarefa(Short.parseShort("0"));
      cd.setDocumento(Byte.parseByte("0"));
      cd.getPk().setCidadaoRa(cidadao.getRa());
      cd.getPk().setData(dataAlist);
      cd.getPk().setTipo(Byte.parseByte("1"));
      cidadao.addCidDocumento(cd);

      // Evento de alistamento
      final CidEvento ce = new CidEvento();
      ce.getPk().setCidadaoRa(cidadao.getRa());
      ce.getPk().setCodigo(CidEvento.ALISTAMENTO);
      ce.getPk().setData(dataAlist);
      ce.setAnotacao("Alistado pela Internet");
      cidadao.addCidEvento(ce);

      // Salvar cidad�o
      this.salvar(cidadao, usr, "ALISTAMENTO " + anotacoes);
      return cidadao;
   }

   /**
    * ALISTAMENTO SERVICO (SERMILWEB).
    * 
    * @throws SermilException
    */
   @Transactional
   public Cidadao alistar(final PreAlistamento alistamento, final String anotacoes) throws SermilException {

      // confere se pre alistamento ja existe
      if (isPreAlistamentoCadastrado(alistamento)) {
         throw new CidadaoCadastradoException();
      }

      // Configura PreAlistamento
      if (alistamento.getDocApresTipo() == DOC_RG) {
         if (StringUtils.isEmpty(alistamento.getRgNr())) {
            alistamento.setRgNr(alistamento.getDocApresNr());
            alistamento.setDocApresMunicipio(this.municipioDao.findById(alistamento.getDocApresMunicipio().getCodigo()));
            alistamento.setRgUf(alistamento.getDocApresMunicipio().getUf().getSigla());
         }
      }
      alistamento.setProtocoloData(new Date());
      alistamento.setTipo(Byte.decode("0"));

      // PreAlistamento - somente para controle
      this.preAlistamentoDao.save(alistamento);

      // Cidadao - alistamento real
      final Cidadao cidadao = this.alistar(alistamento, new Date(), null, SituacaoMilitar.ALISTADO, new Usuario("99999999999"), anotacoes);

      // Enviar email de confirmacao de alistamento online
      this.emailSender.enviarEmailConfirmacaoAlistamentoOnLine(cidadao);

      return cidadao;
   }

   public boolean isPreAlistamentoCadastrado(final PreAlistamento alistamento) {
      boolean status = false;
      List<PreAlistamento> lista = this.preAlistamentoDao.findByNamedQuery("PreAlistamento.listarUnico", alistamento.getNome(), alistamento.getMae(), alistamento.getNascimentoData());
      if (lista != null && lista.size() > 0 && lista.get(0) != null) {
         status = true;
      }
      lista = this.preAlistamentoDao.findByNamedQuery("PreAlistamento.listarPorCpf", alistamento.getCpf());
      if (lista != null && lista.size() > 0 && lista.get(0) != null) {
         status = true;
      }
      lista = null;
      return status;
   }

   public boolean isForaPrazo(final Date data) {
      boolean status = false;
      final Calendar dtNasc = Calendar.getInstance();
      dtNasc.setTime(data);
      final Calendar hoje = Calendar.getInstance();
      final Calendar jul = Calendar.getInstance();
      final Calendar dez = Calendar.getInstance();
      int anoAtual = hoje.get(Calendar.YEAR);
      jul.set(anoAtual, 6, 1); // 1 jul
      dez.set(anoAtual, 11, 31); // 31 dez
      if (dtNasc.get(Calendar.YEAR) < anoAtual - 18) {
         status = true;
      } else if (dtNasc.get(Calendar.YEAR) < anoAtual - 17) {
         if (hoje.getTimeInMillis() >= jul.getTimeInMillis() && hoje.getTimeInMillis() <= dez.getTimeInMillis()) {
            status = true;
         }
      }
      logger.debug("Classe={}, Ano={}, Inicio={}, Fim={}", dtNasc.get(Calendar.YEAR), anoAtual, jul, dez);
      return status;
   }

   public boolean temEvento(Cidadao cidadao, Byte codigo) {
      List<CidEvento> eventos = cidadao.getCidEventoCollection();
      for (CidEvento ev : eventos) {
         if (ev.getPk().getCodigo() == codigo) {
            return true;
         }
      }
      return false;
   }

   public boolean podeImprimirCertSitMilitar(Long ra) throws CidadaoNotFoundException, CidadaoCadastradoException, CidadaoNaoTemEventoException, CidadaoNaoTemDocApresException  {
      // Recuperar cidadao
      Cidadao cid = null;
      try {
         cid = recuperar(ra);
      } catch (SermilException e) {
         throw new CidadaoNotFoundException();
      }
      return this.podeImprimirCertSitMilitar(cid);
   }
   
   public boolean podeImprimirCertSitMilitar(Cidadao cid) throws CidadaoCadastradoException, CidadaoNaoTemEventoException, CidadaoNaoTemDocApresException{
      /**
       * REGRAS DE NEGOCIO
       */
      // Situacao Militar = licenciado
      if (cid.getSituacaoMilitar() != Cidadao.SITUACAO_MILITAR_LICENCIADO)
         throw new CidadaoCadastradoException();
      // Tem que ter evento licenciamento
      if (!temEvento(cid, CidEvento.LICENCIAMENTO))
         throw new CidadaoNaoTemEventoException();
      // Pelo menos um documento apresentado.
      if (cid.getCidDocApresColletion().size() <= 0)
         throw new CidadaoNaoTemDocApresException();
      return true;
   }
}
