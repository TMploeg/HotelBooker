package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String username;

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings;

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
