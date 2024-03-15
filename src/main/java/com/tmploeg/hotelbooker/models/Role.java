package com.tmploeg.hotelbooker.models;

import com.tmploeg.hotelbooker.enums.RoleName;
import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Role {
  @Id @GeneratedValue private Long id;

  private RoleName name;

  @OneToMany(mappedBy = "role")
  private Set<User> users;

  public Role(RoleName name) {
    this.name = name;
  }
}
