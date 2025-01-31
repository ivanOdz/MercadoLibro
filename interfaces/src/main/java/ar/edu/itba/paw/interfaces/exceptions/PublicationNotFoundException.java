package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class PublicationNotFoundException extends NotFoundException {
  public PublicationNotFoundException(String message) {
    super(message);
  }
}
