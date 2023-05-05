import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { PasswordResetRequest } from './domain/password-reset-request';

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  static CONTENT_TYPE = "application/vnd.carbonid.password-reset-request.1+json"
  
  resetPassword(request: PasswordResetRequest) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  PasswordService.CONTENT_TYPE,

      })
    };
  
    return this.http.post<void>("http://localhost:8381/identities/reset-password", request, httpOptions)
    .pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }

  constructor(private http: HttpClient) { }
}
