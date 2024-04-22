import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environment/environment';
import { AuthTokenDTO } from '../models/dtos/dtos.auth';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private apiService: ApiService) { }

  login(loginData: { username: string, password: string }): Observable<boolean> {
    return this.apiService.post<AuthTokenDTO>('auth/login', loginData).pipe(map(response => {
      response.ifSucceeded(body => sessionStorage.setItem(environment.tokenStorageLocation, body.token));

      return response.succeeded();
    }));
  }

  register(registerData: { username: string, password: string }) {
    return this.apiService.post('auth/register', registerData).pipe(map(response => response.succeeded));
  }
}
