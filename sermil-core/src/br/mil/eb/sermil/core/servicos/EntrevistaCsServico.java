package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.EntrevistaCsDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.EntrevistaCsPersistenseFailException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.EntrevistaCs;
import br.mil.eb.sermil.modelo.Usuario;

@Named("entrevistaCsServico")
@RemoteProxy(name = "entrevistaCsServico")
public class EntrevistaCsServico {

   protected static final Logger logger = LoggerFactory.getLogger(DelegaciaServico.class);

   @Inject
   CidadaoServico cidadaoServico;

   @Inject
   EntrevistaCsDao dao;

   public EntrevistaCs recuperar(final Long ra) {
      return this.dao.findById(ra);
   }

   @Transactional
   public EntrevistaCs salvarEntrevistaCidadao(final EntrevistaCs entrevista, final Cidadao cidadao, final String ra, final Usuario usu) throws EntrevistaCsPersistenseFailException {
      try {
         // recuperando o cidadao completo do banco
         final Cidadao cid = this.cidadaoServico.recuperar(Long.valueOf(ra));

         // Alterando apenas as informacoes de cidadao que foram alteradas na entrevista
         cid.setEndereco(cidadao.getEndereco());
         cid.setBairro(cidadao.getBairro());
         cid.setCep(cidadao.getCep());
         cid.setTelefone(cidadao.getTelefone());
         cid.setMunicipioResidencia(cidadao.getMunicipioResidencia());
         cid.setEmail(cidadao.getEmail());
         cid.setReligiao(cidadao.getReligiao());
         cid.setOcupacao(cidadao.getOcupacao());
         cid.setEstadoCivil(cidadao.getEstadoCivil());
         cid.setEscolaridade(cidadao.getEscolaridade());

         // salvando cidadao
         this.cidadaoServico.salvar(cid, usu, "Salvando cidadão que foi alterado na Entrevista Cs.");

         // preparando e salvando entrevista
         entrevista.setRa(Long.valueOf(ra));
         return this.dao.save(entrevista);
      } catch (SermilException e) {
         logger.error(e.getMessage());
         throw new EntrevistaCsPersistenseFailException();
      }
   }

   public boolean entrevistaJaExisteParaRA(Long ra) throws CidadaoNotFoundException, IllegalArgumentException {
      if (ra == null)
         throw new IllegalArgumentException();
      EntrevistaCs ent;
      try {
         ent = dao.findById(ra);
      } catch (Exception e) {
         throw new CidadaoNotFoundException();
      }
      return (ent != null);
   }
   
   public void checaCidadao(Long ra) throws CidadaoNotFoundException{
      try {
         dao.findById(ra);
      } catch (Exception e) {
         throw new CidadaoNotFoundException();
      }
   }

   public EntrevistaCs recuperarEntrevistaNaoArrimo(Long ra, EntrevistaCs ent) {
      EntrevistaCs entrevista = dao.findById(ra);
      entrevista.setG10(ent.getG10());
      entrevista.setG12(ent.getG12());
      entrevista.setG121(ent.getG121());
      entrevista.setG13(ent.getG13());
      entrevista.setG13A(ent.getG13A());
      entrevista.setG14(ent.getG14());
      entrevista.setG14A(ent.getG14A());
      entrevista.setG15(ent.getG15());
      entrevista.setG15A(ent.getG15A());
      entrevista.setH1(ent.getH1());
      return entrevista;
   }

   public EntrevistaCs recuperarEntrevistaIndicadores(Long ra, EntrevistaCs entrevista) {
      EntrevistaCs ent = dao.findById(ra);
      entrevista.setL1(ent.getL1());
      entrevista.setM27(ent.getM27());
      entrevista.setN27(ent.getN27());
      entrevista.setN28(ent.getN28());
      entrevista.setO29(ent.getO29());
      entrevista.setInd1(ent.getInd1());
      entrevista.setInd2(ent.getInd2());
      entrevista.setInd3(ent.getInd3());
      entrevista.setInd3A(ent.getInd3A());
      entrevista.setInd4(ent.getInd4());
      entrevista.setInd5(ent.getInd5());
      entrevista.setInd5A(ent.getInd5A());
      entrevista.setInd6(ent.getInd6());
      entrevista.setInd7(ent.getInd7());
      entrevista.setInd8(ent.getInd8());
      entrevista.setInd9(ent.getInd9());
      return entrevista;
   }

   /**
    * Regra de Negocio: se qq indicador == false o cidadao se torna inapto para servir.
    * 
    * @param e
    */
   public void defineAvaliacaoFinal(EntrevistaCs e) {
      if (e.getInd1().equals("true") || e.getInd2().equals("true") || e.getInd3().equals("true") || e.getInd4().equals("true") || e.getInd5().equals("true") || e.getInd6().equals("true") || e.getInd7().equals("true") || e.getInd8().equals("true"))
         e.setInd9("false");
      else
         e.setInd9("true");
   }
}
