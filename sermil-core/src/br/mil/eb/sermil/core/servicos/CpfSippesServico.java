package br.mil.eb.sermil.core.servicos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import br.mil.eb.sermil.core.exceptions.CPFSippesException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.tipos.CpfInfoSippes;

/** Integração entre SERMIL e SIPPES, padrão Web Service (JAX-RS).
 * Verifica a situação cadastral do CPF no InfoConv da Receita Federal.
 * Utiliza o SIPPES como intermediário na transação.
 * IMPORTANTE: Importar o certificado digital do SIPPES no cacerts da JVM do servidor SERMIL.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.2.6
 */
public class CpfSippesServico {

   protected static final Logger log = LoggerFactory.getLogger(CpfSippesServico.class);

   private static final String URL = "https://www.sippes.eb.mil.br/consultacpf/rest/consultarcpf";

   private static final String KEY = "4f8653a9-95be-476a-8b12-5c5a1904aaa3";

   private static final String USR_ID = "dsm";

   private static final String USR_CPF = "98106546772";

   public CpfSippesServico() {
      log.info("CpfSippesServico iniciado");
   }

   public CpfInfoSippes pesquisarCpf(final String cpf) throws CPFSippesException, SermilException {
      // Usando a implementação default do Spring para REST-RS
      final RestTemplate restClient = new RestTemplate();
      restClient.setErrorHandler(new CustomResponseErrorHandler());
      // Objeto JSON padrão de consulta no Web Service
      final JsonObject query = Json.createObjectBuilder()
            .add("cpf", cpf)
            .add("cpfUsuario", USR_CPF)
            .add("usuario", USR_ID)
            .build();
      // Cabeçalhos necessários na requisição
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("service_key", KEY);
      // Envio da Requisição, em caso de erro será emitida uma CPFSippesException
      final HttpEntity<String> request= new HttpEntity<String>(query.toString(), headers);
      final CpfInfoSippes info = restClient.postForObject(URL, request, CpfInfoSippes.class);
      // Tratamento da Resposta do servidor
      if (info != null && info.getErro() != null) {
         throw new SermilException(info.getErro());
      } else {
         log.info("Resposta: {}", info);
         return info;
      }
   }

   /** Manipulador de erro na resposta do servidor.
    * @author Abreu Lopes
    * @since 5.2.5
    * @version 5.2.6
    */
   public class CustomResponseErrorHandler implements ResponseErrorHandler {
      
      private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

      @Override
      public boolean hasError(ClientHttpResponse response) throws IOException {
         return this.errorHandler.hasError(response);
      }

      @Override
      public void handleError(ClientHttpResponse response) throws IOException {
         final String body = IOUtils.toString(response.getBody());
         final CPFSippesException cpfException = new CPFSippesException();
         final Map<String, Object> properties = new HashMap<String, Object>();
         properties.put("code", response.getStatusCode().toString());
         properties.put("body", body);
         properties.put("header", response.getHeaders());
         cpfException.setProperties(properties);
         throw cpfException;
      }
   
   }

   public static void main(String[] args) {
      try {
         log.info(new CpfSippesServico().pesquisarCpf("98106546772").toString());
      } catch (Exception e) {
         if (e.getCause() instanceof CPFSippesException) {
            final Map<String,Object> prop = ((CPFSippesException)e.getCause()).getProperties();
            final String code = (String) prop.get("code");
            log.error("HTTP {}", code);
            log.error("{}", prop.get("header"));
         } else {
            log.error(e.getMessage());
         }
      }
   }
   
}
