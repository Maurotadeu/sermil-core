package br.mil.eb.sermil.core.servicos;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsEntrevistaDao2;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsEntrevista2;
import br.mil.eb.sermil.modelo.Municipio;
import br.mil.eb.sermil.modelo.Pais;
import br.mil.eb.sermil.tipos.TipoDispensa;

/** Servico de Entrevista de Selecao.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 4.4
 * @version 4.5.6
 */
@Named("csEntrevistaServico2")
public class CsEntrevistaServico2 {

  protected static final Logger logger = LoggerFactory.getLogger(CsEntrevistaServico2.class);

  @Inject
  CidadaoServico cidadaoServico;

  @Inject
  CsEntrevistaDao2 csEntrevistaDao;

  public CsEntrevista2 recuperar(final Long ra) throws SermilException {
    CsEntrevista2 entrevista = this.csEntrevistaDao.findById(ra);
    if (entrevista == null) {
      entrevista = new CsEntrevista2();
      Cidadao cidadao = this.cidadaoServico.recuperar(ra);
      entrevista.setCidadao(cidadao);
    }
    return entrevista;
  }

  @Transactional
  public CsEntrevista2 salvar(final CsEntrevista2 entrevista) throws SermilException {
    if (entrevista == null || entrevista.getCidadao() == null || entrevista.getCidadao().getRa() == null) {
      throw new SermilException("Não é possível salvar entrevista sem informações ou sem cidadão.");
    }
    // Recupera informações antigas do cidadao no banco
    final Cidadao cidadao = this.cidadaoServico.recuperar(entrevista.getCidadao().getRa());
    final Municipio mun = this.csEntrevistaDao.getEntityManager().find(Municipio.class, entrevista.getCidadao().getMunicipioResidencia().getCodigo());
    final Pais pais = this.csEntrevistaDao.getEntityManager().find(Pais.class, entrevista.getCidadao().getPaisResidencia().getCodigo());
        
    // Alterando apenas as informações do cidadao que foram alteradas na entrevista
    cidadao.setEndereco(entrevista.getCidadao().getEndereco());
    cidadao.setBairro(entrevista.getCidadao().getBairro());
    cidadao.setCep(entrevista.getCidadao().getCep());
    cidadao.setTelefone(entrevista.getCidadao().getTelefone());
    cidadao.setMunicipioResidencia(mun);
    cidadao.setPaisResidencia(pais);
    cidadao.setEmail(entrevista.getCidadao().getEmail());
    cidadao.setReligiao(entrevista.getCidadao().getReligiao());
    cidadao.setEstadoCivil(entrevista.getCidadao().getEstadoCivil());
    cidadao.setEscolaridade(entrevista.getCidadao().getEscolaridade());
    //cidadao.setOcupacao(entrevista.getCidadao().getOcupacao());
    cidadao.setPadraoPq1Codigo(entrevista.getCidadao().getPadraoPq1Codigo());
    cidadao.setPadraoPq2Codigo(entrevista.getCidadao().getPadraoPq2Codigo());
    cidadao.setSabeNadar(entrevista.getCidadao().getSabeNadar());
    cidadao.setDesejaServir(entrevista.getCidadao().getDesejaServir());
    cidadao.setExpressaoOral(entrevista.getCidadao().getExpressaoOral());
    cidadao.setCseIndicacao(entrevista.getCidadao().getCseIndicacao());
    // Arrimo de Família
    if ("S".equals(entrevista.getQ11b())) {
      cidadao.setDispensa(TipoDispensa.ARRIMO.getCodigo());
    } else if ("N".equals(entrevista.getQ11b())) {
      // Cidadão não arrimo são avaliados os indicadores de seleção
      entrevista.setI09b(this.avaliacaoFinal(entrevista));
      if (cidadao.getDispensa().equals(TipoDispensa.ARRIMO.getCodigo())) {
        cidadao.setDispensa(TipoDispensa.SEM_DISPENSA.getCodigo());
      }
    }
    // Problema Social
    if ("S".equals(entrevista.getQ25b())) {
      cidadao.setDispensa(TipoDispensa.PROB_SOCIAL.getCodigo());
    } else if ("N".equals(entrevista.getQ25b()) && cidadao.getDispensa().equals(TipoDispensa.PROB_SOCIAL.getCodigo())) {
      cidadao.setDispensa(TipoDispensa.SEM_DISPENSA.getCodigo());
    }
    
    // Associa Cidadao e salva Entrevista
    final Date hoje = new Date();
    cidadao.addCidAuditoria(new CidAuditoria(cidadao.getRa(), hoje, "Entrevista CS", entrevista.getUsuario().getAcessoIp(), entrevista.getUsuario().getCpf()));
    entrevista.setCidadao(cidadao);
    entrevista.setAtualizacaoData(hoje);
    return this.csEntrevistaDao.save(entrevista);
  }

  /** Se algum Indicador de Seleção for SIM (S) o cidadao se torna Inapto K (I), senão será APTO (A). */
  private String avaliacaoFinal(final CsEntrevista2 e) {
    return ("S".equals(e.getI01b()) || "S".equals(e.getI02b()) || "S".equals(e.getI03b()) || "S".equals(e.getI04b()) ||
            "S".equals(e.getI05b()) || "S".equals(e.getI06b()) || "S".equals(e.getI07b()) || "S".equals(e.getI08b())) ? "I" : "A";
  }

}
