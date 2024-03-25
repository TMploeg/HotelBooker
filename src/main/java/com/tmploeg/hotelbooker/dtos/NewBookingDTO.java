package com.tmploeg.hotelbooker.dtos;

public record NewBookingDTO(
    Long id, String checkIn, String checkOut, Long hotelId, Integer roomCount) {}
