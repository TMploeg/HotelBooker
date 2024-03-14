package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.User;

import java.util.UUID;

public class UserDTO {
  private String id;
  private String username;

  public UserDTO(String id, String username) {
    this.id = id;
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public static UserDTO fromUser(User user) {
    return new UserDTO(user.getId().toString(), user.getUsername());
  }
}
