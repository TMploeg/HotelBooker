package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Booking {
  @Id @GeneratedValue private Long id;

  private String ownerName;

  private LocalDateTime checkIn;

  private LocalDateTime checkOut;

  Booking() {}

  public Booking(String ownerName, LocalDateTime checkIn, LocalDateTime checkOut) {
    this.ownerName = ownerName;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }

  public Long getId() {
    return id;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public LocalDateTime getCheckIn() {
    return checkIn;
  }

  public LocalDateTime getCheckOut() {
    return checkOut;
  }
}
