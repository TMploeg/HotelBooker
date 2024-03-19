package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.NewBookingDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.ForbiddenException;
import com.tmploeg.hotelbooker.exceptions.NotFoundException;
import com.tmploeg.hotelbooker.helpers.CollectionHelper;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
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
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController {
  private final BookingService bookingService;
  private final UserService userService;
  private final HotelService hotelService;
  private final RoomService roomService;

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
      throw new BadRequestException("hotel id is required");
    }

    Hotel hotel = hotelService.findById(bookingDTO.hotelId()).orElseThrow(NotFoundException::new);

    if (bookingDTO.roomNumbers() == null) {
      throw new BadRequestException("rooms is required");
    }

    if (CollectionHelper.hasDuplicates(bookingDTO.roomNumbers())) {
      throw new BadRequestException("room numbers cannot have duplicates");
    }

    Set<Room> rooms =
        Arrays.stream(bookingDTO.roomNumbers())
            .map(
                n ->
                    roomService
                        .findByHotelAndRoomNumber(hotel, n)
                        .orElseThrow(
                            () -> new BadRequestException("invalid room number(s) detected")))
            .collect(Collectors.toSet());

    ValueResult<Booking> saveBookingResult = bookingService.save(user, checkIn, checkOut, rooms);

    if (!saveBookingResult.succeeded()) {
      throw new BadRequestException(String.join(";", saveBookingResult.getErrors()));
    }

    URI newBookingLocation =
        ucb.path("{id}").buildAndExpand(saveBookingResult.getValue().getId()).toUri();

    return ResponseEntity.created(newBookingLocation)
        .body(BookingDTO.fromBooking(saveBookingResult.getValue()));
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

    Booking booking =
        bookingService
            .findById(bookingDTO.id())
            .filter(b -> b.getUser() == user)
            .orElseThrow(NotFoundException::new);

    LocalDateTime newCheckIn =
        LocalDateTimeHelper.tryParse(bookingDTO.checkIn())
            .orElseThrow(() -> new BadRequestException("checkIn is invalid"));
    LocalDateTime newCheckOut =
        LocalDateTimeHelper.tryParse(bookingDTO.checkOut())
            .orElseThrow(() -> new BadRequestException("checkOut is invalid"));

    if (newCheckIn != booking.getCheckIn()) {
      if (LocalDateTimeHelper.hasDatePassed(booking.getCheckIn())) {
        throw new ForbiddenException("cannot change checkIn when already passed");
      }
      if (LocalDateTimeHelper.hasDatePassed(newCheckIn)) {
        throw new ForbiddenException("checkIn has already passed");
      }
    }

    if (newCheckOut != booking.getCheckOut()) {
      if (LocalDateTimeHelper.hasDatePassed(booking.getCheckOut())) {
        throw new ForbiddenException("cannot change checkOut when already passed");
      }
      if (LocalDateTimeHelper.hasDatePassed(newCheckOut)) {
        throw new ForbiddenException("checkOut has already passed");
      }
    }

    if (newCheckOut.isBefore(newCheckIn)) {
      throw new ForbiddenException("checkOut cannot be before checkIn");
    }

    booking.setCheckIn(newCheckIn);
    booking.setCheckOut(newCheckOut);

    bookingService.update(booking);

    return ResponseEntity.ok(BookingDTO.fromBooking(booking));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id, Authentication authentication) {
    if (id == null) {
      throw new BadRequestException("id is required");
    }

    User user = getUser(authentication);

    Booking deleteBooking =
        bookingService
            .findById(id)
            .filter(b -> b.getUser() == user)
            .orElseThrow(NotFoundException::new);

    bookingService.delete(deleteBooking);
    return ResponseEntity.noContent().build();
  }

  private User getUser(@NotNull Authentication authentication) {
    return (User) authentication.getPrincipal();
  }
}
