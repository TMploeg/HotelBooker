package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.AuthorityRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.enums.Role;
import com.tmploeg.hotelbooker.models.Authority;
import com.tmploeg.hotelbooker.models.User;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final AuthorityRepository authorityRepository;
  private final PasswordEncoder passwordEncoder;

  public User save(String username, String password, Role role) {
    authorityRepository.save(new Authority(username, role.toString()));
    return userRepository.save(new User(username, passwordEncoder.encode(password)));
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Set<User> findByRole(Role role) {
    return authorityRepository.findByAuthority(role.toString()).stream()
        .map(a -> userRepository.findByUsername(a.getUsername()).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }
}
