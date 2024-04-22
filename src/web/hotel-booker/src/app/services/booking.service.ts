import { Injectable } from '@angular/core';
import { Observable, of, switchMap } from 'rxjs';
import { ApiResponse } from '../models/api.response';
import { CanBookDTO } from '../models/dtos/dtos.canbook';
import { NewBookingDTO } from '../models/dtos/dtos.newbooking';
import { Booking } from '../models/entities/booking';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  constructor(private apiService: ApiService) { }

  getBookings() {
    return this.apiService.get<Booking[]>('bookings');
  }

  placeBooking(
    hotelId: number,
    checkIn: string,
    checkOut: string,
    roomCount: number
  ): Observable<ApiResponse<Booking>> {
    return this.apiService.get<CanBookDTO>('hotels/' + hotelId + '/rooms/can-book', {
      roomCount: roomCount,
      checkIn: checkIn,
      checkOut: checkOut
    }).pipe(switchMap((canBookResponse, _) => {
      if (canBookResponse.succeeded()) {

        const newBooking: NewBookingDTO = {
          checkIn: checkIn,
          checkOut: checkOut,
          hotelId: hotelId,
          roomCount: roomCount
        }

        return this.apiService.post<Booking>('bookings', newBooking);
      }

      return of(ApiResponse.convertFailureResponse<CanBookDTO, Booking>(canBookResponse));
    }));
  }
}
