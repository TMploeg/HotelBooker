package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
  private final HotelRepository hotelRepository;
}
