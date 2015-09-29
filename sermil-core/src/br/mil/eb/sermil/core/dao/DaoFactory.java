package br.mil.eb.sermil.core.dao;

/**
 * Fábrica abstrata de DAO. (Padrão GoF Abstract Factory)
 * 
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.2.4
 */
public abstract class DaoFactory {

   /** Constante do nome da implementação JPA (EclipseLink). */
   public static final Class<? extends DaoFactory> JPA = DaoFactoryJpaImpl.class;

   /**
    * Retorna a fábrica apropriada para a implementação solicitada.
    * 
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

   // Adicionar os métodos para criar os DAO necessários na aplicação, devem ser instanciados na
   // classe AppConfig definida
   // para a aplicação (classe anotada com @Configuration para inicialização pelo Spring).

   public abstract CepDao getCepDao();

   public abstract CidadaoDao getCidadaoDao();

   public abstract CidArrecadacaoDao getCidArrecadacaoDao();

   public abstract CidAuditoriaDao getCidAuditoriaDao();

   public abstract CidAverbacaoDao getCidAverbacaoDao();

   public abstract CidCertificadoDao getCidCertificadoDao();

   public abstract CidContatoDao getCidContatoDao();

<<<<<<< HEAD
  public abstract CidEventoDao getCidEventoDao();

  public abstract CidFotoDao getCidFotoDao();
=======
   public abstract CidFotoDao getCidFotoDao();
>>>>>>> 6d14ac3b0eb46a015291302ba958cac6cd35d85e

   public abstract CsmDao getCsmDao();

<<<<<<< HEAD
  public abstract CselDao getCselDao();
  
  public abstract CselEnderecoDao getCselEnderecoDao();

  public abstract CselFeriadoDao getCselFeriadoDao();
  
  public abstract CselFuncionamentoDao getCselFuncionamentoDao();
  
  public abstract DelegaciaDao getDelegaciaDao();
=======
   public abstract DelegaciaDao getDelegaciaDao();
>>>>>>> 6d14ac3b0eb46a015291302ba958cac6cd35d85e

   public abstract DominiosDao getDominiosDao();

   public abstract DstbBolNecDao getDstbBolNecDao();

   public abstract DstbExclusaoDao getDstbExclusaoDao();

   public abstract DstbGdDao getDstbGdDao();

   public abstract DstbParametroDao getDstbParametroDao();

<<<<<<< HEAD
  public abstract EntrevistaCsDao getEntrevistaCsDao();

  public abstract EstatAlistamentoEscDao getEstatAlistamentoEscDao();
=======
   public abstract EmpresaDao getEmpresaDao();
>>>>>>> 6d14ac3b0eb46a015291302ba958cac6cd35d85e

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

<<<<<<< HEAD
  public abstract UfDao getUfDao();
  
=======
   public abstract UsuarioDao getUsuarioDao();

   public abstract UfDao getUfDao();

   public abstract CidEventoDao getCidEventoDao();

   public abstract EntrevistaCsDao getEntrevistaCsDao();

   public abstract CselDao getCselDao();

   public abstract CselFuncionamentoDao getCselFuncionamentoDao();

   public abstract CselEnderecoDao getCselEnderecoDao();

   public abstract CselFeriadoDao getCselFeriadoDao();

>>>>>>> 6d14ac3b0eb46a015291302ba958cac6cd35d85e
}
