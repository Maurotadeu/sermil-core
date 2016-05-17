package br.mil.eb.sermil.core.exceptions;

public class EventoNotFoundException extends NoDataFoundException {

  private static final long serialVersionUID = 3514296159672514331L;

  public EventoNotFoundException() {
    super();
  }

  public EventoNotFoundException(String msg) {
    super(msg);
  }

}
