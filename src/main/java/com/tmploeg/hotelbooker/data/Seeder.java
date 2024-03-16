package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.models.User;
import com.tmploeg.hotelbooker.services.RoleService;
import com.tmploeg.hotelbooker.services.UserService;
import java.util.Arrays;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final UserService userService;
  private final RoleService roleService;

  @Override
  public void run(String... args) throws Exception {
    seedRoles();
    seedUsers();
  }

  private void seedRoles() {
    Arrays.stream(RoleName.values()).forEach(rN -> roleService.save(rN.toString()));
  }

  private void seedUsers() {
    Role adminRole = roleService.getByName(RoleName.ADMIN);
    Set<User> adminUsers = userService.findByRole(adminRole);

    if (!adminUsers.isEmpty()) {
      return;
    }

    String adminUsername = "ADMIN";
    String adminPassword = "DigitalGold#0153";

    userService.save(adminUsername, adminPassword, adminRole);
  }
}
