package br.mil.eb.sermil.core.servicos;

import static br.mil.eb.sermil.core.Constantes.SUPORTE_CONTA_EMAIL;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.mil.eb.sermil.core.Constantes;
import br.mil.eb.sermil.core.dao.CidAuditoriaDao;
import br.mil.eb.sermil.core.dao.UsuarioDao;
import br.mil.eb.sermil.core.exceptions.CPFDuplicadoException;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.NoDataFoundException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.exceptions.UserNotFoundException;
import br.mil.eb.sermil.core.exceptions.UsuarioSalvarException;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.core.security.Randomize;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.modelo.UsuarioPerfil;

/**
 * Gerenciamento dos usuários do sistema.
 * 
 * @author Abreu Lopes, Anselmo Ribeiro <anselmo.sr@gmail.com>
 * @since 3.0
 * @version $Id: UsuarioServico.java 2524 2014-08-26 12:04:21Z wlopes $
 */
@Named("usuarioServico")
public class UsuarioServico {

   protected static final Logger logger = LoggerFactory.getLogger(UsuarioServico.class);

   protected static final Configurador cfg = Configurador.getInstance();

   @Inject
   private UsuarioDao dao;

   @Inject
   private JavaMailSender mailSender;

   @Inject
   private VelocityEngine velocityEngine;

   @Inject
   private CidAuditoriaDao cidAuditoriaDao;

   public UsuarioServico() {
      logger.debug("UsuarioServico iniciado");
   }

   @Transactional
   public void recadastrar(final Usuario usr) throws SermilException {
      final Usuario u = (Usuario) this.dao.findById(usr.getCpf());
      if (usr.getEmail() == null || usr.getEmail().isEmpty() || !usr.getEmail().matches(Constantes.EMAIL_REGEXP)) {
         throw new SermilException("Informe um e-mail válido");
      }
      u.setEmail(usr.getEmail());
      this.dao.save(u);
   }

   @Transactional
   public void alterarSenha(final Usuario usr) throws SermilException {
      final Usuario u = (Usuario) this.dao.findById(usr.getCpf());
      u.setSenha(this.validarSenha(usr.getSenha(), usr.getConfirma()));
      this.dao.save(u);
      this.enviarEmail(u, "emailSenha.vm", "******");
   }

   @Transactional
   public void recuperarSenha(final String cpf) throws SermilException {
      final Usuario usr = (Usuario) dao.findById(cpf);
      if (usr == null) {
         throw new SermilException("CPF INEXISTENTE, entre em contato com a Região Militar para ser cadastrado no sistema.");
      }
      if ("N".equalsIgnoreCase(usr.getAtivo())) {
         throw new SermilException("CPF BLOQUEADO, entre em contato com a Região Militar para desbloquear o acesso ao sistema.");
      }
      final String senha = new Randomize().execute();
      usr.setSenha(new BCryptPasswordEncoder().encode(senha));
      this.dao.save(usr);
      this.enviarEmail(usr, "emailSenha.vm", senha);
   }

   public List<Usuario> listar(final Usuario usr) throws SermilException {
      if (usr == null || (usr.getCpf() == null || usr.getCpf().isEmpty()) && (usr.getNome() == null || usr.getNome().isEmpty()) && (usr.getOm() == null || usr.getOm().getCodigo() == null)) {
         throw new CriterioException();
      }
      List<Usuario> lista = null;
      if (usr.getCpf() != null || usr.getNome() != null) {
         if (usr.getCpf() != null) {
            final Usuario u = this.dao.findById(usr.getCpf());
            if (u != null) {
               lista = new ArrayList<Usuario>(1);
               lista.add(u);
            }
         } else {
            lista = this.dao.findByNamedQuery("Usuario.listarPorNome", usr.getNome().concat("%"));
         }
      } else if (usr.getOm() != null && usr.getOm().getCodigo() != null) {
         lista = this.dao.findByNamedQuery("Usuario.listarPorOm", usr.getOm().getCodigo());
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
      final Usuario usuario = (Usuario) this.dao.findById(usr.getCpf());
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
      this.dao.save(usuario);
      try {
         final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
               final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
               message.setTo(usuario.getEmail());
               message.setFrom(cfg.getConfiguracao(SUPORTE_CONTA_EMAIL));
               message.setSubject("SERMIL - Conta Alterada");
               final Map<String, Object> model = new HashMap<String, Object>(4);
               model.put("cpf", usuario.getCpf());
               model.put("nome", usuario.getNome());
               model.put("email", usuario.getEmail());
               model.put("senha", usuario.getSenha());
               model.put("data", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date()));
               final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailAcesso.vm", "utf-8", model);
               message.setText(text, true);
            }
         };
         this.mailSender.send(preparator);
      } catch (MailException e) {
         logger.error("Erro ao tentar enviar e-mail {}", e.getMessage());
      }
      return usuario;
   }

   @Transactional
   public void solicitarCadastramento(final Usuario usr) throws SermilException {
      if (this.dao.findById(usr.getCpf()) != null) {
         throw new SermilException(new StringBuilder("ERRO: CPF ").append(usr.getCpf()).append(" já se encontra cadastrado no sistema.").toString());
      } else if (!this.dao.findByNamedQuery("Usuario.listarPorEmail", usr.getEmail()).isEmpty()) {
         throw new SermilException(new StringBuilder("ERRO: e-mail ").append(usr.getEmail()).append(" já se encontra cadastrado no sistema.").toString());
      } else {
         usr.setSenha(this.validarSenha(usr.getSenha(), usr.getConfirma()));
         usr.setAcessoData(new Date());
         usr.setAcessoQtd(0);
         usr.setAtivo("N");
         final Usuario novo = this.dao.save(usr);
         this.enviarEmail(novo, "emailCadastro.vm", null);
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
      this.dao.delete(this.dao.findById(usr.getCpf()));
   }

   @Transactional
   public void removerPerfis(final String cpf) throws SermilException {
      this.dao.execute("UsuarioPerfil.excluirPorCpf", cpf);
   }

   public Usuario recuperar(final String id) throws SermilException {
      return this.dao.findById(id);
   }

   private void enviarEmail(final Usuario usr, final String template, final String senha) throws SermilException {
      try {
         final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
               final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
               message.setTo(usr.getEmail());
               message.setFrom(cfg.getConfiguracao(SUPORTE_CONTA_EMAIL));
               message.setSubject("SERMILWEB - Alteração de Usuário");
               final Map<String, Object> model = new HashMap<String, Object>(4);
               model.put("cpf", usr.getCpf());
               model.put("nome", usr.getNome());
               if (senha != null && !senha.isEmpty())
                  model.put("senha", senha);
               model.put("data", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date()));
               final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "utf-8", model);
               message.setText(text, true);
            }
         };
         this.mailSender.send(preparator);
      } catch (MailException e) {
         logger.error("Erro ao tentar enviar e-mail {}", e);
      }
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

   public Boolean isAdmin(Usuario user) {
      Boolean isAdmin = false;
      List<UsuarioPerfil> usuPerfis = user.getUsuarioPerfilCollection();
      for (UsuarioPerfil usuPerfil : usuPerfis) {
         if (usuPerfil.getPerfil().getCodigo().equals("adm")) {
            isAdmin = true;
         }
      }
      return isAdmin;
   }

   /**
    * 
    * @param CPF
    * @return Usuario
    * @throws UserNotFoundException
    * @throws CPFDuplicadoException
    * @author Anselmo Ribeiro
    */
   public Usuario findByCPF(String CPF) throws UserNotFoundException, CPFDuplicadoException {
      Usuario usu = dao.findById(CPF);
      if (usu == null) {
         throw new UserNotFoundException("O usuario referente ao CPF " + CPF + " nao foi achado.");
      } else {
         return usu;
      }
   }

}
