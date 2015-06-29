package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/** Gerenciamento de Apresentação de Reservista (EXAR).
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id$
 */
@Named("exarServico")
public class ExarServico {

  protected static final Logger logger = LoggerFactory.getLogger(ExarServico.class);

  @Inject
  private CidadaoServico cidadaoServico;
  
  public ExarServico() {
    logger.debug("ExarServico iniciado");
  }

  public String gerarAutenticador(final Long ra, final Byte nrApres) throws SermilException {
    if (ra == null || nrApres == null) {
      throw new SermilException("ERRO: informe o RA e o número da apresentação.");
    }
    final Cidadao cid = this.cidadaoServico.recuperar(ra);
    CidExar apres = null;
    for(CidExar e: cid.getCidExarCollection()){
      if (e.getPk().getApresentacaoQtd() == nrApres) {
        apres = e;
      }
    }
    if (apres == null) {
      throw new SermilException("Apresentação não está cadastrada.");
    }
    byte[] b = (cid.getNome() + cid.getMae() + apres.getApresentacaoData()).getBytes();
    StringBuilder sb = new StringBuilder();
    DigestUtils.appendMd5DigestAsHex(b, sb);
    return sb.toString();
  }

  public boolean isReciboExarValido(final Long ra, final Byte nrApres, final String codigo) throws SermilException {
    return this.gerarAutenticador(ra, nrApres).equals(codigo);
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
  @Transactional
  public Cidadao adicionarApresentacao(final Cidadao cidadao, final CidExar apresentacao, final Usuario usr) throws SermilException {
      final Cidadao cid = this.cidadaoServico.recuperar(cidadao.getRa());
      if (cid.getSituacaoMilitar() != Cidadao.SITUACAO_MILITAR_LICENCIADO) {
        throw new SermilException("ERRO: Para cadastrar uma apresentação o cidadão deve estar na situação LICENCIADO.");
     }
     final List<CidExar> lista = new ArrayList<CidExar>(apresentacao.getPk().getApresentacaoQtd());
     for (int i = 1; i <= apresentacao.getPk().getApresentacaoQtd(); i++) {
        final CidExar apr = new CidExar();
        apr.getPk().setApresentacaoQtd((byte) i);
        apr.getPk().setCidadaoRa(apresentacao.getPk().getCidadaoRa());
        apr.setApresentacaoData(apresentacao.getApresentacaoData());
        apr.setApresentacaoForma(apresentacao.getApresentacaoTipo());
        apr.setApresentacaoTipo(apresentacao.getApresentacaoTipo());
        apr.setMunicipio(apresentacao.getMunicipio());
        apr.setOm(apresentacao.getOm());
        apr.setPais(apresentacao.getPais());
        apr.setIp(apresentacao.getIp());
        lista.add(apr);
     }
     if (cid.getCidExarCollection().size() > 0) {
        for (int i = 0; i < cid.getCidExarCollection().size(); i++) {
           lista.remove(cid.getCidExarCollection().get(i));
        }
     }
     for (CidExar ce : lista) {
        cid.addCidExar(ce);
     }
     return this.cidadaoServico.salvar(cid, usr, new StringBuilder("APRESENTAÇÃO: ").append(apresentacao).toString());
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','del','jsm','om','mob')")
  @Transactional
  public Cidadao excluirApresentacao(final Cidadao cidadao, final CidExar apresentacao, final Usuario usr) throws SermilException {
      final Cidadao cid = this.cidadaoServico.recuperar(cidadao.getRa());
      cid.getCidExarCollection().remove(apresentacao);
      return this.cidadaoServico.salvar(cid, usr, new StringBuilder("APRESENTAÇÃO EXCLUÍDA: ").append(apresentacao).toString());
  }
  
}
