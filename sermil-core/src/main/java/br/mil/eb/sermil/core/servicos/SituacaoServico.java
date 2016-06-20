package br.mil.eb.sermil.core.servicos;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CsAgendamentoDao;
import br.mil.eb.sermil.core.exceptions.CidadaoNotFoundException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.CsException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsAgendamento;
import br.mil.eb.sermil.modelo.CsEndereco;

/** Verificação de situação no serviço militar.
 * @author Abreu lopes
 * @since 5.1
 * @version 5.4
 */
@Named("situacaoServico")
public class SituacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(SituacaoServico.class);

  @Inject
  private CidadaoServico servico;

  @Inject
  private CsAgendamentoDao csAgendamentoDao;

  public SituacaoServico() {
    logger.debug("SituacaoServico iniciado.");
  }

  public Cidadao verificar(final Cidadao cidadao) throws CriterioException, CidadaoNotFoundException, CsException {
    if (cidadao == null || (cidadao.getRa() == null && StringUtils.isBlank(cidadao.getCpf()))) {
      throw new CriterioException("Informe um RA ou CPF válido.");
    }
    Cidadao cid = null;
    if (cidadao.getRa() != null) {
      cid = this.servico.recuperar(cidadao.getRa());
    } else {
      cid = this.servico.recuperar(cidadao.getCpf());
    }
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
      if ("N".equals(internet)) {
        cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no Órgão de Serviço Militar (Junta ou Comissão de Seleção).");
      } else {
        if (cid.getJsm().getPk().getCsmCodigo() == 99) {
          cid.setAnotacoes("Cidadão, no exterior compareça no Consulado para solicitar adiamento ou o certificado do serviço militar, estando no Brasil compareça em uma Junta de Serviço Militar.");
        } else {
          cid.setAnotacoes("Verifique no início do próximo mês a sua situação em http://www.alistamento.eb.mil.br.");
        }
      }
      break;
    case 2:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no Órgão de Serviço Militar (Junta ou Comissão de Seleção).");
      } else {
        if (csAgendamento != null) {
           if(null==cid.getCs())
              throw new CsException();
          final CsEndereco end = cid.getCs().getCsFuncionamentoCollection().stream().findFirst().get().getCsEndereco();
          final String endereco = new StringBuilder(end.getEndereco()).append(" - ").append(end.getBairro()).append(" - ").append(end.getMunicipio()).toString();
          final StringBuilder msg = new StringBuilder("Comparecer na Comissão de Seleção ")
              .append(cid.getCs())
              .append(" na data ")
              .append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(csAgendamento.getDataSelecao()))
              .append(", caso tenha perdido a data de comparecimento, apresentar-se o mais rapidamente possível na Comissão de Seleção.")
              .append("<br/><b>Endereço: ")
              .append(endereco)
              .append("</b>");
          cid.setAnotacoes(msg.toString());
        } else {
          cid.setAnotacoes("Verifique no início do próximo mês a sua situação em http://www.alistamento.eb.mil.br.");
        }
      }
      break;
    case 3:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Caso não possua Certificado de Dispensa de Incorporção (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Serviço Militar.");
      } else {
        if (cid.getJsm().getPk().getCsmCodigo() == 99) {
          cid.setAnotacoes("Cidadão, no exterior compareça no Consulado para solicitar seu certificado, estando no Brasil compareça em uma Junta de Serviço Militar.");
        } else {
          final StringBuilder msg = new StringBuilder("Caso não possua Certificado de Dispensa de Incorporção (CDI), comparecer na Junta de Serviço Militar ")
              .append(cid.getJsm() != null ? cid.getJsm().toString() : " (JSM)")
              .append(" na data ")
              .append(definirRetorno(cid))
              .append(" (final de semana ou feriado, no dia útil seguinte).")
              .append("<br/><b>Endereço: ")
              .append(cid.getJsm().getEndereco() != null ? cid.getJsm().getEndereco() : " N/D")
              .append("</b>");
          cid.setAnotacoes(msg.toString());
        }
      }
      break;
    case 4:
      cid.setAnotacoes("Verifique no período de 2 a 15 de janeiro a sua situação em http://www.alistamento.eb.mil.br.");
      break;
    case 6:
      cid.setAnotacoes("Comparecer na Junta de Serviço Militar " + (cid.getJsm() != null ? cid.getJsm().toString() : "") + ", para solicitar, se necessário, a prorrogação do adiamento do Serviço Militar Inicial.");
      break;
    case 7:
      cid.setAnotacoes("Comparecer no " + (cid.getOm() != null ? cid.getOm().getDescricao() : "Órgão de Serviço Militar") + ", para realizar a Seleção Complementar. <br>Em caso de falta será considerado REFRATÁRIO e ficará sujeito as penas previstas na Lei de Serviço Militar.");
      break;
    case 5:  
    case 8:
    case 9:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Caso ainda não tenha recebido seu Certificado de Dispensa de Incorporção (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Serviço Militar.");
      } else {
        cid.setAnotacoes("Comparecer na Junta de Serviço Militar " + (cid.getJsm() != null ? cid.getJsm().toString() : "") + ", para solicitar o Certificado de Dispensa de Incorporação (CDI).");
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
      cid.setAnotacoes("Caso não possua Certificado do Serviço Militar válido, compareça em um órgão de Serviço Militar para regularizar sua situação.");
      break;
    }
    return cid;
  }

  private String definirRetorno(final Cidadao c) {
    try {
      final LocalDate dataAlist = new java.sql.Date(c.getAlistamentoData().getTime()).toLocalDate();
      LocalDate dataRetorno = LocalDate.of(LocalDate.now().getYear(), 7, dataAlist.getDayOfMonth());
      if(dataAlist.getDayOfMonth() > 15) {
        dataRetorno = dataRetorno.plusMonths(1);
      }
      return dataRetorno.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (SermilException e) {
      return "31/08/" + LocalDate.now().getYear();
    }
  }

}
