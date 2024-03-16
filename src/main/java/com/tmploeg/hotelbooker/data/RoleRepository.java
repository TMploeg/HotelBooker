package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Role;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {
  public Optional<Role> findByName(String name);
}
