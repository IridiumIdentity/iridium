import { Injectable } from '@angular/core';
import { StateGeneratorService } from './service/state-generator.service';
import { CookieService } from './service/cookie.service';
import { PKCEService } from './service/pkce.service';
import { AuthorizationCodeFlowParameterService } from './service/authorization-code-flow-parameter.service';
import { UrlGeneratorService } from './service/url-generator.service';
import { ActivatedRoute } from '@angular/router';
import { AuthorizationService } from './service/authorization.service';
import { OauthConstants } from './service/oauth-constants';
import { AccessTokenResponse } from './domain/access-token-response';
import { BehaviorSubject, Observable } from 'rxjs';
import { IdentityResponse } from './domain/identity-response';

@Injectable({
  providedIn: 'root'
})
export class NgxIridiumClientService {


  constructor(
    private stateGenerator: StateGeneratorService,
    private cookieService: CookieService,
    private pkceService: PKCEService,
    private authCodeService: AuthorizationCodeFlowParameterService,
    private urlGenerator: UrlGeneratorService,
    private route: ActivatedRoute,
    private authorizationService: AuthorizationService,
  ) { }

  public authenticateWithExternalRedirect(): void {
    let state = this.stateGenerator.generate();

    this.cookieService.setCookie(OauthConstants.STATE, state, 1, OauthConstants.COOKIE_PATH);

    this.pkceService.generateCodeChallenge()
      .then(codeChallenge => {
        const httpOptions = this.authCodeService.generateHttpParams();
        this.cookieService.setCookie(OauthConstants.CODE_CHALLENGE, codeChallenge, 1, OauthConstants.COOKIE_PATH);
        window.location.href = this.urlGenerator.retrieveIridiumAuthUrl(state, codeChallenge);
      });
  }

  public async authorize() {
    const state = this.route.snapshot.queryParamMap.get('state');
    const code = this.route.snapshot.queryParamMap.get('code');
    const successful = false;
    if (state === this.cookieService.getCookie(OauthConstants.STATE)) {
      this.cookieService.deleteCookie(OauthConstants.STATE);

      try {
        const accessCodeResponse = await this.authorizationService.exchange(code)
        console.log("local exchanged access code is ", accessCodeResponse)
        if (accessCodeResponse != undefined) {
          this.cookieService.setCookie('iridium-token', accessCodeResponse.access_token, 1, OauthConstants.COOKIE_PATH);
          return true;
        }
      } catch (error) {
        const externalAccessCodeResponse = await this.authorizationService.exchangeForExternalIdentity(code, state);
        if (externalAccessCodeResponse != undefined) {
          this.cookieService.setCookie('iridium-token', externalAccessCodeResponse.access_token, 1, OauthConstants.COOKIE_PATH);
          return true;
        }
      }
    }
    // otherwise check for valid cookie
    const response = await this.authorizationService.getIdentity(this.cookieService.getCookie('iridium-token'))
    return response != undefined;

  }

  public isAuthenticated(): Observable<IdentityResponse> {
      return this.authorizationService.getIdentity(this.cookieService.getCookie('iridium-token'))
  }

}
