package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
  private final RoomRepository roomRepository;
}
