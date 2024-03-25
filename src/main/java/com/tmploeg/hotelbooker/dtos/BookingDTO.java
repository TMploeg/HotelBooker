package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.entities.Booking;

public record BookingDTO(Long id, String username, String checkIn, String checkOut) {
  public static BookingDTO fromBooking(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        booking.getUser().getUsername(),
        LocalDateTimeHelper.format(booking.getCheckIn()),
        LocalDateTimeHelper.format(booking.getCheckOut()));
  }
}
