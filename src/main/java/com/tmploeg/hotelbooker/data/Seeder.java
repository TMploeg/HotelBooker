package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.enums.Role;
import com.tmploeg.hotelbooker.models.Authority;
import com.tmploeg.hotelbooker.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final UserRepository userRepository;
  private final AuthorityRepository authorityRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    seedUsers();
  }

  private void seedUsers() {
    for (Authority authority : authorityRepository.findAll()) {
      if (authority.getAuthority().equals(Role.ADMIN.getName())) {
        return;
      }
    }

    String adminUsername = "ADMIN";
    String adminPassword = "DigitalGold#0153";
    User admin = new User(adminUsername, passwordEncoder.encode(adminPassword));
    userRepository.save(admin);

    Authority adminRole = new Authority(admin.getUsername(), Role.ADMIN.getName());
    authorityRepository.save(adminRole);
  }
}
