package com.tmploeg.hotelbooker.models;

import lombok.Value;

@Value
public class RoomNumber {
  private static final int MIN_VALUE = 1;

  Integer value;

  public RoomNumber(int value) {
    if (value < MIN_VALUE) {
      throw new IllegalArgumentException(
          "value must be greater than or equal to '" + MIN_VALUE + "'");
    }

    this.value = value;
  }
}
