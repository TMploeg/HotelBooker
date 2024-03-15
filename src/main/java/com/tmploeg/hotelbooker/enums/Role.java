package com.tmploeg.hotelbooker.enums;

import lombok.Getter;

@Getter
public enum Role {
  USER,
  ADMIN;

  private static final String NAME_PREFIX = "ROLE_";
  private final String name;

  private Role() {
    this.name = NAME_PREFIX + this.toString();
  }
}
