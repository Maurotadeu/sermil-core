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

/**
 * Município.
 * 
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id: Municipio.java 1637 2011-11-25 13:52:11Z wlopes $
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Municipio.listarPorUf", query = "SELECT m FROM Municipio m WHERE m.uf.sigla LIKE ?1 ORDER BY m.descricao"),
      @NamedQuery(name = "Municipio.listarPorDescricao", query = "SELECT m FROM Municipio m WHERE m.descricao LIKE CONCAT(?1,'%') ORDER BY m.descricao"),
      @NamedQuery(name = "Municipio.listarPorRmCsm", query = "SELECT DISTINCT m.codigo, m.descricao, m.uf.sigla, m.sigla, m.ddd, c.codigo, c.rm.codigo FROM Municipio m, Jsm j, Csm c WHERE m.codigo = j.municipio.codigo AND j.csm.codigo = c.codigo AND m.descricao LIKE CONCAT(?1,'%') ORDER BY m.descricao")
      // @NamedQuery(name = "Municipio.gruparPorMesoregiao", query = "SELECT m.mesoregiao, COUNT(m)
      // FROM Cidadao c JOIN Municipio m ON c.municipioResidencia.codigo = m.codigo WHERE
      // c.vinculacaoAno = ?1 GROUP BY m.mesoregiao ORDER BY COUNT(m) DESC"),
})
@PrimaryKey(validation = IdValidation.NULL)
public final class Municipio implements Serializable {

   /** serialVersionUID. */
   private static final long serialVersionUID = 5550910662728300648L;

   @Id
   private Integer codigo;

   private String ddd;

   private String descricao;

   private String sigla;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "UF_SIGLA", nullable = false)
   private Uf uf;

   public Municipio() {
   }

   public Municipio(final Integer codigo, final String descricao, final Uf uf) {
      this.setCodigo(codigo);
      this.setDescricao(descricao);
      this.setUf(uf);
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

   @Override
   public String toString() {
      return new StringBuilder(this.getDescricao() == null ? "DESCRICAO" : this.getDescricao()).append(" - ").append(this.getUf() == null ? "UF" : this.getUf()).toString();
   }

   /*
    * Enderecos de Csel collection
    */

   @OneToMany(mappedBy = "municipio", fetch = FetchType.EAGER)
   private List<CselEndereco> enderecosDeCsel;

   public List<CselEndereco> getEnderecosDeCsel() {
      return enderecosDeCsel;
   }

   public void setEnderecosDeCsel(List<CselEndereco> enderecosDeCsel) {
      this.enderecosDeCsel = enderecosDeCsel;
   }

   public void addEnderecoDeCsel(CselEndereco endereco) throws EnderecoJaExisteException {
      if (this.enderecosDeCsel.contains(endereco))
         throw new EnderecoJaExisteException();
      if (this.enderecosDeCsel == null)
         this.enderecosDeCsel = new ArrayList<CselEndereco>();
      this.enderecosDeCsel.add(endereco);
      if (endereco.getMunicipio() == null || endereco.getMunicipio() != this)
         endereco.setMunicipio(this);
   }
   
   public void removeEnderecoDeCsel(CselEndereco endereco) throws EnderecoNaoExisteException{
      if(!this.enderecosDeCsel.contains(endereco))
         throw new EnderecoNaoExisteException();
      this.enderecosDeCsel.remove(endereco);
      endereco.setMunicipio(null);
   }
}
