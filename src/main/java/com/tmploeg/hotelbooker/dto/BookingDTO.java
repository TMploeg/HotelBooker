package com.tmploeg.hotelbooker.dto;

import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import java.time.LocalDateTime;

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

  public Booking convert() {
    LocalDateTime parsedCheckIn = LocalDateTimeHelper.tryParse(checkIn).orElse(null);
    LocalDateTime parsedCheckOut = LocalDateTimeHelper.tryParse(checkOut).orElse(null);
    return new Booking(ownerName, parsedCheckIn, parsedCheckOut);
  }

  public static BookingDTO fromBooking(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        booking.getOwnerName(),
        LocalDateTimeHelper.format(booking.getCheckIn()),
        LocalDateTimeHelper.format(booking.getCheckOut()));
  }
}
