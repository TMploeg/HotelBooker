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
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status)
        .body(ProblemDetail.forStatusAndDetail(status, exception.getMessage()));
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetail> forbiddenExceptionHandler(ForbiddenException exception) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    return ResponseEntity.status(status)
        .body(ProblemDetail.forStatusAndDetail(status, exception.getMessage()));
  }
}
