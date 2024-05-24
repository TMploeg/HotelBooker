package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.Address;
import com.tmploeg.hotelbooker.models.entities.Hotel;

public record HotelDTO(Long id, String name, Address address) {
  public static HotelDTO fromHotel(Hotel hotel) {
    return new HotelDTO(hotel.getId(), hotel.getName(), hotel.getAddress());
  }
}
