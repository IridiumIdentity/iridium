import { StateGeneratorService } from './stateGenerator.service';
import { CookieService } from './cookie.service';
import { PKCEService } from './pkce.service';
import { OauthConstants } from './oauth-constants';
import { UrlGeneratorService } from './urlGenerator.service';
import { useSearchParams } from 'next/navigation'
import { AccessTokenService, exchange } from './accessToken.service';
import { IdentityService } from './identity.service';
import { ExchangeResponseProcessor } from './exchange-response-processor';
import { ExchangeURLGeneratorService } from './exchange-URL-generator.service';
import { ExchangeHeaderGeneratorService } from './exchange-header-generator.service';

export class IridiumNextJSClientService {

    private stateGenerator: StateGeneratorService;
    private cookieService: CookieService;
    private pkceService: PKCEService;
    private urlGenerator: UrlGeneratorService;
    private accessTokenService: AccessTokenService;
    private identityService: IdentityService;
    private exchangeResponseProcessor: ExchangeResponseProcessor;
    private exchangeURLGenerator: ExchangeURLGeneratorService;
    private exchangeHeaderGenerator: ExchangeHeaderGeneratorService;
    constructor() {
        this.stateGenerator = new StateGeneratorService();
        this.cookieService = new CookieService();
        this.pkceService = new PKCEService();
        this.urlGenerator = new UrlGeneratorService();
        this.accessTokenService = new AccessTokenService();
        this.identityService = new IdentityService();
        this.exchangeResponseProcessor = new ExchangeResponseProcessor();
        this.exchangeURLGenerator = new ExchangeURLGeneratorService();
        this.exchangeHeaderGenerator = new ExchangeHeaderGeneratorService();
    }

    public authenticateWithExternalRedirect(): void {
        let state = this.stateGenerator.generate();

        this.cookieService.setCookie(OauthConstants.STATE, state, 1, OauthConstants.COOKIE_PATH);

        this.pkceService.generateCodeChallenge()
            .then(codeChallenge => {
                this.cookieService.setCookie(OauthConstants.CODE_CHALLENGE, codeChallenge, 1, OauthConstants.COOKIE_PATH);
                window.open(this.urlGenerator.retrieveIridiumAuthUrl(state, codeChallenge), '_blank')
            })

    }



    public isAuthenticated() {
        return this.identityService.get(this.cookieService.getCookie('iridium-token'))
    }

    public processExchangeResponse(response: Response) {
        return this.exchangeResponseProcessor.processExchangeResponse(response)
    }

    public generateExchangeURL() {
        return this.exchangeURLGenerator.generate();
    }

    public generateExchangeHeaders() {
        return this.exchangeHeaderGenerator.generate();
    }
}


