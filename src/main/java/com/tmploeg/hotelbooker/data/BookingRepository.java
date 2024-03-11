package com.tmploeg.hotelbooker.data;

import com.tmploeg.hotelbooker.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByOwnerName(String ownerName);
}
