package br.mil.eb.sermil.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** DstbExclusao.
 * @author Abreu Lopes
 * @since 5.1
 * @version $Id$
 */
@Entity
@Table(name="DSTB_EXCLUSAO")
@NamedQuery(name = "Info.listar", query = "SELECT c.ra, c.nome, c.mae, c.nascimentoData, c.situacaoMilitar FROM Cidadao c JOIN DstbExclusao e ON c.ra = e.cidadaoRa ORDER BY c.nome")
public final class DstbExclusao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CIDADAO_RA")
    private Long cidadaoRa;

    public DstbExclusao() {
        super();
    }

    public DstbExclusao(Long ra) {
        this.cidadaoRa = ra;
    }

    public Long getCidadaoRa() {
        return cidadaoRa;
    }

    public void setCidadaoRa(Long cidadaoRa) {
        this.cidadaoRa = cidadaoRa;
    }

}
