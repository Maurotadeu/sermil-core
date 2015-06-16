package br.mil.eb.sermil.core.servicos;

import static br.mil.eb.sermil.core.Constantes.SUPORTE_CONTA_EMAIL;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.CidCertificadoDao;
import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.MunicipioDao;
import br.mil.eb.sermil.core.dao.PreAlistamentoDao;
import br.mil.eb.sermil.core.exceptions.AuditoriaJaExisteException;
import br.mil.eb.sermil.core.exceptions.CertificateNotFoundException;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.DocumentoJaExisteException;
import br.mil.eb.sermil.core.exceptions.EventNotFoundException;
import br.mil.eb.sermil.core.exceptions.EventoJaExisteException;
import br.mil.eb.sermil.core.exceptions.GeracaoDeRAException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.OutOfSituationException;
import br.mil.eb.sermil.core.exceptions.PreAlistamentoErroException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.CidDocumento;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.CidQualidadeReserva;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.PreAlistamento;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.Ra;

/**
 * Serviço de Cidadão. (Tabelas CIDADAO e CID_AUDITORIA)
 * 
 * @author Abreu Lopes, Anselmo
 * @since 3.0
 * @version $Id$
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
   private PreAlistamentoDao preAlistamentoDao;

   @Inject
   private RaServico raServico;

   private static final int DOC_RG = 3;

   @Inject
   private MunicipioDao municipioDao;

   @Inject
   private JavaMailSender mailSender;

   @Inject
   private VelocityEngine velocityEngine;

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
      return this.cidadaoDao.findById(ra);
   }

   public Cidadao recuperar(final String cpf) throws SermilException {
      List<Cidadao> lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", cpf);
      return lista.isEmpty() ? null : lista.get(0);
   }

   public Cidadao retrieve(final Long ra) throws IllegalArgumentException, CidadaoNotFoundException, SermilException {
      // Confere RA
      if (ra == null)
         throw new IllegalArgumentException();
      // Confere Se cidadao eh valido
      try {
         Cidadao cid = recuperar(ra);
         return cid;
      } catch (SermilException e) {
         logger.error(e.getMessage());
         throw new CidadaoNotFoundException();
      } catch (Exception e) {
         logger.error(e.getMessage());
         throw new SermilException(e.getMessage());
      }
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
         throw new CriterioException("Informe ao menos um critÃ©rio de pesquisa de cidadÃ£o.");
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
         throw new SermilException("ERRO: Para cadastrar uma apresentaÃ§Ã£o o cidadÃ£o deve estar na situaÃ§Ã£o LICENCIADO.");
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
    * O cidadao tem que ter Pelo menos um evento do tipo 3,6,13,14 ou 24 Pelo um certificado do tipo
    * 3,4 ou 6 E ele tem que estar em uma das situacoes militares: 3,8, ou 9
    * 
    * @param cidadao
    * @throws EventNotFoundException
    * @throws CertificateNotFoundException
    * @throws OutOfSituationException
    * @throws CidadaoNaoTemEventoException
    */

   public boolean cidadaoPodeImprimirCdi(Cidadao cidadao) throws EventNotFoundException, CertificateNotFoundException, OutOfSituationException {
      if (!cidadaoTemEvento(cidadao, CidEvento.EXCESSO_DE_CONTINGENTE_CODIGO)) {
         throw new EventNotFoundException();
      }
      if (!cidadaoTemPeloMenosUmCertificado(cidadao, new Byte[] { CidCertificado.DISPENSA_DE_INCORPORACAO_COMPUTADOR, CidCertificado.DISPENSA_DE_INCORPORACAO_INFOR, CidCertificado.DISPENSA_DE_INCORPORACAO_PLANO })) {
         throw new CertificateNotFoundException();
      }
      if (!StringUtils.containsAny(cidadao.getSituacaoMilitar().toString(), "389")) {
         throw new OutOfSituationException();
      }
      return true;
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

   public Boolean cidadaoJaTemCdi(Cidadao cidadao) {
      List<CidCertificado> certificados = cidCertificadoDao.findByNamedQuery("Certificado.cidadaoTemCdi", cidadao.getRa());
      if (certificados.isEmpty()) {
         return false;
      }
      return true;
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

   public Boolean cidadaoJaFezEntrevista(Cidadao cidadao) {
      // TODO: implementar assim que a tabela entrevista for criada
      return false;
   }

   public CidCertificado getCDIDeCidadao(Cidadao cidadao) {
      List<CidCertificado> certificados = cidadao.getCidCertificadoCollection();
      for (CidCertificado certificado : certificados) {
         if (certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_COMPUTADOR || certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_INFOR
               || certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_PLANO) {
            return certificado;
         }
      }
      return null;
   }

   public boolean cidadaoPodeImprimirCAM(Cidadao cidadao) {
      if (cidadao == null)
         return false;
      return (cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_ALISTADO || cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_REFRATARIO);
   }

   public boolean isCidadaoLicenciado(Cidadao cidadao) {
      return (cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_LICENCIADO) ? true : false;
   }

   @Transactional
   public void addCertificateAndSaveCidadao(CidCertificado certificado, Cidadao cidadao, Usuario usuario) throws SermilException {
      cidadao.addCidCertificado(certificado);
      salvar(cidadao, usuario, "CERTIFICADO: " + certificado);
   }

   /**
    * ALISTAMENTO SERVICO (PORTAL)
    */

   @Transactional
   public void excluirPreAlistamento(final PreAlistamento cadastro) throws PreAlistamentoErroException {
      try {
         this.preAlistamentoDao.delete(cadastro);
      } catch (Exception e) {
         logger.error(e.getMessage());
         throw new PreAlistamentoErroException();
      }
   }

   public List<PreAlistamento> pesquisarPreAlistamento(final PreAlistamento cadastro) throws CriterioException, NoDataFoundException {
      if (cadastro == null || (cadastro.getCodigo() == null && cadastro.getNome() == null)) {
         throw new CriterioException();
      }
      List<PreAlistamento> lista = null;
      if (cadastro.getCodigo() != null) {
         lista = new ArrayList<PreAlistamento>(1);
         lista.add(this.preAlistamentoDao.findById(cadastro.getCodigo()));
      } else if (cadastro.getNome() != null && !cadastro.getNome().isEmpty()) {
         lista = this.preAlistamentoDao.findByNamedQuery("PreAlistamento.listarPorNome", cadastro.getNome().toUpperCase().trim().concat("%"));
      } else {
         throw new CriterioException();
      }
      if (lista == null || lista.isEmpty() || lista.get(0) == null) {
         lista = null;
         throw new NoDataFoundException();
      }
      return lista;
   }

   // Esse metodo alistar veio do PORTAL
   @Transactional
   public Cidadao alistar(final PreAlistamento alistamento, final Date dataAlist, final Long ra, final Byte situacaoMilitar, final Usuario usr) throws SermilException, DocumentoJaExisteException, EventoJaExisteException {
      final Cidadao cidadao = new Cidadao();
      // Verifica se já foi alistado anteriormente
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
      // Configurar informações
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
      ce.getPk().setCodigo(Byte.parseByte("1"));
      ce.getPk().setData(dataAlist);
      ce.setAnotacao("Alistado pela Internet");
      cidadao.addCidEvento(ce);
      // Salvar cidadão
      this.salvar(cidadao, usr, "ALISTAMENTO");
      return cidadao;
   }

   /**
    * ALISTAMENTO SERVICO (SERMILWEB)
    */


   //Este metodo veio do SERMILWEB
   @Transactional
   public Cidadao alistar(final PreAlistamento preAlistamento, final String anotacoes) throws CriterioException, CidadaoCadastradoException, GeracaoDeRAException, DocumentoJaExisteException, EventoJaExisteException, AuditoriaJaExisteException {
      if (StringUtils.isEmpty(preAlistamento.getNome()) || StringUtils.isEmpty(preAlistamento.getMae()) || preAlistamento.getNascimentoData() == null) {
         throw new CriterioException();
      }
      // confere se pre alistamento ja existe
      if (isPreAlistamentoCadastrado(preAlistamento)) {
         throw new CidadaoCadastradoException();
      }

      // confere se cidadao ja existe
      final Cidadao cidadao = new Cidadao();
      cidadao.setNome(preAlistamento.getNome());
      cidadao.setMae(preAlistamento.getMae());
      cidadao.setNascimentoData(preAlistamento.getNascimentoData());
      if (isCidadaoCadastrado(cidadao)) {
         throw new CidadaoCadastradoException();
      }

      // Configura PRE_ALISTAMENTO
      if (preAlistamento.getDocApresTipo() == DOC_RG) {
         if (StringUtils.isEmpty(preAlistamento.getRgNr())) {
            preAlistamento.setRgNr(preAlistamento.getDocApresNr());
            preAlistamento.setDocApresMunicipio(this.municipioDao.findById(preAlistamento.getDocApresMunicipio().getCodigo()));
            preAlistamento.setRgUf(preAlistamento.getDocApresMunicipio().getUf().getSigla());
         }
      }
      preAlistamento.setProtocoloData(new Date());

      // Gerar RA
      try {
         cidadao.setRa(this.raServico.gerar(preAlistamento.getJsm().getPk().getCsmCodigo(), preAlistamento.getJsm().getPk().getCodigo()));
      } catch (SermilException e) {
         throw new GeracaoDeRAException();
      }
      // Configurar informações
      cidadao.setPai(preAlistamento.getPai());
      cidadao.setDispensa(preAlistamento.getTipo());
      cidadao.setMunicipioNascimento(preAlistamento.getMunicipioNascimento());
      cidadao.setPaisNascimento(preAlistamento.getPaisNascimento());
      cidadao.setEstadoCivil(preAlistamento.getEstadoCivil());
      cidadao.setSexo(preAlistamento.getSexo());
      cidadao.setEscolaridade(preAlistamento.getEscolaridade());
      cidadao.setOcupacao(preAlistamento.getOcupacao());
      cidadao.setVinculacaoAno(Calendar.getInstance().get(Calendar.YEAR));
      cidadao.setJsm(preAlistamento.getJsm());
      cidadao.getJsm().setCsm(preAlistamento.getJsm().getCsm());
      cidadao.setZonaResidencial(preAlistamento.getZonaResidencial());
      cidadao.setMunicipioResidencia(preAlistamento.getMunicipioResidencia());
      cidadao.setPaisResidencia(preAlistamento.getPaisResidencia());
      cidadao.setEndereco(preAlistamento.getEndereco());
      cidadao.setBairro(preAlistamento.getBairro());
      cidadao.setCep(preAlistamento.getCep());
      cidadao.setRg(preAlistamento.getRgNr() == null ? null : new StringBuilder(preAlistamento.getRgUf()).append(preAlistamento.getRgNr()).toString());
      cidadao.setEmail(preAlistamento.getEmail());
      cidadao.setTelefone(preAlistamento.getTelefone());
      cidadao.setSituacaoMilitar(Byte.valueOf("1"));
      cidadao.setDesejaServir(preAlistamento.getDesejaServir());
      cidadao.setAtualizacaoData(new Date());
      cidadao.setDispensa(Byte.valueOf("0"));
      cidadao.setCpf(preAlistamento.getCpf());
      cidadao.setAnotacoes(anotacoes);
      // Documento apresentado
      final CidDocApres cda = new CidDocApres();
      cda.getPk().setCidadaoRa(cidadao.getRa());
      cda.getPk().setNumero(preAlistamento.getDocApresNr());
      cda.setTipo(preAlistamento.getDocApresTipo());
      cda.setEmissaoData(preAlistamento.getDocApresEmissaoData());
      cda.setLivro(preAlistamento.getDocApresLivro());
      cda.setFolha(preAlistamento.getDocApresFolha());
      cda.setMunicipioCodigo(preAlistamento.getDocApresMunicipio() == null ? null : preAlistamento.getDocApresMunicipio().getCodigo());
      cda.setCartorio(preAlistamento.getDocApresCartorio());
      try {
         cidadao.addCidDocApres(cda);
      } catch (SermilException e) {
         throw new DocumentoJaExisteException();
      }
      // Documento do sistema (FAMCO)
      final CidDocumento cd = new CidDocumento();
      cd.setServico(new StringBuilder("100").append(new DecimalFormat("00").format(preAlistamento.getJsm().getCsmCodigo())).append(new DecimalFormat("000").format(preAlistamento.getJsm().getCodigo())).append("888").toString());
      cd.setTarefa(Short.parseShort("0"));
      cd.setDocumento(Byte.parseByte("0"));
      cd.getPk().setCidadaoRa(cidadao.getRa());
      cd.getPk().setData(new Date());
      cd.getPk().setTipo(Byte.parseByte("1"));
      try {
         cidadao.addCidDocumento(cd);
      } catch (SermilException e) {
         throw new DocumentoJaExisteException();
      }
      // Evento de alistamento
      final CidEvento ce = new CidEvento();
      ce.getPk().setCidadaoRa(cidadao.getRa());
      ce.getPk().setCodigo(Byte.parseByte("1"));
      ce.getPk().setData(new Date());
      ce.setAnotacao("Alistamento ONLINE");
      try {
         cidadao.addCidEvento(ce);
      } catch (SermilException e) {
         throw new EventoJaExisteException();
      }
      // Salvar Alistamento ONLINE
      final Usuario usr = new Usuario();
      usr.setCpf("99999999999");
      Cidadao cidadaoNovo;
      try {
         cidadaoNovo = salvar(cidadao, usr, "ALISTAMENTO ONLINE");
         this.preAlistamentoDao.save(preAlistamento);
         this.enviarEmail(cidadaoNovo);
      } catch (SermilException e) {
         throw new AuditoriaJaExisteException();
      }
      return cidadaoNovo;
   }

   private boolean isPreAlistamentoCadastrado(final PreAlistamento alistamento) {
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

   private void enviarEmail(final Cidadao cadastro) {
      final Configurador cfg = Configurador.getInstance();
      final MimeMessagePreparator preparator = new MimeMessagePreparator() {
         public void prepare(MimeMessage mimeMessage) throws Exception {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(cadastro.getEmail());
            message.setFrom(cfg.getConfiguracao(SUPORTE_CONTA_EMAIL));
            message.setSubject("Alistamento ONLINE do Serviço Militar");
            final Map<String, Object> model = new HashMap<String, Object>(5);
            model.put("ra", cadastro.getRa());
            model.put("nome", cadastro.getNome());
            model.put("pai", cadastro.getPai());
            model.put("mae", cadastro.getMae());
            model.put("dtnasc", DateFormat.getDateInstance(DateFormat.MEDIUM).format(cadastro.getNascimentoData()));
            model.put("jsm", cadastro.getJsm());
            model.put("endereco", cadastro.getJsm().getJsmInfo().getEndereco());
            model.put("bairro", cadastro.getJsm().getJsmInfo().getBairro());
            model.put("telefone", cadastro.getJsm().getJsmInfo().getTelefone());
            model.put("municipio", cadastro.getJsm().getMunicipio());
            model.put("data", DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()));
            final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailCadastro.vm", "utf-8", model);
            message.setText(text, true);
         }
      };
      try {
         this.mailSender.send(preparator);
      } catch (Exception e) {
         logger.warn(e.getMessage());
      }
   }

   public boolean isForaPrazo(Date data) {
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
   
   /**
    * Metodos incluidos depois que alistamentoServico (do portal e do sermilweb) foram trazidos pra ca.
    */
   
   

}
