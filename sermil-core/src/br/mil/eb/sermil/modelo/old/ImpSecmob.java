package br.mil.eb.sermil.modelo.old;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/** Informações da Seção Mobilizadora.
 * @author Neckel
 * @since 3.0
 * @version $Id: ImpSecmob.java 2441 2014-05-29 17:16:31Z wlopes $
 */
@Entity
@Table(name = "IMP_SECMOB")
@Deprecated
public final class ImpSecmob implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="IMP_SECMOB")
  @TableGenerator(name="IMP_SECMOB", allocationSize=1)
  private Long id;

  private String tipo;

  private String informacao;

  public ImpSecmob() {
    super();
  }

  public ImpSecmob(final String tipo, final String informacao) {
    this.setTipo(tipo);
    this.setInformacao(informacao);
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(final String tipo) {
    this.tipo = tipo;
  }

  public String getInformacao() {
    return informacao;
  }

  public void setInformacao(final String informacao) {
    this.informacao = informacao;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  /** Valida o cabeçalho (1ª linha) do arquivo.
   * @param cabecalho 1ª linha do arquivo
   * @return true se cabeçalho válido
   */
  public boolean arquivoValido(final String cabecalho) {
    boolean status = false;
    if (("03".equals(cabecalho.substring(2, 4)) && "OP".equals(cabecalho.substring(32, 34))) ||
        ("06".equals(cabecalho.substring(2, 4)) && "AT".equals(cabecalho.substring(32, 34)))) {
      status = true;  
    }
    return status;
  }
  
  /** Separa a linha em tipo e informação.
   * @param linha informação do arquivo
   */
  public void trim(final String linha) {
    this.setTipo(linha.substring(0, 2));
    this.setInformacao(linha.substring(2));
  }
  
}
