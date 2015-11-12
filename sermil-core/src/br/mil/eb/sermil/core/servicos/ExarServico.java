package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import br.mil.eb.sermil.core.exceptions.ExarException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/** Gerenciamento de Apresentação de Reservista (EXAR).
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.2.6
 */
@Named("exarServico")
public class ExarServico {

   protected static final Logger logger = LoggerFactory.getLogger(ExarServico.class);

   @Inject
   private CidadaoServico cidadaoServico;

   public ExarServico() {
      logger.debug("ExarServico iniciado");
   }

   public String gerarAutenticador(final Long ra, final Byte nrApres) throws SermilException {
      if (ra == null || nrApres == null) {
         throw new ExarException("ERRO: informe o RA do cidadão e o número da apresentação.");
      }
      final Cidadao cid = this.cidadaoServico.recuperar(ra);
      CidExar apres = null;
      for(CidExar e: cid.getCidExarCollection()){
         if (e.getPk().getApresentacaoQtd() == nrApres) {
            apres = e;
         }
      }
      if (apres == null) {
         throw new ExarException("Apresentação do EXAR não está cadastrada, verifique o cadastro do cidadão.");
      }
      byte[] b = (cid.getNome() + cid.getMae() + apres.getApresentacaoData()).getBytes();
      StringBuilder sb = new StringBuilder();
      DigestUtils.appendMd5DigestAsHex(b, sb);
      return sb.toString();
   }

   public boolean isReciboExarValido(final Long ra, final Byte nrApres, final String codigo) throws SermilException {
      return this.gerarAutenticador(ra, nrApres).equals(codigo);
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
   @Transactional
   public Cidadao adicionarApresentacao(final CidExar apresentacao, final Usuario usr) throws SermilException {
      if (apresentacao == null || apresentacao.getPk() == null || apresentacao.getPk().getCidadaoRa() == null) {
         throw new ExarException("ERRO: Informe o RA do cidadão para cadastrar uma apresentação do EXAR.");
      }
      final Cidadao cid = this.cidadaoServico.recuperar(apresentacao.getPk().getCidadaoRa());
      if (!isExarLiberado(cid)) {
         throw new ExarException("ERRO: Para cadastrar uma apresentação o cidadão deve estar na situação LICENCIADO, ou EXCESSO com CDI em Situação Especial cadastrado.");
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
      if (cid.getCidExarCollection().size() > 0) {
         for (int i = 0; i < cid.getCidExarCollection().size(); i++) {
            lista.remove(cid.getCidExarCollection().get(i));
         }
      }
      for (CidExar ce : lista) {
         cid.addCidExar(ce);
      }
      return this.cidadaoServico.salvar(cid, usr, new StringBuilder("APRESENTAÇÃO: ").append(apresentacao).toString());
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
   @Transactional
   public Cidadao excluirApresentacao(final CidExar apresentacao, final Usuario usr) throws SermilException {
      final Cidadao cid = this.cidadaoServico.recuperar(apresentacao.getPk().getCidadaoRa());
      cid.getCidExarCollection().remove(apresentacao);
      return this.cidadaoServico.salvar(cid, usr, new StringBuilder("APRESENTAÇÃO EXCLUÍDA: ").append(apresentacao).toString());
   }

   /** Regrs para permitir inclusão de apresentação do EXAR.
    * @param cid Cidadão
    * @return true permitido
    */
   private boolean isExarLiberado(final Cidadao cid) {
      boolean status = false;
      if(cid.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_LICENCIADO) {
         status = true;
      } else if (cid.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_EXCESSO) {
         for (CidCertificado c: cid.getCidCertificadoCollection()) {
            if (!"S".equals(c.getAnulado())) {
               if (c.getPk().getTipo() == 3 || c.getPk().getTipo() == 4 || c.getPk().getTipo() == 6) {
                  if ("S".equals(c.getSituacaoEspecial())) {
                     status = true;
                  }
               }
            }
         }
      }
      return status;
   }

}
