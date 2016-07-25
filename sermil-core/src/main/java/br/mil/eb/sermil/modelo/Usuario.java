package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.tipos.Cpf;

/** Entidade Usuário.
 * @author Abreu Lopes, Daniel Gardino
 * @since 3.0
 * @version 5.4.5
 */
@Entity
@Table(name = "USUARIO")
@NamedQueries({
   @NamedQuery(name = "Usuario.listarPorNome", query = "SELECT u FROM Usuario u WHERE u.nome LIKE CONCAT(?1,'%')"),
   @NamedQuery(name = "Usuario.listarPorEmail", query = "SELECT u FROM Usuario u WHERE u.email = ?1"),
   @NamedQuery(name = "Usuario.listarPorOm", query = "SELECT u FROM Usuario u WHERE u.om.codigo = ?1")
})
public final class Usuario extends User implements Serializable {

   private static final long serialVersionUID = 4022352622571265315L;

   @Id
   private String cpf;

   private String nome;

   private String email;

   private String senha;

   private String telefone;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "ACESSO_DATA")
   private Date acessoData;

   @Column(name="ACESSO_IP")
   private String acessoIp;

   @Column(name="ACESSO_QTD")
   private Integer acessoQtd;

   @Transient
   private String confirma;

   @ManyToOne
   @JoinColumn(name="OM_CODIGO", referencedColumnName="CODIGO")
   private Om om;

   @ManyToOne
   @JoinColumn(name="POSTO_GRADUACAO_CODIGO", referencedColumnName="CODIGO")
   private PostoGraduacao postoGraduacao;

   @OneToMany(mappedBy="usuario", fetch=FetchType.EAGER, orphanRemoval=true)
   private List<UsuarioPerfil> usuarioPerfilCollection;

   private String ativo;

   @Column(name="TENTATIVAS_LOGIN")
   private int tentativaslogin;

   public Usuario() {
      super("N/A","N/D", new ArrayList<GrantedAuthority>(1)); 
   }

   public Usuario(String cpf) {
      this();
      this.cpf = cpf;
   }

   public Usuario(String cpf, String senha, Collection<GrantedAuthority> roles) {
      super(cpf, senha, roles);
      this.cpf = cpf;
      this.senha = senha;
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getCpf() == null ? "CPF" : this.getCpf()).append(" - ").append(this.getNome() == null ? "NOME" : this.getNome()).toString();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      Usuario other = (Usuario) obj;
      if (cpf == null) {
         if (other.cpf != null)
            return false;
      } else if (!cpf.equals(other.cpf))
         return false;
      return true;
   }

   @Override
   public Collection<GrantedAuthority> getAuthorities() {
      final List<GrantedAuthority> lista = new ArrayList<GrantedAuthority>();
      for(UsuarioPerfil p : this.getUsuarioPerfilCollection()) {
         lista.add(new SimpleGrantedAuthority(p.getPk().getPerfil()));
      }
      return lista;
   }

   @Override
   public String getPassword() {
      return this.getSenha();
   }

   @Override
   public String getUsername() {
      return this.getCpf();
   }

   @Override
   public boolean isAccountNonExpired() {
      final Calendar hoje = Calendar.getInstance();
      final Calendar login = Calendar.getInstance();
      login.setTime(this.getAcessoData());
      login.add(Calendar.MONTH, 3);
      return hoje.before(login);
   }

   @Override
   public boolean isAccountNonLocked() {
      return this.tentativaslogin < 5 ;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return "S".equals(ativo) ? true : false;
   }

   public int getTentativaslogin() {
      return tentativaslogin;
   }

   public void setTentativaslogin(int tentativaslogin) {
      this.tentativaslogin = tentativaslogin;
   }

   public void setAtivo(String ativo) {
      this.ativo = ativo;
   }

   public String getAtivo() {
      return this.ativo;
   }

   public Date getAcessoData() {
      return this.acessoData;
   }

   public String getAcessoIp() {
      return this.acessoIp;
   }

   public Integer getAcessoQtd() {
      return this.acessoQtd;
   }

   public String getConfirma() {
      return confirma;
   }

   public String getCpf() {
      return this.cpf;
   }

   public String getEmail() {
      return this.email;
   }

   public String getNome() {
      return this.nome;
   }

   public Om getOm() {
      return om;
   }

   public PostoGraduacao getPostoGraduacao() {
      return postoGraduacao;
   }

   public String getSenha() {
      return this.senha;
   }

   public String getTelefone() {
      return this.telefone;
   }

   public List<UsuarioPerfil> getUsuarioPerfilCollection() {
      return this.usuarioPerfilCollection;
   }

   public void setAcessoData(Date acessoData) {
      this.acessoData = acessoData;
   }

   public void setAcessoIp(String acessoIp) {
      this.acessoIp = acessoIp;
   }

   public void setAcessoQtd(Integer acessoQtd) {
      this.acessoQtd = acessoQtd;
   }

   public void setConfirma(String confirma) {
      this.confirma = confirma;
   }

   public void setCpf(String cpf) throws SermilException {
      this.cpf = (cpf == null || cpf.isEmpty() ? null : cpf.trim());
      if (this.cpf != null) {
         if (!Cpf.isCpf(this.cpf)) {
            throw new SermilException("CPF inválido.");
         }
      }
   }

   public void setEmail(String email) {
      this.email = (email == null || email.trim().isEmpty() ? null : email.trim().toLowerCase());
   }

   public void setNome(String nome) {
      this.nome = (nome == null || nome.trim().isEmpty() ? null : nome.trim().toUpperCase());
   }

   public void setOm(Om om) {
      this.om = om;
   }

   public void setPostoGraduacao(PostoGraduacao postoGraduacao) {
      this.postoGraduacao = postoGraduacao;
   }

   public void setSenha(String senha) {
      this.senha = senha;
   }

   public void setTelefone(String telefone) {
      this.telefone = (telefone == null || telefone.trim().isEmpty() ? null : telefone.trim());
   }

   public void setUsuarioPerfilCollection(List<UsuarioPerfil> usuarioPerfilCollection) {
      this.usuarioPerfilCollection = usuarioPerfilCollection;
   }

   public void addUsuarioPerfil(final UsuarioPerfil p) throws SermilException {
      if (this.getUsuarioPerfilCollection() == null) {
         this.setUsuarioPerfilCollection(new ArrayList<UsuarioPerfil>(1));
      }
      if (this.getUsuarioPerfilCollection().contains(p)) {
         throw new SermilException("Perfil já está incluído");
      }
      this.getUsuarioPerfilCollection().add(p);
      if (p.getUsuario() != this) {
         p.setUsuario(this);
      }
   }

   public Boolean isAdmin() {
      final List<UsuarioPerfil> perfis = this.getUsuarioPerfilCollection();
      if (perfis != null && !perfis.isEmpty()) {
         for (UsuarioPerfil p : perfis) {
            if (p.getPerfil().getCodigo().equals("adm")) {
               return true;
            }
         }
      }
      return false;
   }

}
