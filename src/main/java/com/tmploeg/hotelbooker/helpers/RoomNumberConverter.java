package com.tmploeg.hotelbooker.helpers;

import com.tmploeg.hotelbooker.models.RoomNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoomNumberConverter implements AttributeConverter<RoomNumber, Integer> {
  @Override
  public Integer convertToDatabaseColumn(RoomNumber roomNumber) {
    return roomNumber == null ? null : roomNumber.getValue();
  }

  @Override
  public RoomNumber convertToEntityAttribute(Integer value) {
    return value == null ? null : new RoomNumber(value);
  }
}
