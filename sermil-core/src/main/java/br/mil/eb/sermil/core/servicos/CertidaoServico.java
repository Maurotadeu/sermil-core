package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;

/** Processamento de certidão de quitação do serviço militar.
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
                cid.setAnotacoes("ESTÁ EXCLUÍDO DO CADASTRO DO SERVIÇO MILITAR, NÃO SENDO MAIS VÁLIDO SEU ALISTAMENTO");
                break;
            case 1:
                cid.setAnotacoes("ESTÁ ALISTADO PARA O SERVIÇO MILITAR, SENDO VÁLIDA ESTA CERTIDÃO ATÉ 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 2:
                cid.setAnotacoes("FOI ENCAMINHADO À SELEÇÃO GERAL, SENDO VÁLIDA ESTA CERTIDÃO ATÉ 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 3:
                cid.setAnotacoes("ESTÁ DISPENSADO DO SERVIÇO MILITAR INICIAL, ESTANDO QUITE COM O SERVIÇO MILITAR");
                break;
            case 4:
                cid.setAnotacoes("FOI APTO NA SELEÇÃO GERAL, SENDO VÁLIDA ESTA CERTIDÃO ATÉ 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 5:  
            case 8:
            case 9:
                cid.setAnotacoes("ESTÁ DISPENSADO DO SERVIÇO MILITAR INICIAL, ESTANDO QUITE COM O SERVIÇO MILITAR INICIAL");
                break;
            case 7:
                cid.setAnotacoes("FOI ENCAMINHADO PARA INCORPORAÇÃO NAS FORÇAS ARMADAS, SENDO VÁLIDA ESTA CERTIDÃO ATÉ 31 DE DEZEMBRO DO CORRENTE ANO");
                break;
            case 12:
                cid.setAnotacoes("ESTÁ INCORPORADO NAS FORÇAS ARMADAS, ESTANDO QUITE COM SUAS OBRIGAÇÕES MILITARES");
                break;
            case 15:
                cid.setAnotacoes("LICENCIADO DAS FORÇAS ARMADAS, ESTANDO QUITE COM SUAS OBRIGAÇÕES MILITARES");
                break;
            case 13:  
            case 16:
            case 17:
                cid.setAnotacoes("SOLICITOU SERVIÇO MILITAR ALTERNATIVO, ESTANDO QUITE COM O SERVIÇO MILITAR INICIAL");
                break;
            default:
                cid.setAnotacoes("NÃO ESTÁ QUITE COM SUAS OBRIGAÇÕES MILITARES");
                break;
            }
        } else {
            throw new SermilException("Cidadão não foi encontrado.");
        }
        return cid;
    }

}
