package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Authority;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, String> {
  public Set<Authority> findByUsername(String username);

  public Set<Authority> findByAuthority(String authority);
}
