package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.QcpDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Qcp;

/** Serviços da Quadro de Cargos de Pessoal (QCP).
 * @author Abreu Lopes, Gardino
 * @since 3.5
 * @version $Id: OmQcpServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("omQcpServico")
@RemoteProxy(name="qcpServico")
public class OmQcpServico {

  protected static final Logger logger = LoggerFactory.getLogger(OmQcpServico.class);

  @Inject
  private CidadaoDao cidadaoDao;
  
  @Inject
  private QcpDao qcpDao;
  
  public OmQcpServico(){
    logger.debug("OmQcpServico iniciado");
  }

  /** Lista o QC por OM, onde o parâmetro "tudo" indica se deve ser
   * retornadas as linhas do QCP com FRACAO_TIPO = 1.
   * @param omCodigo CODOM da OM
   * @param tudo true = FRACAO_TIPO 1 e 2, false FRACAO_TIPO = 2
   * @return QC de uma OM.
   * @throws SermilException Erro na consulta.
   */
  @RemoteMethod
  public List<Qcp> listarQcp(final Integer omCodigo, final boolean tudo) throws SermilException {
	  return this.qcpDao.findByNamedQuery(tudo ? "Qcp.listarPorOmTudo" : "Qcp.listarPorOm", omCodigo);
  }

  /** Lista os integrantes de uma fração da OM.
   * @param omCodigo CODOM da OM
   * @param fracaoId ID da Fração da OM
   * @return Lista de cidadãos na Fração
   * @throws SermilException Erro na aplicação
   */
  public List<Cidadao> listarFracao(final Integer omCodigo, final String fracaoId) throws SermilException {
    return this.cidadaoDao.findByNamedQuery("Cidadao.listarPorFracao", omCodigo, fracaoId);
  }

}
