package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {
  @Id @GeneratedValue private Long id;

  @ManyToOne private User user;

  private LocalDateTime checkIn;

  private LocalDateTime checkOut;

  Booking() {}

  public Booking(User user, LocalDateTime checkIn, LocalDateTime checkOut) {
    this.user = user;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
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
