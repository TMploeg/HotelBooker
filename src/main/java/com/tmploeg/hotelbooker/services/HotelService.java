package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.HotelRepository;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
  private final HotelRepository hotelRepository;

  public Optional<Hotel> findById(Long id) {
    return hotelRepository.findById(id);
  }

  public Set<Hotel> getAll() {
    return hotelRepository.findByOrderById();
  }

  public ValueResult<Hotel> save(String name, String address) {
    if (name == null) {
      throw new IllegalArgumentException("name is null");
    }
    if (address == null) {
      throw new IllegalArgumentException("address is null");
    }

    return ValueResult.succesResult(hotelRepository.save(new Hotel(name, address)));
  }
}
