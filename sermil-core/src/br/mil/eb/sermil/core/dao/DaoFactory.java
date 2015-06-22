package br.mil.eb.sermil.core.dao;

/** Fábrica abstrata de DAO. (Padrão GoF Abstract Factory)
 * @author Abreu Lopes
 * @since 4.0
 * @version $Id$
 */
public abstract class DaoFactory {

  /** Constante do nome da implementação JPA (EclipseLink). */
  public static final Class<? extends DaoFactory> JPA = DaoFactoryJpaImpl.class;

  /** Retorna a fábrica apropriada para a implementação solicitada.
   * @param factory Classe de implementação da fábrica
   * @return Fábrica de DAO apropriada
   */
  public static DaoFactory getInstance(Class<? extends DaoFactory> factory) {
    try {
      return factory.newInstance();
    } catch (Exception erro) {
      throw new RuntimeException("Erro ao criar a DaoFactory: " + factory, erro);
    }
  }

  /** Construtor com acesso restrito, usar o método getInstance() para obter uma fábrica. */
  protected DaoFactory() {
    // Construtor default.
  }

  // Adicionar os métodos para criar os DAO necessários na aplicação, devem ser instanciados na classe AppConfig definida
  // para a aplicação (classe anotada com @Configuration para inicialização pelo Spring).

  public abstract CepDao getCepDao();

  public abstract CidadaoDao getCidadaoDao();

  public abstract CidArrecadacaoDao getCidArrecadacaoDao();

  public abstract CidAuditoriaDao getCidAuditoriaDao();

  public abstract CidAverbacaoDao getCidAverbacaoDao();

  public abstract CidCertificadoDao getCidCertificadoDao();

  public abstract CidContatoDao getCidContatoDao();

  public abstract CidFotoDao getCidFotoDao();

  public abstract CsmDao getCsmDao();

  public abstract DelegaciaDao getDelegaciaDao();

  public abstract DominiosDao getDominiosDao();

  public abstract DstbBolNecDao getDstbBolNecDao();

  public abstract DstbExclusaoDao getDstbExclusaoDao();
  
  public abstract DstbGdDao getDstbGdDao();

  public abstract DstbParametroDao getDstbParametroDao();

  public abstract EmpresaDao getEmpresaDao();

  public abstract EstatAlistamentoEscDao getEstatAlistamentoEscDao();

  public abstract EstatArrecadacaoDao getEstatArrecadacaoDao();

  public abstract EstatExarDao getEstatExarDao();

  public abstract HabilitacaoDao getHabilitacaoDao();

  public abstract ImpServicoDao getImpServicoDao();

  public abstract InfoLocalDao getInfoLocalDao();

  public abstract JsmDao getJsmDao();

  public abstract MunicipioDao getMunicipioDao();

  public abstract OcupacaoDao getOcupacaoDao();

  public abstract OmBoletimDao getOmBoletimDao();

  public abstract OmDao getOmDao();

  public abstract PadraoDao getPadraoDao();

  public abstract PaisDao getPaisDao();

  public abstract PerfilDao getPerfilDao();

  public abstract PortalMensagemDao getPortalMensagemDao();

  public abstract PostoGraduacaoDao getPostoGraduacaoDao();

  public abstract PreAlistamentoDao getPreAlistamentoDao();

  public abstract QcpDao getQcpDao();

  public abstract QmDao getQmDao();

  public abstract RaItensDao getRaItensDao();

  public abstract RaPedidoDao getRaPedidoDao();

  public abstract RaMestreDao getRaMestreDao();

  public abstract RmDao getRmDao();

  public abstract SelBccDao getSelBccDao();

  public abstract SelJsmDao getSelJsmDao();

  public abstract SelTributacaoDao getSelTributacaoDao();

  public abstract TaxaMultaDao getTaxaMultaDao();

  public abstract UsuarioDao getUsuarioDao();

  public abstract UfDao getUfDao();
  
  public abstract CidEventoDao getCidEventoDao();

}
