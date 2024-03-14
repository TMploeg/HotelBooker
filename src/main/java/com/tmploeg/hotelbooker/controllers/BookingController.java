package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.UpdateCheckOutDTO;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
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
  public ResponseEntity<?> addBooking(
      @RequestBody @NotNull BookingDTO bookingDTO, UriComponentsBuilder ucb) {
    Booking booking =
        BookingDTO.convert(
            bookingDTO, userName -> userRepository.findByUsername(userName).orElse(null));

    LinkedList<String> errorMessages = new LinkedList<>();

    if (!isValidUsername(booking.getUser().getUsername())) {
      errorMessages.add("username is invalid");
    }

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
      return getBadRequestResponse(String.join(";", errorMessages));
    }

    if (!findOverlappingBookings(booking.getCheckIn(), booking.getCheckOut()).isEmpty()) {
      return getBadRequestResponse("booking is (partially) occupied");
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
  public ResponseEntity<?> updateCheckOut(@RequestBody UpdateCheckOutDTO updateCheckOutDTO) {
    if (updateCheckOutDTO.getId() == null) {
      return getBadRequestResponse("id is required");
    }

    Optional<Booking> updateBooking = bookingRepository.findById(updateCheckOutDTO.getId());

    if (updateBooking.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (hasDatePassed(updateBooking.get().getCheckOut())) {
      return getBadRequestResponse("booking has already passed");
    }

    Optional<LocalDateTime> parsedNewCheckOut =
        LocalDateTimeHelper.tryParse(updateCheckOutDTO.getNewCheckOut());

    if (parsedNewCheckOut.isEmpty()) {
      return getBadRequestResponse("checkOut is invalid");
    }

    if (parsedNewCheckOut.get().isBefore(updateBooking.get().getCheckIn())) {
      return getBadRequestResponse("checkOut must not be earlier than checkIn");
    }

    List<Booking> overlappingBookings =
        findOverlappingBookings(updateBooking.get().getCheckIn(), parsedNewCheckOut.get()).stream()
            .filter(b -> b != updateBooking.get())
            .toList();

    if (!overlappingBookings.isEmpty()) {
      return getBadRequestResponse("new booking is occupied");
    }

    updateBooking.get().setCheckout(parsedNewCheckOut.get());

    return ResponseEntity.ok(updateBooking);
  }

  private boolean isValidUsername(String ownerName) {
    return ownerName != null && !ownerName.isBlank();
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
