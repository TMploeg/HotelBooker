package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.RoleRepository;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.models.entities.Role;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  public Role findByName(RoleName name) {
    return roleRepository
        .findByName(name.toString())
        .orElseThrow(() -> new NoSuchElementException("role '" + name.toString() + "' not found"));
  }

  public void save(String name) {
    roleRepository.save(new Role(name));
  }
}
