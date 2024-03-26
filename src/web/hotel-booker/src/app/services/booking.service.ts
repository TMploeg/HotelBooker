import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Booking } from '../models/entities/booking';
import { Observable, map, of, switchMap } from 'rxjs';
import { NewBookingDTO } from '../models/dtos/dtos.newbooking';
import { CanBookDTO } from '../models/dtos/dtos.canbook';
import { ErrorResult, SuccesResult } from '../models/results';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  constructor(private apiService: ApiService) { }

  placeBooking(
    hotelId: number,
    checkIn: string,
    checkOut: string,
    roomCount: number
  ): Observable<SuccesResult<Booking> | ErrorResult> {
    return this.apiService.get<CanBookDTO>('hotels/' + hotelId + '/rooms/can-book', {
      roomCount: roomCount,
      checkIn: checkIn,
      checkOut: checkOut
    }).pipe(switchMap((canBookResponse, _) => {
      console.log(canBookResponse.body!.canBook);
      if (!canBookResponse.succeeded) {
        return of(new ErrorResult([canBookResponse.error]));
      }

      const value: CanBookDTO = canBookResponse.body!;

      if (!value.canBook) {
        return of(new ErrorResult(value.errors));
      }

      const newBooking: NewBookingDTO = {
        checkIn: checkIn,
        checkOut: checkOut,
        hotelId: hotelId,
        roomCount: roomCount
      }

      return this.apiService.post<Booking>('bookings', newBooking).pipe(map(postBookingResponse => {
        if (!postBookingResponse.succeeded) {
          return new ErrorResult(['booking failed due to unknown error']);
        }

        return new SuccesResult<Booking>(postBookingResponse.body!);
      }));
    }));
  }
}
