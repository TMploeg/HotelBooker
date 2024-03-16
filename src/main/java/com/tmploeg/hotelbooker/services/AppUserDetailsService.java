package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.AuthorityRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.models.AppUserDetails;
import com.tmploeg.hotelbooker.models.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final AuthorityRepository authorityRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username == null || username.isBlank()) {
      throw new UsernameNotFoundException("username is null or empty");
    }

    Optional<User> user = userRepository.findByUsername(username);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("user '" + username + "' not found");
    }

    return new AppUserDetails(user.get(), authorityRepository.findByUsername(username));
  }
}
