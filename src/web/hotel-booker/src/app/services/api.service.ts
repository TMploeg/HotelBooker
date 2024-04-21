import { HttpClient, HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { ApiResponse } from '../models/api.response';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  get<TResponse>(url: string, params?: { [key: string]: string | number | boolean }): Observable<ApiResponse<TResponse>> {
    return this.handleResponse(this.httpClient.get<TResponse>(
      environment.apiBaseUrl + url,
      {
        observe: 'response',
        params: params,
        headers: {
          Authorization: this.getAuthHeader()
        }
      }
    ));
  }

  post<TResponse>(url: string, body: any, params?: { [key: string]: string }) {
    return this.handleResponse(this.httpClient.post<TResponse>(
      environment.apiBaseUrl + url,
      body,
      {
        observe: 'response',
        params: params,
        headers: {
          Authorization: this.getAuthHeader()
        }
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

  getAuthHeader(): string {
    const token: string = 'eyJhbGciOiJIUzI1NiJ9.' +
      'eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwic3ViIjoidGVzdHVzZXIiLCJpYXQiOjE3MTM3MTIxMDQsImV4cCI6MTcxMzcxNTcwNH0.' +
      'DThDNRCSBirvCLZdW1L1aFpqvRWxRewA_d31gxRtVIg';

    return 'Bearer ' + token;
  }
}
