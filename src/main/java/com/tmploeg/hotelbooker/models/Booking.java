package com.tmploeg.hotelbooker.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private String ownerName;

    private LocalDateTime startDT;

    private LocalDateTime endDT;

    Booking(){}

    public Booking(String ownerName, LocalDateTime startDT, LocalDateTime endDT) {
        this.ownerName = ownerName;
        this.startDT = startDT;
        this.endDT = endDT;
    }

    public Long getId(){
        return id;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public LocalDateTime getStart(){
        return startDT;
    }

    public LocalDateTime getEnd(){
        return endDT;
    }
}
