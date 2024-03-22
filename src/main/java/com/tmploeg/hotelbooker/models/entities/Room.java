package com.tmploeg.hotelbooker.models.entities;

import com.tmploeg.hotelbooker.models.RoomId;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(RoomId.class)
@NoArgsConstructor
@Getter
public class Room {
  @Id @ManyToOne private Hotel hotel;

  @Id private int roomNumber;

  @ManyToMany(mappedBy = "rooms")
  private Set<Booking> bookings;

  public Room(Hotel hotel, int roomNumber) {
    this.hotel = hotel;
    this.roomNumber = roomNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Room otherRoom)) {
      return false;
    }

    return Objects.equals(this.hotel, otherRoom.hotel)
        && Objects.equals(this.roomNumber, otherRoom.roomNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hotel, roomNumber);
  }
}
