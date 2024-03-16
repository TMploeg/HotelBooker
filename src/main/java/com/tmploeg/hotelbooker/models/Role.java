package com.tmploeg.hotelbooker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "roles")
@NoArgsConstructor
@Getter
public class Role {
  @Id private String name;

  @OneToMany(mappedBy = "role")
  private Set<User> users;

  public Role(String name) {
    this.name = name;
  }
}
