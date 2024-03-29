package com.tmploeg.hotelbooker.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Booking {
  @Id @GeneratedValue private Long id;

  @ManyToOne private User user;

  @Setter private LocalDateTime checkIn;

  @Setter private LocalDateTime checkOut;

  @ManyToMany private Set<Room> rooms;

  public Booking(User user, LocalDateTime checkIn, LocalDateTime checkOut, Set<Room> rooms) {
    this.user = user;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.rooms = rooms;
  }

  public boolean isOwnedByUser(User user) {
    return user.getUsername().equals(this.user.getUsername());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Booking otherBooking)) {
      return false;
    }

    return Objects.equals(this.id, otherBooking.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
