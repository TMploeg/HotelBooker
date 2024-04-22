import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api.response';
import { Hotel } from '../models/entities/hotel';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  constructor(private apiService: ApiService) { }

  getAll(): Observable<ApiResponse<Hotel[]>> {
    return this.apiService.get<Hotel[]>('hotels');
  }

  getById(hotelId: number): Observable<ApiResponse<Hotel>> {
    return this.apiService.get<Hotel>('hotels/' + hotelId);
  }
}
