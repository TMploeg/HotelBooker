package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
  Set<Hotel> findByOrderById();

  Set<Hotel> findByNameContainingIgnoreCaseOrderById(String search);
}
