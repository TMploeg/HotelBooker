package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.entities.Booking;
import com.tmploeg.hotelbooker.models.entities.User;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  Set<Booking> findByOrderByCheckIn();

  Set<Booking> findByUserOrderByCheckIn(User user);
}
