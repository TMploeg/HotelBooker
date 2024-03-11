package com.tmploeg.hotelbooker.dto;

import com.tmploeg.hotelbooker.models.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookingDTO {
    private final String ownerName;

    private final String startDTString;

    private final String endDTString;

    public BookingDTO(String ownerName, String startDTString, String endDTString) {
        this.ownerName = ownerName;

        this.startDTString = startDTString;
        this.endDTString = endDTString;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getStartDTString() {
        return startDTString;
    }

    public String getEndDTString() {
        return endDTString;
    }

    public Booking convert(){
        LocalDateTime startDT = null;
        try{
            startDT = LocalDateTime.parse(startDTString);
        }
        catch(DateTimeParseException ignored){ }

        LocalDateTime endDT = null;
        try{
            endDT = LocalDateTime.parse(endDTString);
        }
        catch(DateTimeParseException ignored){ }

        return new Booking(ownerName, startDT, endDT);
    }

    public static BookingDTO fromBooking(Booking booking){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new BookingDTO(
                booking.getOwnerName(),
                booking.getStart().format(formatter),
                booking.getEnd().format(formatter)
        );
    }
}
