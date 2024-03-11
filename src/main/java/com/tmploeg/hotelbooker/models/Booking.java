package com.tmploeg.hotelbooker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private String ownerName;

    Booking(){}

    public Booking(String ownerName){
        this.ownerName = ownerName;
    }

    public Long getId(){
        return id;
    }

    public String getOwnerName(){
        return ownerName;
    }
}
