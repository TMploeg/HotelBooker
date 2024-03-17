package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Role;
import com.tmploeg.hotelbooker.models.User;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  Set<User> findByRole(Role role);
}
