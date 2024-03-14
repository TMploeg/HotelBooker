package com.tmploeg.hotelbooker.dto;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
import java.time.LocalDateTime;
import java.util.function.Function;

public class BookingDTO {
  private final Long id;

  private final String ownerName;

  private final String checkIn;

  private final String checkOut;

  public BookingDTO(Long id, String ownerName, String checkIn, String checkOut) {
    this.id = id;
    this.ownerName = ownerName;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }

  public Long getId() {
    return id;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getCheckIn() {
    return checkIn;
  }

  public String getCheckOut() {
    return checkOut;
  }

  public static Booking convert(BookingDTO dto, Function<String, User> getOwner) {
    if (getOwner == null) {
      throw new NullPointerException("getOwner function is null");
    }

    LocalDateTime parsedCheckIn = LocalDateTimeHelper.tryParse(dto.getCheckIn()).orElse(null);
    LocalDateTime parsedCheckOut = LocalDateTimeHelper.tryParse(dto.getCheckOut()).orElse(null);
    return new Booking(getOwner.apply(dto.getOwnerName()), parsedCheckIn, parsedCheckOut);
  }

  public static BookingDTO fromBooking(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        booking.getOwner().getUsername(),
        LocalDateTimeHelper.format(booking.getCheckIn()),
        LocalDateTimeHelper.format(booking.getCheckOut()));
  }
}
