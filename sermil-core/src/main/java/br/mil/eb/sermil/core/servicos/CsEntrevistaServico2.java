package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.CsEntrevistaDao2;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsEntrevista2;

/** Servico de Entrevista de Selecao.
 * @author Anselmo Ribeiro, Abreu Lopes
 * @since 4.4
 * @version 4.5.6
 */
@Named("csEntrevistaServico2")
@RemoteProxy(name = "csEntrevistaServico2")
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

    // salvando cidadao
    //this.cidadaoServico.salvar(cidadao, entrevista.getUsuario(), "Alteração Entrevista Seleção.");

    // preparando e salvando entrevista
    entrevista.setCidadao(cidadao);
    entrevista.setI09b(this.defineAvaliacaoFinal(entrevista));
    return this.csEntrevistaDao.save(entrevista);
  }

  public boolean existeEntrevista(final Long ra) throws SermilException {
    if (ra == null) {
      throw new CriterioException("RA não informado");
    }
    return this.csEntrevistaDao.findById(ra) != null;
  }

  /*
  public void checaCidadao(Long ra) throws CidadaoNotFoundException {
    try {
      this.csEntrevistaDao.findById(ra);
    } catch (Exception e) {
      throw new CidadaoNotFoundException();
    }
  }
  */

  public CsEntrevista2 recuperarEntrevistaNaoArrimo(CsEntrevista2 ent) {
    final CsEntrevista2 entrevista = this.csEntrevistaDao.findById(ent.getCidadao().getRa());
    entrevista.setQ06l(ent.getQ06l());
    entrevista.setQ07b(ent.getQ07b());
    entrevista.setQ07n(ent.getQ07n());
    entrevista.setQ08l(ent.getQ08l());
    entrevista.setQ08d(ent.getQ08d());
    entrevista.setQ09l(ent.getQ09l());
    entrevista.setQ09d(ent.getQ09d());
    entrevista.setQ10b(ent.getQ10b());
    entrevista.setQ10d(ent.getQ10d());
    entrevista.setQ11b(ent.getQ11b());
    entrevista.setPendencia(ent.getPendencia());
    return entrevista;
}

  public CsEntrevista2 recuperarEntrevistaIndicadores(final CsEntrevista2 entrevista) {
    final CsEntrevista2 ent = this.csEntrevistaDao.findById(entrevista.getCidadao().getRa());
    entrevista.setQ25b(ent.getQ25b());
    entrevista.setQ26d(ent.getQ26d());
    entrevista.setQ27d(ent.getQ27d());
    entrevista.setI01b(ent.getI01b());
    entrevista.setI02b(ent.getI02b());
    entrevista.setI03b(ent.getI03b());
    entrevista.setI03d(ent.getI03d());
    entrevista.setI04b(ent.getI04b());
    entrevista.setI05b(ent.getI05b());
    entrevista.setI05d(ent.getI05d());
    entrevista.setI06b(ent.getI06b());
    entrevista.setI07b(ent.getI07b());
    entrevista.setI08b(ent.getI08b());
    entrevista.setI09b(ent.getI09b());
    return entrevista;
  }

  /**
   * Regra de Negocio: se qq indicador 'S' o cidadao se torna Inapto K.
   * @param e
   */
  public String defineAvaliacaoFinal(final CsEntrevista2 e) {
    return ("S".equals(e.getI01b()) || "S".equals(e.getI02b()) || "S".equals(e.getI03b()) || "S".equals(e.getI04b()) || "S".equals(e.getI05b()) ||
            "S".equals(e.getI06b()) || "S".equals(e.getI07b()) || "S".equals(e.getI08b())) ? "N" : "S";
  }

}
