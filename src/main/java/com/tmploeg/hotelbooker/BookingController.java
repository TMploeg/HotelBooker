package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.dto.BookingDTO;
import com.tmploeg.hotelbooker.models.Booking;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("bookings")
public class BookingController {
  private final BookingRepository bookingRepository;

  public BookingController(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  @GetMapping
  public ResponseEntity<List<BookingDTO>> getAll() {
    return ResponseEntity.ok(
        bookingRepository.findAll().stream()
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

  @GetMapping("get-by-ownername/{ownerName}")
  public ResponseEntity<List<BookingDTO>> getByOwnerName(@PathVariable String ownerName) {
    return ResponseEntity.ok(
        bookingRepository.findByOwnerName(ownerName).stream()
            .map(BookingDTO::fromBooking)
            .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<Object> addBooking(
      @RequestBody @NotNull BookingDTO bookingDTO, UriComponentsBuilder ucb) {
    Booking booking = bookingDTO.convert();

    if (!isValidOwnerName(booking.getOwnerName())) {
      return ResponseEntity.badRequest()
          .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "invalid name"));
    }

    if (!isValidDateRange(booking.getCheckIn(), booking.getCheckOut())) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "invalid booking start and/or end"));
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

  private boolean isValidOwnerName(String ownerName) {
    return ownerName != null && !ownerName.isBlank();
  }

  private boolean isValidDateRange(LocalDateTime checkIn, LocalDateTime checkOut) {
    return checkIn != null
        && checkOut != null
        && checkIn.isAfter(LocalDateTime.now())
        && checkIn.isBefore((checkOut));
  }
}
