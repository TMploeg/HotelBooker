package com.tmploeg.hotelbooker.dtos;

public class AuthDTO {
  private String username;

  public AuthDTO(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
