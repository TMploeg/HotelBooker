package com.tmploeg.hotelbooker.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class LocalDateTimeHelper {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private LocalDateTimeHelper() {}

  public static Optional<LocalDateTime> tryParse(String rawDateTime) {
    if (rawDateTime == null) {
      throw new NullPointerException("rawDateTime is null");
    }

    try {
      return Optional.of(LocalDateTime.parse(rawDateTime));
    } catch (DateTimeParseException ignored) {
      return Optional.empty();
    }
  }

  public static String format(LocalDateTime dateTime) {
    return dateTime.format(FORMATTER);
  }
}
