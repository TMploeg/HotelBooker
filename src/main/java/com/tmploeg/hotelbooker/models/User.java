package com.tmploeg.hotelbooker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String username;

  public User() {}

  public User(String username) {
    this.username = username;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }
}
