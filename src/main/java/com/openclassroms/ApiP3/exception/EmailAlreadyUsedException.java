package com.openclassroms.ApiP3.exception;

public class EmailAlreadyUsedException extends RuntimeException {

  public EmailAlreadyUsedException(String message) {
    super(message);
  }

  public EmailAlreadyUsedException(String message, Throwable cause) {
    super(message, cause);
  }
}
