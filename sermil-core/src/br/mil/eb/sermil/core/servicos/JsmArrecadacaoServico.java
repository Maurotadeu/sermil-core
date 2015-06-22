package br.mil.eb.sermil.core.servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.EstatArrecadacaoDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.EstatArrecadacao;

/** Serviço de arrecadação de taxas e multas.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: JsmArrecadacaoServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("jsmArrecadacaoServico")
public class JsmArrecadacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(JsmArrecadacaoServico.class);

  @Inject
  private EstatArrecadacaoDao arrecadacaoDao;

  public JsmArrecadacaoServico() {
    logger.debug("JsmArrecadacaoServico iniciado");
  }

  public List<EstatArrecadacao> listar(final EstatArrecadacao arrecadacao) throws SermilException {
    if (arrecadacao == null || arrecadacao.getJsm() == null ||
        arrecadacao.getJsm().getCsmCodigo() == null ||
        arrecadacao.getJsm().getCodigo() == null) {
      throw new CriterioException();
    }
    final Map<String, Object> params = new HashMap<String, Object>();
    String cmd = "EstatArrecadacao.listarPorJsm";
    params.put("jsm", arrecadacao.getJsm());
    if (arrecadacao.getAno() != null) {
      cmd = "EstatArrecadacao.listarPorAno";
      params.put("ano", arrecadacao.getAno());
      if (arrecadacao.getMes() != null) {
        cmd = "EstatArrecadacao.listarPorMes";
        params.put("mes", arrecadacao.getMes());
      }
    }
    return arrecadacaoDao.findByNamedQueryAndNamedParams(cmd, params);
  }

  @Transactional
  public void excluir(final EstatArrecadacao arrecadacao) throws SermilException {
    arrecadacaoDao.delete(arrecadacao);
  }

  public EstatArrecadacao recuperar(final Integer codigo) throws SermilException {
    return arrecadacaoDao.findById(codigo);
  }

  @Transactional
  public void salvar(final EstatArrecadacao arrecadacao) throws SermilException {
    arrecadacaoDao.save(arrecadacao);
  }

}
