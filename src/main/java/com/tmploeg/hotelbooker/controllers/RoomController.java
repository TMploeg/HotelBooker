package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.CanBookDTO;
import com.tmploeg.hotelbooker.dtos.RoomDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.NotFoundException;
import com.tmploeg.hotelbooker.helpers.CollectionHelper;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
import com.tmploeg.hotelbooker.services.HotelService;
import com.tmploeg.hotelbooker.services.RoomService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping(ControllerRoutes.HOTELS + "/{hotelId}/" + ControllerRoutes.ROOMS)
@RestController
public class RoomController {
  private final RoomService roomService;
  private final HotelService hotelService;

  @GetMapping
  public Set<RoomDTO> getAll(@PathVariable Long hotelId) {
    Hotel hotel = hotelService.findById(hotelId).orElseThrow(NotFoundException::new);

    return roomService.findByHotel(hotel).stream()
        .map(RoomDTO::fromRoom)
        .collect(Collectors.toSet());
  }

  @PostMapping
  public ResponseEntity<Set<RoomDTO>> addRoom(
      @PathVariable Long hotelId, @RequestBody RoomDTO[] newRooms, UriComponentsBuilder ucb) {
    if (newRooms == null) {
      throw new BadRequestException("room data is required");
    }
    if (newRooms.length == 0) {
      throw new BadRequestException("roomNumber is required");
    }

    List<Integer> roomNumbers = Arrays.stream(newRooms).map(RoomDTO::roomNumber).toList();
    if (CollectionHelper.hasDuplicates(roomNumbers)) {
      throw new BadRequestException("room numbers must be unique");
    }

    Hotel hotel = hotelService.findById(hotelId).orElseThrow(NotFoundException::new);

    Set<Integer> duplicateRoomNumbers =
        roomNumbers.stream()
            .filter(
                roomNumber -> roomService.findByHotelAndRoomNumber(hotel, roomNumber).isPresent())
            .collect(Collectors.toSet());

    if (!duplicateRoomNumbers.isEmpty()) {
      throw new BadRequestException(
          "rooms { "
              + String.join(", ", duplicateRoomNumbers.stream().map(Object::toString).toList())
              + " } already present in this hotel");
    }

    Set<ValueResult<Room>> saveRoomResults =
        roomNumbers.stream().map(n -> roomService.save(hotel, n)).collect(Collectors.toSet());

    List<String> errors =
        saveRoomResults.stream()
            .filter(r -> !r.succeeded())
            .map(r -> String.join(";", r.getErrors()))
            .toList();
    if (!errors.isEmpty()) {
      throw new BadRequestException(String.join("|", errors));
    }

    URI newRoomPath =
        ucb.path(ControllerRoutes.HOTELS + "/{id}/rooms").buildAndExpand(hotel.getId()).toUri();

    return ResponseEntity.created(newRoomPath)
        .body(
            saveRoomResults.stream()
                .map(r -> RoomDTO.fromRoom(r.getValue()))
                .collect(Collectors.toSet()));
  }

  @GetMapping("{roomNumber}")
  public RoomDTO getHotelRoomByRoomNumber(
      @PathVariable Long hotelId, @PathVariable Integer roomNumber) {
    Room room =
        hotelService
            .findById(hotelId)
            .flatMap(hotel -> roomService.findByHotelAndRoomNumber(hotel, roomNumber))
            .orElseThrow(NotFoundException::new);

    return RoomDTO.fromRoom(room);
  }

  @GetMapping("can-book")
  public CanBookDTO canBookRooms(
      @PathVariable Long hotelId,
      @RequestParam Integer roomCount,
      @RequestParam String checkIn,
      @RequestParam String checkOut) {
    if (hotelId == null) {
      throw new BadRequestException("hotelId is required");
    }
    Hotel hotel = hotelService.findById(hotelId).orElseThrow(NotFoundException::new);

    if (checkIn == null) {
      throw new BadRequestException("checkIn is required");
    }
    LocalDateTime parsedCheckIn =
        LocalDateTimeHelper.tryParse(checkIn)
            .filter(dT -> dT.isAfter(LocalDateTime.now()))
            .orElseThrow(() -> new BadRequestException("checkIn is invalid"));

    if (checkOut == null) {
      throw new BadRequestException("checkOut is required");
    }
    LocalDateTime parsedCheckOut =
        LocalDateTimeHelper.tryParse(checkOut)
            .filter(dT -> !dT.isBefore(parsedCheckIn))
            .orElseThrow(() -> new BadRequestException("checkOut is invalid"));

    if (roomCount == null) {
      throw new BadRequestException("roomCount is required");
    }

    List<String> errors = new LinkedList<>();

    if (roomService.getAvailableRooms(hotel, parsedCheckIn, parsedCheckOut).size() < roomCount) {
      errors.add("insufficient available rooms");
    }

    return new CanBookDTO(errors);
  }
}
