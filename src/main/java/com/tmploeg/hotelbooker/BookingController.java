package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.data.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<Booking> getById(@PathVariable long id){
        return bookingRepository
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("add")
    public ResponseEntity<Void> addBooking(@RequestBody Booking booking){
        bookingRepository.save(booking);

        return ResponseEntity.noContent().build();
    }
}
