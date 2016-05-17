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

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoCertificado;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Gerenciamento de Apresentação de Reservista (EXAR).
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.4
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
         throw new SermilException("Informe o RA do cidadão e o número da apresentação.");
      }
      final Cidadao cid = this.cidadaoServico.recuperar(ra);
      CidExar apres = null;
      for(CidExar e: cid.getCidExarCollection()){
         if (e.getPk().getApresentacaoQtd() == nrApres) {
            apres = e;
         }
      }
      if (apres == null) {
         throw new SermilException("Apresentação do EXAR não está cadastrada, verifique o cadastro do cidadão.");
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
   public String adicionarApresentacao(final Cidadao cidadao, final CidExar apresentacao, final Usuario usr) throws SermilException {
      if (apresentacao == null || apresentacao.getPk() == null || apresentacao.getPk().getCidadaoRa() == null) {
         throw new SermilException("Informe o RA do cidadão para cadastrar uma apresentação do EXAR.");
      }
      if (!isExarLiberado(cidadao)) {
         throw new SermilException("Para cadastrar uma apresentação o cidadão deve estar na situação LICENCIADO, ou EXCESSO com CDI em Situação Especial cadastrado.");
      }
      if (!cidadao.getCidExarCollection().isEmpty() && cidadao.getCidExarCollection().contains(apresentacao)) {
        throw new SermilException("Apresentação já existe: Nr " + apresentacao.getPk().getApresentacaoQtd());
      }
      // Inclui as apresentações que faltam até o número informado (ajuda o usuário do sistema)      
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
      for (CidExar ce : lista) {
        try {
          cidadao.addCidExar(ce);
        } catch (Exception e) {
          // ignora se apresentacao ja existir, adiciona as demais
        }
      }
      this.cidadaoServico.salvar(cidadao, usr, new StringBuilder("APRESENTAÇÃO: ").append(apresentacao).toString());
      return new StringBuilder(cidadao.toString()).append(": apresentação salva.").toString();
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
   @Transactional
   public String excluirApresentacao(final Cidadao cidadao, final CidExar apresentacao, final Usuario usr) throws SermilException {
      cidadao.getCidExarCollection().remove(apresentacao);
      this.cidadaoServico.salvar(cidadao, usr, new StringBuilder("APRESENTAÇÃO EXCLUÍDA: ").append(apresentacao).toString());
      return new StringBuilder(cidadao.toString()).append(": apresentação excluída.").toString();
   }

   private boolean isExarLiberado(final Cidadao cid) {
      boolean status = false;
      if(cid.getSituacaoMilitar() == TipoSituacaoMilitar.LICENCIADO.ordinal()) {
         status = true;
      } else if (cid.getSituacaoMilitar() == TipoSituacaoMilitar.EXCESSO.ordinal()) {
         for (CidCertificado c: cid.getCidCertificadoCollection()) {
            if (!"S".equals(c.getAnulado())) {
               if (c.getPk().getTipo() == TipoCertificado.CDI.ordinal()) {
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
