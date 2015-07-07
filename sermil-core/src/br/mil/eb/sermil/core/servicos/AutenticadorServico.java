package br.mil.eb.sermil.core.servicos;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;

/** Gerador de Hash MD5 para autenticação.
 * @author Abreu lopes
 * @since 5.1
 * @version $Id$
 */
@Named("autenticadorServico")
public class AutenticadorServico {

    protected static final Logger logger = LoggerFactory.getLogger(AutenticadorServico.class);

    public AutenticadorServico() {
        logger.debug("AutenticadorServico iniciado.");
    }

    public String gerar(final Cidadao cid) throws SermilException {
        if (cid == null || cid.getRa() == null) {
            throw new SermilException("ERRO: informe dados do cidadão para gerar autenticador.");
        }
        //TODO: verificar se a condição hash MD5 é suficiente para evitar colisão ou está muito estática para o mesmo cidadão 
        byte[] b = (cid.getRa().toString() + cid.getSituacaoMilitar() + cid.getNascimentoData().toString() + cid.getAtualizacaoData()).getBytes();
        final StringBuilder sb = new StringBuilder();
        DigestUtils.appendMd5DigestAsHex(b, sb);
        return sb.toString().toUpperCase();
    }

    public boolean isValido(final Cidadao cid, final String codigo) throws SermilException {
        final String aux = codigo.replaceAll("\\.", "").trim();
        return aux.equals(this.gerar(cid));
    }

}
