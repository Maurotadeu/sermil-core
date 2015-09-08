package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 * Periodos e Locais de funcionamento das CSs.
 * 
 * @author Anselmo S Ribeiro
 * @since 5.2.3
 */
@Entity
@PrimaryKey(validation = IdValidation.NEGATIVE)
@Table(name = "CSEL_ENDERECO")
@NamedQueries({ 
   @NamedQuery(name = "Endereco.listar", query = "select f from CselFuncionamento f where f.anoBase = ?1 and f.csel.codigo = ?2 ") 
   })
public final class CselEndereco implements Serializable {

   /** serialVersionUID.*/
   private static final long serialVersionUID = 2174684503038866912L;

   @Id
   private Integer codigo;

   @Column(length=100, nullable=false)
   private String endereco;

   @Column(length=100, nullable=true )
   private String bairro;

   @Column
   private String cep ;
   
   @ManyToOne
   @JoinColumn(name="municipio_id", nullable=false, insertable=false, updatable=false)
   private Municipio municipio;


   public CselEndereco() {
      super();
   }

   @Override
   public String toString() {
      return new StringBuilder(endereco).append(", ").append(bairro).append(", ").append(municipio).append(", CEP: ").append(cep).toString();
   }

   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   public String getEndereco() {
      return endereco;
   }

   public void setEndereco(String endereco) {
      this.endereco = endereco;
   }

   public String getBairro() {
      return bairro;
   }

   public void setBairro(String bairro) {
      this.bairro = bairro;
   }

   public String getCep() {
      return cep;
   }

   public void setCep(String cep) {
      this.cep = cep;
   }

   public Municipio getMunicipio() {
      return municipio;
   }

   public void setMunicipio(Municipio municipio) {
      this.municipio = municipio;
   }

}
