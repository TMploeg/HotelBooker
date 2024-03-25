package com.tmploeg.hotelbooker.dtos;

import java.util.List;

public record CanBookDTO(List<String> errors) {
  public boolean canBook() {
    return errors.isEmpty();
  }

  public CanBookDTO {
    if (errors == null) {
      throw new IllegalArgumentException("errors is null");
    }
  }
}
