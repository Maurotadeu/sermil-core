package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.EstatArrecadacaoDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.EstatArrecadacao;
import br.mil.eb.sermil.modelo.Jsm;

/** Serviço de arrecadação de taxas e multas pela JSM.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.5
 */
@Named("jsmArrecadacaoServico")
public class JsmArrecadacaoServico {

  protected static final Logger logger = LoggerFactory.getLogger(JsmArrecadacaoServico.class);

  @Inject
  private EstatArrecadacaoDao arrecadacaoDao;

  public JsmArrecadacaoServico() {
    logger.debug("JsmArrecadacaoServico iniciado");
  }

  @Transactional
  public void excluir(final EstatArrecadacao arrecadacao) throws SermilException {
    this.arrecadacaoDao.delete(arrecadacao);
  }

  public List<EstatArrecadacao> listar(final Jsm jsm, final Short ano) throws SermilException {
     if (jsm == null || jsm.getCsmCodigo() == null || jsm.getCodigo() == null || ano == null) {
       throw new CriterioException("Informe a JSM e o ANO da arrecadação.");
     }
     return this.arrecadacaoDao.findByNamedQuery("EstatArrecadacao.listarJsmAno", jsm, ano);
   }

  public EstatArrecadacao recuperar(final Integer codigo) throws SermilException {
    return this.arrecadacaoDao.findById(codigo);
  }

  @Transactional
  public EstatArrecadacao salvar(final List<EstatArrecadacao> lista) throws SermilException {
    if (lista == null || lista.isEmpty()) {
       throw new SermilException("Lista de arrecadações está vazia.");
    }
    List<EstatArrecadacao> listaOld = this.listar(lista.get(0).getJsm(), lista.get(0).getAno());
    if (listaOld == null) {
       listaOld = new ArrayList<>();
    }
    for(EstatArrecadacao arr: lista) {
      if (listaOld.contains(arr)) {
         listaOld.add(listaOld.indexOf(arr), arr);
      } else {
         listaOld.add(arr);
      }
    }
    for(EstatArrecadacao arr: listaOld) {
      this.arrecadacaoDao.save(arr);
    }
    return lista.get(0);
  }

}
