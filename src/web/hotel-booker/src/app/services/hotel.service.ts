import { Injectable } from '@angular/core';
import { Hotel } from '../models/entities/hotel';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  constructor(private apiService: ApiService) { }

  getAll(): Observable<Hotel[] | null> {
    return this.apiService.get<Hotel[]>('hotels').pipe(map(response => response.body));
  }

  getById(hotelId: number): Observable<Hotel | null> {
    return this.apiService.get<Hotel>('hotels/' + hotelId).pipe(map(response => response.body));
  }
}
