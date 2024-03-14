package com.tmploeg.hotelbooker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ControllerBase {
  protected ResponseEntity<ProblemDetail> getErrorResponse(HttpStatus status, String errorMessage) {
    return ResponseEntity.status(status)
        .body(ProblemDetail.forStatusAndDetail(status, errorMessage));
  }
}
