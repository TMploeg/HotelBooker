package com.tmploeg.hotelbooker.models;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {
  private final String username;
  private final String password;
  private final boolean accountNonExpired = true;
  private final boolean accountNonLocked = true;
  private final boolean credentialsNonExpired = true;
  private final boolean enabled;
  private final List<GrantedAuthority> authorities;

  public AppUserDetails(User user, Collection<Authority> authorities) {
    username = user.getUsername();
    password = user.getPassword();
    enabled = user.isEnabled();
    this.authorities =
        authorities.stream()
            .map(a -> (GrantedAuthority) new SimpleGrantedAuthority(a.getAuthority()))
            .toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
