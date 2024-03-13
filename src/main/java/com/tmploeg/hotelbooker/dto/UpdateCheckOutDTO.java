package com.tmploeg.hotelbooker.dto;

public class UpdateCheckOutDTO {
  private final Long id;
  private final String newCheckOut;

  public UpdateCheckOutDTO(Long id, String newCheckOut) {
    this.id = id;
    this.newCheckOut = newCheckOut;
  }

  public Long getId() {
    return id;
  }

  public String getNewCheckOut() {
    return newCheckOut;
  }
}
