package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.entities.User;

public record UserDTO(String username) {
  public static UserDTO fromUser(User user) {
    return new UserDTO(user.getUsername());
  }
}
