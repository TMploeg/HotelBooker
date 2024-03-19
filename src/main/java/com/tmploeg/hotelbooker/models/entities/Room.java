package com.tmploeg.hotelbooker.models.entities;

import com.tmploeg.hotelbooker.models.RoomId;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
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
}
