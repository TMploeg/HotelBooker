package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.services.RoleService;
import com.tmploeg.hotelbooker.services.UserService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final UserService userService;
  private final RoleService roleService;
  private final Environment environment;

  private static final String ADMIN_PASSWORD_ENVIRONMENT_KEY = "hotelbooker.admin-password";

  @Override
  public void run(String... args) throws Exception {
    seedRoles();
    seedUsers();
  }

  private void seedRoles() {
    Arrays.stream(RoleName.values()).forEach(rN -> roleService.save(rN.toString()));
  }

  private void seedUsers() {
    String adminUsername = "ADMIN";
    String adminPassword = environment.getProperty(ADMIN_PASSWORD_ENVIRONMENT_KEY);
    Role adminRole = roleService.getByName(RoleName.ADMIN);

    userService.save(adminUsername, adminPassword, adminRole);
  }
}
