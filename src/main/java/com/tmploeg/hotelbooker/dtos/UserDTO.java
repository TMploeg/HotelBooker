package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTO {
  private final String id;
  private final String username;

  public static UserDTO fromUser(User user) {
    return new UserDTO(user.getId().toString(), user.getUsername());
  }
}
