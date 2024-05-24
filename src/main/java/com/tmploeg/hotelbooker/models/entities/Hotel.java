package com.tmploeg.hotelbooker.models.entities;

import com.tmploeg.hotelbooker.models.Address;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Hotel {
  @Id @GeneratedValue private Long id;

  private String name;

  @Embedded private Address address;

  @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
  @Setter
  private Set<Room> rooms = new HashSet<>();

  public Hotel(String name, Address address) {
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
