import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Booking } from '../models/entities/booking';
import { Observable, map, of, switchMap } from 'rxjs';
import { NewBookingDTO } from '../models/dtos/dtos.newbooking';
import { RoomDTO } from '../models/dtos/dtos.room';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  constructor(private apiService: ApiService) { }

  placeBooking(
    hotelId: number,
    checkIn: string,
    checkOut: string,
    nrOfRooms: number
  ): Observable<{
    newBooking: Booking | null,
    succes: boolean,
    errorMessage: string | null
  }> {
    return this.apiService.get<RoomDTO[]>('hotels/' + hotelId + '/rooms/available', {
      checkIn: checkIn,
      checkOut: checkOut
    }).pipe(switchMap((availableRoomsResponse, _) => {
      if (!availableRoomsResponse.succeeded) {
        const errorMessage: string = availableRoomsResponse.error;
        return of(this.placeBookingErrorResponse('unknown error occurred' + errorMessage));
      }

      if (availableRoomsResponse.body!.length < nrOfRooms) {
        return of(this.placeBookingErrorResponse('not enough rooms available'));
      }

      const newBooking: NewBookingDTO = {
        checkIn: checkIn,
        checkOut: checkOut,
        hotelId: hotelId,
        roomNumbers: availableRoomsResponse.body!.slice(0, nrOfRooms).map(r => r.roomNumber)
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
