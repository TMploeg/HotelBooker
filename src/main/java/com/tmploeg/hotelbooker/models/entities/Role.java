package com.tmploeg.hotelbooker.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
