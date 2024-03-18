package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.RoomId;
import com.tmploeg.hotelbooker.models.entities.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, RoomId> {}
