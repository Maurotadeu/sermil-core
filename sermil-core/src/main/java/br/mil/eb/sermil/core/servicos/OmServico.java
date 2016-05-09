package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
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
import br.mil.eb.sermil.tipos.Lista;

/** Serviços de Organização Militar (OM).
 * @author Abreu Lopes
 * @since 3.0 
 * @version 5.4
 */
@Named("omServico")
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
      if (om == null || (om.getCodigo() == null && StringUtils.isBlank(om.getSigla()) && (om.getMunicipio() == null || StringUtils.isBlank(om.getMunicipio().getDescricao())))) {
         throw new CriterioException("Informe um CODOM, uma Sigla ou um Município para executar a pesquisa");
      }
      List<Om> lista = null;
      if (om.getCodigo() != null) {
         logger.debug("OM = {}", om);
         lista = new ArrayList<Om>(1);
         lista.add(this.omDao.findById(om.getCodigo()));
      } else {
         final String sigla = (om.getSigla() != null ? om.getSigla() : "%");
         final String municipio = (om.getMunicipio() != null && om.getMunicipio().getDescricao() != null ? om.getMunicipio().getDescricao() : "%");
         logger.debug("Sigla = {}, Mun = ", municipio);
         lista = this.omDao.findByNamedQuery("Om.listarPorCriterio", sigla, municipio);
      }
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      logger.debug("Lista = {}", lista);
      return lista;
   }

   public Lista[] listarPorRm(final Byte rm) throws SermilException {
     final TypedQuery<Object[]> query = this.omDao.getEntityManager().createNamedQuery("Om.listarPorRm", Object[].class);
     query.setParameter(1, rm);
     return query.getResultList().stream().map(o -> new Lista(((Integer)o[0]).toString(), (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   public Lista[] listarOmMob(final Byte rm) throws SermilException {
     final TypedQuery<Object[]> query = this.omDao.getEntityManager().createNamedQuery("Om.listarMob", Object[].class);
     query.setParameter(1, rm);
     return query.getResultList().stream().map(o -> new Lista((String)o[0], (String)o[1])).collect(Collectors.toList()).toArray(new Lista[0]);
   }

   public List<Om> listarDescricao(final String descricao) throws SermilException {
      return this.omDao.findByNamedQuery("Om.listarPorDescricao", descricao);
   }

   public Om recuperar(final Integer codigo) throws SermilException {
      if (codigo == null) {
         throw new SermilException("Informe o CODOM da OM");
      }
      logger.debug("CODOM = {}", codigo);
      final Om om = this.omDao.findById(codigo);
      if (om.getOmCabecalho() == null) {
         om.setOmCabecalho(new OmCabecalho(om));
      }
      logger.debug("OM = {}", om);
      return om;
   }

   @Transactional
   public String salvar(final Om om) throws SermilException {
      if (om == null) {
         throw new SermilException("Não há dados de OM a serem salvos");
      }
      logger.debug("OM = {}", om);
      final OmCabecalho info = this.recuperar(om.getCodigo()).getOmCabecalho();
      if (info != null && om.getOmCabecalho() == null) {
         om.setOmCabecalho(info);
      } else if (om.getOmCabecalho() == null) {
         om.setOmCabecalho(new OmCabecalho(om));
      }
      this.omDao.save(om);
      return new StringBuilder(om.toString()).append(" : informações de OM salvas").toString();
   }

   @Transactional
   public String salvarInfo(final OmCabecalho info) throws SermilException {
      if (info == null || info.getOm() == null || info.getOm().getCodigo() == null) {
         throw new SermilException("Não há informações de OM a serem salvas");
      }
      logger.debug("INFO = {}", info);
      final Om om = this.omDao.findById(info.getOm().getCodigo());
      logger.debug("OM = {}", om);
      om.setEndereco(info.getOm().getEndereco());
      om.setBairro(info.getOm().getBairro());
      om.setCep(info.getOm().getCep());
      om.setMunicipio(info.getOm().getMunicipio());
      if (om.getOmCabecalho() == null) {
         om.setOmCabecalho(new OmCabecalho(om));
      }
      if (info.getAssDigital() == null && om.getOmCabecalho().getAssDigital() != null) {
         info.setAssDigital(om.getOmCabecalho().getAssDigital());
      }
      om.setOmCabecalho(info);
      return this.salvar(om);
   }

   public Map<String, String> getListaDePadroes() {
      @SuppressWarnings("unchecked")
      final Map<String, String> mapPadroes = new ListOrderedMap();
      final List<Padrao> padroes = this.padraoDao.findByNamedQuery("Padrao.padroesOrdenados");
      final List<String> filtro = new ArrayList<String>(Arrays.asList("A99","B99","C99","E99","G99","M99","Z99","F01","F02","F03"));
      for (Padrao padrao : padroes) {
         if (!filtro.contains(padrao.getCodigo()))
            mapPadroes.put(padrao.getCodigo(), padrao.getCodigo());
      }
      return mapPadroes;
   }

}
