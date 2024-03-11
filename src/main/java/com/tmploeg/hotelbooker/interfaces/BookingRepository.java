package com.tmploeg.hotelbooker.interfaces;

import com.tmploeg.hotelbooker.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
