package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.RoomRepository;
import com.tmploeg.hotelbooker.models.ValueResult;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
  private final RoomRepository roomRepository;
  private final HotelService hotelService;

  public Optional<Room> findByHotelAndRoomNumber(Hotel hotel, int roomNumber) {
    return roomRepository.findByHotelAndRoomNumber(hotel, roomNumber);
  }

  public Set<Room> findByHotel(Hotel hotel) {
    return roomRepository.findByHotelOrderByRoomNumberAsc(hotel);
  }

  public ValueResult<Room> save(Hotel hotel, int roomNumber) {
    if (hotel == null) {
      throw new IllegalArgumentException("hotel is null");
    }

    List<String> errors = new LinkedList<String>();

    if (hotelService.findById(hotel.getId()).isEmpty()) {
      errors.add("hotel does not exist");
    }

    if (roomNumber < 1) {
      errors.add("room number must be greater than or equal to '1'");
    }

    return errors.isEmpty()
        ? ValueResult.succesResult(roomRepository.save(new Room(hotel, roomNumber)))
        : ValueResult.errorResult(errors);
  }

  public Set<Room> findAvailableRooms(Hotel hotel, LocalDateTime checkIn, LocalDateTime checkOut) {
    return roomRepository.findAvailableRoomsInHotel(hotel, checkIn, checkOut);
  }
}
