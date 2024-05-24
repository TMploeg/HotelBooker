package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.HotelRepository;
import com.tmploeg.hotelbooker.models.Address;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
  private final HotelRepository hotelRepository;

  public Optional<Hotel> findById(Long id) {
    return hotelRepository.findById(id);
  }

  public Set<Hotel> getAll() {
    return hotelRepository.findByOrderByName();
  }

  public Set<Hotel> search(String nameQuery, String cityQuery) {
    String notNullNameQuery = nameQuery != null ? nameQuery : "";
    return cityQuery == null
        ? hotelRepository.findByNameContainingIgnoreCaseOrderByName(notNullNameQuery)
        : hotelRepository.findByNameContainingIgnoreCaseAndAddressCityContainingIgnoreCase(
            notNullNameQuery,
            cityQuery,
            Sort.by(
                Sort.Order.asc("address.city").ignoreCase(), Sort.Order.asc("name").ignoreCase()));
  }

  public ValueResult<Hotel> save(String name, Address address) {
    if (name == null) {
      throw new IllegalArgumentException("name is null");
    }
    if (address == null) {
      throw new IllegalArgumentException("address is null");
    }

    return ValueResult.succesResult(hotelRepository.save(new Hotel(name, address)));
  }

  public Hotel update(Hotel hotel) {
    return hotelRepository.save(hotel);
  }
}
