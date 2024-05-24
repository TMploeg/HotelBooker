package com.tmploeg.hotelbooker.controllers;

import com.tmploeg.hotelbooker.dtos.HotelDTO;
import com.tmploeg.hotelbooker.exceptions.BadRequestException;
import com.tmploeg.hotelbooker.exceptions.NotFoundException;
import com.tmploeg.hotelbooker.models.Address;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.services.BookingService;
import com.tmploeg.hotelbooker.services.HotelService;
import com.tmploeg.hotelbooker.services.RoomService;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
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
  public List<HotelDTO> getAll(
      @RequestParam(required = false) String search, @RequestParam(required = false) String city) {
    return hotelService.search(search, city).stream()
        .map(HotelDTO::fromHotel)
        .collect(Collectors.toList());
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

    validateHotelAddress(newHotelDTO.address());

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

  private void validateHotelAddress(Address address) {
    if (address == null) {
      throw new BadRequestException("hotel address is required");
    }

    List<String> addressErrors = new LinkedList<>();

    if (address.getCity() == null || address.getCity().isBlank()) {
      addressErrors.add("address city is required");
    }
    if (address.getStreet() == null || address.getStreet().isBlank()) {
      addressErrors.add("address street is required");
    }
    if (address.getHouseNumber() == 0) {
      addressErrors.add("address house number must be greater than 0");
    }

    if (!addressErrors.isEmpty()) {
      throw new BadRequestException(String.join("; ", addressErrors));
    }
  }
}
