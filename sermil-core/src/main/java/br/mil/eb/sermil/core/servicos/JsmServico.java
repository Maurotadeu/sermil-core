package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.CsException;
import br.mil.eb.sermil.core.exceptions.JsmException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Municipio;

/** Gerenciamento de Junta de Serviço Militar (JSM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.3.2
 */
@Named("jsmServico")
@RemoteProxy(name = "jsmServico")
public class JsmServico {

   protected static final Logger logger = LoggerFactory.getLogger(JsmServico.class);

   @Inject
   private JsmDao jsmDao;

   public JsmServico() {
      logger.debug("JsmServico iniciado");
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md')")
   public List<Jsm> listar(final Jsm jsm) throws SermilException {
      if (jsm == null || (jsm.getCsm() == null && jsm.getMunicipio() == null && jsm.getDescricao() == null)) {
         throw new CriterioException();
      }
      List<Jsm> lista = null;
      if (jsm.getCsm() != null && jsm.getCsm().getCodigo() != null) {
         lista = this.jsmDao.findByNamedQuery("Jsm.listarPorCsm", jsm.getCsm().getCodigo());
      } else if (jsm.getMunicipio() != null && jsm.getMunicipio().getCodigo() != null) {
         lista = this.jsmDao.findByNamedQuery("Jsm.listarPorMunicipio", jsm.getMunicipio().getCodigo());
      } else if (jsm.getDescricao() != null) {
         lista = this.jsmDao.findByNamedQuery("Jsm.listarPorDescricao", jsm.getDescricao());
      }
      if (lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md')")
   public Jsm recuperar(final Byte csmCodigo, final Short codigo) throws SermilException {
      return this.jsmDao.findById(new Jsm.PK(csmCodigo, codigo));
   }

   /** Realiza a conversão explícita dos códigos de CSM/JSM para uso no javascript (DWR).
    * @param csm código da CSM
    * @param jsm código da JSM
    * @return JSM
    * @throws SermilException exceção da aplicação
    */
   @RemoteMethod
   public Jsm recuperarJsm(final String csm, final String jsm) throws SermilException {
      return this.recuperar(Byte.valueOf(csm), Short.valueOf(jsm));
   }

   @RemoteMethod
   public Object[] listarPorCsm(final Byte csm) throws SermilException {
      // final List<Jsm> lista = jsmDao.findByNamedQuery("Jsm.listarPorCsm", csm);
      // return lista.toArray(new Jsm[0]);
      final List<Object[]> lista = this.jsmDao.findBySQL("SELECT codigo, descricao||' ('||codigo||')' FROM jsm WHERE csm_codigo = ? ORDER BY descricao", csm);
      Object[] o = lista.toArray();
      return o;
   }

   public Jsm[] listarPorCsmRa(final Byte csm) throws SermilException {
      final List<Jsm> lista = this.jsmDao.findByNamedQuery("Jsm.listarPorCsm", csm);
      return lista.toArray(new Jsm[0]);
   }

   public List<Jsm> listarPorMunicipio(final Municipio mun) throws SermilException {
      return this.jsmDao.findByNamedQuery("Jsm.listarInternet", mun.getCodigo());
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm')")
   @Transactional
   public Jsm salvar(final Jsm jsm) throws SermilException {
      final Jsm jsmBd = this.recuperar(jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo());
      if (jsm.getJsmInfo() == null && jsmBd != null && jsmBd.getJsmInfo() != null) {
         jsm.setJsmInfo(jsmBd.getJsmInfo());
      }
      jsm.setInfor("S");
      return this.jsmDao.save(jsm);
   }

   /** Verifica se a JSM realiza Alistamento ONLINE (Internet = "S").
    * @param jsm JSM a ser verificada
    * @return JSM
    * @throws JsmException
    */
   public Jsm verificarAOL(final Jsm jsm) throws JsmException {
      final Jsm j = this.jsmDao.findById(jsm.getPk());
      if ("N".equalsIgnoreCase(j.getJsmInfo().getInternet())) {
         throw new JsmException("JSM não realiza alistamento ONLINE.");
      }
      return j;
   }

   public boolean jsmTributariaTemCs(final Jsm jsm) throws CsException, CriterioException, JsmException {
      if (jsm.getTributacao() == null) {
         throw new CriterioException();
      }
      if (jsm.isTributaria() && jsm.getCs() == null) {
         throw new CsException();
      }
      return true;
   }

}
