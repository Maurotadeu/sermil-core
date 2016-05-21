package br.mil.eb.sermil.core.exceptions;

public class ObjectDuplicatedException extends SermilException {

  private static final long serialVersionUID = -4296738211711340609L;

  private Object objeto;

  private String mensagem = "Objeto já existe";
  
  public ObjectDuplicatedException() {
    super();
  }

  public ObjectDuplicatedException(final String msg) {
    this.mensagem = msg;
  }

  public ObjectDuplicatedException(final Object obj) {
    this.objeto = obj;
  }

  public ObjectDuplicatedException(final String msg, final Object obj) {
    this.mensagem = msg;
    this.objeto = obj;
  }

  @Override
  public String getMessage() {
    return new StringBuilder(this.mensagem).append(": ").append(this.objeto).toString() ;
  }
  
  public Object getObjeto(){
    return this.objeto;
  }
}
