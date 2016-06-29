package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.Constantes;
import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.UsuarioDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.Randomize;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.modelo.UsuarioPerfil;

/** Gerenciamento de usuário da aplicação.
 * @author Abreu Lopes, Anselmo Ribeiro
 * @since 3.0
 * @version 5.4.3
 */
@Named("usuarioServico")
public class UsuarioServico {

  protected static final Logger logger = LoggerFactory.getLogger(UsuarioServico.class);

  @Inject
  private UsuarioDao usuarioDao;

  @Inject
  private CidAuditoriaDao cidAuditoriaDao;

  @Inject
  private EmailServico emailServico;

  public UsuarioServico() {
    logger.debug("UsuarioServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public void alterarUsuarios(final List<Usuario> listaUsuarios, final String[] listaPerfis, final String ativo) throws SermilException {
    for (Usuario usr : listaUsuarios) {
      String[] perfisUsr = {};
      if (listaPerfis.length > 0) {
        perfisUsr = listaPerfis;
      } else {
        final List<String> ps = new ArrayList<String>();
        for (int i = 0; i < usr.getUsuarioPerfilCollection().size(); i++) {
          String p = usr.getUsuarioPerfilCollection().get(i).toString();
          ps.add(p.substring(p.length() - 3, p.length()));
        }
        perfisUsr = (String[]) ps.toArray(new String[ps.size()]);
      }
      usr.setAtivo(ativo);
      try {
        this.salvar(usr, perfisUsr);
      } catch (Exception e) {
        throw new SermilException("Não foi possível salvar informações referente ao CPF = " + usr.getCpf().toString());
      }
    }
  }

  @Transactional
  public String alterarSenha(final String cpf, final String senhaAtual, final String senhaNova, final String senhaNova2) throws SermilException {
    final Usuario usuario = (Usuario) this.usuarioDao.findById(cpf);
    if (StringUtils.isBlank(senhaAtual) || !new BCryptPasswordEncoder().matches(senhaAtual, usuario.getSenha())) {
      throw new SermilException("Senha atual inválida.");
    }
    if (new BCryptPasswordEncoder().matches(senhaNova, usuario.getSenha())) {
      throw new SermilException("Nova Senha não pode ser igual a senha atual.");
    }
    usuario.setSenha(this.validarSenha(senhaAtual, senhaNova, senhaNova2));
    this.usuarioDao.save(usuario);
    this.emailServico.confirmarAlteracaoUsuario(usuario, "emailSenha.vm", "******", 2);
    return "Senha alterada com sucesso.";
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr')")
  @Transactional
  public String bloquear(final String cpf) throws SermilException {
    final Usuario usuario = this.usuarioDao.findById(cpf);
    usuario.setAtivo("N");
    this.usuarioDao.save(usuario);
    return new StringBuilder("Usuário ").append(usuario).append(" bloqueado.").toString();
  }

  @Transactional
  public String cadastrarUsuario(final Usuario usr) throws SermilException {
    if (this.usuarioDao.findById(usr.getCpf()) != null) {
      throw new SermilException(new StringBuilder("Usuario CPF = ").append(usr.getCpf()).append(" já se encontra cadastrado no sistema.").toString());
    } else if (!this.usuarioDao.findByNamedQuery("Usuario.listarPorEmail", usr.getEmail()).isEmpty()) {
      throw new SermilException(new StringBuilder("Usuario com e-mail ").append(usr.getEmail()).append(" já se encontra cadastrado no sistema.").toString());
    } else {
      usr.setSenha(this.validarSenha("NA", usr.getSenha(), usr.getConfirma()));
      usr.setAcessoData(new Date());
      usr.setAcessoQtd(0);
      usr.setAtivo("N");
      final Usuario novo = this.usuarioDao.save(usr);
      this.emailServico.confirmarAlteracaoUsuario(novo, "emailCadastro.vm", null, 2);
      return new StringBuilder("Usuário CPF = ").append(usr.getCpf()).append(" cadastrado.").toString();
    }
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public String excluir(final String cpf) throws SermilException {
    final Usuario usuario = this.usuarioDao.findById(cpf);
    this.usuarioDao.delete(usuario);
    return new StringBuilder("Usuário ").append(usuario).append(" excluído.").toString();
  }

  public Usuario findByCPF(final String cpf) throws SermilException {
    if (StringUtils.isBlank(cpf)) {
      throw new SermilException("Informe um CPF válido.");
    }
    final Usuario usuario = this.usuarioDao.findById(cpf);
    if (usuario == null) {
      throw new NoDataFoundException(new StringBuilder("Usuário CPF = ").append(cpf).append(" não esta cadastrado no sistema.").toString());
    }
    return usuario;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','om','del','cs')")
  public List<Usuario> listar(final Usuario usr) throws SermilException {
    List<Usuario> lista = null;
    if (!StringUtils.isBlank(usr.getCpf())) {
      final Usuario usuario = this.usuarioDao.findById(usr.getCpf());
      if (usuario != null) {
        lista = new ArrayList<Usuario>(1);
        lista.add(usuario);
      }
    } else if (!StringUtils.isBlank(usr.getNome())) {
      lista = this.usuarioDao.findByNamedQuery("Usuario.listarPorNome", usr.getNome());
    } else if (usr.getOm() != null && usr.getOm().getCodigo() != null && usr.getOm().getCodigo() != -1) {
      lista = this.usuarioDao.findByNamedQuery("Usuario.listarPorOm", usr.getOm().getCodigo());
    } else {
      throw new CriterioException();
    }
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  @PreAuthorize("hasAnyRole('adm','dsm','smr','csm','om','del')")
  public List<CidAuditoria> listarAuditoria(final String cpf) throws SermilException {
    final List<CidAuditoria> lista = this.cidAuditoriaDao.findByNamedQuery("CidAuditoria.listarPorCpf", cpf);
    if (lista == null || lista.isEmpty()) {
      throw new NoDataFoundException();
    }
    return lista;
  }

  @Transactional
  public String recadastrar(final String cpf, final String email) throws SermilException {
    if (StringUtils.isBlank(email) || !email.matches(Constantes.EMAIL_REGEXP)) {
      throw new SermilException("Informe um e-mail válido.");
    }
    final Usuario usuario = (Usuario) this.usuarioDao.findById(cpf);
    usuario.setEmail(email);
    this.usuarioDao.save(usuario);
    return "Recadastramento efetuado com sucesso.";
  }

  @Transactional
  public String recuperarSenha(final String cpf) throws SermilException {
    final Usuario usuario = (Usuario) usuarioDao.findById(cpf);
    if (usuario == null) {
      throw new SermilException("CPF INEXISTENTE, entre em contato com sua Região Militar para efetuar o cadastramento no sistema.");
    }
    if ("N".equalsIgnoreCase(usuario.getAtivo())) {
      throw new SermilException("Usuãrio BLOQUEADO, entre em contato com sua Região Militar para desbloquear o acesso ao sistema.");
    }
    final String senha = new Randomize().execute();
    usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
    this.usuarioDao.save(usuario);
    this.emailServico.confirmarAlteracaoUsuario(usuario, "emailSenha.vm", senha, 2);
    return "Nova senha gerada e enviada para o e-mail cadastrado no sistema.";
  }

  @Transactional
  public String salvar(final Usuario usr) throws SermilException {
    final Usuario usuario = (Usuario) this.usuarioDao.findById(usr.getCpf());
    if (!StringUtils.isBlank(usr.getEmail()) && !usr.getEmail().equals(usuario.getEmail())) {
      usuario.setEmail(usr.getEmail());
    }
    if (!StringUtils.isBlank(usr.getNome()) && !usr.getNome().equals(usuario.getNome())) {
      usuario.setNome(usr.getNome());
    }
    if (usr.getPostoGraduacao() != null && !StringUtils.isBlank(usr.getPostoGraduacao().getCodigo()) && !usr.getPostoGraduacao().getCodigo().equals(usuario.getPostoGraduacao().getCodigo())) {
      usuario.setPostoGraduacao(usr.getPostoGraduacao());
    }
    if (!StringUtils.isBlank(usr.getTelefone()) && !usr.getTelefone().equals(usuario.getTelefone())) {
      usuario.setTelefone(usr.getTelefone());
    }
    this.usuarioDao.save(usuario);
    return "Usuário salvo.";
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public String salvar(final Usuario usr, final String[] perfis) throws SermilException {
    final Usuario usuario = (Usuario) this.usuarioDao.findById(usr.getCpf());
    if (usr.getAtivo() != null && !usr.getAtivo().equals(usuario.getAtivo())) {
      usuario.setAtivo(usr.getAtivo());
    }
    if (usr.getEmail() != null && !usr.getEmail().equals(usuario.getEmail())) {
      usuario.setEmail(usr.getEmail());
    }
    if (usr.getNome() != null && !usr.getNome().equals(usuario.getNome())) {
      usuario.setNome(usr.getNome());
    }
    if (usr.getOm() != null && usr.getOm().getCodigo() != null && !usr.getOm().getCodigo().equals(usuario.getOm().getCodigo())) {
      usuario.setOm(usr.getOm());
    }
    if (usr.getPostoGraduacao() != null && usr.getPostoGraduacao().getCodigo() != null && !usr.getPostoGraduacao().getCodigo().equals(usuario.getPostoGraduacao().getCodigo())) {
      usuario.setPostoGraduacao(usr.getPostoGraduacao());
    }
    if (usr.getTelefone() != null && !usr.getTelefone().equals(usuario.getTelefone())) {
      usuario.setTelefone(usr.getTelefone());
    }
    if (perfis != null) {
      final List<UsuarioPerfil> lista = new ArrayList<UsuarioPerfil>(perfis.length);
      for (String perfil : perfis) {
        lista.add(new UsuarioPerfil(usuario.getCpf(), perfil));
      }
      usuario.setUsuarioPerfilCollection(lista);
    }
    usuario.setTentativaslogin(0);
    usuario.setAcessoData(new Date());
    this.usuarioDao.save(usuario);
    return new StringBuilder("Usuário CPF = ").append(usuario.getCpf()).append(" salvo.").toString();
  }

  private String validarSenha(final String senhaAtual, final String senha1, final String senha2) throws SermilException {
    if (senha1 == null || !senha1.matches(Constantes.SENHA_REGEXP)) {
      throw new SermilException("Nova senha inválida, verifique abaixo as regras para a criação de senhas.");
    } else if (!senha2.equals(senha1)) {
      throw new SermilException("Nova senha diferente da confirmação.");
    } else if (senha1.equals(senhaAtual)) {
      throw new SermilException("Nova senha não pode ser igual a senha atual.");
    } else {
      return new BCryptPasswordEncoder().encode(senha1);
    }
  }

}
