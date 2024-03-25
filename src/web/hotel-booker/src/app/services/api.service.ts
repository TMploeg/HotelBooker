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

  get<TResponse>(url: string, params?: { [key: string]: string }): Observable<ApiResponse<TResponse>> {
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
        params: this.generateHttpParams(params),
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
    const username: string = 'Yggdrasil';
    const password: string = 'BlueQueue_0428';

    const encodedCredentials = 'Basic ' + btoa(username + ':' + password);
    return encodedCredentials;
  }

  generateHttpParams(params?: { [key: string]: string }) {
    const httpParams: HttpParams = new HttpParams();

    if (!params) {
      return httpParams;
    }

    Object.keys(params).forEach(key => {
      httpParams.set(key, params[key]);
    });

    return httpParams;
  }
}
