package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.SaveBookingResult;
import com.tmploeg.hotelbooker.models.User;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;

  public Set<Booking> getAll() {
    return bookingRepository.findByOrderByCheckIn();
  }

  public SaveBookingResult save(User user, LocalDateTime checkIn, LocalDateTime checkOut) {
    List<String> errors = new LinkedList<>();

    if (user == null) {
      throw new NullPointerException("user is required");
    }
    if (checkIn == null) {
      throw new NullPointerException("checkIn is required");
    }
    if (checkOut == null) {
      throw new NullPointerException("checkOut is required");
    }

    if (LocalDateTimeHelper.hasDatePassed(checkIn)) {
      errors.add("checkIn has passed");
    } else if (!checkOut.isAfter(checkIn)) {
      errors.add("checkOut is not after checkIn");
    }

    if (!findBookingsInRange(checkIn, checkOut).isEmpty()) {
      errors.add("booking is occupied");
    }

    return errors.isEmpty()
        ? SaveBookingResult.succesResult(
            bookingRepository.save(new Booking(user, checkIn, checkOut)))
        : SaveBookingResult.errorResult(errors);
  }

  public void update(Booking booking) {
    if (booking == null) {
      throw new NullPointerException("booking is null");
    }

    bookingRepository
        .findById(booking.getId())
        .ifPresentOrElse(
            bookingRepository::save,
            () -> {
              throw new RuntimeException("booking does not exist");
            });
  }

  public void delete(Booking booking) {
    if (booking == null) {
      throw new NullPointerException("booking is null");
    }

    bookingRepository
        .findById(booking.getId())
        .ifPresentOrElse(
            bookingRepository::delete,
            () -> {
              throw new RuntimeException("booking does not exist");
            });
  }

  public List<Booking> findBookingsInRange(LocalDateTime checkIn, LocalDateTime checkOut) {
    return bookingRepository
        .findByCheckInBetweenAndCheckOutBetween(checkIn, checkOut, checkIn, checkOut)
        .stream()
        .toList();
  }

  public Optional<Booking> findById(Long id) {
    return bookingRepository.findById(id);
  }

  public Set<Booking> findByUser(User user) {
    return bookingRepository.findByUserOrderByCheckIn(user);
  }
}
