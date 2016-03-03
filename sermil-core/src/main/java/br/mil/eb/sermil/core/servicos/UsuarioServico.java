package br.mil.eb.sermil.core.servicos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.Constantes;
import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.UsuarioDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.exceptions.UserNotFoundException;
import br.mil.eb.sermil.core.exceptions.UsuarioSalvarException;
import br.mil.eb.sermil.core.security.Randomize;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.modelo.UsuarioPerfil;

/** Gerenciamento de usuário da aplicação.
 * @author Abreu Lopes, Anselmo Ribeiro <anselmo.sr@gmail.com>
 * @since 3.0
 * @version 5.3.1
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

   @Transactional
   public void recadastrar(final Usuario usr) throws SermilException {
      final Usuario u = (Usuario) this.usuarioDao.findById(usr.getCpf());
      if (usr.getEmail() == null || usr.getEmail().isEmpty() || !usr.getEmail().matches(Constantes.EMAIL_REGEXP)) {
         throw new SermilException("Informe um e-mail válido");
      }
      u.setEmail(usr.getEmail());
      this.usuarioDao.save(u);
   }

   @Transactional
   public void alterarSenha(final Usuario usr) throws SermilException {
      final Usuario u = (Usuario) this.usuarioDao.findById(usr.getCpf());
      u.setSenha(this.validarSenha(usr.getSenha(), usr.getConfirma()));
      this.usuarioDao.save(u);
      this.emailServico.confirmarAlteracaoUsuario(u, "emailSenha.vm", "******", 2);
   }

   @Transactional
   public void recuperarSenha(final String cpf) throws SermilException {
      final Usuario usr = (Usuario) usuarioDao.findById(cpf);
      if (usr == null) {
         throw new SermilException("CPF INEXISTENTE, entre em contato com a Região Militar para ser cadastrado no sistema.");
      }
      if ("N".equalsIgnoreCase(usr.getAtivo())) {
         throw new SermilException("CPF BLOQUEADO, entre em contato com a Região Militar para desbloquear o acesso ao sistema.");
      }
      final String senha = new Randomize().execute();
      usr.setSenha(new BCryptPasswordEncoder().encode(senha));
      this.usuarioDao.save(usr);
      this.emailServico.confirmarAlteracaoUsuario(usr, "emailSenha.vm", senha, 2);
   }

   public List<Usuario> listar(final Usuario usr) throws SermilException {
      if (usr == null || (usr.getCpf() == null || usr.getCpf().isEmpty()) && (usr.getNome() == null || usr.getNome().isEmpty()) && (usr.getOm() == null || usr.getOm().getCodigo() == null)) {
         throw new CriterioException();
      }
      List<Usuario> lista = null;
      if (usr.getCpf() != null || usr.getNome() != null) {
         if (usr.getCpf() != null) {
            final Usuario u = this.usuarioDao.findById(usr.getCpf());
            if (u != null) {
               lista = new ArrayList<Usuario>(1);
               lista.add(u);
            }
         } else {
            lista = this.usuarioDao.findByNamedQuery("Usuario.listarPorNome", usr.getNome().concat("%"));
         }
      } else if (usr.getOm() != null && usr.getOm().getCodigo() != null) {
         lista = this.usuarioDao.findByNamedQuery("Usuario.listarPorOm", usr.getOm().getCodigo());
      }
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   public List<CidAuditoria> listarAuditoria(final String cpf) throws SermilException {
      final List<CidAuditoria> lista = this.cidAuditoriaDao.findByNamedQuery("CidAuditoria.listarPorCpf", cpf);
      if (lista == null || lista.isEmpty()) {
         throw new NoDataFoundException();
      }
      return lista;
   }

   @Transactional
   public Usuario salvar(final Usuario usr, final String[] perfis) throws SermilException {
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
      this.emailServico.confirmarAlteracaoUsuario(usuario, "emailCadastro.vm", null, 1);
      return usuario;
   }

   @Transactional
   public void solicitarCadastramento(final Usuario usr) throws SermilException {
      if (this.usuarioDao.findById(usr.getCpf()) != null) {
         throw new SermilException(new StringBuilder("ERRO: CPF ").append(usr.getCpf()).append(" já se encontra cadastrado no sistema.").toString());
      } else if (!this.usuarioDao.findByNamedQuery("Usuario.listarPorEmail", usr.getEmail()).isEmpty()) {
         throw new SermilException(new StringBuilder("ERRO: e-mail ").append(usr.getEmail()).append(" já se encontra cadastrado no sistema.").toString());
      } else {
         usr.setSenha(this.validarSenha(usr.getSenha(), usr.getConfirma()));
         usr.setAcessoData(new Date());
         usr.setAcessoQtd(0);
         usr.setAtivo("N");
         final Usuario novo = this.usuarioDao.save(usr);
         this.emailServico.confirmarAlteracaoUsuario(novo, "emailCadastro.vm", null, 2);
      }
   }

   @Transactional
   public void alterarUsuarios(List<Usuario> usuarios2, String[] perfis, Usuario usuarioPadrao) throws UsuarioSalvarException {
      for (Usuario usu : usuarios2) {
         // Perfis
         String[] perfisDoUsu = {};
         if (perfis.length > 0) {
            perfisDoUsu = perfis;
         } else {
            List<String> ps = new ArrayList<String>();
            for (int i = 0; i < usu.getUsuarioPerfilCollection().size(); i++) {
               String p = usu.getUsuarioPerfilCollection().get(i).toString();
               ps.add(p.substring(p.length() - 3, p.length()));
            }
            perfisDoUsu = (String[]) ps.toArray(new String[ps.size()]);
         }
         // Ativo/Inativo
         if (usuarioPadrao != null) {
            usu.setAtivo(usuarioPadrao.getAtivo());
         }
         try {
            this.salvar(usu, perfisDoUsu);
         } catch (SermilException e) {
            throw new UsuarioSalvarException("Não foi possível salvar informações referente ao CPF " + usu.getCpf().toString());
         }
      }
   }

   @Transactional
   public void excluir(final Usuario usr) throws SermilException {
      this.usuarioDao.delete(this.usuarioDao.findById(usr.getCpf()));
   }

   @Transactional
   public void removerPerfis(final String cpf) throws SermilException {
      this.usuarioDao.execute("UsuarioPerfil.excluirPorCpf", cpf);
   }

   public Usuario recuperar(final String id) throws SermilException {
      return this.usuarioDao.findById(id);
   }

   private String validarSenha(final String senha1, final String senha2) throws SermilException {
      if (senha1 == null || !senha1.matches(Constantes.SENHA_REGEXP)) {
         throw new SermilException("Senha inválida, verifique abaixo as regras para a criação de senhas.");
      } else if (!senha2.equals(senha1)) {
         throw new SermilException("Senha diferente da confirmação.");
      } else {
         return new BCryptPasswordEncoder().encode(senha1);
      }
   }

   /** Busca usuário pelo CPF.
    * @param cpf
    * @return Usuario
    * @throws SermilException
    */
   public Usuario findByCPF(final String cpf) throws SermilException {
      if (StringUtils.isBlank(cpf)) {
         throw new SermilException("Informe um CPF válido.");
      }
      final Usuario usr = this.usuarioDao.findById(cpf);
      if (usr == null) {
         throw new UserNotFoundException("O usuário referente ao CPF " + cpf + " não foi encontrado na base de dados.");
      }
      return usr;
   }

}
