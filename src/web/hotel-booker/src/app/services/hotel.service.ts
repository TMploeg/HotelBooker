import { Injectable } from '@angular/core';
import { Hotel } from '../models/hotel';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  constructor(private apiService: ApiService) { }

  getAll(): Observable<Hotel[] | null> {
    return this.apiService.get<Hotel[]>(environment.apiBaseUrl + 'hotels').pipe(map(response => response.body));
  }
}
