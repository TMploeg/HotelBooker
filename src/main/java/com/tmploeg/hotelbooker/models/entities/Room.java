package com.tmploeg.hotelbooker.models.entities;

import com.tmploeg.hotelbooker.helpers.RoomNumberConverter;
import com.tmploeg.hotelbooker.models.RoomId;
import com.tmploeg.hotelbooker.models.RoomNumber;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(RoomId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Room {
  @Id @ManyToOne private Hotel hotel;

  @Id
  @Convert(converter = RoomNumberConverter.class)
  private RoomNumber roomNumber;

  @ManyToMany(mappedBy = "rooms")
  private Set<Booking> bookings;
}
