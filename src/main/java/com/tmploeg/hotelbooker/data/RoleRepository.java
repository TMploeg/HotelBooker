package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Role;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
  public Optional<Role> findByName(String name);
}
