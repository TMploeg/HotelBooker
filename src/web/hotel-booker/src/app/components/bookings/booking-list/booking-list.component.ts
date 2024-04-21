import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Booking } from 'src/app/models/entities/booking';
import { BookingService } from 'src/app/services/booking.service';

@Component({
  selector: 'app-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.scss']
})
export class BookingListComponent implements OnInit {
  bookings?: Booking[];
  loading: boolean = false;

  constructor(private bookingService: BookingService, private router: Router) { }

  ngOnInit(): void {
    this.loading = true;
    this.bookingService.getBookings().subscribe(response => {
      this.loading = false;

      if (!response.succeeded) {
        alert('unknown error occurred, could not get bookings');
        console.error(response.error);
        return;
      }

      this.bookings = response.body!;
    });
  }

  navigate(booking: Booking) {
    this.router.navigateByUrl('/bookings/' + booking.id);
  }
}
