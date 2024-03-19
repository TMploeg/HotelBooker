package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.entities.Room;

public record RoomDTO(Integer roomNumber) {
  public static RoomDTO fromRoom(Room room) {
    return new RoomDTO(room.getRoomNumber());
  }
}
