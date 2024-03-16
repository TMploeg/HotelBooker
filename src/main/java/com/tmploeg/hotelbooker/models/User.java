package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@NoArgsConstructor
@Getter
public class User {
  @Id private String username;

  private String password;

  @Setter private boolean enabled;

  @ManyToOne private Role role;

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings;

  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.enabled = true;
    this.role = role;
  }
}
