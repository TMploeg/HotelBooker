package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.interfaces.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.tmploeg.hotelbooker.models.Booking;

@RestController
@RequestMapping("bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    public BookingController(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Booking>> getAll(){
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("{id}")
    public ResponseEntity<Booking> getById(@PathVariable int id){
        return ResponseEntity.notFound().build();
    }

    @PostMapping("add")
    public ResponseEntity<Void> addBooking(@RequestBody Booking booking){
        return ResponseEntity.noContent().build();
    }
}
