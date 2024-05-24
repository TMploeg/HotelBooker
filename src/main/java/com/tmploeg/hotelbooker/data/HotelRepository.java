package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
  Set<Hotel> findByOrderByName();

  Set<Hotel> findByNameContainingIgnoreCaseOrderByName(String nameQuery);

  Set<Hotel>
      findByNameContainingIgnoreCaseAndAddressCityContainingIgnoreCaseOrderByAddressCityAscNameAsc(
          String nameQuery, String cityQuery);

  Set<Hotel> findByNameContainingIgnoreCaseAndAddressCityContainingIgnoreCase(
      String nameQuery, String cityQuery, Sort sort);
}
