package com.tmploeg.hotelbooker.models;

import java.util.Date;

public record JwtToken(String username, String[] roles, Date issueDate, Date expirationDate) {
  public boolean isExpired() {
    return expirationDate.before(new Date());
  }
}
