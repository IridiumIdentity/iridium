import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ApiDataResponse } from '../domain/api-data-response';
import { AccessTokenResponse } from '../domain/access-token-response';
import { OauthConstants } from './oauth-constants';
import { CookieService } from './cookie.service';
import { AbstractBaseService } from './abstract-base-service';
import { IdentityResponse } from '../domain/identity-response';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService extends AbstractBaseService {

  constructor(protected http: HttpClient,
              @Inject('config') private config:any,
  private cookieService: CookieService) {
    super();
  }

  exchangeForExternalIdentity(code: string | null, state: string | null) {
    const headers = new HttpHeaders({
      Accept:  'application/json',
    });

    const httpOptions = {
      headers,
    };
    const redirectUri = this.config.iridium.redirectUri;
    const clientId = this.config.iridium.clientId;

    const codeVerifier = this.cookieService.getCookie(OauthConstants.PKCE_CODE_VERIFIER);
    const url = this.config.iridium.domain
      + 'oauth/token?grant_type=authorization_code&code='
      // tslint:disable-next-line:max-line-length
      + code + '&redirect_uri=' + redirectUri + '&client_id=' + clientId + '&code_verifier=' + codeVerifier + '&state=' + state
    return this.http.post<AccessTokenResponse>(url,null, httpOptions).toPromise();
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

    const codeVerifier = this.cookieService.getCookie(OauthConstants.PKCE_CODE_VERIFIER);

    return this.http.post<AccessTokenResponse>(
      this.config.iridium.domain
      + 'oauth/token?grant_type=authorization_code&code='
      + code + '&redirect_uri=' + redirectUri + '&client_id=' + clientId + '&code_verifier=' + codeVerifier, null, httpOptions).toPromise()
  }

  getIdentity(bearerToken: string) {
    const headers = new HttpHeaders({
      Accept:  'application/vnd.iridium.id.identity-response.1+json',
      Authorization: 'Bearer ' + bearerToken
    });

    const httpOptions = {
      headers,
    };

    return this.http.get<IdentityResponse>(
      this.config.iridium.domain
      + 'identities', httpOptions).toPromise()
  }
}
