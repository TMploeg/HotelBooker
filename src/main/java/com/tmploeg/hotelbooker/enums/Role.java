package com.tmploeg.hotelbooker.enums;

import lombok.Getter;

@Getter
public enum Role {
  USER,
  ADMIN;

  private static final String NAME_PREFIX = "ROLE_";

  @Override
  public String toString() {
    return NAME_PREFIX + name();
  }
}
