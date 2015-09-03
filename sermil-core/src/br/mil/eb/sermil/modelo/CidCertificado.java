package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Certificado Militar.
 * @author Abreu Lopes, Anselmo
 * @since 3.0
 * @version 5.2.3
 */
@Entity
@Table(name = "CID_CERTIFICADO")
@NamedQueries({
    @NamedQuery(name = "Certificado.cidadaoTemCdi", query = " SELECT c.numero FROM CidCertificado c WHERE c.cidadao.ra = ?1 and c.pk.tipo in(3,4,6) ")
})
public final class CidCertificado implements Comparable<CidCertificado>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -891971755849986133L;

    public final static Byte RESERVISTA_1_CATEGORIA_TIPO = 1;
    public final static Byte RESERVISTA_2_CATEGORIA_TIPO = 2;
    public final static Byte DISPENSA_DE_INCORPORACAO_PLANO = 3;
    public final static Byte DISPENSA_DE_INCORPORACAO_COMPUTADOR = 4;
    public final static Byte ISENCAO = 5;
    public final static Byte DISPENSA_DE_INCORPORACAO_INFOR = 6;

    @EmbeddedId
    private CidCertificado.PK pk;

    private String motivo;

    private Integer numero;

    private String responsavel;

    private String serie;

    @Column(name = "SITUACAO_ESPECIAL")
    private String situacaoEspecial;

    private String entregue;

    private String anulado;

    @ManyToOne
    @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false, nullable = false)
    private Cidadao cidadao;

    @ManyToOne
    @JoinColumn(name = "OM_CODIGO", updatable = false, nullable = false)
    private Om om;

    public CidCertificado() {
        this.setPk(new CidCertificado.PK());
    }

    public CidCertificado(final Long ra, final Byte tipo, final Date data) {
        this.setPk(new CidCertificado.PK(ra, tipo, data));
    }

    @Override
    public int compareTo(CidCertificado o) {
        return this.getPk().compareTo(o.getPk());
    }

    @Override
    public String toString() {
        return this.getPk().toString();
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
        CidCertificado other = (CidCertificado) obj;
        if (this.pk == null) {
            if (other.pk != null)
                return false;
        } else if (!this.pk.equals(other.pk))
            return false;
        return true;
    }

    public void decode(String linha) throws Exception {
        this.getPk().setCidadaoRa(Long.valueOf(linha.substring(2, 14)));
        this.getPk().setTipo(Byte.valueOf(linha.substring(14, 16)));
        final Calendar data = Calendar.getInstance();
        data.set(Integer.parseInt(linha.substring(20, 24)), Integer.parseInt(linha.substring(18, 20)) - 1, Integer.parseInt(linha.substring(16, 18)));
        this.getPk().setData(data.getTime());
        this.setNumero(Integer.valueOf(linha.substring(24, 35).replaceAll("[^0-9 ]", "").trim()));
        this.setSerie(linha.substring(24, 35).replaceAll("[^A-Z ]", ""));
        this.setResponsavel("Módulo JSM");
    }

    public Cidadao getCidadao() {
        return cidadao;
    }

    public String getMotivo() {
        return motivo;
    }

    public Integer getNumero() {
        return numero;
    }

    public Om getOm() {
        return om;
    }

    public CidCertificado.PK getPk() {
        return pk;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getSerie() {
        return serie;
    }

    public String getSituacaoEspecial() {
        return situacaoEspecial;
    }

    public void setCidadao(Cidadao cid) {
        this.cidadao = cid;
        if (!cid.getCidCertificadoCollection().contains(this)) {
            cid.getCidCertificadoCollection().add(this);
        }
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setOm(Om om) {
        this.om = om;
    }

    public void setPk(CidCertificado.PK pk) {
        this.pk = pk;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setSituacaoEspecial(String situacaoEspecial) {
        this.situacaoEspecial = situacaoEspecial;
    }

    public String getEntregue() {
        return entregue;
    }

    public void setEntregue(String entregue) {
        this.entregue = entregue;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado;
    }

    /** Chave primária (PK) de CidCertificado.
     * @author Abreu Lopes
     * @since 3.0
     * @version 5.2.3
     */
    @Embeddable
    public static class PK implements Comparable<CidCertificado.PK>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 3544350334696621626L;

        @Column(name = "CIDADAO_RA")
        private Long cidadaoRa;

        @Temporal(TemporalType.DATE)
        @Column(name = "DATA")
        private Date data;

        @Column(name = "TIPO")
        private Byte tipo;

        public PK() {
            super();
        }

        public PK(final Long cidadaoRa, final Byte tipo, final Date data) {
            super();
            this.setCidadaoRa(cidadaoRa);
            this.setTipo(tipo);
            this.setData(data);
        }

        @Override
        public int compareTo(CidCertificado.PK o) {
            int status = this.getCidadaoRa().compareTo(o.getCidadaoRa());
            if (status == 0) {
                status = this.getData().compareTo(o.getData());
                if (status == 0) {
                    status = this.getTipo().compareTo(o.getTipo());
                }
            }
            return status;
        }

        @Override
        public String toString() {
            return new StringBuilder(this.getCidadaoRa() == null ? "RA" : this.getCidadaoRa().toString())
                    .append(" - ").append(this.getTipo() == null ? "TIPO" : this.getTipo())
                    .append(" - ").append(this.getData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getData()))
                    .toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
            result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
            result = prime * result + ((this.tipo == null) ? 0 : this.tipo.hashCode());
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
            if (this.cidadaoRa == null) {
                if (other.cidadaoRa != null)
                    return false;
            } else if (!this.cidadaoRa.equals(other.cidadaoRa))
                return false;
            if (this.data == null) {
                if (other.data != null)
                    return false;
            } else if (!this.data.equals(other.data))
                return false;
            if (this.tipo == null) {
                if (other.tipo != null)
                    return false;
            } else if (!this.tipo.equals(other.tipo))
                return false;
            return true;
        }

        public Long getCidadaoRa() {
            return this.cidadaoRa;
        }

        public Date getData() {
            return this.data;
        }

        public Byte getTipo() {
            return this.tipo;
        }

        public void setCidadaoRa(Long cidadaoRa) {
            this.cidadaoRa = cidadaoRa;
        }

        public void setData(Date data) {
            Calendar cal = Calendar.getInstance();
            if (cal.getTime().before(data)) {
                throw new IllegalArgumentException("Data maior que a data atual.");
            } else {
                cal.set(1950, 0, 1); // 01-01-1950
                if (cal.getTime().after(data)) {
                    throw new IllegalArgumentException("Data menor que 01/01/1950.");
                }
            }
            this.data = data;
        }

        public void setTipo(Byte tipo) {
            this.tipo = tipo;
        }

    }

}
