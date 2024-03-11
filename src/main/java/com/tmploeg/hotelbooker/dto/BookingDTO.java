package com.tmploeg.hotelbooker.dto;

import com.tmploeg.hotelbooker.models.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookingDTO {
  private final String ownerName;

  private final String checkInString;

  private final String checkOutString;

  public BookingDTO(String ownerName, String checkInString, String checkOutString) {
    this.ownerName = ownerName;

    this.checkInString = checkInString;
    this.checkOutString = checkOutString;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getStartDTString() {
    return checkInString;
  }

  public String getEndDTString() {
    return checkOutString;
  }

  public Booking convert() {
    LocalDateTime startDT = null;
    try {
      startDT = LocalDateTime.parse(checkInString);
    } catch (DateTimeParseException ignored) {
    }

    LocalDateTime endDT = null;
    try {
      endDT = LocalDateTime.parse(checkOutString);
    } catch (DateTimeParseException ignored) {
    }

    return new Booking(ownerName, startDT, endDT);
  }

  public static BookingDTO fromBooking(Booking booking) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    return new BookingDTO(
        booking.getOwnerName(),
        booking.getCheckIn().format(formatter),
        booking.getCheckOut().format(formatter));
  }
}
