import { Injectable } from '@angular/core';
import { Hotel } from '../models/hotel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  constructor() { }

  getAll(): Observable<Hotel[]> {
    let hotels: Hotel[] = [];

    for (let i: number = 1; i <= 5; i++) {
      hotels.push({ id: i, name: 'hotel' + i, address: 'hotel' + i + '_address' });
    }

    return new Observable<Hotel[]>(observer => {
      setInterval(() => observer.next(hotels), 3000);
    });
  }
}
