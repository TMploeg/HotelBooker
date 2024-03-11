package com.tmploeg.hotelbooker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bookings")
public class BookingController {
    public record Booking(int id, String ownerName, LocalDateTime timeslot){}

    private final List<Booking> bookings;

    public BookingController(){
        int count = 10;

        bookings = new ArrayList<>(count);

        for(int i = 0; i < count; i++){
            bookings.add(new Booking(i + 1, "person_" + i, LocalDateTime.now()));
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Booking>> getAll(){
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("{id}")
    public ResponseEntity<Booking> getById(@PathVariable int id){
        for(Booking booking : bookings){
            if(booking.id() == id){
                return ResponseEntity.ok(booking);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
