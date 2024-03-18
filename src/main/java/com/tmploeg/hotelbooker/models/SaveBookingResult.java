package com.tmploeg.hotelbooker.models;

import com.tmploeg.hotelbooker.models.entities.Booking;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class SaveBookingResult {
  private List<String> errors;
  private Booking savedBooking;

  private SaveBookingResult(List<String> errors) {
    this.errors = errors;
  }

  private SaveBookingResult(Booking savedBooking) {
    this.savedBooking = savedBooking;
    errors = new ArrayList<>();
  }

  public boolean succeeded() {
    return errors == null || errors.isEmpty();
  }

  public static SaveBookingResult succesResult(Booking booking) {
    return new SaveBookingResult(booking);
  }

  public static SaveBookingResult errorResult(List<String> errors) {
    return new SaveBookingResult(errors);
  }
}
