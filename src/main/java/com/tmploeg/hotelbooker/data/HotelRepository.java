package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {}
