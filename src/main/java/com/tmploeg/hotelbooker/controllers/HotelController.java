package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.BookingDTO;
import com.tmploeg.hotelbooker.dtos.HotelDTO;
import com.tmploeg.hotelbooker.dtos.RoomDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.NotFoundException;
import com.tmploeg.hotelbooker.helpers.CollectionHelper;
import com.tmploeg.hotelbooker.helpers.LocalDateTimeHelper;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
import com.tmploeg.hotelbooker.services.BookingService;
import com.tmploeg.hotelbooker.services.HotelService;
import com.tmploeg.hotelbooker.services.RoomService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ControllerRoutes.HOTELS)
@RequiredArgsConstructor
public class HotelController {
  private final HotelService hotelService;
  private final RoomService roomService;
  private final BookingService bookingService;

  @GetMapping
  public Set<HotelDTO> getAll() {
    return hotelService.getAll().stream().map(HotelDTO::fromHotel).collect(Collectors.toSet());
  }

  @GetMapping("{hotelId}")
  public HotelDTO getById(@PathVariable Long hotelId) {
    Hotel hotel = hotelService.findById(hotelId).orElseThrow(NotFoundException::new);

    return HotelDTO.fromHotel(hotel);
  }

  @PostMapping
  ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO newHotelDTO, UriComponentsBuilder ucb) {
    if (newHotelDTO == null) {
      throw new BadRequestException("new hotel data is required");
    }

    if (newHotelDTO.name() == null || newHotelDTO.name().isBlank()) {
      throw new BadRequestException("name is required");
    }

    if (newHotelDTO.address() == null || newHotelDTO.address().isBlank()) {
      throw new BadRequestException("address is required");
    }

    ValueResult<Hotel> saveHotelResult =
        hotelService.save(newHotelDTO.name(), newHotelDTO.address());

    if (!saveHotelResult.succeeded()) {
      throw new BadRequestException(String.join(";", saveHotelResult.getErrors()));
    }

    URI newHotelURI =
        ucb.path(ControllerRoutes.HOTELS + "{id}")
            .buildAndExpand(saveHotelResult.getValue().getId())
            .toUri();

    return ResponseEntity.created(newHotelURI).body(HotelDTO.fromHotel(saveHotelResult.getValue()));
  }
}
