import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { ApiResponse } from '../models/api.response';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  get<TResponse>(url: string): Observable<ApiResponse<TResponse>> {
    return this.handleResponse(this.httpClient.get<TResponse>(
      url,
      {
        observe: 'response'
      }
    ));
  }

  handleResponse<TResponse>(obsResponse: Observable<HttpResponse<TResponse>>): Observable<ApiResponse<TResponse>> {
    return obsResponse.pipe(catchError(err => of(err)))
      .pipe(map((response: HttpResponse<TResponse> | HttpErrorResponse) => {
        const isErrorResponse = response instanceof HttpErrorResponse;

        return {
          body: !isErrorResponse ? response.body : null,
          error: isErrorResponse ? response.error : null,
          succeeded: !isErrorResponse
        }
      }));
  }
}
