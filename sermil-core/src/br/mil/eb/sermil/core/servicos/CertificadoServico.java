package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.exceptions.CertificateNotFoundException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.EventNotFoundException;
import br.mil.eb.sermil.core.exceptions.OutOfSituationException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/** Processamento de certificados do serviço militar.
 * @author Abreu lopes
 * @since 5.1
 * @version 5.2.3
 */
@Named("certificadoServico")
public class CertificadoServico {

    protected static final Logger logger = LoggerFactory.getLogger(CertificadoServico.class);

    @Inject
    private CidadaoServico servico;
    
    public CertificadoServico() {
        logger.debug("CertificadoServico iniciado.");
    }

    @Transactional
    public Cidadao excluir(final CidCertificado certificado, final Usuario usuario) throws SermilException {
        final Cidadao cidadao = this.servico.recuperar(certificado.getPk().getCidadaoRa());
        cidadao.getCidCertificadoCollection().remove(certificado);
        logger.debug("CERTIFICADO: {}", certificado);
        logger.debug("USUARIO: {}", usuario);
        logger.debug("CIDADAO: {}", cidadao);
        return this.servico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO EXCLUIDO: ").append(certificado).toString());
    }
    
    @Transactional
    public Cidadao salvar(final CidCertificado certificado, final Usuario usuario) throws SermilException {
        final Cidadao cidadao = this.servico.recuperar(certificado.getPk().getCidadaoRa());
        if ((certificado.getPk().getTipo() == CidCertificado.RESERVISTA_1_CATEGORIA_TIPO || certificado.getPk().getTipo() == CidCertificado.RESERVISTA_2_CATEGORIA_TIPO) &&
            cidadao.getSituacaoMilitar() != Cidadao.SITUACAO_MILITAR_LICENCIADO) {
            throw new SermilException("Somente LICENCIADOS possuem Certificado de Reservista.");
        }
        cidadao.addCidCertificado(certificado);
        logger.debug("CERTIFICADO: {}", certificado);
        logger.debug("USUARIO: {}", usuario);
        logger.debug("CIDADAO: {}", cidadao);
        return this.servico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO: ").append(certificado).toString());
    }

    public void podeImprimirCAM(final Cidadao cidadao) throws SermilException {
        if (cidadao == null) {
           throw new SermilException("Cidadão não foi informado.");
        }
        if (cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_EXCLUIDO &&
        	cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_INCORPORADO &&
        	cidadao.getSituacaoMilitar() == Cidadao.SITUACAO_MILITAR_LICENCIADO) {
            throw new SermilException("Para imprimir o CAM, o cidadão NÃO pode já ter sido incorporado nas Forças Armadas.");
        }      
     }

    /** O cidadao tem que ter Pelo menos um evento do tipo 3, 6, 13, 14 ou 24 e pelo menos um certificado
     *  do tipo 3, 4 ou 6 e tem que estar em uma das situacoes militares: 3, 8, ou 9.
     * @param cidadao
     * @throws EventNotFoundException
     * @throws CertificateNotFoundException
     * @throws OutOfSituationException
     * @throws CidadaoNaoTemEventoException
     */
    public boolean podeImprimirCDI(final Cidadao cidadao) throws EventNotFoundException, CertificateNotFoundException, OutOfSituationException {
       if (!this.servico.cidadaoTemEvento(cidadao, CidEvento.EXCESSO_CONTINGENTE) && !this.servico.cidadaoTemEvento(cidadao, CidEvento.DISPENSA_SELECAO)) {
          throw new EventNotFoundException();
       }
       if (!temPeloMenosUmCertificado(cidadao, new Byte[] { CidCertificado.DISPENSA_DE_INCORPORACAO_COMPUTADOR, CidCertificado.DISPENSA_DE_INCORPORACAO_INFOR, CidCertificado.DISPENSA_DE_INCORPORACAO_PLANO })) {
          throw new CertificateNotFoundException();
       }
       if (!StringUtils.containsAny(cidadao.getSituacaoMilitar().toString(), "389")) {
          throw new OutOfSituationException();
       }
       return true;
    }

    public CidCertificado obterCDI(final Cidadao cidadao) {
        for (CidCertificado certificado : cidadao.getCidCertificadoCollection()) {
           if (certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_COMPUTADOR ||
               certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_INFOR ||
               certificado.getPk().getTipo() == CidCertificado.DISPENSA_DE_INCORPORACAO_PLANO) {
              return certificado;
           }
        }
        return null;
     }
    
    private boolean temCertificado(final Cidadao cidadao, final Byte tipo) {
        final List<CidCertificado> certificados = cidadao.getCidCertificadoCollection();
        if (certificados != null && certificados.size() > 0) {
           for (final CidCertificado certificado : certificados) {
              if (certificado.getPk().getTipo() == tipo) {
                 return true;
              }
           }
        }
        return false;
     }

     private boolean temPeloMenosUmCertificado(final Cidadao cidadao, final Byte[] tipos) {
        for (Byte tipo : tipos) {
           if (temCertificado(cidadao, tipo)) {
              return true;
           }
        }
        return false;
     }

     /*     
     private Boolean cidadaoJaTemCdi(final Cidadao cidadao) {
         final List<CidCertificado> certificados = this.certDao.findByNamedQuery("Certificado.cidadaoTemCdi", cidadao.getRa());
         if (certificados.isEmpty()) {
            return false;
         }
         return true;
      }
      */
     
}
