package br.mil.eb.sermil.core.servicos;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.JsmException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.JsmInfo;
import br.mil.eb.sermil.tipos.Lista;

/** Gerenciamento de Junta de Servi�o Militar (JSM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4.5
 */
@Named("jsmServico")
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

   public Lista[] listarPorCsm(final Byte csm) throws SermilException {
     final TypedQuery<Object[]> query = this.jsmDao.getEntityManager().createNamedQuery("Jsm.listarPorCsm2", Object[].class);
     query.setParameter(1, csm);
     return query.getResultList().stream().map(o -> new Lista(((Short)o[0]).toString(), (String)o[1] + "(" + ((Short)o[0]).toString() + ")")).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   public Lista[] listarCsmDotJsm(final Integer mun) throws SermilException {
     final TypedQuery<Object[]> query = this.jsmDao.getEntityManager().createNamedQuery("Jsm.listarPorMun", Object[].class);
     query.setParameter(1, mun);
     return query.getResultList().stream().map(o -> new Lista(((Byte)o[0]).toString() + "." + ((Short)o[1]).toString(), (String)o[2])).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   public Lista[] listarPorMun(final Integer mun) throws SermilException {
     final TypedQuery<Object[]> query = this.jsmDao.getEntityManager().createNamedQuery("Jsm.listarPorMun", Object[].class);
     query.setParameter(1, mun);
     return query.getResultList().stream().map(o -> new Lista(((Short)o[1]).toString(), (String)o[2] + "(" + ((Short)o[1]).toString() + ")")).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob','md')")
   public Jsm recuperar(final Byte csmCodigo, final Short codigo) throws SermilException {
      return this.jsmDao.findById(new Jsm.PK(csmCodigo, codigo));
   }

   @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm')")
   @Transactional
   public Jsm salvar(final Jsm jsm) throws SermilException {
      if (jsm.getCs() != null && jsm.getCs().getCodigo() == null) {
        jsm.setCs(null);
      }
      final Jsm jsmBd = this.recuperar(jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo());
      if (jsm.getJsmInfo() == null && jsmBd != null && jsmBd.getJsmInfo() != null) {
         jsm.setJsmInfo(jsmBd.getJsmInfo());
      }
      if (jsm.getJsmInfo() == null) {
        jsm.setJsmInfo(new JsmInfo());
        jsm.getJsmInfo().getPk().setCsmCodigo(jsm.getPk().getCsmCodigo());
        jsm.getJsmInfo().getPk().setJsmCodigo(jsm.getPk().getCodigo());
        jsm.getJsmInfo().setInternet("N");
      }
      if(jsm.getJsmInfo().getInternet() == null){
        jsm.getJsmInfo().setInternet("N");
      }
      if(jsm.getJsmInfo().getJsm() == null){
        jsm.getJsmInfo().setJsm(jsmBd);
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
         throw new JsmException("JSM n�o realiza alistamento ONLINE.");
      }
      return j;
   }

}
