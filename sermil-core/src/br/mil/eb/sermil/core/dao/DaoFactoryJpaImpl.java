package br.mil.eb.sermil.core.dao;

import br.mil.eb.sermil.modelo.Cep;
import br.mil.eb.sermil.modelo.CidAdiamento;
import br.mil.eb.sermil.modelo.CidArrecadacao;
import br.mil.eb.sermil.modelo.CidAuditoria;
import br.mil.eb.sermil.modelo.CidAverbacao;
import br.mil.eb.sermil.modelo.CidCertificado;
import br.mil.eb.sermil.modelo.CidContato;
import br.mil.eb.sermil.modelo.CidEvento;
import br.mil.eb.sermil.modelo.CidExar;
import br.mil.eb.sermil.modelo.CidFoto;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.CsAgendamento;
import br.mil.eb.sermil.modelo.CsAnamnese;
import br.mil.eb.sermil.modelo.Csel;
import br.mil.eb.sermil.modelo.CselEndereco;
import br.mil.eb.sermil.modelo.CselFeriado;
import br.mil.eb.sermil.modelo.CselFuncionamento;
import br.mil.eb.sermil.modelo.Csm;
import br.mil.eb.sermil.modelo.Delegacia;
import br.mil.eb.sermil.modelo.Dominios;
import br.mil.eb.sermil.modelo.DstbBolNec;
import br.mil.eb.sermil.modelo.DstbExclusao;
import br.mil.eb.sermil.modelo.DstbGd;
import br.mil.eb.sermil.modelo.DstbParametro;
import br.mil.eb.sermil.modelo.Empresa;
import br.mil.eb.sermil.modelo.EntrevistaCs;
import br.mil.eb.sermil.modelo.EstatAlistamentoEsc;
import br.mil.eb.sermil.modelo.EstatArrecadacao;
import br.mil.eb.sermil.modelo.EstatExar;
import br.mil.eb.sermil.modelo.Habilitacao;
import br.mil.eb.sermil.modelo.ImpServico;
import br.mil.eb.sermil.modelo.InfoLocal;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.modelo.Municipio;
import br.mil.eb.sermil.modelo.Ocupacao;
import br.mil.eb.sermil.modelo.Om;
import br.mil.eb.sermil.modelo.OmBoletim;
import br.mil.eb.sermil.modelo.Padrao;
import br.mil.eb.sermil.modelo.Pais;
import br.mil.eb.sermil.modelo.Perfil;
import br.mil.eb.sermil.modelo.Pgc;
import br.mil.eb.sermil.modelo.PortalMensagem;
import br.mil.eb.sermil.modelo.PostoGraduacao;
import br.mil.eb.sermil.modelo.PreAlistamento;
import br.mil.eb.sermil.modelo.Qcp;
import br.mil.eb.sermil.modelo.Qm;
import br.mil.eb.sermil.modelo.RaItens;
import br.mil.eb.sermil.modelo.RaMestre;
import br.mil.eb.sermil.modelo.RaPedido;
import br.mil.eb.sermil.modelo.Rm;
import br.mil.eb.sermil.modelo.SelBcc;
import br.mil.eb.sermil.modelo.SelJsm;
import br.mil.eb.sermil.modelo.SelTributacao;
import br.mil.eb.sermil.modelo.TaxaMulta;
import br.mil.eb.sermil.modelo.Uf;
import br.mil.eb.sermil.modelo.Usuario;

/** Implementação de fábrica abstrata de DAO para uso com o EclipseLink (JPA).
 * @author Abreu Lopes
 * @since 4.0
 * @version 5.2.8
 */
public class DaoFactoryJpaImpl extends DaoFactory {

  /** Método auxiliar para instanciar os DAO.
   * @param daoClass Classe do DAO a ser instanciado
   * @return DAO que manipula uma entidade do modelo
   */
  private GenericDaoJpaImpl<?, ?> instanciarDao(final Class<?> daoClass) {
    try {
      return (GenericDaoJpaImpl<?, ?>) daoClass.newInstance();
    } catch (Exception erro) {
      throw new RuntimeException("Erro instanciando DAO: " + daoClass, erro);
    }
  }

  // Implementar métodos definidos na fábrica abstrata DaoFactory para instanciar o DAO

  @Override
  public CepDao getCepDao() {
    return (CepDao) instanciarDao(CepDaoJpa.class);
  }
  
  @Override
  public CidadaoDao getCidadaoDao() {
    return (CidadaoDao) instanciarDao(CidadaoDaoJpa.class);
  }

  @Override
  public CidAdiamentoDao getCidAdiamentoDao() {
    return (CidAdiamentoDao) instanciarDao(CidAdiamentoDaoJpa.class);
  }

  @Override
  public CidArrecadacaoDao getCidArrecadacaoDao() {
    return (CidArrecadacaoDao) instanciarDao(CidArrecadacaoDaoJpa.class);
  }

  @Override
  public CidAuditoriaDao getCidAuditoriaDao() {
    return (CidAuditoriaDao) instanciarDao(CidAuditoriaDaoJpa.class);
  }

  @Override
  public CidAverbacaoDao getCidAverbacaoDao() {
    return (CidAverbacaoDao) instanciarDao(CidAverbacaoDaoJpa.class);
  }

  @Override
  public CidCertificadoDao getCidCertificadoDao() {
    return (CidCertificadoDao) instanciarDao(CidCertificadoDaoJpa.class);
  }

  @Override
  public CidContatoDao getCidContatoDao() {
    return (CidContatoDao) instanciarDao(CidContatoDaoJpa.class);
  }

  @Override
  public CidEventoDao getCidEventoDao() {
    return (CidEventoDao) instanciarDao(CidEventoJpa.class);
  }

  @Override
  public CidExarDao getCidExarDao() {
    return (CidExarDao) instanciarDao(CidExarJpa.class);
  }
  
  @Override
  public CidFotoDao getCidFotoDao() {
    return (CidFotoDao) instanciarDao(CidFotoDaoJpa.class);
  }

  @Override
  public CsAgendamentoDao getCsAgendamentoDao() {
    return (CsAgendamentoDao) instanciarDao(CsAgendamentoDaoJpa.class);
  }
  
  @Override
  public CsAnamneseDao getCsAnamneseDao() {
    return (CsAnamneseDao) instanciarDao(CsAnamneseDaoJpa.class);
  }

  @Override
  public CselDao getCselDao() {
    return (CselDao) instanciarDao(CselDaoJpa.class);
  }

  @Override
  public CselEnderecoDao getCselEnderecoDao() {
    return (CselEnderecoDao) instanciarDao(CselEnderecoDaoJpa.class);
  }

  @Override
  public CselFeriadoDao getCselFeriadoDao() {
    return (CselFeriadoDao) instanciarDao(CselFeriadoJpa.class);
  }

  @Override
  public CselFuncionamentoDao getCselFuncionamentoDao() {
    return (CselFuncionamentoDao) instanciarDao(CselFuncionamentoDaoJpa.class);
  }

  @Override
  public CsmDao getCsmDao() {
    return (CsmDao) instanciarDao(CsmDaoJpa.class);
  }

  @Override
  public DelegaciaDao getDelegaciaDao() {
    return (DelegaciaDao) instanciarDao(DelegaciaDaoJpa.class);
  }

  @Override
  public DominiosDao getDominiosDao() {
    return (DominiosDao) instanciarDao(DominiosDaoJpa.class);
  }

  @Override
  public DstbBolNecDao getDstbBolNecDao() {
    return (DstbBolNecDao) instanciarDao(DstbBolNecDaoJpa.class);
  }

  @Override
  public DstbExclusaoDao getDstbExclusaoDao() {
    return (DstbExclusaoDao) instanciarDao(DstbExclusaoDaoJpa.class);
  }

  @Override
  public DstbGdDao getDstbGdDao() {
    return (DstbGdDao) instanciarDao(DstbGdDaoJpa.class);
  }

  @Override
  public DstbParametroDao getDstbParametroDao() {
    return (DstbParametroDao) instanciarDao(DstbParametroDaoJpa.class);
  }

  @Override
  public EmpresaDao getEmpresaDao() {
    return (EmpresaDao) instanciarDao(EmpresaDaoJpa.class);
  }

  @Override
  public EntrevistaCsDao getEntrevistaCsDao() {
   return (EntrevistaCsDao) instanciarDao(EntrevistaCsDaoJpa.class);
  }

  @Override
  public EstatAlistamentoEscDao getEstatAlistamentoEscDao() {
    return (EstatAlistamentoEscDao) instanciarDao(EstatAlistamentoEscDaoJpa.class);
  }
  @Override
  public EstatArrecadacaoDao getEstatArrecadacaoDao() {
    return (EstatArrecadacaoDao) instanciarDao(EstatArrecadacaoDaoJpa.class);
  }

  @Override
  public EstatExarDao getEstatExarDao() {
    return (EstatExarDao) instanciarDao(EstatExarDaoJpa.class);
  }

  @Override
  public HabilitacaoDao getHabilitacaoDao() {
    return (HabilitacaoDao) instanciarDao(HabilitacaoDaoJpa.class);
  }

  @Override
  public ImpServicoDao getImpServicoDao() {
    return (ImpServicoDao) instanciarDao(ImpServicoDaoJpa.class);
  }

  @Override
  public InfoLocalDao getInfoLocalDao() {
    return (InfoLocalDao) instanciarDao(InfoLocalDaoJpa.class);
  }

  @Override
  public JsmDao getJsmDao() {
    return (JsmDao) instanciarDao(JsmDaoJpa.class);
  }

  @Override
  public MunicipioDao getMunicipioDao() {
    return (MunicipioDao) instanciarDao(MunicipioDaoJpa.class);
  }

  @Override
  public OcupacaoDao getOcupacaoDao() {
    return (OcupacaoDao) instanciarDao(OcupacaoDaoJpa.class);
  }

  @Override
  public OmDao getOmDao() {
    return (OmDao) instanciarDao(OmDaoJpa.class);
  }

  @Override
  public OmBoletimDao getOmBoletimDao() {
    return (OmBoletimDao) instanciarDao(OmBoletimDaoJpa.class);
  }
  @Override
  public PadraoDao getPadraoDao() {
    return (PadraoDao) instanciarDao(PadraoDaoJpa.class);
  }

  @Override
  public PaisDao getPaisDao() {
    return (PaisDao) instanciarDao(PaisDaoJpa.class);
  }

  @Override
  public PerfilDao getPerfilDao() {
    return (PerfilDao) instanciarDao(PerfilDaoJpa.class);
  }

   @Override
   public PgcDao getPgcDao() {
      return (PgcDao) instanciarDao(PgcDaoJpa.class);
   }

  @Override
  public PortalMensagemDao getPortalMensagemDao() {
    return (PortalMensagemDao) instanciarDao(PortalMensagemDaoJpa.class);
  }

  @Override
  public PostoGraduacaoDao getPostoGraduacaoDao() {
    return (PostoGraduacaoDao) instanciarDao(PostoGraduacaoDaoJpa.class);
  }

  @Override
  public PreAlistamentoDao getPreAlistamentoDao() {
    return (PreAlistamentoDao) instanciarDao(PreAlistamentoDaoJpa.class);
  }

  @Override
  public QcpDao getQcpDao() {
    return (QcpDao) instanciarDao(QcpDaoJpa.class);
  }

  @Override
  public QmDao getQmDao() {
    return (QmDao) instanciarDao(QmDaoJpa.class);
  }

   @Override
   public RaItensDao getRaItensDao() {
      return (RaItensDao) instanciarDao(RaItensDaoJpa.class);
   }

   @Override
   public RaMestreDao getRaMestreDao() {
    return (RaMestreDao) instanciarDao(RaMestreDaoJpa.class);
   }

   @Override
   public RaPedidoDao getRaPedidoDao() {
    return (RaPedidoDao) instanciarDao(RaPedidoDaoJpa.class);
   }

   @Override
   public RmDao getRmDao() {
      return (RmDao) instanciarDao(RmDaoJpa.class);
   }

   @Override
   public SelBccDao getSelBccDao() {
      return (SelBccDao) instanciarDao(SelBccDaoJpa.class);
   }

   @Override
   public SelJsmDao getSelJsmDao() {
      return (SelJsmDao) instanciarDao(SelJsmDaoJpa.class);
   }

   @Override
   public SelTributacaoDao getSelTributacaoDao() {
      return (SelTributacaoDao) instanciarDao(SelTributacaoDaoJpa.class);
   }

   @Override
   public TaxaMultaDao getTaxaMultaDao() {
      return (TaxaMultaDao) instanciarDao(TaxaMultaDaoJpa.class);
   }

   @Override
   public UfDao getUfDao() {
      return (UfDao) instanciarDao(UfDaoJpa.class);
   }

   @Override
   public UsuarioDao getUsuarioDao() {
      return (UsuarioDao) instanciarDao(UsuarioDaoJpa.class);
   }

   // Criar implementações concretas dos DAO a serem instanciados
   // (usando classes internas para evitar criaação de classes explícitas)

   public static class CepDaoJpa extends GenericDaoJpaImpl<Cep, String>implements CepDao {
   }

   public static class CidadaoDaoJpa extends GenericDaoJpaImpl<Cidadao, Long>implements CidadaoDao {
   }

   public static class CidAdiamentoDaoJpa extends GenericDaoJpaImpl<CidAdiamento, CidAdiamento.PK> implements CidAdiamentoDao {
   }

   public static class CidArrecadacaoDaoJpa extends GenericDaoJpaImpl<CidArrecadacao, CidArrecadacao.PK> implements CidArrecadacaoDao {
   }

   public static class CidAuditoriaDaoJpa extends GenericDaoJpaImpl<CidAuditoria, CidAuditoria.PK> implements CidAuditoriaDao {
   }

   public static class CidAverbacaoDaoJpa extends GenericDaoJpaImpl<CidAverbacao, CidAverbacao.PK> implements CidAverbacaoDao {
   }

   public static class CidCertificadoDaoJpa extends GenericDaoJpaImpl<CidCertificado, CidCertificado.PK> implements CidCertificadoDao {
   }

   public static class CidContatoDaoJpa extends GenericDaoJpaImpl<CidContato, CidContato.PK>implements CidContatoDao {
   }

   public static class CidEventoJpa extends GenericDaoJpaImpl<CidEvento, CidEvento.PK>implements CidEventoDao {
   }

   public static class CidExarJpa extends GenericDaoJpaImpl<CidExar, CidExar.PK>implements CidExarDao {
   }

   public static class CidFotoDaoJpa extends GenericDaoJpaImpl<CidFoto, Long>implements CidFotoDao {
   }

   public static class CsAgendamentoDaoJpa extends GenericDaoJpaImpl<CsAgendamento, Integer> implements CsAgendamentoDao {
   }

   public static class CsAnamneseDaoJpa extends GenericDaoJpaImpl<CsAnamnese, Long>implements CsAnamneseDao {
   }

   public static class CselDaoJpa extends GenericDaoJpaImpl<Csel, Integer>implements CselDao {
   }

   public static class CselEnderecoDaoJpa extends GenericDaoJpaImpl<CselEndereco, Integer>implements CselEnderecoDao {
   }

   public static class CselFeriadoJpa extends GenericDaoJpaImpl<CselFeriado, Integer>implements CselFeriadoDao {
   }

   public static class CselFuncionamentoDaoJpa extends GenericDaoJpaImpl<CselFuncionamento, Integer> implements CselFuncionamentoDao {
   }

   public static class CsmDaoJpa extends GenericDaoJpaImpl<Csm, Byte>implements CsmDao {
   }

   public static class DelegaciaDaoJpa extends GenericDaoJpaImpl<Delegacia, Delegacia.PK>implements DelegaciaDao {
   }

   public static class DominiosDaoJpa extends GenericDaoJpaImpl<Dominios, Dominios.PK>implements DominiosDao {
   }

   public static class DstbBolNecDaoJpa extends GenericDaoJpaImpl<DstbBolNec, DstbBolNec.PK>implements DstbBolNecDao {
   }

   public static class DstbExclusaoDaoJpa extends GenericDaoJpaImpl<DstbExclusao, Long>implements DstbExclusaoDao {
   }

   public static class DstbGdDaoJpa extends GenericDaoJpaImpl<DstbGd, DstbGd.PK>implements DstbGdDao {
   }

   public static class DstbParametroDaoJpa extends GenericDaoJpaImpl<DstbParametro, DstbParametro.PK> implements DstbParametroDao {
   }

   public static class EmpresaDaoJpa extends GenericDaoJpaImpl<Empresa, String>implements EmpresaDao {
   }

   public static class EntrevistaCsDaoJpa extends GenericDaoJpaImpl<EntrevistaCs, Long>implements EntrevistaCsDao {
   }

   public static class EstatAlistamentoEscDaoJpa extends GenericDaoJpaImpl<EstatAlistamentoEsc, Integer> implements EstatAlistamentoEscDao {
   }

   public static class EstatArrecadacaoDaoJpa extends GenericDaoJpaImpl<EstatArrecadacao, Integer> implements EstatArrecadacaoDao {
   }

   public static class EstatExarDaoJpa extends GenericDaoJpaImpl<EstatExar, EstatExar.PK>implements EstatExarDao {
   }

   public static class HabilitacaoDaoJpa extends GenericDaoJpaImpl<Habilitacao, String>implements HabilitacaoDao {
   }

   public static class ImpServicoDaoJpa extends GenericDaoJpaImpl<ImpServico, ImpServico.PK>implements ImpServicoDao {
   }

   public static class InfoLocalDaoJpa extends GenericDaoJpaImpl<InfoLocal, Byte>implements InfoLocalDao {
   }

   public static class JsmDaoJpa extends GenericDaoJpaImpl<Jsm, Jsm.PK>implements JsmDao {
   }

   public static class MunicipioDaoJpa extends GenericDaoJpaImpl<Municipio, Integer>implements MunicipioDao {
   }

   public static class OcupacaoDaoJpa extends GenericDaoJpaImpl<Ocupacao, String>implements OcupacaoDao {
   }

   public static class OmDaoJpa extends GenericDaoJpaImpl<Om, Integer>implements OmDao {
   }

   public static class OmBoletimDaoJpa extends GenericDaoJpaImpl<OmBoletim, OmBoletim.PK>implements OmBoletimDao {
   }

   public static class PadraoDaoJpa extends GenericDaoJpaImpl<Padrao, String>implements PadraoDao {
   }

   public static class PaisDaoJpa extends GenericDaoJpaImpl<Pais, Short>implements PaisDao {
   }

   public static class PerfilDaoJpa extends GenericDaoJpaImpl<Perfil, String>implements PerfilDao {
   }

   public static class PgcDaoJpa extends GenericDaoJpaImpl<Pgc, Integer>implements PgcDao {
   }

   public static class PortalMensagemDaoJpa extends GenericDaoJpaImpl<PortalMensagem, Integer> implements PortalMensagemDao {
   }

   public static class PostoGraduacaoDaoJpa extends GenericDaoJpaImpl<PostoGraduacao, String> implements PostoGraduacaoDao {
   }

   public static class PreAlistamentoDaoJpa extends GenericDaoJpaImpl<PreAlistamento, Long> implements PreAlistamentoDao {
   }

   public static class QcpDaoJpa extends GenericDaoJpaImpl<Qcp, Qcp.PK>implements QcpDao {
   }

   public static class QmDaoJpa extends GenericDaoJpaImpl<Qm, String>implements QmDao {
   }

   public static class RaItensDaoJpa extends GenericDaoJpaImpl<RaItens, RaItens.PK>implements RaItensDao {
   }

   public static class RaMestreDaoJpa extends GenericDaoJpaImpl<RaMestre, RaMestre.PK>implements RaMestreDao {
   }

   public static class RaPedidoDaoJpa extends GenericDaoJpaImpl<RaPedido, Integer>implements RaPedidoDao {
   }

   public static class RmDaoJpa extends GenericDaoJpaImpl<Rm, Byte>implements RmDao {
   }

   public static class SelBccDaoJpa extends GenericDaoJpaImpl<SelBcc, Long>implements SelBccDao {
   }

   public static class SelJsmDaoJpa extends GenericDaoJpaImpl<SelJsm, SelJsm.PK>implements SelJsmDao {
   }

   public static class SelTributacaoDaoJpa extends GenericDaoJpaImpl<SelTributacao, SelTributacao.PK> implements SelTributacaoDao {
   }

   public static class TaxaMultaDaoJpa extends GenericDaoJpaImpl<TaxaMulta, TaxaMulta.PK>implements TaxaMultaDao {
   }

   public static class UfDaoJpa extends GenericDaoJpaImpl<Uf, String>implements UfDao {
   }

   public static class UsuarioDaoJpa extends GenericDaoJpaImpl<Usuario, String>implements UsuarioDao {
   }

}
