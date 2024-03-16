package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.models.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username == null || username.isBlank()) {
      throw new UsernameNotFoundException("username is null or empty");
    }

    return userService
        .findByUsername(username)
        .map(AppUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("user '" + username + "' not found"));
  }
}
