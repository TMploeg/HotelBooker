package com.tmploeg.hotelbooker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController {
    public record Booking(String ownerName, LocalDateTime timeslot){}

    @GetMapping("")
    public List<Booking> getAll(){
        int count = 10;

        List<Booking> bookings = new ArrayList<>(count);

        for(int i = 0; i < count; i++){
            bookings.add(new Booking("person_" + i, LocalDateTime.now()));
        }

        return bookings;
    }
}
