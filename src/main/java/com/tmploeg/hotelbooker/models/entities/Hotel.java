package com.tmploeg.hotelbooker.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Hotel {
  @Id @GeneratedValue private Long id;

  private String name;

  private String address;

  @OneToMany(mappedBy = "hotel")
  private Set<Room> rooms;

  public Hotel(String name, String address) {
    this.name = name;
    this.address = address;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Hotel otherHotel)) {
      return false;
    }

    return Objects.equals(this.id, otherHotel.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
