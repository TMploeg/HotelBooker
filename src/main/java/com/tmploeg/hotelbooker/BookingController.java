package com.tmploeg.hotelbooker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController {
    public record Booking(int id, String ownerName){}

    private final List<Booking> bookings;

    public BookingController(){
        int count = 10;

        bookings = new LinkedList<>();

        for(int i = 0; i < count; i++){
            bookings.add(new Booking(i + 1, "person_" + i));
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

    @PostMapping("add")
    public ResponseEntity<Void> addBooking(@RequestBody Booking booking){
        bookings.add(booking);
        return ResponseEntity.noContent().build();
    }
}
