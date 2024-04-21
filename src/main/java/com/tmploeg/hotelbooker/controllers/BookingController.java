package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.NewBookingDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.NotFoundException;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.User;
import com.tmploeg.hotelbooker.services.BookingService;
import com.tmploeg.hotelbooker.services.HotelService;
import com.tmploeg.hotelbooker.services.RoomService;
import com.tmploeg.hotelbooker.services.UserService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ControllerRoutes.BOOKINGS)
@RequiredArgsConstructor
public class BookingController {
  private final BookingService bookingService;
  private final UserService userService;
  private final HotelService hotelService;
  private final RoomService roomService;

  @GetMapping
  public List<BookingDTO> getAll(@NotNull Authentication authentication) {
    User user = getUser(authentication);

    Set<Booking> bookings =
        userService.isAdmin(user) ? bookingService.getAll() : bookingService.findByUser(user);

    return bookings.stream().map(BookingDTO::fromBooking).collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public BookingDTO getById(@PathVariable Long id, Authentication authentication) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    User user = getUser(authentication);

    return bookingService
        .findById(id)
        .filter(b -> userService.isAdmin(user) || b.isOwnedByUser(user))
        .map(BookingDTO::fromBooking)
        .orElseThrow(NotFoundException::new);
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

    if (bookingDTO.hotelId() == null) {
      throw new BadRequestException("hotelId is required");
    }

    Hotel hotel =
        hotelService
            .findById(bookingDTO.hotelId())
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "no hotel with id '" + bookingDTO.hotelId() + "' exists"));

    if (bookingDTO.roomCount() == null) {
      throw new BadRequestException("roomCount is required");
    }

    ValueResult<Booking> saveBookingResult =
        bookingService.save(user, hotel, checkIn, checkOut, bookingDTO.roomCount());

    if (!saveBookingResult.succeeded()) {
      throw new BadRequestException(String.join(";", saveBookingResult.getErrors()));
    }

    URI newBookingLocation =
        ucb.path(ControllerRoutes.BOOKINGS + "{id}")
            .buildAndExpand(saveBookingResult.getValue().getId())
            .toUri();

    return ResponseEntity.created(newBookingLocation)
        .body(BookingDTO.fromBooking(saveBookingResult.getValue()));
  }

  @PatchMapping
  public BookingDTO updateBooking(
      @RequestBody NewBookingDTO bookingDTO, Authentication authentication) {
    if (bookingDTO == null) {
      throw new BadRequestException("booking data is required");
    }

    User user = getUser(authentication);

    if (bookingDTO.id() == null) {
      throw new BadRequestException("id is required");
    }

    Booking booking =
        bookingService
            .findById(bookingDTO.id())
            .filter(b -> b.isOwnedByUser(user))
            .orElseThrow(
                () ->
                    new BadRequestException("no booking with id '" + bookingDTO.id() + "' exists"));

    LocalDateTime newCheckIn =
        bookingDTO.checkIn() != null
            ? LocalDateTimeHelper.tryParse(bookingDTO.checkIn())
                .orElseThrow(() -> new BadRequestException("checkIn is invalid"))
            : null;
    LocalDateTime newCheckOut =
        bookingDTO.checkOut() != null
            ? LocalDateTimeHelper.tryParse(bookingDTO.checkOut())
                .orElseThrow(() -> new BadRequestException("checkOut is invalid"))
            : null;

    updateBookingCheckInAndCheckOut(booking, newCheckIn, newCheckOut);

    return BookingDTO.fromBooking(booking);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id, Authentication authentication) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    Booking deleteBooking =
        bookingService
            .findById(id)
            .filter(b -> b.getUser() == getUser(authentication))
            .orElseThrow(NotFoundException::new);

    bookingService.delete(deleteBooking);
    return ResponseEntity.noContent().build();
  }

  private User getUser(@NotNull Authentication authentication) {
    return (User) authentication.getPrincipal();
  }

  private void updateBookingCheckInAndCheckOut(
      Booking booking, LocalDateTime newCheckIn, LocalDateTime newCheckOut) {
    if (booking == null) {
      throw new IllegalArgumentException("booking is null");
    }

    if (LocalDateTimeHelper.hasDatePassed(booking.getCheckIn())) {
      throw new BadRequestException("cannot update booking when checkIn has already passed");
    }

    if (newCheckIn != null) {
      if (LocalDateTimeHelper.hasDatePassed(newCheckIn)) {
        throw new BadRequestException("new checkIn value has already passed");
      }

      if (newCheckOut == null && newCheckIn.isAfter(booking.getCheckOut())) {
        throw new BadRequestException("new checkIn value is after checkOut");
      }

      booking.setCheckIn(newCheckIn);
    }

    if (newCheckOut != null) {
      if (LocalDateTimeHelper.hasDatePassed(newCheckOut)) {
        throw new BadRequestException("new checkOut value has already passed");
      }

      if (newCheckOut.isBefore(booking.getCheckIn())) {
        throw new BadRequestException("new checkOut value is before checkIn");
      }

      booking.setCheckOut(newCheckOut);
    }

    bookingService.update(booking);
  }
}
