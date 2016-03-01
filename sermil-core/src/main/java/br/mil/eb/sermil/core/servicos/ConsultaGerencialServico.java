package br.mil.eb.sermil.core.servicos;

import java.text.DateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.tipos.Pesquisa;

/** Serviço de geração de SQL de consulta gerencial.
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.3.1
 */
@Named("consultaGerencialServico")
public class ConsultaGerencialServico {

  protected static final Logger logger = LoggerFactory.getLogger(ConsultaGerencialServico.class);

  private static final String FROM_1 = " FROM cidadao WHERE vinculacao_ano = ";
  
  private static final String FROM_2 = " FROM cidadao c, cid_evento e WHERE c.vinculacao_ano = ";

  @Inject
  private CidadaoDao dao;
  
  public ConsultaGerencialServico() {
    logger.debug("ConsultaGerencialServico iniciado");
  }
  
  /** Gerador de comando SQL da consulta gerencial.
   * @param pesquisa parâmetros da consulta
   * @return comando SQL
   * @throws SermilException erro no processamento
   */
  public String pesquisar(final Pesquisa pesquisa) throws SermilException {
    final StringBuilder resultado = new StringBuilder();
    String sql = "SELECT ";
    String vGrp = "";
    // Grupos de Resumo
    if (!pesquisa.getGrupo1().isEmpty()) {
      sql += "SUBSTR(ssm_dominios.converte('" + pesquisa.getGrupo1() + "'," + pesquisa.getGrupo1() + "),1,15)";
      vGrp += pesquisa.getGrupo1();
      if (!pesquisa.getGrupo2().isEmpty()) {
        sql += "," + "SUBSTR(ssm_dominios.converte('" + pesquisa.getGrupo2() + "'," + pesquisa.getGrupo2() + "),1,15)";
        vGrp += "," + pesquisa.getGrupo2();
        if (!pesquisa.getGrupo3().isEmpty()) {
          sql += "," + "SUBSTR(ssm_dominios.converte('" + pesquisa.getGrupo3() + "'," + pesquisa.getGrupo3() + "),1,15)";
          vGrp += "," + pesquisa.getGrupo3();
        }
      }
      sql += ",COUNT(*)";
    } else {
      throw new SermilException("Escolha um grupo para pesquisar.");
    }
    // Intervalo de Datas e FROM/WHERE
    if (pesquisa.getEvento() == null) {
      sql += FROM_1 + pesquisa.getAno();
    } else {
      if (pesquisa.getDataInicial() == null || pesquisa.getDataFinal() == null) {
        throw new SermilException("Defina o intervalo de datas.");
      } else {
        sql += FROM_2 + pesquisa.getAno() +
               " AND c.ra = e.cidadao_ra AND e.codigo=" + pesquisa.getEvento() +
               " AND e.data BETWEEN TO_DATE('" +
               DateFormat.getDateInstance(DateFormat.MEDIUM).format(pesquisa.getDataInicial()) + "','DD/MM/YYYY') AND TO_DATE('" +
               DateFormat.getDateInstance(DateFormat.MEDIUM).format(pesquisa.getDataFinal()) + "','DD/MM/YYYY')";
      }
    }
    // Restrições de Colunas
    if (!pesquisa.getRestricao1().isEmpty()) {
      sql += " AND " + pesquisa.getRestricao1() + pesquisa.getOperador1() + pesquisa.getValor1();
      if (!pesquisa.getRestricao2().isEmpty()) {
        sql += " AND " + pesquisa.getRestricao2() + pesquisa.getOperador2() + pesquisa.getValor2();
        if (!pesquisa.getRestricao3().isEmpty()) {
          sql += " AND " + pesquisa.getRestricao3() + pesquisa.getOperador3() + pesquisa.getValor3();
          if (!pesquisa.getRestricao4().isEmpty()) {
            sql += " AND " + pesquisa.getRestricao4() + pesquisa.getOperador4() + pesquisa.getValor4();
            if (!pesquisa.getRestricao5().isEmpty()) {
              sql += " AND " + pesquisa.getRestricao5() + pesquisa.getOperador5() + pesquisa.getValor5();
            }
          }
        }
      }
    }
    // Finaliza o comando
    sql += " GROUP BY " + vGrp + " ORDER BY " + vGrp;
    logger.debug("SQL = {}", sql);
    // Consulta
    final List<Object[]> lista = this.dao.findBySQL(sql);
    // Monta resultado    
    resultado.append(pesquisa.getGrupo1()).append(" ");
    if (!pesquisa.getGrupo2().isEmpty() && !pesquisa.getGrupo3().isEmpty()) {
       resultado.append(pesquisa.getGrupo2()).append(" ").append(pesquisa.getGrupo3()).append(" ");
    }
    else if (!pesquisa.getGrupo2().isEmpty()) {
       resultado.append(pesquisa.getGrupo2()).append(" ");
    }
    resultado.append("TOTAL\n");
    resultado.append(this.espacar('=', pesquisa.getGrupo1().length() - 1));
    if (!pesquisa.getGrupo2().isEmpty() && !pesquisa.getGrupo3().isEmpty()) {
      resultado.append(" ")
               .append(this.espacar('=', pesquisa.getGrupo2().length() - 1))
               .append(" ")
               .append(this.espacar('=', pesquisa.getGrupo3().length() - 1));
   }
   else if (!pesquisa.getGrupo2().isEmpty()) {
      resultado.append(" ")
               .append(this.espacar('=', pesquisa.getGrupo2().length() - 1));
   }
   resultado.append(" ").append(this.espacar('=',4)).append("\n");
   for (Object[] info : lista) {
     resultado.append(info[0])
     .append(this.espacar(' ', pesquisa.getGrupo1().length() - ((String)info[0]).length()));
     if (!pesquisa.getGrupo2().isEmpty() && !pesquisa.getGrupo3().isEmpty()) {
       resultado.append(info[1])
       .append(this.espacar(' ', pesquisa.getGrupo2().length() - ((String)info[1]).length()))
       .append(info[2])
       .append(this.espacar(' ', pesquisa.getGrupo3().length() - ((String)info[2]).length()))
       .append(info[3])
       .append("\n");
     }
     else if (!pesquisa.getGrupo2().isEmpty()) {
       resultado.append(info[1])
       .append(this.espacar(' ', pesquisa.getGrupo2().length() - ((String)info[1]).length()))
       .append(info[2])
       .append("\n");
     } else {
       resultado.append(info[1]).append("\n");
     }
   }
   resultado.append("\n\nComando SQL:\n===========\n").append(sql);
   logger.debug("RESULT = {}", resultado);
   return resultado.toString();
  }
  
  /** Insere quantidade de caracteres solicitados.
   * @param c caracter
   * @param espaco quantidade de caracteres
   * @return nova string
   */
  private StringBuilder espacar(char c, int espaco) {
    final StringBuilder sb = new StringBuilder(espaco);
    for (int i=0;i < espaco + 1;i++) {
      sb.append(c);
    }
    return sb;
  }

}
