package br.mil.eb.sermil.tipos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.Cidadao;

public class Cam implements Serializable {

  private static final long serialVersionUID = 1L;

  private Cidadao cidadao;

  private CidDocApres documento;

  private String autoridade;
  
  private Date retorno;
  
  public Cam() {
    super();
  }
  
  @Override
  public String toString() {
    return new StringBuilder("RA").append("=").append(this.getCidadao().getRa()).append(System.lineSeparator())
        .append("Nome").append("=").append(this.getCidadao().getNome()).append(System.lineSeparator())
        .append("Pai").append("=").append(this.getCidadao().getPai()).append(System.lineSeparator())
        .append("Mãe").append("=").append(this.getCidadao().getMae()).append(System.lineSeparator())
        .append("Dt Nasc").append("=").append(new SimpleDateFormat("dd/MM/yyyy").format(this.getCidadao().getNascimentoData())).append(System.lineSeparator())
        .append("Mun Nasc").append("=").append(this.getCidadao().getMunicipioNascimento()).append(System.lineSeparator())
        .append("Endereço").append("=").append(this.getCidadao().getEndereco()).append(System.lineSeparator())
        .append("Bairro").append("=").append(this.getCidadao().getBairro()).append(System.lineSeparator())
        .append("CEP").append("=").append(this.getCidadao().getCep()).append(System.lineSeparator())
        .append("Mun Resid").append("=").append(this.getCidadao().getMunicipioResidencia()).append(System.lineSeparator())
        .append("Doc Apres").append("=").append(this.getDocumento()).append(System.lineSeparator())
        .append("Autoridade").append("=").append(this.getAutoridade()).append(System.lineSeparator())
        .append("Retorno").append("=").append(new SimpleDateFormat("dd/MM/yyyy").format(this.getRetorno())).append(System.lineSeparator())
        .toString();
  }
  
  public Cidadao getCidadao() {
    return cidadao;
  }

  public void setCidadao(Cidadao cidadao) {
    this.cidadao = cidadao;
  }

  public CidDocApres getDocumento() {
    return documento;
  }

  public void setDocumento(CidDocApres documento) {
    this.documento = documento;
  }

  public String getAutoridade() {
    return autoridade;
  }

  public void setAutoridade(String autoridade) {
    this.autoridade = autoridade;
  }

  public Date getRetorno() {
    return retorno;
  }

  public void setRetorno(Date retorno) {
    this.retorno = retorno;
  }
  
}
