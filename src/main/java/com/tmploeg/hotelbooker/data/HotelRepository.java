package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
  Set<Hotel> findByOrderById();
}
