package ar.edu.itba.paw.interfaces.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;

public class ImageNotFoundException extends NotFoundException {
  public ImageNotFoundException(String message) {
    super(message);
  }
}
