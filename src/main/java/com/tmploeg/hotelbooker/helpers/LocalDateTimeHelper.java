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
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    try {
      return Optional.of(LocalDateTime.from(formatter.parse(rawDateTime)));
    } catch (DateTimeParseException ignored) {
      return Optional.empty();
    }
  }

  public static String format(LocalDateTime dateTime) {
    return dateTime.format(FORMATTER);
  }

  public static boolean hasDatePassed(LocalDateTime dt) {
    return !dt.isAfter(LocalDateTime.now());
  }
}
