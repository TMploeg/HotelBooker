package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.models.User;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User save(String username, String password, Role role) {
    return userRepository.save(new User(username, passwordEncoder.encode(password), role));
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Set<User> findByRole(Role role) {
    return userRepository.findByRole(role);
  }
}
