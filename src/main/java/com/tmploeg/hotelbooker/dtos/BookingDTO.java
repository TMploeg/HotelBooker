package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.User;
import java.time.LocalDateTime;

public record BookingDTO(Long id, String username, String checkIn, String checkOut) {
  public static Booking convert(BookingDTO dto, User user) {
    LocalDateTime parsedCheckIn = LocalDateTimeHelper.tryParse(dto.checkIn()).orElse(null);
    LocalDateTime parsedCheckOut = LocalDateTimeHelper.tryParse(dto.checkOut()).orElse(null);
    return new Booking(user, parsedCheckIn, parsedCheckOut);
  }

  public static BookingDTO fromBooking(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        booking.getUser().getUsername(),
        LocalDateTimeHelper.format(booking.getCheckIn()),
        LocalDateTimeHelper.format(booking.getCheckOut()));
  }
}
