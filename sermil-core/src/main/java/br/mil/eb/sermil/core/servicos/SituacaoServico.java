package br.mil.eb.sermil.core.servicos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidEventoDao;
import br.mil.eb.sermil.core.dao.CsAgendamentoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsAgendamento;

/** Verificação de situação no serviço militar.
 * @author Abreu lopes
 * @since 5.1
 * @version 5.3.2
 */
@Named("situacaoServico")
public class SituacaoServico {

    protected static final Logger logger = LoggerFactory.getLogger(SituacaoServico.class);

    @Inject
    private CidadaoServico servico;

    @Inject
    private CidEventoDao eventoDao;

    @Inject
    private CsAgendamentoDao csAgendamentoDao;
    
    public SituacaoServico() {
        logger.debug("SituacaoServico iniciado.");
    }

    public Cidadao verificar(final Cidadao cidadao) throws SermilException {
        if (cidadao == null || (cidadao.getRa() == null && StringUtils.isBlank(cidadao.getCpf()))) {
            throw new CriterioException("Informe um RA ou CPF válido.");
        }
        Cidadao cid = null;
        if (cidadao.getRa() != null) {
            cid = this.servico.recuperar(cidadao.getRa());
        } else {
            if (cidadao.getCpf() != null) {
                cid = this.servico.recuperar(cidadao.getCpf());
            } else {
              throw new SermilException("Foi informado um RA ou CPF válido? Verifique e consulte novamente.");
            }
        }
        if (cid != null) {
            // JSM está alistando pela Internet?
            CsAgendamento csAgendamento = null;
            String internet = "N";
            if (cid.getJsm() != null && cid.getJsm().getJsmInfo() != null && "S".equalsIgnoreCase(cid.getJsm().getJsmInfo().getInternet())) {
              // Verificar se há agendamento de CS
              final List<CsAgendamento> lista = this.csAgendamentoDao.findByNamedQuery("CsAgendamento.listarPorRa", cid.getRa());
              if (lista != null && lista.size() > 0) {
                 csAgendamento = lista.get(0);
              }
              internet = "S";
            }
            // Definir mensagem de alerta ao cidadão (campo ANOTACOES usado de maneira temporaria para transportar a mensagem)
            switch (cid.getSituacaoMilitar()) {
            case 1:
              if (Calendar.getInstance().get(Calendar.MONTH) > 5) {
                cid.setAnotacoes("Verifique a partir do próximo ANO a data de comparecimento no Órgão de Serviço Militar.");
              } else {
                if ("N".equals(internet)) {
                  cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no Órgão de Serviço Militar (Junta ou Comissão de Seleção).");
                } else {
                  cid.setAnotacoes("Verifique no início do próximo mês a sua situação em http://www.alistamento.eb.mil.br.");
                }
              }
              break;
            case 2:
              if (csAgendamento != null) {
                final StringBuilder msg = new StringBuilder("Comparecer na Comissão de Seleção ")
                                                           .append(cid.getCs())
                                                           .append(" na data ")
                                                           .append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(csAgendamento.getDataSelecao()))
                                                           .append(", caso tenha perdido a data de comparecimento, apresentar-se o mais rapidamente possível na Comissão de Seleção.");
                cid.setAnotacoes(msg.toString());
              } else {
                cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no Órgão de Serviço Militar (Junta ou Comissão de Seleção).");
              }
              break;
            case 3:
              if ("N".equals(internet)) {
                cid.setAnotacoes("Caso ainda não tenha recebido seu Certificado de Dispensa de Incorporção (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Serviço Militar.");
              } else {
                cid.setAnotacoes("Comparecer na Junta de Serviço Militar " + cid.getJsm() + ", em " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(definirRetorno(cid.getRa()))  + " (final de semana ou feriado, no dia útil seguinte).");
              }
              break;
            case 4:
                cid.setAnotacoes("Verifique no período de 2 a 10 de janeiro novamente a sua situação no Serviço Militar.");
                break;
            case 6:
                cid.setAnotacoes("Comparecer na Junta de Serviço Militar " + cid.getJsm() + ", para solicitar, se necessário, a prorrogação do adiamento do Serviço Militar Inicial.");
                break;
            case 7:
                cid.setAnotacoes("Comparecer no " + cid.getOm().getDescricao() + ", na data agendada, para realizar a Seleção Complementar. <br>Em caso de falta será considerado REFRATÁRIO e ficará sujeito as penas previstas na Lei de Serviço Militar.");
                break;
            case 5:  
            case 8:
            case 9:
              if ("N".equals(internet)) {
                cid.setAnotacoes("Caso ainda não tenha recebido seu Certificado de Dispensa de Incorporção (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Serviço Militar.");
              } else {
                cid.setAnotacoes("Comparecer na Junta de Serviço Militar " + cid.getJsm() + ", na data agendada, para solicitar o Certificado de Dispensa de Incorporação (CDI).");
              }
              break;
            case 12:
                cid.setAnotacoes("Incorporado nas Forças Armadas.");
                break;
            case 15:
                cid.setAnotacoes("Reservista das Forças Armadas. Não se esqueça de realizar o EXAR durante os primeiros 5 anos após seu licenciamento.");
                break;
            case 13:  
            case 16:
            case 17:
                cid.setAnotacoes("Serviço Militar Alternativo. Caso não possua o Certificado correspondente solicite em uma Junta de Serviço Militar.");
                break;
            default:
                cid.setAnotacoes("Compareça a uma Junta de Serviço Militar e regularize sua situação.");
                break;
            }
        } else {
            throw new CidadaoNotFoundException();
        }
        return cid;
    }

    // TODO: Verificar Agendamento para Dispensados de Seleção
    private Date definirRetorno(final Long ra) throws SermilException {
        final List<CidEvento> e = this.eventoDao.findByNamedQuery("Evento.listarPorCodigo", ra, 1);
        if (e == null || e.isEmpty()) {
            throw new SermilException("Cidadão não possui data de alistamento.");
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
