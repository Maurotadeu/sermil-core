package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** OM Boletim de Incorporação (Tabela OM_BOLETIM).
 * @author Abreu Lopes, gardino
 * @since 4.6
 * @version 5.3.0
 */
@Entity
@Table(name="OM_BOLETIM")
@NamedQueries({
   @NamedQuery(name = "OmBoletim.listarEfetivo",    query = "SELECT c FROM Cidadao c WHERE c.vinculacaoAno = ?1 AND c.om.codigo = ?2 AND c.gptIncorp= ?3 AND c.situacaoMilitar = 12 AND NOT EXISTS (SELECT o.pk.cidadaoRa FROM OmBoletimCidadao o JOIN Cidadao c1 ON o.pk.cidadaoRa = c1.ra WHERE c1.ra = c.ra AND o.omCodigo = ?2 AND o.ano = ?4)"),
   @NamedQuery(name = "OmBoletim.listarBoletimOm",  query = "SELECT b FROM OmBoletim b where b.pk.ano= ?1 and b.pk.omCodigo= ?2"),
   @NamedQuery(name = "OmBoletim.listarBoletimGpt", query = "SELECT b FROM OmBoletim b where b.pk.ano= ?1 and b.pk.omCodigo= ?2 and b.pk.gptIncorp= ?3"),
   @NamedQuery(name = "OmBoletim.listarBoletimSU",  query = "SELECT b FROM OmBoletim b where b.pk.ano= ?1 and b.pk.omCodigo= ?2 and b.pk.gptIncorp= ?3 and b.subunidade= ?4"),
   @NamedQuery(name = "OmBoletim.listarBoletimCod", query = "SELECT b FROM OmBoletim b where b.pk.ano= ?1 and b.pk.omCodigo= ?2 and b.pk.codigo= ?3")
})
@PrimaryKey(validation=IdValidation.NULL)
public final class OmBoletim implements Comparable<OmBoletim>, Serializable {

   private static final long serialVersionUID = -5461621016766083192L;

   @EmbeddedId
   private OmBoletim.PK pk;

   private String subunidade;

   @OneToMany(mappedBy="omBoletim", fetch=FetchType.EAGER, orphanRemoval=true)
   private List<OmBoletimCidadao> omBoletimCidadao;

   public OmBoletim() {
      this.setPk(new OmBoletim.PK());
   }

   @Override
   public String toString() {
      return this.getPk().toString();
   }

   @Override
   public int compareTo(OmBoletim o) {
      return this.getPk().compareTo(o.getPk());
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
      OmBoletim other = (OmBoletim) obj;
      if (pk == null) {
         if (other.pk != null)
            return false;
      } else if (!pk.equals(other.pk))
         return false;
      return true;
   }

   public OmBoletim.PK getPk() {
      return this.pk;
   }

   public void setPk(OmBoletim.PK pk) {
      this.pk = pk;
   }

   public String getSubunidade() {
      return new StringBuilder(this.subunidade).append(" - ").append(this.getPk().getGptIncorp()).toString();
   }

   public void setSubunidade(String subunidade) {
      this.subunidade = (subunidade == null || subunidade.trim().isEmpty() ? null : subunidade);
   }

   public List<OmBoletimCidadao> getOmBoletimCidadao() {
      if (this.omBoletimCidadao != null) {
         Collections.sort(this.omBoletimCidadao);
      }
      return this.omBoletimCidadao;
   }

   public void setOmBoletimCidadao(List<OmBoletimCidadao> omBoletimCidadaos) {
      this.omBoletimCidadao = omBoletimCidadaos;
   }

   public void addOmBoletimCidadao(final OmBoletimCidadao obc) throws SermilException {
      if (this.getOmBoletimCidadao() == null) {
         this.setOmBoletimCidadao(new ArrayList<OmBoletimCidadao>(1));
      }
      if (this.getOmBoletimCidadao().contains(obc)) {
         throw new SermilException("Cidadão já está incluído no Boletim");
      }
      this.getOmBoletimCidadao().add(obc);
      if (obc.getOmBoletim() != this) {
         obc.setOmBoletim(this);
      }
   }  

   @Embeddable
   public static class PK implements Serializable {

      private static final long serialVersionUID = -1651400292072870316L;

      private Integer codigo;

      @Column(name="OM_CODIGO")
      private Integer omCodigo;

      @Column(name="GPT_INCORP")
      private String gptIncorp;

      private Integer ano;

      public PK() {
         super();
      }

      public PK(final Integer codigo, final Integer omCodigo, final String gptIncorp, final Integer ano) {
         super();
         this.setCodigo(codigo);
         this.setOmCodigo(omCodigo);
         this.setGptIncorp(gptIncorp);
         this.setAno(ano);
      }

      public int compareTo(OmBoletim.PK e) {
         int status = this.getCodigo().compareTo(e.getCodigo());
         if (status == 0 ) {
            status = this.getOmCodigo().compareTo(e.getOmCodigo());
         }
         if (status == 0 ) {
            status = this.getGptIncorp().compareTo(e.getGptIncorp());
         }
         if (status == 0 ) {
            status = this.getAno().compareTo(e.getAno());
         }
         return status;
      }

      @Override
      public String toString() {
         return new StringBuilder("COD=").append(this.getCodigo())
               .append(" - OM=").append(this.getOmCodigo())
               .append(" - Gpt=").append(this.getGptIncorp())
               .append(" - Ano=").append(this.getAno()).toString();
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((ano == null) ? 0 : ano.hashCode());
         result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
         result = prime * result
               + ((gptIncorp == null) ? 0 : gptIncorp.hashCode());
         result = prime * result + ((omCodigo == null) ? 0 : omCodigo.hashCode());
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
         if (ano == null) {
            if (other.ano != null)
               return false;
         } else if (!ano.equals(other.ano))
            return false;
         if (codigo == null) {
            if (other.codigo != null)
               return false;
         } else if (!codigo.equals(other.codigo))
            return false;
         if (gptIncorp == null) {
            if (other.gptIncorp != null)
               return false;
         } else if (!gptIncorp.equals(other.gptIncorp))
            return false;
         if (omCodigo == null) {
            if (other.omCodigo != null)
               return false;
         } else if (!omCodigo.equals(other.omCodigo))
            return false;
         return true;
      }

      public String getGptIncorp() {
         return gptIncorp;
      }

      public void setGptIncorp(String gptIncorp) {
         this.gptIncorp = gptIncorp;
      }

      public Integer getCodigo() {
         return this.codigo;
      }

      public void setCodigo(Integer codigo) {
         this.codigo = codigo;
      }

      public Integer getOmCodigo() {
         return this.omCodigo;
      }

      public void setOmCodigo(Integer omCodigo) {
         this.omCodigo = omCodigo;
      }

      public Integer getAno() {
         return ano;
      }

      public void setAno(Integer ano) {
         this.ano = ano;
      }

   }

}
