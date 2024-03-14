package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Booking {
  @Id @GeneratedValue private Long id;

  @ManyToOne private User user;

  private LocalDateTime checkIn;

  @Setter private LocalDateTime checkOut;

  public Booking(User user, LocalDateTime checkIn, LocalDateTime checkOut) {
    this.user = user;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }
}
