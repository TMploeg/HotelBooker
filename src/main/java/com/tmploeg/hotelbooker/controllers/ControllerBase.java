package com.tmploeg.hotelbooker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ControllerBase {
  protected ResponseEntity<ProblemDetail> getBadRequestResponse(String errorMessage) {
    return ResponseEntity.badRequest()
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage));
  }
}
