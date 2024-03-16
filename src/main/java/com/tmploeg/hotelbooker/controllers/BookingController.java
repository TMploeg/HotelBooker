package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.data.BookingRepository;
import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.NewBookingDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.ForbiddenException;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
import com.tmploeg.hotelbooker.services.UserService;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController extends ControllerBase {
  private final BookingRepository bookingRepository;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<BookingDTO>> getAll(@NotNull Principal principal) {
    User user = userService.getFromPrincipal(principal);
    Set<Booking> bookings = userService.getBookingsForUser(user);

    return ResponseEntity.ok(
        bookings.stream().map(BookingDTO::fromBooking).collect(Collectors.toList()));
  }

  @GetMapping("{id}")
  public ResponseEntity<BookingDTO> getById(@PathVariable Long id, Principal principal) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    User user = userService.getFromPrincipal(principal);

    return bookingRepository
        .findById(id)
        .filter(b -> b.getUser() == user)
        .map(b -> ResponseEntity.ok(BookingDTO.fromBooking(b)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<BookingDTO> addBooking(
      @RequestBody NewBookingDTO bookingDTO,
      UriComponentsBuilder ucb,
      @NotNull Principal principal) {
    if (bookingDTO == null) {
      throw new BadRequestException("booking data is required");
    }

    User user = userService.getFromPrincipal(principal);

    LocalDateTime checkIn =
        LocalDateTimeHelper.tryParse(bookingDTO.checkIn())
            .filter(dT -> !hasDatePassed(dT))
            .orElseThrow(() -> new BadRequestException("checkIn is invalid"));

    LocalDateTime checkOut =
        LocalDateTimeHelper.tryParse(bookingDTO.checkOut())
            .filter(dT -> !dT.isBefore(checkIn))
            .orElseThrow(() -> new BadRequestException("checkOut is invalid"));

    Booking booking = new Booking(user, checkIn, checkOut);

    if (hasOverlappingBookings(booking.getCheckIn(), booking.getCheckOut())) {
      throw new ForbiddenException("booking is occupied");
    }

    bookingRepository.save(booking);
    URI newBookingLocation = ucb.path("{id}").buildAndExpand(booking.getId()).toUri();
    return ResponseEntity.created(newBookingLocation).body(BookingDTO.fromBooking(booking));
  }

  @PatchMapping
  public ResponseEntity<BookingDTO> updateCheckOut(
      @RequestBody NewBookingDTO bookingDTO, Principal principal) {
    if (bookingDTO == null) {
      throw new BadRequestException("booking data is required");
    }

    User user = userService.getFromPrincipal(principal);

    if (bookingDTO.id() == null) {
      throw new BadRequestException("id is required");
    }

    Optional<Booking> booking = userService.getUserBooking(user, bookingDTO.id());

    if (booking.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    LocalDateTime newCheckIn =
        LocalDateTimeHelper.tryParse(bookingDTO.checkIn())
            .orElseThrow(() -> new BadRequestException("checkIn is invalid"));
    LocalDateTime newCheckOut =
        LocalDateTimeHelper.tryParse(bookingDTO.checkOut())
            .orElseThrow(() -> new BadRequestException("checkOut is invalid"));

    if (newCheckIn != booking.get().getCheckIn()) {
      if (hasDatePassed(booking.get().getCheckIn())) {
        throw new ForbiddenException("cannot change checkIn when already passed");
      }
      if (hasDatePassed(newCheckIn)) {
        throw new ForbiddenException("checkIn has already passed");
      }
    }

    if (newCheckOut != booking.get().getCheckOut()) {
      if (hasDatePassed(booking.get().getCheckOut())) {
        throw new ForbiddenException("cannot change checkOut when already passed");
      }
      if (hasDatePassed(newCheckOut)) {
        throw new ForbiddenException("checkOut has already passed");
      }
    }

    if (newCheckOut.isBefore(newCheckIn)) {
      throw new ForbiddenException("checkOut cannot be before checkIn");
    }

    booking.get().setCheckIn(newCheckIn);
    booking.get().setCheckOut(newCheckOut);

    bookingRepository.save(booking.get());

    return ResponseEntity.ok(BookingDTO.fromBooking(booking.get()));
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
