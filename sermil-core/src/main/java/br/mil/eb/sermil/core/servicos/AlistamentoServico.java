package br.mil.eb.sermil.core.servicos;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.LockModeType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.MunicipioDao;
import br.mil.eb.sermil.core.dao.PreAlistamentoDao;
import br.mil.eb.sermil.core.dao.RaMestreDao;
import br.mil.eb.sermil.core.exceptions.CPFDuplicadoException;
import br.mil.eb.sermil.core.exceptions.CidadaoCadastradoException;
import br.mil.eb.sermil.core.exceptions.RaMestreException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.CidDocumento;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.PreAlistamento;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.tipos.Ra;
import br.mil.eb.sermil.tipos.TipoDocApres;
import br.mil.eb.sermil.tipos.TipoEvento;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Alistamento de Cidadão.
 * @author Abreu Lopes
 * @since 5.2.7
 * @version 5.2.7
 */
@Named("alistamentoServico")
public class AlistamentoServico {

   protected static final Logger logger = LoggerFactory.getLogger(AlistamentoServico.class);

   @Inject
   private CidadaoDao cidadaoDao;

   @Inject
   private PreAlistamentoDao preAlistamentoDao;

   @Inject
   private MunicipioDao municipioDao;

   @Inject
   private RaMestreDao raMestreDao;

   private static final String SQL_ANO = "select m.uf_sigla, count(*) from pre_alistamento p join municipio m on p.municipio_residencia_codigo = m.codigo where extract(year from protocolo_data) = ?1 group by m.uf_sigla order by 1";

   private static final String SQL_UF = "select m.descricao, count(*) from pre_alistamento p join municipio m on p.municipio_residencia_codigo = m.codigo where extract(year from protocolo_data) = ?1 and m.uf_sigla = ?2 group by m.descricao order by 1";

   public AlistamentoServico() {
      logger.debug("AlistamentoServico iniciado");
   }

   public List<Object[]> listarAno(final Integer ano) throws SermilException {
      return this.cidadaoDao.findBySQL(SQL_ANO, ano);
   }

   public List<Object[]> listarUf(final Integer ano, final String uf) throws SermilException {
      return this.cidadaoDao.findBySQL(SQL_UF, ano, uf);
   }

   @Transactional
   public Cidadao alistar(final PreAlistamento alistamento) throws SermilException {

      final Date dataAtual = new Date();

      // Informações do Alistamento
      final Cidadao cidadao = new Cidadao();
      cidadao.setJsm(alistamento.getJsm());
      cidadao.setNome(alistamento.getNome());
      cidadao.setMae(alistamento.getMae());
      cidadao.setNascimentoData(alistamento.getNascimentoData());
      cidadao.setCpf(alistamento.getCpf());
      cidadao.setPai(alistamento.getPai());
      cidadao.setDispensa(alistamento.getTipo());
      cidadao.setMunicipioNascimento(alistamento.getMunicipioNascimento());
      cidadao.setPaisNascimento(alistamento.getPaisNascimento());
      cidadao.setEstadoCivil(alistamento.getEstadoCivil());
      cidadao.setSexo(alistamento.getSexo());
      cidadao.setEscolaridade(alistamento.getEscolaridade());
      cidadao.setOcupacao(alistamento.getOcupacao());
      cidadao.setVinculacaoAno(Calendar.getInstance().get(Calendar.YEAR));
      cidadao.setZonaResidencial(alistamento.getZonaResidencial());
      cidadao.setMunicipioResidencia(alistamento.getMunicipioResidencia());
      cidadao.setPaisResidencia(alistamento.getPaisResidencia());
      cidadao.setEndereco(alistamento.getEndereco());
      cidadao.setBairro(alistamento.getBairro());
      cidadao.setCep(alistamento.getCep());
      cidadao.setRg(alistamento.getRgNr() == null ? null : new StringBuilder(alistamento.getRgUf()).append(alistamento.getRgNr()).toString());
      cidadao.setEmail(alistamento.getEmail());
      cidadao.setTelefone(alistamento.getTelefone());
      cidadao.setDesejaServir(alistamento.getDesejaServir());
      cidadao.setAtualizacaoData(dataAtual);
      cidadao.setSituacaoMilitar(TipoSituacaoMilitar.ALISTADO.ordinal());

      // Verificar se CPF está disponível
      if (!StringUtils.isEmpty(alistamento.getCpf()) && !this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCpf", alistamento.getCpf()).isEmpty()) {
         logger.debug("CPF ja cadastrado: CPF={}", alistamento.getCpf());
         throw new CPFDuplicadoException(alistamento.getCpf());
      }

      // Verifica se já foi alistado anteriormente
      if (this.isCadastrado(alistamento)) {
         logger.debug("Cidadao ja Alistado: nome={}, mae={}, dt nasc={}", alistamento.getNome(), alistamento.getMae(), alistamento.getNascimentoData());
         throw new CidadaoCadastradoException(alistamento.getNome(), alistamento.getMae(), alistamento.getNascimentoData());
      }
      
      // Verifica os limites de idade
      if (cidadao.isForaLimiteIdade()) {
         throw new SermilException("Alistamento permitido somente dos 17 aos 45 anos. Procure a JSM se for o caso.");
      }

      // Configura PreAlistamento
      if (alistamento.getDocApresMunicipio().getCodigo() == -1) {
         alistamento.setDocApresMunicipio(null);
      }
      if (alistamento.getDocApresTipo() == TipoDocApres.RG.ordinal()) {
         if (StringUtils.isEmpty(alistamento.getRgNr())) {
            alistamento.setRgNr(alistamento.getDocApresNr());
            if (alistamento.getDocApresMunicipio() != null && alistamento.getDocApresMunicipio().getCodigo() != 99999) {
               alistamento.setDocApresMunicipio(this.municipioDao.findById(alistamento.getDocApresMunicipio().getCodigo()));
               alistamento.setRgUf(alistamento.getDocApresMunicipio().getUf().getSigla());
            }
         }
      }
      alistamento.setProtocoloData(dataAtual);
      alistamento.setTipo(Byte.decode("0"));

      // Gerando novo RA
      final RaMestre raMestre = this.raMestreDao.findById(new RaMestre.PK(cidadao.getJsm().getPk().getCsmCodigo(), cidadao.getJsm().getPk().getCodigo()));
      if (raMestre == null) {
         logger.error("JSM não existe na tabela RA_MESTRE ({})", cidadao.getJsm());
         throw new RaMestreException("CSM/JSM não existe na tabela de controle de RA (RA_MESTRE)");
      }
      this.preAlistamentoDao.getEntityManager().lock(raMestre, LockModeType.PESSIMISTIC_WRITE);
      final Integer nrSequencial = raMestre.getSequencial() + 1;
      cidadao.setRa(new Ra.Builder().csm(raMestre.getPk().getCsmCodigo()).jsm(raMestre.getPk().getJsmCodigo()).sequencial(nrSequencial).build().getValor());
      raMestre.setSequencial(nrSequencial);
      logger.debug("RA gerado: {} (JSM={} - MESTRE SEQUENCIAL: {})", cidadao.getRa(), cidadao.getJsm(), raMestre.getSequencial());

      // Documento apresentado
      final CidDocApres cda = new CidDocApres(cidadao.getRa(), alistamento.getDocApresNr());
      cda.setTipo(alistamento.getDocApresTipo());
      cda.setEmissaoData(alistamento.getDocApresEmissaoData());
      cda.setLivro(alistamento.getDocApresLivro());
      cda.setFolha(alistamento.getDocApresFolha());
      cda.setMunicipioCodigo(alistamento.getDocApresMunicipio() == null ? null : alistamento.getDocApresMunicipio().getCodigo());
      cda.setCartorio(alistamento.getDocApresCartorio());
      cidadao.addCidDocApres(cda);

      // Documento do sistema (FAMCO)
      final CidDocumento cd = new CidDocumento(cidadao.getRa(), dataAtual, Byte.parseByte("1"));
      cd.setServico(new StringBuilder("100").append(new DecimalFormat("00").format(alistamento.getJsm().getCsmCodigo())).append(new DecimalFormat("000").format(alistamento.getJsm().getCodigo())).append("888").toString());
      cd.setTarefa(Short.parseShort("0"));
      cd.setDocumento(Byte.parseByte("0"));
      cidadao.addCidDocumento(cd);

      // Evento de alistamento
      final CidEvento ce = new CidEvento(cidadao.getRa(), TipoEvento.ALISTAMENTO.ordinal(), dataAtual);
      ce.setAnotacao("Alistado pela Internet");
      cidadao.addCidEvento(ce);

      // Salvar cidadão
      this.cidadaoDao.save(cidadao);
      this.preAlistamentoDao.save(alistamento);
      this.raMestreDao.save(raMestre);
      this.preAlistamentoDao.getEntityManager().flush();
      return cidadao;
   }

   public boolean isCadastrado(final PreAlistamento alistamento) {
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
      if (!this.cidadaoDao.findByNamedQuery("Cidadao.listarUnico", alistamento.getNome(), alistamento.getMae(), alistamento.getNascimentoData()).isEmpty()) {
         status = true;
      }
      return status;
   }

/*   
   public boolean isForaPrazo(final Date dataNasc) {
      boolean status = false;
      final Calendar dtNasc = Calendar.getInstance();
      dtNasc.setTime(dataNasc);
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

   public boolean isForaLimiteIdade(final Date dataNasc) {
      final Calendar limInf = Calendar.getInstance();
      limInf.add(Calendar.YEAR, -17);
      final Calendar limSup = Calendar.getInstance();
      limSup.add(Calendar.YEAR, -45);
      final Calendar dtNasc = Calendar.getInstance();
      dtNasc.setTime(dataNasc);
      if (dtNasc.get(Calendar.YEAR) > limInf.get(Calendar.YEAR) || dtNasc.get(Calendar.YEAR) < limSup.get(Calendar.YEAR)) {
         return true;
      }
      return false;
   }
*/
}
