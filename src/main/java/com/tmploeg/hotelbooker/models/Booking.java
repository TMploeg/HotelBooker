package com.tmploeg.hotelbooker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private long id;

    private String ownerName;

    public Booking(String ownerName){
        this.ownerName = ownerName;
    }

    public long getId(){
        return id;
    }

    public String getOwnerName(){
        return ownerName;
    }
}
