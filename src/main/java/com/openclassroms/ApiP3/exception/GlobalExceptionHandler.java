package com.openclassroms.ApiP3.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.openclassroms.ApiP3.dto.ErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle specific exceptions
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
    return buildErrorResponse("Image not found", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorDTO> handleIOException(IOException ex, WebRequest request) {
    return buildErrorResponse("Internal server error while processing the image", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle custom exceptions
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorDTO> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorDTO> handleForbiddenException(ForbiddenException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  // Handle all other exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex, WebRequest request) {
    return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Helper method to construct the error response
  private ResponseEntity<ErrorDTO> buildErrorResponse(String message, HttpStatus status) {
    ErrorDTO errorResponse = new ErrorDTO(message, status.value());
    return ResponseEntity.status(status).body(errorResponse);
  }

  // Gestion de l'exception EmailAlreadyUsedException
  @ExceptionHandler(EmailAlreadyUsedException.class)
  public ResponseEntity<ErrorDTO> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Gestion de l'exception InvalidInputException
  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ErrorDTO> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Gestion des exceptions personnalisées
  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleImageNotFoundException(ImageNotFoundException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidImageFormatException.class)
  public ResponseEntity<ErrorDTO> handleInvalidImageFormatException(InvalidImageFormatException ex,
      WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Gestion de l'exception AuthenticationFailedException
  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorDTO> handleAuthenticationFailedException(AuthenticationFailedException ex,
      WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

}
