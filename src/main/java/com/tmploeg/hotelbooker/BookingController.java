package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.UpdateCheckOutDTO;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
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
  private final UserRepository userRepository;

  public BookingController(BookingRepository bookingRepository, UserRepository userRepository) {
    this.bookingRepository = bookingRepository;
    this.userRepository = userRepository;
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
  public ResponseEntity<List<BookingDTO>> getByUsername(@PathVariable String username) {
    return userRepository
        .findByUsername(username)
        .map(
            value ->
                ResponseEntity.ok(
                    value.getBookings().stream().map(BookingDTO::fromBooking).toList()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Object> addBooking(
      @RequestBody @NotNull BookingDTO bookingDTO, UriComponentsBuilder ucb) {
    Booking booking =
        BookingDTO.convert(
            bookingDTO, userName -> userRepository.findByUsername(userName).orElse(null));

    if (!isValidOwnerName(booking.getUser().getUsername())) {
      return ResponseEntity.badRequest()
          .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "invalid name"));
    }

    if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "checkIn and/of checkOut is missing"));
    }

    if (!isValidDateRange(booking.getCheckIn(), booking.getCheckOut())) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "booking range is invalid"));
    }

    if (hasDatePassed(booking.getCheckIn())) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "booking checkIn has passed"));
    }

    if (!findOverlappingBookings(booking.getCheckIn(), booking.getCheckOut()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "booking is (partially) occupied"));
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

  @PutMapping("update-checkout")
  public ResponseEntity<Object> updateCheckOut(@RequestBody UpdateCheckOutDTO updateCheckOutDTO) {
    if (updateCheckOutDTO.getId() == null) {
      return ResponseEntity.badRequest()
          .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "id is missing"));
    }

    Optional<Booking> updateBooking = bookingRepository.findById(updateCheckOutDTO.getId());

    if (updateBooking.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (hasDatePassed(updateBooking.get().getCheckOut())) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "booking has already passed"));
    }

    Optional<LocalDateTime> parsedNewCheckOut =
        LocalDateTimeHelper.tryParse(updateCheckOutDTO.getNewCheckOut());

    if (parsedNewCheckOut.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(
                  HttpStatus.BAD_REQUEST, "booking checkOut is invalid"));
    }

    if (!isValidDateRange(updateBooking.get().getCheckIn(), parsedNewCheckOut.get())) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "booking range is invalid"));
    }

    List<Booking> overlappingBookings =
        findOverlappingBookings(updateBooking.get().getCheckIn(), parsedNewCheckOut.get()).stream()
            .filter(b -> b != updateBooking.get())
            .toList();

    if (!overlappingBookings.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "new booking is occupied"));
    }

    updateBooking.get().setCheckout(parsedNewCheckOut.get());

    return ResponseEntity.ok(updateBooking);
  }

  private boolean isValidOwnerName(String ownerName) {
    return ownerName != null && !ownerName.isBlank();
  }

  private boolean isValidDateRange(
      @NotNull LocalDateTime checkIn, @NotNull LocalDateTime checkOut) {
    return checkIn.isBefore(checkOut);
  }

  private boolean hasDatePassed(LocalDateTime dt) {
    return !dt.isAfter(LocalDateTime.now());
  }

  private List<Booking> findOverlappingBookings(LocalDateTime checkIn, LocalDateTime checkOut) {

    return bookingRepository.findAll().stream()
        .filter(
            b ->
                (b.getCheckIn().isAfter(checkIn) && b.getCheckIn().isBefore(checkOut))
                    || (b.getCheckOut().isAfter(checkIn) && b.getCheckIn().isBefore(checkOut)))
        .collect(Collectors.toList());
  }
}
