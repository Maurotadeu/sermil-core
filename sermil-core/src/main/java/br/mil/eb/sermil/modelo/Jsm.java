package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.tipos.Utils;

/** Junta de Serviço Militar (JSM).
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.5
 */
@Entity
@NamedQueries({
   @NamedQuery(name = "Jsm.listarPorCsm", query = "SELECT j FROM Jsm j WHERE j.pk.csmCodigo = ?1 ORDER BY j.descricao"),
   @NamedQuery(name = "Jsm.listarPorDescricao", query = "SELECT j FROM Jsm j WHERE j.descricao LIKE CONCAT(?1,'%')"),
   @NamedQuery(name = "Jsm.listarPorMunicipio", query = "SELECT j FROM Jsm j WHERE j.municipio.codigo = ?1"),
   @NamedQuery(name = "Jsm.listarPorTributacao", query = "SELECT j.tributacao, COUNT(j) FROM Jsm j WHERE j.csm.rm.codigo = ?1 GROUP BY j.tributacao"),
   //@NamedQuery(name = "Jsm.listarPorUfAno", query = "SELECT m.descricao, m.latitude, m.longitude, j.pk.csmCodigo, j.pk.codigo, COUNT(c.ra) FROM Jsm j JOIN Municipio m ON j.municipio.codigo = m.codigo JOIN Cidadao c ON c.jsm.pk.csmCodigo = j.pk.csm.codigo AND c.jsm.pk.codigo = j.pk.codigo WHERE m.uf.sigla = ?1 AND c.vinculacao_ano = ?2 GROUP BY m.descricao, m.latitude, m.longitude, j.pk.csmCodigo, j.pk.codigo"),
   @NamedQuery(name = "Jsm.listarPorRm", query = "SELECT j FROM Jsm j WHERE EXISTS (SELECT c FROM Csm c WHERE c.codigo = j.pk.csmCodigo AND c.rm.codigo = ?1) AND j.tributacao <> 0 AND j.tributacao <> 5"),
   @NamedQuery(name = "Jsm.listarInternet", query = "SELECT j FROM Jsm j WHERE j.municipio.codigo = ?1 AND j.jsmInfo.internet = 'S'")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class Jsm implements Comparable<Jsm>, Serializable {

   private static final long serialVersionUID = 1L;

   public static final byte OM_Ativa_OFOR = 1;
   public static final byte OM_Ativa_TG = 2;
   public static final byte Tiro_de_Guerra = 3;
   public static final byte OM_Ativa = 4;
   public static final byte JSM_Desativada = 5;        


   @EmbeddedId
   private PK pk;

   private String descricao;

   private Short cs;

   private Short delsm;

   private Byte tributacao;

   private String infor;

   @OneToOne(mappedBy="jsm", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
   private JsmInfo jsmInfo;

   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name="MUNICIPIO_CODIGO", referencedColumnName="CODIGO")
   private Municipio municipio;

   @ManyToOne
   @JoinColumn(name="CSM_CODIGO", referencedColumnName="CODIGO", insertable=false, updatable=false, nullable=false)
   private Csm csm;

   public Jsm() {
      this.setPk(new Jsm.PK());
   }

   public Jsm(final Byte csm, final Short jsm) {
      this.setPk(new Jsm.PK(csm, jsm));
   }

   @Override
   public int compareTo(Jsm o) {
      return this.getPk().compareTo(o.getPk());
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.pk == null) ? 0 : this.pk.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Jsm other = (Jsm) obj;
      if (this.pk == null) {
         if (other.pk != null)
            return false;
      } else if (!this.pk.equals(other.pk))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return new StringBuilder(this.getPk().toString())
            .append(" - ")
            .append(this.getDescricao() == null ? "DESCRICAO" : this.getDescricao())
            .toString();
   }

   public Short getCs() {
      return this.cs;
   }

   public Csm getCsm() {
      return this.csm;
   }

   public Short getDelsm() {
      return this.delsm;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public String getEndereco() {
      final StringBuilder sb = new StringBuilder("");
      if (this.getJsmInfo() != null && !StringUtils.isEmpty(this.getJsmInfo().getEndereco())) {
         sb.append(this.getJsmInfo().getEndereco()).append(", ");
         if (!StringUtils.isEmpty(this.getJsmInfo().getBairro())) {
            sb.append(this.getJsmInfo().getBairro()).append(", ");
         }
      }
      sb.append(this.getMunicipio().toString());
      return sb.toString();
   }

   public String getInfor() {
      return this.infor;
   }

   public Municipio getMunicipio() {
      return this.municipio;
   }

   public Byte getTributacao() {
      return this.tributacao;
   }

   public void setCs(Short cs) {
      this.cs = cs;
   }

   public void setCsm(Csm csm) {
      this.csm = csm;
   }

   public void setDelsm(Short delsm) {
      this.delsm = delsm;
   }

   public void setDescricao(String descricao) {
      this.descricao = (descricao == null || descricao.trim().isEmpty() ? null : Utils.limpaAcento(descricao).toUpperCase());
   }

   public void setInfor(String infor) {
      this.infor = (infor != null && infor.equalsIgnoreCase("S") ? "S" : "N");
   }

   public void setMunicipio(Municipio municipio) {
      this.municipio = municipio;
   }

   public String getTelefone() {
      final StringBuilder sb = new StringBuilder("");
      if (this.getJsmInfo() != null && !StringUtils.isEmpty(this.getJsmInfo().getTelefone())) {
         sb.append(this.getJsmInfo().getTelefone());
      }
      return sb.toString();
   }
   
   public void setTributacao(Byte tributacao) {
      this.tributacao = tributacao;
   }

   public void setCsmCodigo(Byte csmCodigo) {
      this.pk.setCsmCodigo(csmCodigo);
   }

   public void setCodigo(Short codigo) {
      this.pk.setCodigo(codigo);
   }

   public Byte getCsmCodigo() {
      return this.pk.getCsmCodigo();
   }

   public Short getCodigo() {
      return this.pk.getCodigo();
   }

   public PK getPk() {
      return pk;
   }

   public void setPk(PK pk) {
      this.pk = pk;
   }

   public JsmInfo getJsmInfo() {
      return jsmInfo;
   }

   public void setJsmInfo(JsmInfo jsmInfo) {
      this.jsmInfo = jsmInfo;
   }

   /** Chave primária (PK) de Jsm.
    * @author Abreu Lopes
    * @since 3.0
    * @version 5.2.5
    */
   @Embeddable
   public static class PK implements Comparable<Jsm.PK>, Serializable {

      private static final long serialVersionUID = -1983860765661998247L;

      @Column(name = "CSM_CODIGO")
      private Byte csmCodigo;

      private Short codigo;

      public PK() {
         super();
      }

      public PK(final Byte csmCodigo, final Short codigo) {
         this.setCodigo(codigo);
         this.setCsmCodigo(csmCodigo);
      }

      @Override
      public int compareTo(PK o) {
         return this.getCsmCodigo().compareTo(o.getCsmCodigo()) == 0 ? this.getCodigo().compareTo(o.getCodigo()) : this.getCsmCodigo().compareTo(o.getCsmCodigo());
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result
               + ((this.codigo == null) ? 0 : this.codigo.hashCode());
         result = prime * result
               + ((this.csmCodigo == null) ? 0 : this.csmCodigo.hashCode());
         return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         PK other = (PK) obj;
         if (this.codigo == null) {
            if (other.codigo != null)
               return false;
         } else if (!this.codigo.equals(other.codigo))
            return false;
         if (this.csmCodigo == null) {
            if (other.csmCodigo != null)
               return false;
         } else if (!this.csmCodigo.equals(other.csmCodigo))
            return false;
         return true;
      }

      @Override
      public String toString() {
         return new StringBuilder()
               .append(this.getCsmCodigo() == null ? "00" : new DecimalFormat("00").format(this.getCsmCodigo()))
               .append("/")
               .append(this.getCodigo() == null ? "000" : new DecimalFormat("000").format(this.getCodigo()))
               .toString();
      }

      public Short getCodigo() {
         return this.codigo;
      }

      public Byte getCsmCodigo() {
         return this.csmCodigo;
      }

      public void setCodigo(Short codigo) {
         this.codigo = codigo;
      }

      public void setCsmCodigo(Byte csmCodigo) {
         this.csmCodigo = csmCodigo;
      }

   }

}
