package com.tmploeg.hotelbooker.dtos;

public class AuthDTO {
  private String username;

  public AuthDTO() {}

  public AuthDTO(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
