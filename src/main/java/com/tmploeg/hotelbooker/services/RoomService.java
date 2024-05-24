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

  public ValueResult<Room> save(Long hotelId, int roomNumber) {
    if (hotelId == null) {
      throw new IllegalArgumentException("hotel is null");
    }

    List<String> errors = new LinkedList<>();

    Optional<Hotel> hotel = hotelService.findById(hotelId);
    if (hotel.isEmpty()) {
      errors.add("hotel does not exist");
    }

    if (roomNumber < 1) {
      errors.add("room number must be greater than or equal to '1'");
    }

    if (!errors.isEmpty()) {
      return ValueResult.errorResult(errors);
    }

    Room room = new Room(hotel.get(), roomNumber);

    Set<Room> hotelRooms = findByHotel(hotel.get());
    hotelRooms.add(room);
    hotel.get().setRooms(hotelRooms);
    hotelService.update(hotel.get());

    return ValueResult.succesResult(roomRepository.save(room));
  }

  public Set<Room> getAvailableRooms(Hotel hotel, LocalDateTime checkIn, LocalDateTime checkOut) {
    return roomRepository.getAvailableRoomsInHotel(hotel, checkIn, checkOut);
  }
}
