package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Booking;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  Set<Booking> findByOrderByCheckIn();

  Set<Booking> findByCheckInBetweenAndCheckOutBetween(
      LocalDateTime checkInRangeStart,
      LocalDateTime checkInRangeEnd,
      LocalDateTime checkOutRangeStart,
      LocalDateTime checkOutRangeEnd);
}
