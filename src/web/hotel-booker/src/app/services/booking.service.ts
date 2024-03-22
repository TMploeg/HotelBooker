import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Booking } from '../models/entities/booking';
import { Observable, map } from 'rxjs';
import { NewBookingDTO } from '../models/dtos/dtos.newbooking';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  constructor(private apiService: ApiService) { }

  placeBooking(booking: NewBookingDTO): Observable<boolean> {
    return this.apiService.post<Booking>('bookings', booking)
      .pipe(map(response => response.succeeded));
  }
}
