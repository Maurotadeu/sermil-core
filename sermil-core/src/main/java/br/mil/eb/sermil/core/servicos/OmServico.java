package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.map.ListOrderedMap;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.OmDao;
import br.mil.eb.sermil.core.dao.PadraoDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Om;
import br.mil.eb.sermil.modelo.OmCabecalho;
import br.mil.eb.sermil.modelo.Padrao;

/**
 * Serviços de Organização Militar (OM).
 * 
 * @author Abreu Lopes
 * @since 3.0 
 * @version $Id$
 */
@Named("omServico")
@RemoteProxy(name = "omServico")
public class OmServico {

   protected static final Logger logger = LoggerFactory.getLogger(OmServico.class);

   @Inject
   private OmDao omDao;

   @Inject
   private PadraoDao padraoDao;

   public OmServico() {
      logger.debug("OmServico iniciado");
   }

   public List<Om> listar(final Om om) throws SermilException {
      if (om == null || (om.getCodigo() == null && om.getSigla() == null && (om.getMunicipio() == null || om.getMunicipio().getDescricao() == null))) {
         throw new CriterioException("Informe o CODOM, Sigla ou Município para pesquisar");
      }
      List<Om> lista = null;
      if (om.getCodigo() != null) {
         lista = new ArrayList<Om>(1);
         lista.add(this.omDao.findById(om.getCodigo()));
      } else {
         final String sigla = (om.getSigla() != null ? om.getSigla() : "%");
         final String municipio = (om.getMunicipio() != null && om.getMunicipio().getDescricao() != null ? om.getMunicipio().getDescricao() : "%");
         lista = this.omDao.findByNamedQuery("Om.listarPorCriterio", sigla, municipio);
      }
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   @RemoteMethod
   public Om[] listarPorRm(final Byte rm) throws SermilException {
      return this.omDao.findByNamedQuery("Om.listarPorRm", rm).toArray(new Om[0]);
   }

   @RemoteMethod
   public Om[] listarOmMob(final Byte rm) throws SermilException {
      return this.omDao.findByNamedQuery("Om.listarOmMob", rm).toArray(new Om[0]);
   }

   public List<Om> listarDescricao(final String descricao) throws SermilException {
      return this.omDao.findByNamedQuery("Om.listarPorDescricao", descricao);
   }

   public Om recuperar(final Integer codigo) throws SermilException {
      return this.omDao.findById(codigo);
   }

   @Transactional
   public Om salvar(final Om om) throws SermilException {
      return this.omDao.save(om);
   }

   @Transactional
   public OmCabecalho salvarInfo(final OmCabecalho cab) throws SermilException {
      final Om om = this.omDao.findById(cab.getOm().getCodigo());
      om.setEndereco(cab.getOm().getEndereco());
      om.setBairro(cab.getOm().getBairro());
      om.setCep(cab.getOm().getCep());
      om.setMunicipio(cab.getOm().getMunicipio());
      if (cab.getAssDigital() == null && om.getOmCabecalho().getAssDigital() != null) {
         cab.setAssDigital(om.getOmCabecalho().getAssDigital());
      }
      om.setOmCabecalho(cab);
      cab.setOm(om);
      return this.salvar(om).getOmCabecalho();
   }

   public Map<String, String> getListaDePadroes() {
      @SuppressWarnings("unchecked")
      Map<String, String> mapPadroes = new ListOrderedMap();
      List<Padrao> padroes = padraoDao.findByNamedQuery("Padrao.padroesOrdenados");
      List<String> filtro = new ArrayList<String>();
      filtro.addAll(Arrays.asList("A99","B99","C99","E99","G99","M99","Z99","F01","F02","F03"));
      for (Padrao padrao : padroes) {
         if (!filtro.contains(padrao.getCodigo()))
            mapPadroes.put(padrao.getCodigo(), padrao.getCodigo());
      }
      return mapPadroes;
   }

}
