package br.mil.eb.sermil.core.servicos;

import static br.mil.eb.sermil.core.Constantes.SUPORTE_CONTA_EMAIL;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;

/** Serviço de envio de e-mail.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.2.6
 */
@Named("emailServico")
public class EmailServico {

   protected static final Logger logger = LoggerFactory.getLogger(EmailServico.class);

   @Inject
   private JavaMailSender mailSender;

   @Inject
   private VelocityEngine velocityEngine;

   private Configurador config;

   public EmailServico() {
      this.config = Configurador.getInstance();
      logger.debug("EmailServico iniciado");
   }   

   public void confirmarAlistamentoOnline(final Cidadao cadastro) {
      if (cadastro != null && !StringUtils.isEmpty(cadastro.getEmail())) {
         try {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
               public void prepare(MimeMessage mimeMessage) throws Exception {
                  final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                  message.setTo(cadastro.getEmail());
                  message.setFrom(config.getConfiguracao(SUPORTE_CONTA_EMAIL));
                  message.setSubject("Alistamento ONLINE do Serviço Militar");
                  final Map<String, Object> model = new HashMap<String, Object>(5);
                  model.put("ra", cadastro.getRa());
                  model.put("nome", cadastro.getNome());
                  model.put("pai", cadastro.getPai());
                  model.put("mae", cadastro.getMae());
                  model.put("dtnasc", DateFormat.getDateInstance(DateFormat.MEDIUM).format(cadastro.getNascimentoData()));
                  model.put("jsm", cadastro.getJsm());
                  model.put("endereco", cadastro.getJsm().getEndereco());
                  model.put("telefone", cadastro.getJsm().getTelefone());
                  model.put("municipio", cadastro.getJsm().getMunicipio());
                  model.put("data", DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()));
                  final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailCadastro.vm", "utf-8", model);
                  message.setText(text, true);
               }
            };
            this.mailSender.send(preparator);
         } catch (Exception e) {
            logger.warn("Falha no envio de e-mail: {}", e);
         }
      }
   }

   public void confirmarAlteracaoUsuario(final Usuario usr, final String template, final String senha, final int versao) throws SermilException {
      if (usr != null && !StringUtils.isEmpty(usr.getEmail())) {
         try {
            MimeMessagePreparator preparator = null;
            switch(versao) {
            case 1:
               preparator = new MimeMessagePreparator() {
                  public void prepare(MimeMessage mimeMessage) throws Exception {
                     final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                     message.setTo(usr.getEmail());
                     message.setFrom(config.getConfiguracao(SUPORTE_CONTA_EMAIL));
                     message.setSubject("SERMILWEB - Conta Alterada");
                     final Map<String, Object> model = new HashMap<String, Object>(4);
                     model.put("cpf", usr.getCpf());
                     model.put("nome", usr.getNome());
                     model.put("email", usr.getEmail());
                     model.put("senha", usr.getSenha());
                     model.put("data", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date()));
                     final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailAcesso.vm", "utf-8", model);
                     message.setText(text, true);
                  }
               };
               break;
            case 2:
               preparator = new MimeMessagePreparator() {
                  public void prepare(MimeMessage mimeMessage) throws Exception {
                     final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                     message.setTo(usr.getEmail());
                     message.setFrom(config.getConfiguracao(SUPORTE_CONTA_EMAIL));
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
            }
            this.mailSender.send(preparator);
         } catch (Exception e) {
            logger.warn("Falha no envio de e-mail: {}", e);
         }
      }
   }

   public void suporteExarnet(final Cidadao cidadao, final String emailRm, final String msg) {
      if (cidadao != null && !StringUtils.isEmpty(cidadao.getEmail())) {
         try {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
               public void prepare(MimeMessage mimeMessage) throws Exception {
                 final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                 message.setTo(emailRm);
                 message.setReplyTo(cidadao.getEmail());
                 message.setCc("sermilweb@dgp.eb.mil.br");
                 message.setFrom(cidadao.getEmail());
                 message.setSubject("EXARNET - Suporte");
                 final Map<String, Object> model = new HashMap<String, Object>(5);
                 model.put("nome", cidadao.getNome());
                 model.put("mae", cidadao.getMae());
                 model.put("dtNasc", DateFormat.getDateInstance(DateFormat.MEDIUM).format(cidadao.getNascimentoData()));
                 model.put("municipio", cidadao.getMunicipioResidencia());
                 model.put("mensagem", msg);
                 final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "suporteExarnet.vm","utf-8", model);
                 message.setText(text, true);
               }
             };
            this.mailSender.send(preparator);
         } catch (Exception e) {
            logger.warn("Falha no envio de e-mail: {}", e);
         }
      }
   }

}
