package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.dao.PreAlistamentoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;

@Named("alistamentoServico")
public class AlistamentoServico {

  protected static final Logger logger = LoggerFactory.getLogger(AlistamentoServico.class);
  
  @Inject
  private CidadaoDao cidadaoDao;
  
  @SuppressWarnings("unused")
  @Inject
  private PreAlistamentoDao preAlistamentoDao;

  private static final String SQL_ANO = "select m.uf_sigla, count(*) from pre_alistamento p join municipio m on p.municipio_residencia_codigo = m.codigo where extract(year from protocolo_data) = ?1 group by m.uf_sigla order by 1";

  private static final String SQL_UF = "select m.descricao, count(*) from pre_alistamento p join municipio m on p.municipio_residencia_codigo = m.codigo where extract(year from protocolo_data) = ?1 and m.uf_sigla = ?2 group by m.descricao order by 1";
  
  public AlistamentoServico() {
    logger.debug("AlistamentoServico iniciado");
  }

  public List<Object[]> listarAno(final Integer ano) throws SermilException {
     return this.cidadaoDao.findBySQL(SQL_ANO, ano);
   }

  public List<Object[]> listarUf(final Integer ano, final String uf) throws SermilException {
     return this.cidadaoDao.findBySQL(SQL_UF, ano, uf);
   }
  
}
