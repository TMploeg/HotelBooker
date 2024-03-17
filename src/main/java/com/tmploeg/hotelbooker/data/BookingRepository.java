package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Booking;
import com.tmploeg.hotelbooker.models.User;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  Set<Booking> findByOrderByCheckIn();

  Set<Booking> findByUserOrderByCheckIn(User user);

  Set<Booking> findByCheckInBetweenAndCheckOutBetween(
      LocalDateTime checkInRangeStart,
      LocalDateTime checkInRangeEnd,
      LocalDateTime checkOutRangeStart,
      LocalDateTime checkOutRangeEnd);
}
