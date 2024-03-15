package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.enums.Role;
import com.tmploeg.hotelbooker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final UserService userService;

  @Override
  public void run(String... args) throws Exception {
    seedUsers();
  }

  private void seedUsers() {
    if (!userService.findByRole(Role.ADMIN).isEmpty()) {
      return;
    }

    String adminUsername = "ADMIN";
    String adminPassword = "DigitalGold#0153";
    Role adminRole = Role.ADMIN;

    userService.save(adminUsername, adminPassword, adminRole);
  }
}
