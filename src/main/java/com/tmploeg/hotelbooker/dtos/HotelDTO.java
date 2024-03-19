package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record HotelDTO(Long id, String name, String address) {
  public Hotel convert() {
    if (name == null || name.isBlank()) {
      throw new IllegalStateException("hotel name must not be null or blank");
    }

    if (address == null || address.isBlank()) {
      throw new IllegalStateException("hotel address must not be null or blank");
    }

    return new Hotel(name, address);
  }

  public static HotelDTO fromHotel(Hotel hotel) {
    return new HotelDTO(hotel.getId(), hotel.getName(), hotel.getAddress());
  }
}
