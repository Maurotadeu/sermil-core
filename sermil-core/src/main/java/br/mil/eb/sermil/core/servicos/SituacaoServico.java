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

/** Verifica��o de situa��o no servi�o militar.
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
      throw new CriterioException("Informe um RA ou CPF v�lido.");
    }
    Cidadao cid = null;
    if (cidadao.getRa() != null) {
      cid = this.servico.recuperar(cidadao.getRa());
    } else {
      cid = this.servico.recuperar(cidadao.getCpf());
    }
    // JSM est� alistando pela Internet?
    CsAgendamento csAgendamento = null;
    String internet = "N";
    if (cid.getJsm() != null && cid.getJsm().getJsmInfo() != null && "S".equalsIgnoreCase(cid.getJsm().getJsmInfo().getInternet())) {
      // Verificar se h� agendamento de CS
      final List<CsAgendamento> lista = this.csAgendamentoDao.findByNamedQuery("CsAgendamento.listarPorRa", cid.getRa());
      if (lista != null && lista.size() > 0) {
        csAgendamento = lista.get(0);
      }
      internet = "S";
    }
    // Definir mensagem de alerta ao cidad�o (campo ANOTACOES usado de maneira temporaria para transportar a mensagem)
    switch (cid.getSituacaoMilitar()) {
    case 1:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no �rg�o de Servi�o Militar (Junta ou Comiss�o de Sele��o).");
      } else {
        if (cid.getJsm().getPk().getCsmCodigo() == 99) {
          cid.setAnotacoes("Cidad�o, no exterior compare�a no Consulado para solicitar adiamento ou o certificado do servi�o militar, estando no Brasil compare�a em uma Junta de Servi�o Militar.");
        } else {
          cid.setAnotacoes("Verifique no in�cio do pr�ximo m�s a sua situa��o em http://www.alistamento.eb.mil.br.");
        }
      }
      break;
    case 2:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Verifique no verso do seu documento de alistamento (CAM) a data de comparecimento no �rg�o de Servi�o Militar (Junta ou Comiss�o de Sele��o).");
      } else {
        if (csAgendamento != null) {
           if(null==cid.getCs())
              throw new CsException();
          final CsEndereco end = cid.getCs().getCsFuncionamentoCollection().stream().findFirst().get().getCsEndereco();
          final String endereco = new StringBuilder(end.getEndereco()).append(" - ").append(end.getBairro()).append(" - ").append(end.getMunicipio()).toString();
          final StringBuilder msg = new StringBuilder("Comparecer na Comiss�o de Sele��o ")
              .append(cid.getCs())
              .append(" na data ")
              .append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(csAgendamento.getDataSelecao()))
              .append(", caso tenha perdido a data de comparecimento, apresentar-se o mais rapidamente poss�vel na Comiss�o de Sele��o.")
              .append("<br/><b>Endere�o: ")
              .append(endereco)
              .append("</b>");
          cid.setAnotacoes(msg.toString());
        } else {
          cid.setAnotacoes("Verifique no in�cio do pr�ximo m�s a sua situa��o em http://www.alistamento.eb.mil.br.");
        }
      }
      break;
    case 3:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Caso n�o possua Certificado de Dispensa de Incorpor��o (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Servi�o Militar.");
      } else {
        if (cid.getJsm().getPk().getCsmCodigo() == 99) {
          cid.setAnotacoes("Cidad�o, no exterior compare�a no Consulado para solicitar seu certificado, estando no Brasil compare�a em uma Junta de Servi�o Militar.");
        } else {
          final StringBuilder msg = new StringBuilder("Caso n�o possua Certificado de Dispensa de Incorpor��o (CDI), comparecer na Junta de Servi�o Militar ")
              .append(cid.getJsm() != null ? cid.getJsm().toString() : " (JSM)")
              .append(" na data ")
              .append(definirRetorno(cid))
              .append(" (final de semana ou feriado, no dia �til seguinte).")
              .append("<br/><b>Endere�o: ")
              .append(cid.getJsm().getEndereco() != null ? cid.getJsm().getEndereco() : " N/D")
              .append("</b>");
          cid.setAnotacoes(msg.toString());
        }
      }
      break;
    case 4:
      cid.setAnotacoes("Verifique no per�odo de 2 a 15 de janeiro a sua situa��o em http://www.alistamento.eb.mil.br.");
      break;
    case 6:
      cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + (cid.getJsm() != null ? cid.getJsm().toString() : "") + ", para solicitar, se necess�rio, a prorroga��o do adiamento do Servi�o Militar Inicial.");
      break;
    case 7:
      cid.setAnotacoes("Comparecer no " + (cid.getOm() != null ? cid.getOm().getDescricao() : "�rg�o de Servi�o Militar") + ", para realizar a Sele��o Complementar. <br>Em caso de falta ser� considerado REFRAT�RIO e ficar� sujeito as penas previstas na Lei de Servi�o Militar.");
      break;
    case 5:  
    case 8:
    case 9:
      if ("N".equals(internet)) {
        cid.setAnotacoes("Caso ainda n�o tenha recebido seu Certificado de Dispensa de Incorpor��o (CDI), verifique no verso do seu documento de alistamento (CAM) a data de comparecimento na Junta de Servi�o Militar.");
      } else {
        cid.setAnotacoes("Comparecer na Junta de Servi�o Militar " + (cid.getJsm() != null ? cid.getJsm().toString() : "") + ", para solicitar o Certificado de Dispensa de Incorpora��o (CDI).");
      }
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
      cid.setAnotacoes("Caso n�o possua Certificado do Servi�o Militar v�lido, compare�a em um �rg�o de Servi�o Militar para regularizar sua situa��o.");
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
