package br.mil.eb.sermil.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/** Pedido de RA.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4
 */
@Entity
@Table(name = "RA_PEDIDO")
@NamedQueries({
  @NamedQuery(name = "RaPedido.listarPorCsm", query = "SELECT p FROM RaPedido p WHERE p.csm.codigo = ?1 AND p.aprovado = ?2 order by p.numero desc"),
  @NamedQuery(name = "RaPedido.listarPorCsmAberto", query = "SELECT p FROM RaPedido p WHERE p.csm.codigo = ?1 AND  p.aprovado = \"N\"  order by p.numero desc"),
  @NamedQuery(name = "RaPedido.listarPorStatus", query = "SELECT p FROM RaPedido p WHERE p.aprovado = ?1  order by p.numero desc"),
  @NamedQuery(name = "RaPedido.excluirItens", query = "DELETE FROM RaItens i WHERE i.pk.raPedidoNumero = ?1"),
  @NamedQuery(name = "RaPedido.listarAprovadosPorAno", query = "SELECT p FROM RaPedido p WHERE p.aprovado = \"S\" order by p.numero desc"),
})
public final class RaPedido implements Serializable {

  private static final long serialVersionUID = 7259444335089719189L;

  public static final String APROVADO_NAO  = "N";
  public static final String APROVADO_SIM  = "S";
  
  public static final String PROCESSADO_NAO  = "N";
  public static final String PROCESSADO_SIM  = "S";

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="RA_PEDIDO")
  @TableGenerator(name="RA_PEDIDO", allocationSize=1)
  private Integer numero;

  @ManyToOne(optional = false)
  @JoinColumn(name = "CSM_CODIGO")
  private Csm csm;

  @Temporal(TemporalType.TIMESTAMP)
  private Date data;

  private String aprovado;

  private String processado;

  @Transient
  private String ymd;

  @OneToMany(mappedBy="raPedido", fetch=FetchType.LAZY, orphanRemoval=true)
  private List<RaItens> raItensCollection;

  public RaPedido() {
    this.setAprovado("N");
    this.setProcessado("N");
    this.setData(new Date());
  }

  public RaPedido(final Csm csm) {
    this();
    this.setCsm(csm);
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getNumero() == null ? "NR" : this.getNumero().toString())
    .append(" - ")
    .append(this.getCsm() == null ? "CSM" : this.getCsm())
    .append(" - ")
    .append(this.getData() == null ? "DATA" : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(this.getData()))
    .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
    final RaPedido other = (RaPedido) obj;
    if (numero == null) {
      if (other.numero != null)
        return false;
    } else if (!numero.equals(other.numero))
      return false;
    return true;
  }

  public List<RaItens> getRaItensCollection() {
    return this.raItensCollection;
  }

  public void setRaItensCollection(List<RaItens> raItensCollection) {
    this.raItensCollection = raItensCollection;
  }

  public Integer getNumero() {
    return this.numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public Csm getCsm() {
    return this.csm;
  }

  public void setCsm(Csm csm) {
    this.csm = csm;
  }

  public String getAprovado() {
    return this.aprovado;
  }

  public void setAprovado(String aprovado) {
    this.aprovado = aprovado;
  }

  public String getProcessado() {
    return this.processado;
  }

  public void setProcessado(String processado) {
    this.processado = processado;
  }

  public Date getData() {
    return this.data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public void addItem(RaItens item) {
    item.setRaPedido(this);
    this.getRaItensCollection().add(item);
  }

  public String getYmd() {
    return ymd;
  }

  public void setYmd(String ymd) {
    this.ymd = ymd;
  }

}
