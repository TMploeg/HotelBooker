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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private static final String ADMIN_USERNAME_ENVIRONMENT_KEY = "hotelbooker-admin-username";
  private static final String ADMIN_PASSWORD_ENVIRONMENT_KEY = "hotelbooker-admin-password";

  private final UserService userService;
  private final RoleService roleService;
  private final Environment environment;

  @Override
  public void run(String... args) throws Exception {
    seedRoles();
    seedUsers();
  }

  private void seedRoles() {
    Arrays.stream(RoleName.values()).forEach(rN -> roleService.save(rN.toString()));
  }

  private void seedUsers() {
    Set<User> adminUsers = userService.findByRole(roleService.findByName(RoleName.ADMIN));

    if (adminUsers.size() > 1) {
      throw new RuntimeException("multiple admin users detected");
    }

    String adminUsername = environment.getProperty(ADMIN_USERNAME_ENVIRONMENT_KEY);

    if (adminUsers.size() == 1) {
      User admin = adminUsers.stream().findFirst().get();

      if (!admin.getUsername().equals(adminUsername)) {
        throw new RuntimeException(
            "attempting to seed admin user while admin with different name already exists");
      }
    }

    String adminPassword = environment.getProperty(ADMIN_PASSWORD_ENVIRONMENT_KEY);
    Role adminRole = roleService.findByName(RoleName.ADMIN);

    userService.save(adminUsername, adminPassword, adminRole);
  }
}
