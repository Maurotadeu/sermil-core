package br.mil.eb.sermil.core.servicos;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.exceptions.CertificateNotFoundException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemDocApresException;
import br.mil.eb.sermil.core.exceptions.CidadaoNaoTemEventoException;
import br.mil.eb.sermil.core.exceptions.EventNotFoundException;
import br.mil.eb.sermil.core.exceptions.OutOfSituationException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Usuario;
import br.mil.eb.sermil.tipos.TipoCertificado;
import br.mil.eb.sermil.tipos.TipoEvento;
import br.mil.eb.sermil.tipos.TipoSituacaoMilitar;

/** Gerenciamento dos certificados de cidad�o.
 * @author Abreu lopes, Anselmo Ribeiro
 * @since 5.1
 * @version 5.3.2
 */
@Named("certificadoServico")
public class CertificadoServico {

   protected static final Logger logger = LoggerFactory.getLogger(CertificadoServico.class);

   @Inject
   private CidadaoServico cidadaoServico;

   public CertificadoServico() {
      logger.debug("CertificadoServico iniciado.");
   }

   @Transactional
   public Cidadao anular(final CidCertificado certificado, final Usuario usuario) throws SermilException {
      final Cidadao cidadao = this.cidadaoServico.recuperar(certificado.getPk().getCidadaoRa());
      final CidCertificado cert = (CidCertificado) CollectionUtils.find(cidadao.getCidCertificadoCollection(), new EqualPredicate(certificado));
      cert.setAnulado("S");
      logger.debug("CERTIFICADO: {}", cert);
      logger.debug("USUARIO: {}", usuario);
      logger.debug("CIDADAO: {}", cidadao);
      return this.cidadaoServico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO ANULADO: ").append(certificado).toString());
   }

   @Transactional
   public Cidadao entregar(final CidCertificado certificado, final Usuario usuario) throws SermilException {
      final Cidadao cidadao = this.cidadaoServico.recuperar(certificado.getPk().getCidadaoRa());
      final CidCertificado cert = (CidCertificado) CollectionUtils.find(cidadao.getCidCertificadoCollection(), new EqualPredicate(certificado));
      cert.setEntregue("S");
      logger.debug("CERTIFICADO: {}", cert);
      logger.debug("USUARIO: {}", usuario);
      logger.debug("CIDADAO: {}", cidadao);
      return this.cidadaoServico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO ENTREGUE: ").append(certificado).toString());
   }

   @Transactional
   public Cidadao excluir(final CidCertificado certificado, final Usuario usuario) throws SermilException {
      final Cidadao cidadao = this.cidadaoServico.recuperar(certificado.getPk().getCidadaoRa());
      cidadao.getCidCertificadoCollection().remove(certificado);
      logger.debug("CERTIFICADO: {}", certificado);
      logger.debug("USUARIO: {}", usuario);
      logger.debug("CIDADAO: {}", cidadao);
      return this.cidadaoServico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO EXCLUIDO: ").append(certificado).toString());
   }

   @Transactional
   public Cidadao salvar(final CidCertificado certificado, final Usuario usuario) throws SermilException {
      final Cidadao cidadao = this.cidadaoServico.recuperar(certificado.getPk().getCidadaoRa());
      if ((certificado.getPk().getTipo() == TipoCertificado.CR1.ordinal() || certificado.getPk().getTipo() == TipoCertificado.CR1.ordinal()) &&
          cidadao.getSituacaoMilitar() != TipoSituacaoMilitar.LICENCIADO.ordinal()) {
         throw new SermilException("Somente LICENCIADOS possuem Certificado de Reservista.");
      }
      certificado.setSituacaoEspecial("false".equals(certificado.getSituacaoEspecial()) ? "N" : "S");
      certificado.setEntregue("false".equals(certificado.getEntregue()) ? "N" : "S");
      certificado.setAnulado("false".equals(certificado.getAnulado()) ? "N" : "S");
      cidadao.addCidCertificado(certificado);
      logger.debug("CERTIFICADO: {}", certificado);
      logger.debug("USUARIO: {}", usuario);
      logger.debug("CIDADAO: {}", cidadao);
      return this.cidadaoServico.salvar(cidadao, usuario, new StringBuilder("CERTIFICADO: ").append(certificado).toString());
   }

   public boolean podeImprimirCAM(final Cidadao cidadao) throws SermilException {
      if (cidadao == null || cidadao.getSituacaoMilitar() == null) {
         throw new SermilException("Cidad�o n�o foi informado.");
      }
      if (cidadao.getSituacaoMilitar() == TipoSituacaoMilitar.EXCLUIDO.ordinal() &&
          cidadao.getSituacaoMilitar() == TipoSituacaoMilitar.INCORPORADO.ordinal() &&
          cidadao.getSituacaoMilitar() == TipoSituacaoMilitar.LICENCIADO.ordinal()) {
         throw new SermilException("Para imprimir o CAM, o cidad�o N�O pode j� ter sido incorporado nas For�as Armadas.");
      }
      return true;
   }

   /** O cidadao tem que ter Pelo menos um evento do tipo 3, 6, 13, 14 ou 24 e pelo menos um certificado
    *  do tipo 3 (CDI) e tem que estar em uma das situacoes militares: 3, 8, ou 9.
    * @param cidadao
    * @throws SermilException
    */
   public boolean podeImprimirCDI(final Cidadao cidadao) throws SermilException {
      if (cidadao == null) {
         throw new SermilException("Cidad�o n�o foi informado.");
      }
      if (!cidadao.hasEvento(TipoEvento.EXCESSO.ordinal()) && !cidadao.hasEvento(TipoEvento.DISPENSA.ordinal())) {
         throw new EventNotFoundException();
      }
      if (!cidadao.hasCertificado(TipoCertificado.CDI.ordinal())) {
         throw new CertificateNotFoundException();
      }
      if (!StringUtils.containsAny(cidadao.getSituacaoMilitar().toString(), "389")) {
         throw new OutOfSituationException();
      }
      return true;
   }

   public boolean podeImprimirCertSitMilitar(final Cidadao cidadao) throws SermilException {
      if (cidadao == null || cidadao.getSituacaoMilitar() == null) {
         throw new SermilException("Cidad�o n�o foi informado.");
      }
      if (cidadao.getSituacaoMilitar() != TipoSituacaoMilitar.LICENCIADO.ordinal()) {
         throw new SermilException("Cidad�o n�o est� na situa��o LICENCIADO (15).");
      }
      if (!cidadao.hasEvento(TipoEvento.LICENCIAMENTO.ordinal())) {
         throw new CidadaoNaoTemEventoException();
      }
      if (cidadao.getCidDocApresColletion().size() <= 0) {
         throw new CidadaoNaoTemDocApresException();
      }
      return true;
   }
   
}
