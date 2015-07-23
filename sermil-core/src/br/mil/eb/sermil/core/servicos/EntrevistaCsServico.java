package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.EntrevistaCsDao;
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
   
   
   public EntrevistaCs recuperar ( Long ra){ 
      return dao.findById(ra);
   }

   @Transactional
   public EntrevistaCs salvarEntrevistaECidadao(EntrevistaCs entrevista, Cidadao cidadao, String ra, Usuario usu) throws EntrevistaCsPersistenseFailException {
      try {

         //recuperando o cidadao completo do banco
         final Cidadao cid = this.cidadaoServico.recuperar(Long.valueOf(ra));
         
         //Alterando apenas as informacoes de cidadao que foram alteradas na entrevista
         cid.setEndereco(cidadao.getEndereco());
         cid.setBairro(cidadao.getBairro());
         cid.setCep(cidadao.getCep());
         cid.setTelefone(cidadao.getTelefone());
         cid.setMunicipioResidencia(cidadao.getMunicipioResidencia());
         cid.setEmail(cidadao.getEmail());
         cid.setReligiao(cidadao.getReligiao());
         cid.setOcupacao(cidadao.getOcupacao());
         cid.setEstadoCivil(cidadao.getEstadoCivil());
         
         //salvando cidadao
         cidadaoServico.salvar(cid, usu, "Salvando cidadao que foi alterado na Entrevista CS");
         
         //preparando e salvando entrevista 
         entrevista.setRa(Long.valueOf(ra));
         return dao.save(entrevista);
         
      } catch (SermilException e) {
         e.printStackTrace();
         logger.error(e.getMessage());
         throw new EntrevistaCsPersistenseFailException();
      }
   }

}
