package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("bookings")
public class BookingController extends ControllerBase {
  private final BookingRepository bookingRepository;
  private final UserRepository userRepository;

  public BookingController(BookingRepository bookingRepository, UserRepository userRepository) {
    this.bookingRepository = bookingRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public ResponseEntity<List<BookingDTO>> getAll() {
    return ResponseEntity.ok(
        bookingRepository.findByOrderByCheckIn().stream()
            .map(BookingDTO::fromBooking)
            .collect(Collectors.toList()));
  }

  @GetMapping("get-by-id/{id}")
  public ResponseEntity<BookingDTO> getById(@PathVariable long id) {
    return bookingRepository
        .findById(id)
        .map(b -> ResponseEntity.ok(BookingDTO.fromBooking(b)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> addBooking(
      @RequestBody BookingDTO bookingDTO, UriComponentsBuilder ucb) {
    if (bookingDTO == null) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "booking data is required");
    }

    Optional<User> user = userRepository.findByUsername(bookingDTO.getUsername());

    if (!isValidUsername(bookingDTO.getUsername()) || user.isEmpty()) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "username is invalid");
    }

    Booking booking = BookingDTO.convert(bookingDTO, user.get());

    LinkedList<String> errorMessages = new LinkedList<>();

    boolean checkInValid = false;
    if (booking.getCheckIn() == null) {
      errorMessages.add("checkIn is required");
    } else if (hasDatePassed(booking.getCheckIn())) {
      errorMessages.add("checkIn has passed");
    } else {
      checkInValid = true;
    }

    if (booking.getCheckOut() == null) {
      errorMessages.add("checkOut is required");
    } else if (checkInValid && booking.getCheckOut().isBefore(booking.getCheckIn())) {
      errorMessages.add("checkOut must not be earlier than checkIn");
    }

    if (!errorMessages.isEmpty()) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, String.join(";", errorMessages));
    }

    if (hasOverlappingBookings(booking.getCheckIn(), booking.getCheckOut())) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "booking is occupied");
    }

    bookingRepository.save(booking);
    URI newBookingLocation = ucb.path("{id}").buildAndExpand(booking.getId()).toUri();
    return ResponseEntity.created(newBookingLocation).body(BookingDTO.fromBooking(booking));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
    Optional<Booking> deleteBooking = bookingRepository.findById(id);

    if (deleteBooking.isPresent()) {
      bookingRepository.delete(deleteBooking.get());
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping
  public ResponseEntity<?> updateCheckOut(@RequestBody BookingDTO bookingDTO) {
    if (bookingDTO == null) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "booking data is required");
    }

    if (bookingDTO.getId() == null) {
      return getErrorResponse(HttpStatus.BAD_REQUEST, "id is required");
    }

    Optional<Booking> booking = bookingRepository.findById(bookingDTO.getId());

    if (booking.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (hasDatePassed(booking.get().getCheckOut())) {
      return getErrorResponse(HttpStatus.FORBIDDEN, "booking has already passed");
    }

    if (bookingDTO.getCheckOut() != null) {
      Optional<LocalDateTime> newCheckOut = LocalDateTimeHelper.tryParse(bookingDTO.getCheckOut());

      if (newCheckOut.isEmpty()) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, "checkOut is invalid");
      }

      if (newCheckOut.get().isBefore(booking.get().getCheckIn())) {
        return getErrorResponse(
            HttpStatus.BAD_REQUEST, "checkOut must not be earlier than checkIn");
      }

      if (!findOverlappingBookings(booking.get().getCheckIn(), newCheckOut.get()).stream()
          .filter(b -> b != booking.get())
          .toList()
          .isEmpty()) {
        return getErrorResponse(HttpStatus.FORBIDDEN, "new booking is occupied");
      }

      booking.get().setCheckout(newCheckOut.get());
    }

    bookingRepository.save(booking.get());

    return ResponseEntity.ok(BookingDTO.fromBooking(booking.get()));
  }

  private boolean isValidUsername(String ownerName) {
    return ownerName != null && !ownerName.isBlank();
  }

  private boolean hasDatePassed(LocalDateTime dt) {
    return !dt.isAfter(LocalDateTime.now());
  }

  private List<Booking> findOverlappingBookings(LocalDateTime checkIn, LocalDateTime checkOut) {
    return bookingRepository
        .findByCheckInBetweenAndCheckOutBetween(checkIn, checkOut, checkIn, checkOut)
        .stream()
        .toList();
  }

  private boolean hasOverlappingBookings(LocalDateTime checkIn, LocalDateTime checkOut) {
    return !findOverlappingBookings(checkIn, checkOut).isEmpty();
  }
}
