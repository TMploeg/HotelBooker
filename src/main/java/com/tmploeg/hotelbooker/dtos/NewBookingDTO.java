package com.tmploeg.hotelbooker.dtos;

import com.tmploeg.hotelbooker.models.RoomNumber;

public record NewBookingDTO(
    Long id, String checkIn, String checkOut, Long hotelId, RoomNumber[] roomNumbers) {}
