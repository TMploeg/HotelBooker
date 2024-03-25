package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.RoomId;
import com.tmploeg.hotelbooker.models.entities.Hotel;
import com.tmploeg.hotelbooker.models.entities.Room;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, RoomId> {
  Optional<Room> findByHotelAndRoomNumber(Hotel hotel, int roomNumber);

  Set<Room> findByHotelOrderByRoomNumberAsc(Hotel hotel);

  @Query(
      "SELECT count(r) FROM Room r WHERE"
          + " r.hotel = ?1 AND"
          + " (select count(b) FROM r.bookings b WHERE b.checkIn < ?3 AND b.checkOut > ?2) = 0")
  Integer getAvailableRoomCountInHotel(Hotel hotel, LocalDateTime checkIn, LocalDateTime checkOut);
}
