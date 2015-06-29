package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/** Processamento de certificados do serviço militar.
 * @author Abreu lopes
 * @since 5.1
 * @version $Id$
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

}
