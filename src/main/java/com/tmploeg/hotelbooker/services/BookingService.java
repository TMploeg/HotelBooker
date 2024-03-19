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
      User user, LocalDateTime checkIn, LocalDateTime checkOut, Set<Room> rooms) {
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
    if (rooms == null) {
      throw new IllegalArgumentException("rooms is required");
    }

    if (LocalDateTimeHelper.hasDatePassed(checkIn)) {
      errors.add("checkIn has passed");
    } else if (!checkOut.isAfter(checkIn)) {
      errors.add("checkOut is not after checkIn");
    }

    if (rooms.isEmpty()) {
      errors.add("at least one room is required");
    }

    Hotel hotel = rooms.stream().findFirst().get().getHotel();
    if (rooms.stream().anyMatch(r -> !Objects.equals(r.getHotel().getId(), hotel.getId()))) {
      throw new IllegalArgumentException("all rooms must have the same hotel");
    }

    Set<Room> availableRooms = roomService.findAvailableRooms(hotel, checkIn, checkOut);
    List<String> unavailableRoomNumbers =
        rooms.stream()
            .filter(availableRooms::contains)
            .map(r -> Integer.toString(r.getRoomNumber()))
            .toList();

    if (!unavailableRoomNumbers.isEmpty()) {
      errors.add("rooms { " + String.join(", ", unavailableRoomNumbers) + " } are occupied");
    }

    return errors.isEmpty()
        ? ValueResult.succesResult(bookingRepository.save(new Booking(user, checkIn, checkOut)))
        : ValueResult.errorResult(errors);
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

  //  public boolean isBooked(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
  //    return !bookingRepository
  //        .findAllByRoomsAndCheckInBetweenAndCheckOutBetween(
  //            room, checkIn, checkOut, checkIn, checkOut)
  //        .isEmpty();
  //  }

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
