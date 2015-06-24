package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;

/** Processamento de certid�o de quita��o do servi�o militar.
 * @author Abreu lopes
 * @since 5.1
 * @version $Id$
 */
@Named("certidaoServico")
public class CertidaoServico {

    protected static final Logger logger = LoggerFactory.getLogger(CertidaoServico.class);

    @Inject
    private CidadaoServico servico;

    public CertidaoServico() {
        logger.debug("CertidaoServico iniciado.");
    }

    public Cidadao verificar(final Cidadao cidadao) throws SermilException {
        Cidadao cid = null;
        if (cidadao.getRa() != null) {
            cid = this.servico.recuperar(cidadao.getRa());
        } else {
            if (cidadao.getCpf() != null) {
                cid = this.servico.recuperar(cidadao.getCpf());
            }
        }
        if (cid != null) {
            switch (cid.getSituacaoMilitar()) {
            case 0:
                cid.setAnotacoes("EST� EXCLU�DO DO CADASTRO DO SERVI�O MILITAR, N�O SENDO MAIS V�LIDO SEU ALISTAMENTO");
                break;
            case 1:
                cid.setAnotacoes("EST� ALISTADO PARA O SERVI�O MILITAR, SENDO V�LIDA ESTA CERTID�O AT� 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 2:
                cid.setAnotacoes("FOI ENCAMINHADO � SELE��O GERAL, SENDO V�LIDA ESTA CERTID�O AT� 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 3:
                cid.setAnotacoes("EST� DISPENSADO DO SERVI�O MILITAR INICIAL, ESTANDO QUITE COM O SERVI�O MILITAR");
                break;
            case 4:
                cid.setAnotacoes("FOI APTO NA SELE��O GERAL, SENDO V�LIDA ESTA CERTID�O AT� 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 5:  
            case 8:
            case 9:
                cid.setAnotacoes("EST� DISPENSADO DO SERVI�O MILITAR INICIAL, ESTANDO QUITE COM O SERVI�O MILITAR INICIAL");
                break;
            case 7:
                cid.setAnotacoes("FOI ENCAMINHADO PARA INCORPORA��O NAS FOR�AS ARMADAS, SENDO V�LIDA ESTA CERTID�O AT� 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 12:
                cid.setAnotacoes("EST� INCORPORADO NAS FOR�AS ARMADAS, ESTANDO QUITE COM SUAS OBRIGA��ES MILITARES");
                break;
            case 15:
                cid.setAnotacoes("LICENCIADO DAS FOR�AS ARMADAS, ESTANDO QUITE COM SUAS OBRIGA��ES MILITARES");
                break;
            case 13:  
            case 16:
            case 17:
                cid.setAnotacoes("SOLICITOU SERVI�O MILITAR ALTERNATIVO, ESTANDO QUITE COM O SERVI�O MILITAR INICIAL");
                break;
            default:
                cid.setAnotacoes("N�O EST� QUITE COM SUAS OBRIGA��ES MILITARES");
                break;
            }
        } else {
            throw new SermilException("Cidad�o n�o foi encontrado.");
        }
        return cid;
    }

}
