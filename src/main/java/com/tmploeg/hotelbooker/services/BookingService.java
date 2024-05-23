package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
import com.tmploeg.hotelbooker.models.entities.User;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;
  private final RoomService roomService;

  public Set<Booking> getAll() {
    return bookingRepository.findByOrderByCheckIn();
  }

  public ValueResult<Booking> save(
      User user, Hotel hotel, LocalDateTime checkIn, LocalDateTime checkOut, int roomCount) {
    List<String> errors = new LinkedList<>();

    if (user == null) {
      throw new IllegalArgumentException("user is required");
    }
    if (checkIn == null) {
      throw new IllegalArgumentException("checkIn is required");
    }
    if (checkOut == null) {
      throw new IllegalArgumentException("checkOut is required");
    }

    if (LocalDateTimeHelper.hasDatePassed(checkIn)) {
      errors.add("checkIn has passed");
    } else if (!checkOut.isAfter(checkIn)) {
      errors.add("checkOut is not after checkIn");
    }

    if (roomCount < 1) {
      errors.add("at least one room is required");
    }

    Set<Room> availableRooms = roomService.getAvailableRooms(hotel, checkIn, checkOut);
    if (availableRooms.size() < roomCount) {
      errors.add("insufficient rooms available");
    }

    return errors.isEmpty()
        ? ValueResult.succesResult(
            bookingRepository.save(
                new Booking(
                    user,
                    checkIn,
                    checkOut,
                    availableRooms.stream().limit(roomCount).collect(Collectors.toSet()))))
        : ValueResult.errorResult(errors);
  }

  public void update(Booking booking) {
    if (booking == null) {
      throw new IllegalArgumentException("booking is null");
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
      throw new IllegalArgumentException("booking is null");
    }

    bookingRepository
        .findById(booking.getId())
        .ifPresentOrElse(
            bookingRepository::delete,
            () -> {
              throw new RuntimeException("booking does not exist");
            });
  }

  public Optional<Booking> findById(Long id) {
    return bookingRepository.findById(id);
  }

  public Set<Booking> findByUser(User user) {
    return bookingRepository.findByUserOrderByCheckIn(user);
  }
}
