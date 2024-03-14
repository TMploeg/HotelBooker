package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@NoArgsConstructor
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String username;

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings;

  public User(String username) {
    this.username = username;
  }
}
