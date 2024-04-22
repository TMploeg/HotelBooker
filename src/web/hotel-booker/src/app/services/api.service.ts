import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from 'src/environment/environment';
import { ApiResponse } from '../models/api.response';

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
        headers: this.generateHeaders()
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
        headers: this.generateHeaders()
      }
    ));
  }

  handleResponse<TResponse>(obsResponse: Observable<HttpResponse<TResponse>>): Observable<ApiResponse<TResponse>> {
    return obsResponse.pipe(catchError(err => of(err)))
      .pipe(map((response: HttpResponse<TResponse> | HttpErrorResponse) => {
        if (!(response instanceof HttpErrorResponse)) {
          const body: TResponse = response.body!;
          return ApiResponse.succesResponse(body);
        }

        return ApiResponse.failureResponse(response.message);
      }
      ));
  }

  generateHeaders(): { [key: string]: string } {
    const authHeader = this.getAuthHeader();

    if (authHeader) {
      return { Authorization: authHeader }
    }

    return {};
  }

  getAuthHeader(): string | undefined {
    const token: string | null = sessionStorage.getItem(environment.tokenStorageLocation);

    if (!token) {
      return undefined;
    }

    return 'Bearer ' + token;
  }
}
