import { Injectable } from '@angular/core';
import { Hotel } from '../models/entities/hotel';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { ErrorResult, SuccesResult } from '../models/results';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  constructor(private apiService: ApiService) { }

  getAll(): Observable<SuccesResult<Hotel[]> | ErrorResult> {
    return this.apiService.get<Hotel[]>('hotels').pipe(map(response => response.succeeded
      ? new SuccesResult(response.body!)
      : new ErrorResult(response.error))
    );
  }

  getById(hotelId: number): Observable<SuccesResult<Hotel> | ErrorResult> {
    return this.apiService.get<Hotel>('hotels/' + hotelId).pipe(map(response => response.succeeded
      ? new SuccesResult(response.body!)
      : new ErrorResult(response.error))
    );
  }
}
