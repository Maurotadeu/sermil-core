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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Exercício de Apresentação da Reserva.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id$
 */
@Entity
@Table(name = "CID_EXAR")
public final class CidExar implements Comparable<CidExar>, Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 4626180614113227696L;

    @EmbeddedId
    private CidExar.PK pk;

    @Column(name = "APRESENTACAO_DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date apresentacaoData;

    @Column(name = "APRESENTACAO_FORMA")
    private Byte apresentacaoForma;

    @Column(name = "APRESENTACAO_TIPO")
    private Byte apresentacaoTipo;

    private String ip;

    //  @ManyToOne, desativado para testar relacionamento unidirecional com a classe Cidadao
    //  @JoinColumn(name = "CIDADAO_RA", insertable = false, updatable = false)
    //  private Cidadao cidadao;

    @ManyToOne
    @JoinColumn(name = "OM_CODIGO")
    private Om om;

    @ManyToOne
    @JoinColumn(name = "PAIS_CODIGO")
    private Pais pais;

    @ManyToOne
    @JoinColumn(name = "MUNICIPIO_CODIGO")
    private Municipio municipio;

    public CidExar() {
        this.setPk(new CidExar.PK());
    }

    public CidExar(final Long ra, final Byte nr) {
        this.setPk(new CidExar.PK(ra, nr));
    }

    public CidExar(final Long ra, final Byte nr, final String ip) {
        this(ra, nr);
        this.setIp(ip);
    }

    @Override
    public int compareTo(CidExar o) {
        return this.getPk().compareTo(o.getPk());
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getPk().toString())
        .append(" - ")
        .append(this.getApresentacaoData() == null ? "DATA" : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getApresentacaoData()))
        .toString();
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
        CidExar other = (CidExar) obj;
        if (this.pk == null) {
            if (other.pk != null)
                return false;
        } else if (!this.pk.equals(other.pk))
            return false;
        return true;
    }

    public Date getApresentacaoData() {
        return this.apresentacaoData;
    }

    public Byte getApresentacaoForma() {
        return this.apresentacaoForma;
    }

    public Byte getApresentacaoTipo() {
        return this.apresentacaoTipo;
    }

    public String getIp() {
        return this.ip;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public Om getOm() {
        return this.om;
    }

    public Pais getPais() {
        return this.pais;
    }

    public CidExar.PK getPk() {
        return this.pk;
    }

    public void setApresentacaoData(Date data) {
        final Calendar cal = Calendar.getInstance();
        if (cal.getTime().before(data)) {
            throw new IllegalArgumentException("Data maior que a data atual.");
        } else {
            cal.set(1970, 0, 1); // 01-01-1970
            if (cal.getTime().after(data)) {
                throw new IllegalArgumentException("Data menor que 01/01/1970.");
            }
        }
        this.apresentacaoData = data;
    }

    public void setApresentacaoForma(Byte apresentacaoForma) {
        this.apresentacaoForma = apresentacaoForma;
    }

    public void setApresentacaoTipo(Byte apresentacaoTipo) {
        this.apresentacaoTipo = apresentacaoTipo;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public void setOm(Om om) {
        this.om = om;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public void setPk(CidExar.PK pk) {
        this.pk = pk;
    }

    /** Chave primária (PK) de CidExar.
     * @author Abreu Lopes
     * @since 3.0
     * @version $Id$
     */
    @Embeddable
    public static class PK implements Comparable<CidExar.PK>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 7569018938972552836L;

        @Column(name = "APRESENTACAO_QTD")
        private Byte apresentacaoQtd;

        @Column(name = "CIDADAO_RA")
        private Long cidadaoRa;

        public PK() {
            super();
        }

        public PK(final Long cidadaoRa, final Byte apresentacaoQtd) {
            super();
            this.setApresentacaoQtd(apresentacaoQtd);
            this.setCidadaoRa(cidadaoRa);
        }

        @Override
        public String toString() {
            return new StringBuilder(this.getCidadaoRa() == null ? "RA" : this.getCidadaoRa().toString())
            .append(" - ").append(this.getApresentacaoQtd() == null ? "NR APRES" : this.getApresentacaoQtd())
            .toString();
        }

        @Override
        public int compareTo(CidExar.PK e) {
            int status = this.getCidadaoRa().compareTo(e.getCidadaoRa());
            if (status == 0 ) {
                status = this.getApresentacaoQtd().compareTo(e.getApresentacaoQtd());
            }
            return status;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime
                    * result
                    + ((this.apresentacaoQtd == null) ? 0 : this.apresentacaoQtd
                            .hashCode());
            result = prime * result
                    + ((this.cidadaoRa == null) ? 0 : this.cidadaoRa.hashCode());
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
            if (this.apresentacaoQtd == null) {
                if (other.apresentacaoQtd != null)
                    return false;
            } else if (!this.apresentacaoQtd.equals(other.apresentacaoQtd))
                return false;
            if (this.cidadaoRa == null) {
                if (other.cidadaoRa != null)
                    return false;
            } else if (!this.cidadaoRa.equals(other.cidadaoRa))
                return false;
            return true;
        }

        public Byte getApresentacaoQtd() {
            return this.apresentacaoQtd;
        }

        public Long getCidadaoRa() {
            return this.cidadaoRa;
        }

        public void setApresentacaoQtd(Byte apresentacaoQtd) {
            this.apresentacaoQtd = apresentacaoQtd;
        }

        public void setCidadaoRa(Long cidadaoRa) {
            this.cidadaoRa = cidadaoRa;
        }

    }

}
