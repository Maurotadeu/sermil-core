package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.EnderecoJaExisteException;
import br.mil.eb.sermil.core.exceptions.EnderecoNaoExisteException;
import br.mil.eb.sermil.tipos.Utils;

/** Entidade Município.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.6
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "Municipio.listarPorUf", query = "SELECT m FROM Municipio m WHERE m.uf.sigla LIKE ?1 ORDER BY m.descricao"),
  @NamedQuery(name = "Municipio.listarPorDescricao", query = "SELECT m FROM Municipio m WHERE m.descricao LIKE CONCAT(?1,'%') ORDER BY m.descricao"),
  @NamedQuery(name = "Municipio.listarPorRmCsm", query = "SELECT DISTINCT m.codigo, m.descricao, m.uf.sigla, m.sigla, m.ddd, c.codigo, c.rm.codigo FROM Municipio m, Jsm j, Csm c WHERE m.codigo = j.municipio.codigo AND j.csm.codigo = c.codigo AND m.descricao LIKE CONCAT(?1,'%') ORDER BY m.descricao")
})
@PrimaryKey(validation = IdValidation.NULL)
public final class Municipio implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = -9051795322080254128L;

   @Id
   private Integer codigo;

   private String ddd;

   private String descricao;

   private String sigla;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "UF_SIGLA", nullable = false)
   private Uf uf;

   /** Endereços de CS. */
   @OneToMany(mappedBy = "municipio", fetch = FetchType.EAGER)
   private List<CsEndereco> enderecosDeCsel;

   public Municipio() {
   }

   public Municipio(final Integer codigo, final String descricao, final Uf uf) {
      this.setCodigo(codigo);
      this.setDescricao(descricao);
      this.setUf(uf);
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getDescricao() == null ? "MUNICIPIO" : this.getDescricao()).append(" - ").append(this.getUf() == null ? "UF" : this.getUf()).toString();
   }

   public Integer getCodigo() {
      return this.codigo;
   }

   public String getDdd() {
      return this.ddd;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public String getSigla() {
      return this.sigla;
   }

   public Uf getUf() {
      return this.uf;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public void setDdd(String ddd) {
      this.ddd = (ddd == null || ddd.trim().isEmpty() ? null : ddd.trim());
   }

   public void setDescricao(String descricao) {
      this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : Utils.limpaAcento(descricao).toUpperCase());
   }

   public void setSigla(String sigla) {
      this.sigla = (sigla == null || sigla.isEmpty() ? null : sigla.trim().toUpperCase());
   }

   public void setUf(Uf uf) {
      this.uf = uf;
   }

   public List<CsEndereco> getEnderecosDeCsel() {
      return enderecosDeCsel;
   }

   public void setEnderecosDeCsel(List<CsEndereco> enderecosDeCsel) {
      this.enderecosDeCsel = enderecosDeCsel;
   }

   public void addEnderecoDeCsel(CsEndereco endereco) throws EnderecoJaExisteException {
      if (this.enderecosDeCsel.contains(endereco))
         throw new EnderecoJaExisteException();
      if (this.enderecosDeCsel == null)
         this.enderecosDeCsel = new ArrayList<CsEndereco>();
      this.enderecosDeCsel.add(endereco);
      if (endereco.getMunicipio() == null || endereco.getMunicipio() != this)
         endereco.setMunicipio(this);
   }
   
   public void removeEnderecoDeCsel(CsEndereco endereco) throws EnderecoNaoExisteException {
      if(!this.enderecosDeCsel.contains(endereco))
         throw new EnderecoNaoExisteException();
      this.enderecosDeCsel.remove(endereco);
      endereco.setMunicipio(null);
   }
}
