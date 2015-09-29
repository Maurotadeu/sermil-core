package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
import br.mil.eb.sermil.core.dao.RmDao;
import br.mil.eb.sermil.core.exceptions.CsPersistErrorException;
import br.mil.eb.sermil.core.exceptions.FuncionamentoJaExisteException;
import br.mil.eb.sermil.modelo.Csel;
import br.mil.eb.sermil.modelo.CselEndereco;
import br.mil.eb.sermil.modelo.CselFeriado;
import br.mil.eb.sermil.modelo.CselFuncionamento;
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
    * @param isAdm se o usuario � ou nao administrador Tente: boolean isAdm =
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
   public Csel salvarCselEFuncionamento(Csel cs, CselFuncionamento funcionamento, List<CselFeriado> feriados, CselEndereco endereco) throws FuncionamentoJaExisteException, CsPersistErrorException {

      // TODO: Feriados
      
      IntStream.range(0, feriados.size()).mapToObj(i -> feriados.get(i)).forEach(fer -> System.out.println(""));
      funcionamento.setFeriados(feriados);

      // Endereco
      if (endereco.getCodigo() != null)
         endereco = enderecoDao.findById(endereco.getCodigo());
      funcionamento.setEndereco(endereco);

      // Funcionamento
      cs.addFuncionamento(funcionamento);

      // Csel
      persistir(cs);
      return cs;
   }

   public List<CselFuncionamento> getFuncionamentosDeCsel(Integer cselCodigo) {
      return funcionamentoDao.findByNamedQuery("listarFuncionamentosDeCsel", cselCodigo);
   }

}