import { Injectable } from '@angular/core';
import { StateGeneratorService } from './service/state-generator.service';
import { CookieService } from './service/cookie.service';
import { PKCEService } from './service/pkce.service';
import { AuthorizationCodeFlowParameterService } from './service/authorization-code-flow-parameter.service';
import { UrlGeneratorService } from './service/url-generator.service';
import { ActivatedRoute } from '@angular/router';
import { AuthorizationService } from './service/authorization.service';
import { OauthConstants } from './service/oauth-constants';

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
        window.open(this.urlGenerator.retrieveIridiumAuthUrl(state, codeChallenge), '_blank');
      });
  }

  public authorize() {
    this.route.queryParamMap
      .subscribe(params => {
        console.log('params ', params);
        if (params.get('state') === this.cookieService.getCookie(OauthConstants.STATE)) {
          this.cookieService.deleteCookie(OauthConstants.STATE);
          const returnedCode = params.get('code');
          if (this.empty(returnedCode)) {
            console.log('an error occurred');
            return;
          }
          // get token from accessCode
          this.authorizationService.exchange(returnedCode).subscribe(authzResponse => {
              const accessToken = authzResponse;
              return true;
            },
            error => {
              console.log('error get  user: ', error);
            });
        } else {
          console.log('an error occurred');
          return;
        }
      });
  }

  private empty(e: any) {
    switch (e) {
      case '':
      case 0:
      case '0':
      case null:
      case false:
      case undefined:
        return true;
      default:
        return false;
    }
  }
}
