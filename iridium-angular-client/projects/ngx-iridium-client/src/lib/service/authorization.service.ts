import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ApiDataResponse } from '../domain/api-data-response';
import { AccessTokenResponse } from '../domain/access-token-response';
import { OauthConstants } from './oauth-constants';
import { CookieService } from './cookie.service';
import { AbstractBaseService } from './abstract-base-service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService extends AbstractBaseService {

  constructor(protected http: HttpClient,
              @Inject('config') private config:any,
  private cookieService: CookieService) {
    super();
  }

  exchange(code: string | null) {
    const headers = new HttpHeaders({
      Accept:  'application/json',
    });

    const httpOptions = {
      headers,
    };
    const redirectUri = this.config.iridium.redirectUri;
    const clientId = this.config.iridium.clientId;

    const codeVerifier = this.cookieService.getCookie('pkce_verifier');

    return this.http.post<AccessTokenResponse>(
      this.config.authenticationApiBaseUrl
      + 'oauth/token?grant_type=authorization_code&code='
      + code + '&redirect_uri=' + redirectUri + '&client_id=' + clientId + '&code_verifier=' + codeVerifier, null, httpOptions)
      .pipe(catchError(this.handleError));
  }
}
