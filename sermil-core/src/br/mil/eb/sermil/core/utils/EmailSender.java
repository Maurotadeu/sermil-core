package br.mil.eb.sermil.core.utils;

import static br.mil.eb.sermil.core.Constantes.SUPORTE_CONTA_EMAIL;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import br.mil.eb.sermil.core.servicos.CidadaoServico;
import br.mil.eb.sermil.modelo.Cidadao;



@Named("emailSender")
public class EmailSender {

   protected static final Logger logger = LoggerFactory.getLogger(CidadaoServico.class);

   @Inject
   private JavaMailSender mailSender;

   @Inject
   private VelocityEngine velocityEngine;

   public void enviarEmailConfirmacaoAlistamentoOnLine(final Cidadao cadastro) {
      final Configurador cfg = Configurador.getInstance();
      final MimeMessagePreparator preparator = new MimeMessagePreparator() {
         public void prepare(MimeMessage mimeMessage) throws Exception {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(cadastro.getEmail());
            message.setFrom(cfg.getConfiguracao(SUPORTE_CONTA_EMAIL));
            message.setSubject("Alistamento ONLINE do Serviço Militar");
            final Map<String, Object> model = new HashMap<String, Object>(5);
            model.put("ra", cadastro.getRa());
            model.put("nome", cadastro.getNome());
            model.put("pai", cadastro.getPai());
            model.put("mae", cadastro.getMae());
            model.put("dtnasc", DateFormat.getDateInstance(DateFormat.MEDIUM).format(cadastro.getNascimentoData()));
            model.put("jsm", cadastro.getJsm());
            model.put("endereco", cadastro.getJsm().getJsmInfo().getEndereco());
            model.put("bairro", cadastro.getJsm().getJsmInfo().getBairro());
            model.put("telefone", cadastro.getJsm().getJsmInfo().getTelefone());
            model.put("municipio", cadastro.getJsm().getMunicipio());
            model.put("data", DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()));
            final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailCadastro.vm", "utf-8", model);
            message.setText(text, true);
         }
      };
      try {
         this.mailSender.send(preparator);
      } catch (Exception e) {
         logger.warn(e.getMessage());
      }
   }

}
