import { CreateIdentityResponse } from './domain/create-identity-response';
import { CreateIdentityRequest } from './domain/create-identity-request';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  static CONTENT_TYPE = "application/vnd.carbonid.create-identity-request.1+json"
  static ACCEPT = "application/vnd.carbonid.identity-response.1+json"


  register(emailAddress: string) {
    var request: CreateIdentityRequest = {
      emailAddress: emailAddress
    }

    console.log('request:', request)
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  RegisterService.CONTENT_TYPE,
        'Accept': RegisterService.ACCEPT
      })
    };
  
    console.log("email address: " + request);
    return this.http.post<CreateIdentityResponse>("http://localhost:8381/identities", request, httpOptions)
    .pipe(
      catchError(this.handleError)
    );
  }



  constructor(private http: HttpClient) {

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
    return throwError(error.error);
  }
}
