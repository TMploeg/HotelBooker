package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.data.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.tmploeg.hotelbooker.models.Booking;
import org.springframework.web.util.UriComponentsBuilder;

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

    @GetMapping("get-by-ownername/{ownerName}")
    public ResponseEntity<List<Booking>> getByOwnerName(@PathVariable String ownerName){
        return ResponseEntity.ok(bookingRepository.findByOwnerName(ownerName));
    }

    @PostMapping("add")
    public ResponseEntity<Booking> addBooking(@RequestBody Booking booking, UriComponentsBuilder ucb){
        bookingRepository.save(booking);

        URI newBookingLocation = ucb
                .path("{id}")
                .buildAndExpand(booking.getId())
                .toUri();

        return ResponseEntity.created(newBookingLocation).body(booking);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id){
        Optional<Booking> deleteBooking = bookingRepository.findById(id);

        if(deleteBooking.isPresent()){
            bookingRepository.delete(deleteBooking.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
