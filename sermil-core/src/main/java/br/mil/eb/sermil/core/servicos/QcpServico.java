package br.mil.eb.sermil.core.servicos;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.QcpDao;
import br.mil.eb.sermil.tipos.Lista;

/** Serviço de informações de QCP. (Tabela QCP)
 * @author Abreu Lopes
 * @since 5.4
 * @version 5.4
 */
@Named("qcpServico")
public class QcpServico {

  protected static final Logger logger = LoggerFactory.getLogger(QcpServico.class);

  @Inject
  private QcpDao qcpDao;

  public QcpServico() {
    logger.debug("QcpServico iniciado");
  }

  public Lista[] listarPorOm(final Integer codom) {
    final TypedQuery<Object[]> query = this.qcpDao.getEntityManager().createNamedQuery("Qcp.listarPorOm2", Object[].class);
    query.setParameter(1, codom);
    return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
  }

}
