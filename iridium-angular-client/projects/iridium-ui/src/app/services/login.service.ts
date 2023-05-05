import { AuthenticationResponse } from './domain/authentication-response';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { sha256 } from 'js-sha256';
import { AuthenticationRequest } from './domain/authentication-request';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  static CONTENT_TYPE = "application/vnd.carbonid.authentication-request.1+json"
  static ACCEPT = "application/vnd.carbonid.authentication-response.1+json"


  authenticate(request: AuthenticationRequest) {


    console.log('request:', request)
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  LoginService.CONTENT_TYPE,
        'Accept': LoginService.ACCEPT
      })
    };

    console.log("email address: " + request);
    return this.http.post<AuthenticationResponse>("http://localhost:8381/authentications", request, httpOptions)
    .pipe(
      catchError(this.handleError)
    );
  }

  authorize() {
    let random = ()=> crypto.getRandomValues(new Uint32Array(12))[0]/2**32;
    let number = random();
    let hexValue = number.toString(16);
    let state = this.utf8ToHex(hexValue);
    console.log( state);

    const params = new HttpParams()
    .set('response_type', 'code')
    .set('client_id', '5e7a719eeb2c2324ef4d')
    .set('redirect_uri', 'http://localhost:4200/authorize/redirect')
    .set('scope', 'user')
    .set('state', state)

    const httpOptions = {
      params: params,

      headers: new HttpHeaders({
        'Content-Type':  'application/x-www-form-urlencoded',
      })
    };
    console.log("sending post")
    return this.http.get<HttpResponse<null>>("http://localhost:4200/api/authorize", httpOptions)
    //return this.http.post<HttpResponse<null>>("", "", httpOptions)
    .pipe(
      //catchError(this.handleError)
    )

  }

  utf8ToHex(str: string) {
    return Array.from(str).map(c =>
      c.charCodeAt(0) < 128 ? c.charCodeAt(0).toString(16) :
      encodeURIComponent(c).replace(/\%/g,'').toLowerCase()
    ).join('');
  }

  constructor(private http: HttpClient) { }


  post(): void {

    console.log('log in attempt')
    var obj = '{"typ":"JWT","alg":"RS256"}'
    var encoded = this.base64_urlencode(obj)
    var codeVerifier = this.randomString(55)
    console.log("code verifier: " + codeVerifier);
    var codeChallenge = this.base64_urlencode(sha256(codeVerifier));
    console.log("code challenge: " + codeChallenge);



  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.' + error.error);
  }

  randomString(length: number): string {
    var randomChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~';
    var result = '';
    for ( var i = 0; i < length; i++ ) {
        result += randomChars.charAt(Math.floor(Math.random() * randomChars.length));
    }
    return result;
}


  base64_urlencode(url: string): string {
    return btoa(url)
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=+$/, '');
}


}
