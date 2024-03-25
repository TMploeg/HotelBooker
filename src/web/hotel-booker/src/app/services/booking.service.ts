import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Booking } from '../models/entities/booking';
import { Observable, map, of, switchMap } from 'rxjs';
import { NewBookingDTO } from '../models/dtos/dtos.newbooking';
import { RoomDTO } from '../models/dtos/dtos.room';
import { CanBookDTO } from '../models/dtos/dtos.canbook';

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
  ): Observable<{
    newBooking: Booking | null,
    succes: boolean,
    errorMessage: string | null
  }> {
    return this.apiService.get<CanBookDTO>('hotels/' + hotelId + '/rooms/can-book', {
      roomCount: roomCount,
      checkIn: checkIn,
      checkOut: checkOut
    }).pipe(switchMap((canBookResponse, _) => {
      if (!canBookResponse.succeeded) {
        const errorMessage: string = canBookResponse.error;
        return of(this.placeBookingErrorResponse('unknown error occurred' + errorMessage));
      }

      const value: CanBookDTO = canBookResponse.body!;

      if (!value.canBook) {
        return of(this.placeBookingErrorResponse('cannot place booking: ' + value.errors.join(';')));
      }

      const newBooking: NewBookingDTO = {
        checkIn: checkIn,
        checkOut: checkOut,
        hotelId: hotelId,
        roomCount: roomCount
      }

      return this.apiService.post<Booking>('bookings', newBooking).pipe(map(postBookingResponse => {
        return {
          newBooking: postBookingResponse.body,
          succes: postBookingResponse.succeeded,
          errorMessage: null
        };
      }));
    }));
  }

  private placeBookingErrorResponse(errorMessage: string): {
    newBooking: Booking | null,
    succes: boolean,
    errorMessage: string | null
  } {
    return {
      newBooking: null,
      succes: false,
      errorMessage: errorMessage
    };
  }
}
