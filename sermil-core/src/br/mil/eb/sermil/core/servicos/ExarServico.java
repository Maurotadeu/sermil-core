package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.Cidadao;

/** Serviço do EXAR.
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id: ExarServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("exarServico")
public class ExarServico {

  protected static final Logger logger = LoggerFactory.getLogger(ExarServico.class);

  @Inject
  private CidadaoDao dao;
  
  public ExarServico() {
    logger.debug("ExarServico iniciado");
  }

  public String gerarAutenticador(final Long ra, final Byte nrApres) throws SermilException {
    if (ra == null || nrApres == null) {
      throw new SermilException("ERRO: informe o RA e o número da apresentação.");
    }
    final Cidadao cid = this.dao.findById(ra);
    if (cid == null) {
      throw new SermilException("Cidadão não existe.");
    }
    CidExar apres = null;
    for(CidExar e: cid.getCidExarCollection()){
      if (e.getPk().getApresentacaoQtd() == nrApres) {
        apres = e;
      }
    }
    if (apres == null) {
      throw new SermilException("Apresentação não está cadastrada.");
    }
    byte[] b = (cid.getNome() + cid.getMae() + apres.getApresentacaoData()).getBytes();
    StringBuilder sb = new StringBuilder();
    DigestUtils.appendMd5DigestAsHex(b, sb);
    return sb.toString();
  }

  public boolean isReciboExarValido(final Long ra, final Byte nrApres, final String codigo) throws SermilException {
    return this.gerarAutenticador(ra, nrApres).equals(codigo);
  }

}

