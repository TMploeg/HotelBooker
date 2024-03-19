package com.tmploeg.hotelbooker.models;

import com.tmploeg.hotelbooker.models.entities.Hotel;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RoomId implements Serializable {
  private Hotel hotel;
  private int roomNumber;

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RoomId roomId)) {
      return false;
    }

    if (roomId.getRoomNumber() != this.getRoomNumber()) {
      return false;
    }

    return Objects.equals(roomId.getHotel().getId(), this.getHotel().getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(hotel.getId(), roomNumber);
  }
}
