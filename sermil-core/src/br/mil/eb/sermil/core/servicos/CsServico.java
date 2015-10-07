package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CselDao;
import br.mil.eb.sermil.core.dao.CselEnderecoDao;
import br.mil.eb.sermil.core.dao.CselFuncionamentoDao;
import br.mil.eb.sermil.core.dao.PgcDao;
import br.mil.eb.sermil.core.dao.RmDao;
import br.mil.eb.sermil.core.exceptions.AnoBaseNaoEhUnicoException;
import br.mil.eb.sermil.core.exceptions.CsPersistErrorException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoAnoBaseException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataInicioErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDataTerminoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoDeletarErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoFeriadoErroException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoJaExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoNaoExisteException;
import br.mil.eb.sermil.core.exceptions.FuncionamentosSobrepostosException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Csel;
import br.mil.eb.sermil.modelo.CselEndereco;
import br.mil.eb.sermil.modelo.CselFeriado;
import br.mil.eb.sermil.modelo.CselFuncionamento;
import br.mil.eb.sermil.modelo.Pgc;
import br.mil.eb.sermil.modelo.Rm;
import br.mil.eb.sermil.modelo.Usuario;

/**
 * Gerenciamento de CS.
 * 
 * @author Anselmo Ribeiro
 * @version 5.2.4
 * @since 5.2.4
 */
@Named("csServico")
@RemoteProxy(name = "csServico")
public class CsServico {

   protected static final Logger logger = LoggerFactory.getLogger(CsServico.class);

   @Inject
   CselDao cselDao;

   @Inject
   CselFuncionamentoDao funcionamentoDao;

   @Inject
   RmDao rmDao;

   @Inject
   CselEnderecoDao enderecoDao;

   @Inject
   PgcDao pgcDao;

   public Map<Integer, String> getCselEnderecos(Integer cselCodigo) {
      Map<Integer, String> ret = new HashMap<Integer, String>();
      List<CselEndereco> enderecos = enderecoDao.findByNamedQuery("listarEnderecosDeCselNative", cselCodigo);
      enderecos.forEach(end -> ret.put(end.getCodigo(), end.toString()));
      return ret;
   }

   public List<CselFuncionamento> listarFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("Funcionamento.listarFuncionamentosDeCsel", cselCodigo);
   }

   public CsServico() {
      logger.debug("CsServico iniciado");
   }

   public List<Csel> listarPorRM(Byte rm_codigo) {
      return (List<Csel>) cselDao.findByNamedQuery("Csel.listarPorRM", rm_codigo);
   }

   public List<Csel> listarPorRmDoUsuario(Byte rm_codigo, Usuario usu) {
      List<Csel> ret = new ArrayList<Csel>();
      if (usu.getAuthorities().stream().anyMatch(x -> x.getAuthority().contains("adm")))
         ret = cselDao.findByNamedQuery("Csel.listarPorRM", rm_codigo);
      else
         ret = cselDao.findByNamedQuery("Csel.listarPorRM", usu.getOm().getRm().getCodigo());
      return ret;
   }

   public List<Csel> ListarPorNome(String nome) {
      return (List<Csel>) cselDao.findByNamedQuery("Csel.listarPorNome", nome);
   }

   @Transactional
   public Csel persistir(Csel cs) throws CsPersistErrorException {
      try {
         cs = cselDao.save(cs);
      } catch (Exception e) {
         logger.error(e.getMessage());
         e.printStackTrace(); // TODO: tirar stacktrace antes de entrar em producao
         throw new CsPersistErrorException();
      }
      return cs;
   }

   public Csel recuperar(Integer cs_codigo) {
      return cselDao.findById(cs_codigo);
   }

   public Map<String, String> getTributacoes() {
      HashMap<String, String> tributacoes = new HashMap<String, String>();
      tributacoes.put(Csel.TRIBUTACAO_EB, Csel.TRIBUTACAO_EB);
      tributacoes.put(Csel.TRIBUTACAO_FAB, Csel.TRIBUTACAO_FAB);
      tributacoes.put(Csel.TRIBUTACAO_MAR, Csel.TRIBUTACAO_MAR);
      tributacoes.put(Csel.TRIBUTACAO_TG, Csel.TRIBUTACAO_TG);
      return tributacoes;
   }

   /**
    * 
    * @param usuRm Rm a rm do usuario. Tente: Rm rm = ((Usuario) ((SecurityContext)
    *           this.session.get("SPRING_SECURITY_CONTEXT")).getAuthentication().getPrincipal()).
    *           getOm().getRm();
    * 
    * @param isAdm se o usuario é ou nao administrador Tente: boolean isAdm =
    *           ServletActionContext.getRequest().isUserInRole("adm");
    * 
    * @return Map codigo -> sigla
    */
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

   public CselEndereco getEndereco(Integer enderecoCodigo) {
      return enderecoDao.findById(enderecoCodigo);
   }

   @Transactional(propagation = Propagation.NESTED)
   public Csel salvarCselEFuncionamento(Csel cs, CselFuncionamento funcionamento, List<CselFeriado> feriados, CselEndereco endereco) throws FuncionamentoJaExisteException, CsPersistErrorException, AnoBaseNaoEhUnicoException,
         FuncionamentoDataInicioErroException, FuncionamentoDataTerminoErroException, FuncionamentoFeriadoErroException, FuncionamentosSobrepostosException, FuncionamentoAnoBaseException {

      feriados.forEach(fer -> {
         if (fer == null)
            feriados.remove(fer);
         else
            fer.setFuncionamento(funcionamento);
      });
      funcionamento.setFeriados(feriados);

      // Endereco
      if (endereco.getCodigo() != null)
         endereco = enderecoDao.findById(endereco.getCodigo());
      funcionamento.setEndereco(endereco);

      // Funcionamento
      cs.addFuncionamento(funcionamento);

      // persistir
      if (isFuncionamentoDeCsCorreto(funcionamento) && isFeriadosDeFuncionamentoCorretos(feriados, funcionamento))
         persistir(cs);
      return cs;
   }

   /**
    * Regras de Negocio para Funcionamento de CS
    * @return boolean
    */
   public boolean isFuncionamentoDeCsCorreto(CselFuncionamento func)
         throws AnoBaseNaoEhUnicoException, FuncionamentoDataInicioErroException, FuncionamentoDataTerminoErroException, FuncionamentoFeriadoErroException, FuncionamentosSobrepostosException, FuncionamentoAnoBaseException {

      // ano base de PGC tem que ser unico
      if (!this.anoBaseDePgcEhUnico(func.getAnoBase())) {  
         logger.error("Exite um PGC com dois lancamento de ano base. Ano base: " + func.getAnoBase());
         throw new AnoBaseNaoEhUnicoException();
      }
      List<Pgc> pgcs = pgcDao.findByNamedQuery("pgc.findByAnoBase", func.getAnoBase());
      Pgc p = pgcs.get(0);

      // O inicio da CS nao pode ser antes do inicio no PGC
      if (func.getInicioData().before(p.getSelecaoGeralInicio()))
         throw new FuncionamentoDataInicioErroException();

      // O termino da CS nao pode ser depois do PGC
      if (func.getTerminoData().after(p.getSelecaoGeralTermino()))
         throw new FuncionamentoDataTerminoErroException();

      // Os blocos de cada inicio e termino de funcionamento para o mesmo ano base nao podem se
      // sobrepor
      List<CselFuncionamento> funcs = cselDao.findById(func.getCsel().getCodigo()).getFuncionamentos();
      for (CselFuncionamento f : funcs) {
         if (func.getInicioData().before(f.getTerminoData()) || func.getTerminoData().after(f.getInicioData()))
            throw new FuncionamentosSobrepostosException();
      }

      // A CS so pode cadastrar funcionamento com ano base ja cadastrado no PGC
      List<Pgc> ps = pgcDao.findByNamedQuery("pgc.findByAnoBase", func.getAnoBase());
      if (ps.size() == 0)
         throw new FuncionamentoAnoBaseException();

      return true;
   }

   /**
    * Regras de Negocio para Feriados de Funcionamento de CS
    * @return boolean
    */
   public boolean isFeriadosDeFuncionamentoCorretos(List<CselFeriado> feriados, CselFuncionamento func) throws FuncionamentoFeriadoErroException{
      // Os feriados tem que estar dentro do periodo declarado
      for (CselFeriado fer : feriados) {
         if (fer.getFeriadoData().before(func.getInicioData()) || fer.getFeriadoData().after(func.getTerminoData()))
            throw new FuncionamentoFeriadoErroException();
      }
      return true;
   }

   public boolean anoBaseDePgcEhUnico(String anoBase) {
      List<Pgc> pgcs = pgcDao.findByNamedQuery("findByAnoBase", anoBase);
      return pgcs.size() == 1?true:false;
   }

   public List<CselFuncionamento> getFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("listarFuncionamentosDeCsel", cselCodigo);
   }

   @Transactional
   public void deletarFuncionamento(Integer codigo) throws FuncionamentoNaoExisteException, FuncionamentoDeletarErroException {
      CselFuncionamento func = funcionamentoDao.findById(codigo);
      if (func == null) {
         logger.error(new StringBuilder("Usuario tentou deletar Funcionamento de CS mas funcionamento nao existe. Funcionamento codigo =  ").append(codigo).toString());
         throw new FuncionamentoNaoExisteException();
      }
      try {
         funcionamentoDao.delete(func);
      } catch (SermilException e) {
         logger.error("Erro ao deletar funcionamento: " + func.toString());
         throw new FuncionamentoDeletarErroException();
      }
   }

}
