package com.tmploeg.hotelbooker.data;

import ch.qos.logback.core.joran.action.NewRuleAction;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.models.User;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    seedRoles();
    seedAdmin();
  }

  private void seedRoles() {
    List<Role> newRoles = new LinkedList<>();

    for (RoleName roleName : RoleName.values()) {
      if (roleRepository.findAll().stream().noneMatch(r -> r.getName().equals(roleName))) {
        newRoles.add(new Role(roleName));
      }
    }

    if (!newRoles.isEmpty()) {
      roleRepository.saveAll(newRoles);
    }
  }

  private void seedAdmin() {
    Role adminRole = roleRepository.findByName(RoleName.ADMIN);

    if (!userRepository.findByRole(adminRole).isEmpty()) {
      return;
    }

    User admin = new User("ADMIN", adminRole);

    userRepository.save(admin);
  }
}
