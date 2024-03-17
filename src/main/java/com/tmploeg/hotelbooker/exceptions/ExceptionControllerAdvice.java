package com.tmploeg.hotelbooker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetail> badRequestExceptionHandler(BadRequestException exception) {
    return createErrorResponseForExceptionAndStatus(HttpStatus.BAD_REQUEST, exception);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetail> forbiddenExceptionHandler(ForbiddenException exception) {
    return createErrorResponseForExceptionAndStatus(HttpStatus.FORBIDDEN, exception);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetail> notFoundExceptionHandler(NotFoundException exception) {
    return createErrorResponseForExceptionAndStatus(HttpStatus.NOT_FOUND, exception);
  }

  private ResponseEntity<ProblemDetail> createErrorResponseForExceptionAndStatus(
      HttpStatus status, RuntimeException exception) {
    return ResponseEntity.status(status)
        .body(ProblemDetail.forStatusAndDetail(status, exception.getMessage()));
  }
}
