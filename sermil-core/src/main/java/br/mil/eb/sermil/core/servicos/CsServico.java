package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsDao;
import br.mil.eb.sermil.core.dao.CsEnderecoDao;
import br.mil.eb.sermil.core.dao.CsFuncionamentoDao;
import br.mil.eb.sermil.core.exceptions.AnoBaseNaoEhUnicoException;
import br.mil.eb.sermil.core.exceptions.ConsultaException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataInicioErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataTerminoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentosSobrepostosException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.PgcNaoExisteException;
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
   CsFuncionamentoDao csFuncionamentoDao;

   @Inject
   CsEnderecoDao csEnderecoDao;

   @Inject
   PgcServico pgcServico;

   public CsServico() {
      logger.debug("CsServico iniciado");
   }

   public List<Cs> listarPorRm(final Byte rm) throws ConsultaException {
      if (rm == null) {
         throw new ConsultaException("Informe o número da Região Militar");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorRm", rm);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS definidas nesta Região Militar");
      }
      return lista;
   }

   public List<Cs> listarPorNome(final String nome) throws ConsultaException {
      if (nome == null) {
         throw new ConsultaException("Informe o nome da CS");
      }
      final List<Cs> lista = this.csDao.findByNamedQuery("Cs.listarPorNome", nome);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há CS definidas nesta Região Militar");
      }
      return lista;
   }

   public List<CsEndereco> listarEnderecos(final Integer municipioCodigo) throws ConsultaException {
      if (municipioCodigo == null) {
         throw new ConsultaException("Informe o código do Município");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorMunicipio", municipioCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados no Município");
      }
      return lista;
   }

   @RemoteMethod
   public List<CsEndereco> listarEnderecosCsRm(final Integer rmCodigo) throws ConsultaException {
      if (rmCodigo == null) {
         throw new ConsultaException("Informe o código da Região Militar");
      }
      final List<CsEndereco> lista = this.csEnderecoDao.findByNamedQuery("CsEndereco.listarPorRm", rmCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há endereços de CS cadastrados na Região Militar");
      }
      return lista;
   }

   public List<CsFuncionamento> listarFuncionamentos(final Integer csCodigo) throws ConsultaException {
      if (csCodigo == null) {
         throw new ConsultaException("Informe o código da CS");
      }
      final List<CsFuncionamento> lista = this.csFuncionamentoDao.findByNamedQuery("CsFuncionamento.listarPorCs", csCodigo);
      if (lista == null || lista.isEmpty()) {
         throw new ConsultaException("Não há funcionamentos cadastrados para esta CS");
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
   public String excluir(final Integer csCodigo) throws SermilException {
      this.csDao.delete(this.recuperar(csCodigo));
      return new StringBuilder("CS ").append(csCodigo).append(" excluída").toString();
   }
   
   @Transactional
   public String excluirEndereco(final Integer codigo) throws SermilException {
      this.csEnderecoDao.delete(this.recuperarEndereco(codigo));
      return new StringBuilder("Endereço cod=").append(codigo).append(" excluído").toString();
   }

   @Transactional
   public Cs salvar(final Cs cs) throws SermilException {
      return this.csDao.save(cs);
   }

   @Transactional
   public CsEndereco salvarEndereco(final CsEndereco csEndereco) throws SermilException {
      return this.csEnderecoDao.save(csEndereco);
   }

   public List<CsFuncionamento> getCsFuncionamentos(final Integer csCodigo) {
      return this.csFuncionamentoDao.findByNamedQuery("CsFuncionamento.listarPorCs", csCodigo);
   }
   
   public CsEndereco getEndereco(Integer enderecoCodigo) {
      return this.csEnderecoDao.findById(enderecoCodigo);
   }

   /** Regras de Negocio para Funcionamento de CS.
    * @return boolean
    * @throws PgcNaoExisteException
    */
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

   public boolean isCsFeriadosCorretos(final List<CsExclusaoData> exclusaoData, final CsFuncionamento csFuncionamento) throws SermilException {
      for (CsExclusaoData fer : exclusaoData) {
         if (fer.getExclusaoData().before(csFuncionamento.getInicioData()) || fer.getExclusaoData().after(csFuncionamento.getTerminoData()))
            throw new SermilException("Data a ser excluída invalida");
      }
      return true;
   }

}


/*
 * Obter RM.
 * 
 * @param usuRm
 *           Rm a rm do usuario. Tente: Rm rm = ((Usuario) ((SecurityContext)
 *           this.session.get("SPRING_SECURITY_CONTEXT")).getAuthentication()
 *           .getPrincipal()). getOm().getRm();
 * 
 * @param isAdm
 *           se o usuario é ou nao administrador Tente: boolean isAdm =
 *           ServletActionContext.getRequest().isUserInRole("adm");
 * 
 * @return Map codigo -> sigla
public Map<Byte, String> getRms(Rm usuRm, boolean isAdm) {
   Map<Byte, String> mappedRms = new HashMap<Byte, String>();
   List<Rm> rms = rmDao.findAll();
   for (int i = 0; i < rms.size(); i++) {
      if (rms.get(i).getCodigo() == 0) {
         rms.remove(i);
         break;
      }
   }
   for (Rm rm2 : rms) {
      if (isAdm)
         mappedRms.put(rm2.getCodigo(), rm2.getSigla());
      else if (usuRm.getCodigo() == rm2.getCodigo())
         mappedRms.put(rm2.getCodigo(), rm2.getSigla());
   }
   return mappedRms;
}
*/
