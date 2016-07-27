package br.mil.eb.sermil.core.servicos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.utils.Constantes;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Usuario;

/** Serviço de envio de e-mail.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.4.5
 */
@Named("emailServico")
public class EmailServico {

   protected static final Logger logger = LoggerFactory.getLogger(EmailServico.class);

   @Inject
   private Environment env;

   @Inject
   private JsmDao jsmDao;

   @Inject
   private JavaMailSender mailSender;

   @Inject
   private VelocityEngine velocityEngine;

   public EmailServico() {
      logger.debug("EmailServico iniciado");
   }   

   public void confirmarAlistamentoOnline(final Cidadao cadastro) {
      if (cadastro != null && !StringUtils.isEmpty(cadastro.getEmail())) {
         final Jsm jsm = this.jsmDao.findById(cadastro.getJsm().getPk());
         try {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
               public void prepare(MimeMessage mimeMessage) throws Exception {
                  final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                  message.setTo(cadastro.getEmail());
                  message.setFrom(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
                  message.setSubject("Alistamento ONLINE do Serviço Militar");
                  final Map<String, Object> model = new HashMap<String, Object>(10);
                  model.put("ra", cadastro.getRa());
                  model.put("nome", cadastro.getNome());
                  model.put("pai", cadastro.getPai());
                  model.put("mae", cadastro.getMae());
                  model.put("dtnasc", DateFormat.getDateInstance(DateFormat.MEDIUM).format(cadastro.getNascimentoData()));
                  model.put("jsm", jsm);
                  model.put("endereco", jsm.getEndereco() == null ? "N/D" : jsm.getEndereco());
                  model.put("telefone", jsm.getTelefone() == null ? "N/D" : jsm.getTelefone());
                  model.put("municipio", jsm.getMunicipio());
                  model.put("data", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cadastro.getAtualizacaoData())); //DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()));
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
                     message.setFrom(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
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
                     message.setFrom(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
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
                  message.setCc(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
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

   @Transactional
   public void agendamentoCs() throws Exception {
      int status[] = {0,0,0};
      final List<Object[]> lista = this.jsmDao.findBySQL("SELECT c.ra, c.email, c.nome, to_char(a.data_selecao,'dd/mm/yyyy hh24:mi') FROM cidadao c JOIN cs_agendamento a ON c.ra = a.cidadao_ra WHERE a.email = 'N' AND rownum < 11");
      if (lista != null && !lista.isEmpty()) {
         final String exp = "^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
         final Query enviado = this.jsmDao.getEntityManager().createNativeQuery("update cs_agendamento set email=?1 where cidadao_ra = ?2");
         final String dataHoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
         for(final Object[] o: lista) {
            String email = (String) o[1];
            if (email != null && email.matches(exp)) {
               // Mensagem
               MimeMessagePreparator preparator = new MimeMessagePreparator() {
                  public void prepare(MimeMessage mimeMessage) throws Exception {
                     final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                     message.setTo(email);
                     message.setFrom(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
                     message.setReplyTo(env.getProperty(Constantes.SUPORTE_CONTA_EMAIL));
                     message.setSubject("Alistamento Online - Convocação para a Seleção Geral");
                     final Map<String, Object> model = new HashMap<String, Object>(1);
                     model.put("ra", o[0]);
                     model.put("email", o[1]);
                     model.put("nome", o[2]);
                     model.put("dataSel", o[3]);
                     model.put("dataHoje", dataHoje);
                     final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailCs.vm","utf-8", model);
                     message.setText(text, true);
                  }
               };
               try {
                  this.mailSender.send(preparator);
                  enviado.setParameter(1, 'S');
                  enviado.setParameter(2, o[0]);
                  enviado.executeUpdate();
                  status[0] += 1;
                  logger.info("1 - ENVIADO: {}", o[1]);
               } catch(Throwable e) {
                  logger.error("2 - ERRO: {} ({})", o[1], e.getMessage());
                  status[1] += 1;
                  enviado.setParameter(1, 'E');
                  enviado.setParameter(2, o[0]);
                  enviado.executeUpdate();
               }
            } else {
               logger.info("3 - REJEITADO: {}", o[1]);
               status[2] += 1;
               enviado.setParameter(1, 'R');
               enviado.setParameter(2, o[0]);
               enviado.executeUpdate();
            }
         }
         final String resumo = new StringBuilder(" *** RESUMO DO PROCESSAMENTO *** ")
               .append("\n - ENVIADOS  : ").append(status[0])
               .append("\n - SMTP ERRO : ").append(status[1])
               .append("\n - REJEITADOS: ").append(status[2])
               .append("\n - TOTAL     : ").append(lista.size())
               .append("\n").toString();
         logger.info("{}", resumo);
      } else {
         logger.warn("Lista de Agendamento da CS está vazia, não há e-mails a enviar.");
      }
   }

}
