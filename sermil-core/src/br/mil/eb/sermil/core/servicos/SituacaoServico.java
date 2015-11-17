package br.mil.eb.sermil.core.servicos;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidEventoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;

/** Verifica��o de situa��o no servi�o militar.
 * @author Abreu lopes
 * @since 5.1
 * @version 5.2.6
 */
@Named("situacaoServico")
public class SituacaoServico {

    protected static final Logger logger = LoggerFactory.getLogger(SituacaoServico.class);

    @Inject
    private CidadaoServico servico;

    @Inject
    private CidEventoDao eventoDao;

    public SituacaoServico() {
        logger.debug("SituacaoServico iniciado.");
    }

    public Cidadao verificar(final Cidadao cidadao) throws SermilException {
        if (cidadao == null || (cidadao.getRa() == null && cidadao.getCpf() == null)) {
            throw new CidadaoNotFoundException();
        }
        Cidadao cid = null;
        if (cidadao.getRa() != null) {
            cid = this.servico.recuperar(cidadao.getRa());
        } else {
            if (cidadao.getCpf() != null) {
                cid = this.servico.recuperar(cidadao.getCpf());
            }
        }
        if (cid != null) {
            if ("N".equalsIgnoreCase(cid.getJsm().getJsmInfo().getInternet())) {
                throw new SermilException("Verifique no seu documento de alistamento (CAM) a data de comparecimento no �rg�o de Servi�o Militar.");
            }
            switch (cid.getSituacaoMilitar()) {
            case 1:
                if (Calendar.getInstance().get(Calendar.MONTH) > 5) {
                    cid.setAnotacoes("Verifique a partir do pr�ximo ANO a data de comparecimento no �rg�o de Servi�o Militar.");
                } else {
                    cid.setAnotacoes("Verifique a partir de 10 de julho a sua situa��o no Servi�o Militar no sistema.");
                }
                break;
            case 2:
                //cid.setAnotacoes("Comparecer na Comiss�o de Sele��o " + (cid.getCs() == null ? "" : cid.getCs()) + ", na data agendada, para realizar a Sele��o Geral. <br>Em caso de falta ser� considerado REFRAT�RIO e ficar� sujeito as penas previstas na Lei de Servi�o Militar.");
                cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + cid.getJsm() + ", em " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(definirRetorno(cid.getRa()))  + " (final de semana ou feriado, no dia �til seguinte), para ser encaminhado � Sele��o Geral.");
                break;
            case 3:
                cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + cid.getJsm() + ", em " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(definirRetorno(cid.getRa()))  + " (final de semana ou feriado, no dia �til seguinte), para solicitar o Certificado de Dispensa de Incorpora��o (CDI). Caso j� tenha data de retorno agendada, compare�a na data informada pela Junta.");
                break;
            case 4:
                cid.setAnotacoes("Verifique no per�odo de 2 a 10 de janeiro novamente a sua situa��o no Servi�o Militar.");
                break;
            case 6:
                cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + cid.getJsm() + ", para solicitar, se necess�rio, a prorroga��o do adiamento do Servi�o Militar Inicial.");
                break;
            case 7:
                cid.setAnotacoes("Comparecer no " + cid.getOm().getDescricao() + ", na data agendada, para realizar a Sele��o Complementar. <br>Em caso de falta ser� considerado REFRAT�RIO e ficar� sujeito as penas previstas na Lei de Servi�o Militar.");
                break;
            case 5:  
            case 8:
            case 9:
                cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + cid.getJsm() + ", na data agendada, para solicitar o Certificado de Dispensa de Incorpora��o (CDI).");
                break;
            case 12:
                cid.setAnotacoes("Incorporado nas For�as Armadas.");
                break;
            case 15:
                cid.setAnotacoes("Reservista das For�as Armadas. N�o se esque�a de realizar o EXAR durante os primeiros 5 anos ap�s seu licenciamento.");
                break;
            case 13:  
            case 16:
            case 17:
                cid.setAnotacoes("Servi�o Militar Alternativo. Caso n�o possua o Certificado correspondente solicite em uma Junta de Servi�o Militar.");
                break;
            default:
                cid.setAnotacoes("Compare�a a uma Junta de Servi�o Militar e regularize sua situa��o.");
                break;
            }
        } else {
            throw new CidadaoNotFoundException();
        }
        return cid;
    }

    // TODO: substituir pela data definida na rotina de Agendamento
    private Date definirRetorno(final Long ra) throws SermilException {
        final List<CidEvento> e = this.eventoDao.findByNamedQuery("Evento.listarPorCodigo", ra, 1);
        if (e == null || e.isEmpty()) {
            throw new SermilException("Cidad�o n�o possui data de alistamento.");
        }
        final Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        cal.setTime(e.get(0).getPk().getData());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.MONTH, dia < 16 ? 7: 6); // 16 a 31 - Jul ou 1 a 15 - Ago
        cal.set(Calendar.YEAR, ano);
        return cal.getTime();
    }

}
