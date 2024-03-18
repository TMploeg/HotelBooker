package com.tmploeg.hotelbooker.models.entities;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "users")
@NoArgsConstructor
@Getter
public class User implements UserDetails {
  @Id private String username;

  private String password;

  @Setter private boolean enabled;

  @ManyToOne private Role role;

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings;

  @Setter private boolean accountNonLocked;

  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.enabled = true;
    this.role = role;
    this.accountNonLocked = true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.getName()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
