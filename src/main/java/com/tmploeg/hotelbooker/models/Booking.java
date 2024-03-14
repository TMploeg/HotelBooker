package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {
  @Id @GeneratedValue private Long id;

  private User owner;

  private LocalDateTime checkIn;

  private LocalDateTime checkOut;

  Booking() {}

  public Booking(User owner, LocalDateTime checkIn, LocalDateTime checkOut) {
    this.owner = owner;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }

  public Long getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public LocalDateTime getCheckIn() {
    return checkIn;
  }

  public LocalDateTime getCheckOut() {
    return checkOut;
  }

  public void setCheckout(LocalDateTime checkOut) {
    this.checkOut = checkOut;
  }
}
