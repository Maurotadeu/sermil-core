package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.CsEnderecoDao;
import br.mil.eb.sermil.core.exceptions.AnoBaseNaoEhUnicoException;
import br.mil.eb.sermil.core.exceptions.ConsultaException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataInicioErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataTerminoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentosSobrepostosException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.CsEndereco;
import br.mil.eb.sermil.modelo.CsExclusaoData;
import br.mil.eb.sermil.modelo.CsFuncionamento;
import br.mil.eb.sermil.modelo.Pgc;

/** Serviço de CS.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @version 5.2.4
 * @since 5.3.2
 */
@Named("csServico")
@RemoteProxy(name = "csServico")
public class CsServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

   @Inject
   CsDao csDao;

   @Inject
   CsEnderecoDao csEnderecoDao;

   @Inject
   PgcServico pgcServico;

   public CsServico() {
      logger.debug("CsServico iniciado");
   }

   @RemoteMethod
   public List<Cs> listarPorRm(final Byte rm) throws ConsultaException {
      if (rm == null) {
         throw new ConsultaException("Informe o número da Região Militar");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorRm", rm);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS cadastrada na " + rm + "ª Região Militar");
      }
      return lista;
   }

   public List<Cs> listarPorNome(final String nome) throws ConsultaException {
      if (nome == null) {
         throw new ConsultaException("Informe o nome da CS");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorNome", nome);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS cadastra com o nome " + nome + ".");
      }
      return lista;
   }

   public List<CsEndereco> listarCsEnderecoMun(final Integer municipioCodigo) throws ConsultaException {
      if (municipioCodigo == null) {
         throw new ConsultaException("Informe o código do Município");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorMunicipio", municipioCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados no município código " + municipioCodigo);
      }
      return lista;
   }

   @RemoteMethod
   public List<CsEndereco> listarCsEnderecoRm(final Byte rmCodigo) throws ConsultaException {
      if (rmCodigo == null) {
         throw new ConsultaException("Informe o código da Região Militar");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorRm", rmCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados na " + rmCodigo + "ª Região Militar");
      }
      return lista;
   }

   @RemoteMethod
   public List<CsFuncionamento> listarCsFuncionamento(final Integer csCodigo) throws ConsultaException {
      if (csCodigo == null) {
         throw new ConsultaException("Informe o código da CS");
      }
      final List<CsFuncionamento> lista = this.recuperar(csCodigo).getCsFuncionamentoCollection();
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há funcionamentos cadastrados para a CS " + csCodigo);
      }
      return lista;
   }

   @RemoteMethod
   public List<CsExclusaoData> listarCsExclusaoData(final Integer csCodigo) throws ConsultaException {
      if (csCodigo == null) {
         throw new ConsultaException("Informe o código da CS");
      }
      final List<CsExclusaoData> lista = this.recuperar(csCodigo).getCsExclusaoDataCollection();
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há exclusões de data cadastradas para a CS " + csCodigo);
      }
      return lista;
   }
   
   public Cs recuperar(final Integer csCodigo) {
      return this.csDao.findById(csCodigo);
   }

   public CsEndereco recuperarEndereco(final Integer codigo) throws SermilException {
      if (codigo == null) {
         throw new SermilException("Informe o código do Endereço");
      }
      final CsEndereco end = this.csEnderecoDao.findById(codigo);
      if (end == null) {
         throw new NoDataFoundException(new StringBuilder("Endereço não foi encontrado (cod=").append(codigo).append(")").toString());
      }
      return end;
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String excluir(final Integer csCodigo) throws SermilException {
      final Cs cs = this.recuperar(csCodigo);
      this.csDao.delete(cs);
      logger.info("Excluída: {}", cs);
      return new StringBuilder(cs.toString()).append(" excluida").toString();
   }
   
   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String excluirEndereco(final Integer codigo) throws SermilException {
      final CsEndereco csEnd = this.recuperarEndereco(codigo);
      this.csEnderecoDao.delete(csEnd);
      logger.info("Excluído: {}", csEnd);
      return new StringBuilder(csEnd.toString()).append(" excluido").toString();
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String salvar(final Cs cs) throws SermilException {
      final Cs csSalva = this.csDao.save(cs);
      logger.info("Salvo: {}", csSalva);
      return new StringBuilder(cs.toString()).append(" salva").toString();
   }

   @Transactional
   @PreAuthorize("hasAnyRole('adm','dsm','smr')")
   public String salvarEndereco(final CsEndereco csEndereco) throws SermilException {
      final CsEndereco csEnd = this.csEnderecoDao.save(csEndereco);
      logger.info("Salvo: {}", csEnd);
      return new StringBuilder(csEnd.toString()).append(" salvo").toString();
   }

   /* Verificar */
   public boolean isCsFuncionamentoCorreto(final CsFuncionamento funcionamento) throws SermilException {
      // ano base de PGC tem que ser unico
      if (!this.pgcServico.isAnoBaseUnico(funcionamento.getAnoBase())) {
         logger.error("Exite um PGC com dois lançamentos de ano base. Ano base: " + funcionamento.getAnoBase());
         throw new AnoBaseNaoEhUnicoException();
      }
      
      final Pgc pgc = this.pgcServico.listarPcg(funcionamento.getAnoBase()).get(0);

      // O inicio da CS nao pode ser antes do inicio no PGC
      if (funcionamento.getInicioData().before(pgc.getSelecaoGeralInicio()))
         throw new FuncionamentoDataInicioErroException();

      // O termino da CS nao pode ser depois do PGC
      if (funcionamento.getTerminoData().after(pgc.getSelecaoGeralTermino()))
         throw new FuncionamentoDataTerminoErroException();

      // Os blocos de cada inicio e termino de funcionamento para o mesmo ano
      // base nao podem se
      // sobrepor
      final List<CsFuncionamento> listaFunc = this.csDao.findById(funcionamento.getCs().getCodigo()).getCsFuncionamentoCollection();
      for (CsFuncionamento f : listaFunc) {
         if (funcionamento.getInicioData().before(f.getTerminoData()) || funcionamento.getTerminoData().after(f.getInicioData()))
            throw new FuncionamentosSobrepostosException();
      }
      return true;
   }

}
