package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.NewBookingDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.ForbiddenException;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.SaveBookingResult;
import com.tmploeg.hotelbooker.models.User;
import com.tmploeg.hotelbooker.services.BookingService;
import com.tmploeg.hotelbooker.services.UserService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController {
  private final BookingService bookingService;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<BookingDTO>> getAll(@NotNull Authentication authentication) {
    User user = getUser(authentication);

    Set<Booking> bookings =
        userService.isAdmin(user) ? bookingService.getAll() : bookingService.findByUser(user);

    return ResponseEntity.ok(
        bookings.stream().map(BookingDTO::fromBooking).collect(Collectors.toList()));
  }

  @GetMapping("{id}")
  public ResponseEntity<BookingDTO> getById(@PathVariable Long id, Authentication authentication) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    User user = getUser(authentication);

    return bookingService
        .findById(id)
        .filter(b -> userService.isAdmin(user) || b.isOwnedByUser(user))
        .map(b -> ResponseEntity.ok(BookingDTO.fromBooking(b)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<BookingDTO> addBooking(
      @RequestBody NewBookingDTO bookingDTO,
      UriComponentsBuilder ucb,
      @NotNull Authentication authentication) {
    if (bookingDTO == null) {
      throw new BadRequestException("booking data is required");
    }

    User user = getUser(authentication);

    LocalDateTime checkIn =
        LocalDateTimeHelper.tryParse(bookingDTO.checkIn())
            .orElseThrow(() -> new BadRequestException("checkIn is invalid"));

    LocalDateTime checkOut =
        LocalDateTimeHelper.tryParse(bookingDTO.checkOut())
            .orElseThrow(() -> new BadRequestException("checkOut is invalid"));

    SaveBookingResult result = bookingService.save(user, checkIn, checkOut);

    if (!result.succeeded()) {
      throw new BadRequestException(String.join(";", result.getErrors()));
    }

    URI newBookingLocation =
        ucb.path("{id}").buildAndExpand(result.getSavedBooking().getId()).toUri();

    return ResponseEntity.created(newBookingLocation)
        .body(BookingDTO.fromBooking(result.getSavedBooking()));
  }

  @PatchMapping
  public ResponseEntity<BookingDTO> updateBooking(
      @RequestBody NewBookingDTO bookingDTO, Authentication authentication) {
    if (bookingDTO == null) {
      throw new BadRequestException("booking data is required");
    }

    User user = getUser(authentication);

    if (bookingDTO.id() == null) {
      throw new BadRequestException("id is required");
    }

    Optional<Booking> booking =
        bookingService.findById(bookingDTO.id()).filter(b -> b.getUser() == user);

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
      if (LocalDateTimeHelper.hasDatePassed(booking.get().getCheckIn())) {
        throw new ForbiddenException("cannot change checkIn when already passed");
      }
      if (LocalDateTimeHelper.hasDatePassed(newCheckIn)) {
        throw new ForbiddenException("checkIn has already passed");
      }
    }

    if (newCheckOut != booking.get().getCheckOut()) {
      if (LocalDateTimeHelper.hasDatePassed(booking.get().getCheckOut())) {
        throw new ForbiddenException("cannot change checkOut when already passed");
      }
      if (LocalDateTimeHelper.hasDatePassed(newCheckOut)) {
        throw new ForbiddenException("checkOut has already passed");
      }
    }

    if (newCheckOut.isBefore(newCheckIn)) {
      throw new ForbiddenException("checkOut cannot be before checkIn");
    }

    booking.get().setCheckIn(newCheckIn);
    booking.get().setCheckOut(newCheckOut);

    bookingService.update(booking.get());

    return ResponseEntity.ok(BookingDTO.fromBooking(booking.get()));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id, Authentication authentication) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    User user = getUser(authentication);

    Optional<Booking> deleteBooking = bookingService.findById(id).filter(b -> b.getUser() == user);

    if (deleteBooking.isPresent()) {
      bookingService.delete(deleteBooking.get());
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private User getUser(@NotNull Authentication authentication) {
    return (User) authentication.getPrincipal();
  }
}
