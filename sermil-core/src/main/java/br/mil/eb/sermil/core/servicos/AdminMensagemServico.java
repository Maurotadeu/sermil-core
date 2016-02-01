package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.PortalMensagemDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.PortalMensagem;
import br.mil.eb.sermil.modelo.PortalMensagemPerfil;

/** Serviços de Mensagens do Portal (Tabela PORTAL_MENSAGEM).
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: AdminMensagemServico.java 2427 2014-05-15 13:23:38Z wlopes $
 */
@Named("adminMensagemServico")
public class AdminMensagemServico {

  protected static final Logger logger = LoggerFactory.getLogger(AdminMensagemServico.class);

  @Inject
  private PortalMensagemDao dao;

  public AdminMensagemServico() {
    logger.debug("AdminMensagemServico iniciado");
  }

  public List<PortalMensagem> listar() throws SermilException {
    final List<PortalMensagem> lista = this.dao.findAll();
    Collections.sort(lista);
    return lista;
  }

  public PortalMensagem recuperar(final Integer codigo) throws SermilException {
    return this.dao.findById(codigo);
  }

  @Transactional
  public void excluir(final PortalMensagem msg) throws SermilException {
    this.dao.delete(msg);
  }

  @Transactional
  public PortalMensagem salvar(final PortalMensagem mensagem, final String[] perfis) throws SermilException {
    final PortalMensagem msg = this.dao.save(mensagem);
    if (perfis != null) {
      final List<PortalMensagemPerfil> lista = new ArrayList<PortalMensagemPerfil>(perfis.length);
      for(String perfil: perfis) {
        lista.add(new PortalMensagemPerfil(msg.getCodigo(),perfil));
      }
      msg.setPortalMensagemPerfilCollection(lista);
    }
    return this.dao.save(msg);
  }

}
