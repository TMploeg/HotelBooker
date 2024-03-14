package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookingDTO {
  private final Long id;
  private final String username;
  private final String checkIn;
  private final String checkOut;

  public static Booking convert(BookingDTO dto, User user) {
    LocalDateTime parsedCheckIn = LocalDateTimeHelper.tryParse(dto.getCheckIn()).orElse(null);
    LocalDateTime parsedCheckOut = LocalDateTimeHelper.tryParse(dto.getCheckOut()).orElse(null);
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
