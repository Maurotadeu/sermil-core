package br.mil.eb.sermil.core.servicos;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.tipos.Lista;

/** Serviços de dados para listas HTML.
 * @author Abreu Lopes
 * @since 5.3.2
 * @version 5.4
 */
@Named("listasServico")
@RemoteProxy(name = "listasServico")
public class ListasServico {

   protected static final Logger logger = LoggerFactory.getLogger(ListasServico.class);
   
   @Inject
   private TaxaMultaServico taxaMultaServico;
   
   @Inject
   private JsmDao jsmDao;

   public ListasServico() {
      logger.debug("ListasServico iniciado");
   }

   @RemoteMethod
   public Lista[] listarArtigo() throws SermilException {
     return this.taxaMultaServico.listarArtigo();
     //return this.execute("SELECT DISTINCT TO_CHAR(artigo), TO_CHAR(artigo) FROM taxa_multa ORDER BY artigo");
   }

   @RemoteMethod
   public Lista[] listarPorArtigo(Short art) throws SermilException {
     return this.execute("SELECT DISTINCT TO_CHAR(numero), TO_CHAR(numero)||' - '||descricao FROM taxa_multa WHERE artigo = ? ORDER BY artigo", art);
   }

   @RemoteMethod
   public Lista[] listarCsPorRm(final Byte rm) throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), nome FROM cs WHERE rm_codigo = ? ORDER BY nome", rm);
   }

   @RemoteMethod
   public Lista[] listarDelPorCsm(final Byte csm) throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), TO_CHAR(codigo) FROM delegacia WHERE csm_codigo = ? ORDER BY codigo", csm);
   }

   @RemoteMethod
   public Lista[] listarEmpresa() throws SermilException {
     return this.execute("SELECT codigo, descricao FROM empresa ORDER BY descricao");
   }

   @RemoteMethod
   public Lista[] listarHabilitacao() throws SermilException {
     return this.execute("SELECT codigo, descricao FROM habilitacao ORDER BY descricao");
   }

   @RemoteMethod
   public Jsm recuperarJsm(final String csm, final String jsm) throws SermilException {
     return this.jsmDao.findById(new Jsm.PK(Byte.valueOf(csm), Short.valueOf(jsm)));
   }

   @RemoteMethod
   public Lista[] listarJsmPorCsm(final Byte csm) throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), descricao||' ('||codigo||')' FROM jsm WHERE csm_codigo = ? ORDER BY descricao", csm);
   }

   @RemoteMethod
   public Lista[] listarCsmDotJsm(final Integer mun) throws SermilException {
     return this.execute("SELECT TO_CHAR(j.csm_codigo)||'.'||TO_CHAR(j.codigo), j.descricao FROM jsm j JOIN jsm_info i ON j.csm_codigo = i.csm_codigo AND j.codigo = i.jsm_codigo WHERE j.municipio_codigo = ? AND i.internet = 'S' ORDER BY j.descricao", mun);
   }
   
   @RemoteMethod
   public Lista[] listarJsmPorMun(final Integer mun) throws SermilException {
     return this.execute("SELECT TO_CHAR(j.codigo), j.descricao||' ('||j.codigo||')' FROM jsm j JOIN jsm_info i ON j.csm_codigo = i.csm_codigo AND j.codigo = i.jsm_codigo WHERE j.municipio_codigo = ? AND i.internet = 'S' ORDER BY j.descricao", mun);
   }

   @RemoteMethod
   public Lista[] listarMunPorUf(final String uf) throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), descricao FROM municipio WHERE uf_sigla = ? ORDER BY descricao", uf);
   }

   @RemoteMethod
   public Lista[] listarOcupacao() throws SermilException {
     return this.execute("SELECT codigo, descricao FROM ocupacao ORDER BY descricao");
   }

   @RemoteMethod
   public Lista[] listarOmPorRm(final Byte rm) throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), sigla FROM om WHERE rm_codigo = ? ORDER BY sigla", rm);
   }

   @RemoteMethod
   public Lista[] listarPais() throws SermilException {
     return this.execute("SELECT TO_CHAR(codigo), descricao FROM pais ORDER BY descricao");
   }
   
   @RemoteMethod
   public Lista[] listarQcpPorOm(final Integer om) throws SermilException {
     return this.execute("SELECT fracao_id, cargo_descricao FROM qcp WHERE om_codigo = ? ORDER BY fracao_id", om);
   }

   @RemoteMethod
   public Lista[] listarQm() throws SermilException {
     return this.execute("SELECT codigo, descricao FROM qm ORDER BY codigo");
   }

   private Lista[] execute(String sql, Object... params) {
     return this.jsmDao.findBySQL(sql, params).stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
   }
}
