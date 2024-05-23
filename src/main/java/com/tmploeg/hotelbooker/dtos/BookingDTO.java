package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.Room;

public record BookingDTO(Long id, HotelDTO hotel, String checkIn, String checkOut) {
  public static BookingDTO fromBooking(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        HotelDTO.fromHotel(booking.getRooms().stream().map(Room::getHotel).toList().getFirst()),
        LocalDateTimeHelper.format(booking.getCheckIn()),
        LocalDateTimeHelper.format(booking.getCheckOut()));
  }
}
