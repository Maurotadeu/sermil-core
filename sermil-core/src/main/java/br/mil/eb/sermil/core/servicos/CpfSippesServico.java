package br.mil.eb.sermil.core.servicos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
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
import br.mil.eb.sermil.tipos.Cpf;
import br.mil.eb.sermil.tipos.CpfInfoSippes;

/** Integra��o entre SERMIL e SIPPES, padr�o Web Service (JAX-RS).
 * Verifica a situa��o cadastral do CPF no InfoConv da Receita Federal.
 * Utiliza o SIPPES como intermedi�rio na transa��o.
 * IMPORTANTE: Importar o certificado digital do SIPPES no cacerts da JVM do servidor SERMIL.
 * @author Abreu Lopes
 * @since 5.2.5
 * @version 5.3.0
 */
@Named("CpfSippesServico")
@RemoteProxy(name = "cpfSippesServico")
public class CpfSippesServico {

   protected static final Logger log = LoggerFactory.getLogger(CpfSippesServico.class);

   /** URL de teste: n�o consome requisi��es no InfoConv. */
   @SuppressWarnings("unused")
   private static final String URL_TESTE = "https://www.sippes.eb.mil.br/consultacpf/rest/consultarcpft";

   /** URL de produ��o: consome requisi��es reais no InfoConv. */
   private static final String URL_PROD = "https://www.sippes.eb.mil.br/consultacpf/rest/consultarcpf";

   private static final String KEY = "4f8653a9-95be-476a-8b12-5c5a1904aaa3";

   private static final String USR_ID = "dsm";

   @Inject
   private CidadaoServico cidadaoServico;
   
   public CpfSippesServico() {
      log.debug("CpfSippesServico iniciado");
   }

   @RemoteMethod
   public CpfInfoSippes pesquisarCpf(final String cpf) throws CPFSippesException, SermilException, URISyntaxException {
      //  CPF � inv�lido
      if (!Cpf.isCpf(cpf)) {
         throw new CPFSippesException("CPF inv�lido, informe um n�mero de CPF v�lido.");
      };
      // Verifica se o CPF j� existe na base de dados
      if (this.cidadaoServico.isCPFCadastrado(cpf)) {
         throw new CPFSippesException("CPF j� foi cadastrado no sistema. Procure o �rg�o de Servi�o Militar se necess�rio.");
      }
      // Usando a implementa��o default do Spring para REST-RS
      final RestTemplate restClient = new RestTemplate();
      restClient.setErrorHandler(new CustomResponseErrorHandler());
      // Objeto JSON padr�o de consulta no Web Service
      final JsonObject query = Json.createObjectBuilder()
            .add("cpf", cpf)
            .add("cpfUsuario", cpf)
            .add("usuario", USR_ID)
            .build();
      // Cabe�alhos necess�rios na requisi��o
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("service_key", KEY);
      // Envio da Requisi��o, em caso de erro ser� emitida uma CPFSippesException
      final HttpEntity<String> request= new HttpEntity<String>(query.toString(), headers);
      final CpfInfoSippes info = restClient.postForObject(URL_PROD, request, CpfInfoSippes.class);
      // Tratamento da Resposta do servidor
      if (info != null && info.getErro() != null) {
         throw new SermilException(info.getErro());
      } else {
         log.debug("JAX-RS: {}", info);
         return info;
      }
   }

   /** Manipulador de erro na resposta do servidor.
    * @author Abreu Lopes
    * @since 5.2.5
    * @version 5.2.7
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

   /* SOMENTE PARA TESTES EM DESENVOLVIMENTO
   public static void main(String[] args) {
      try {
         log.info(new CpfSippesServico().pesquisarCpf("99999999999").toString());
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
   */
}
