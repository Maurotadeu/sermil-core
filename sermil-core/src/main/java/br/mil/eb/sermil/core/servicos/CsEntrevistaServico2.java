package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsEntrevistaDao2;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsEntrevista2;

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
    // recuperando o cidadao completo do banco
    final Cidadao cidadao = this.cidadaoServico.recuperar(entrevista.getCidadao().getRa());

    // Alterando apenas as informacoes de cidadao que foram alteradas na entrevista
    cidadao.setEndereco(entrevista.getCidadao().getEndereco());
    cidadao.setBairro(entrevista.getCidadao().getBairro());
    cidadao.setCep(entrevista.getCidadao().getCep());
    cidadao.setTelefone(entrevista.getCidadao().getTelefone());
    cidadao.setMunicipioResidencia(entrevista.getCidadao().getMunicipioResidencia());
    cidadao.setEmail(entrevista.getCidadao().getEmail());
    cidadao.setReligiao(entrevista.getCidadao().getReligiao());
    cidadao.setOcupacao(entrevista.getCidadao().getOcupacao());
    cidadao.setEstadoCivil(entrevista.getCidadao().getEstadoCivil());
    cidadao.setEscolaridade(entrevista.getCidadao().getEscolaridade());

    // Não Arrimo valida indicadores
    if ("N".equals(entrevista.getQ11b())) {
      entrevista.setI09b(this.avaliacaoFinal(entrevista));
    }

    // Associa Cidadao
    entrevista.setCidadao(cidadao);
    
    //this.csEntrevistaDao.save(entrevista);
    //this.cidadaoServico.salvar(cidadao, entrevista.getUsuario(), "Alteração Entrevista Seleção.");
    return entrevista;
  }

  /** Se algum indicador 'S' o cidadao se torna Inapto K. */
  private String avaliacaoFinal(final CsEntrevista2 e) {
    return ("S".equals(e.getI01b()) || "S".equals(e.getI02b()) || "S".equals(e.getI03b()) || "S".equals(e.getI04b()) ||
            "S".equals(e.getI05b()) || "S".equals(e.getI06b()) || "S".equals(e.getI07b()) || "S".equals(e.getI08b())) ? "I" : "A";
  }

}
