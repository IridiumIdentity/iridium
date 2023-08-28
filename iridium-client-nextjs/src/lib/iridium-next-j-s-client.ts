import { StateGeneratorService } from './services';
import { CookieService } from './services';
import { PKCEService } from './services';
import { OauthConstants } from './services';
import { UrlGeneratorService } from './services';
import { useSearchParams } from 'next/navigation'
import { AccessTokenService } from './services';
import { IdentityService } from './services';

export class IridiumNextJSClient {

    private stateGenerator: StateGeneratorService;
    private cookieService: CookieService;
    private pkceService: PKCEService;
    private urlGenerator: UrlGeneratorService;
    private accessTokenService: AccessTokenService;
    private identityService: IdentityService;
    constructor() {
        this.stateGenerator = new StateGeneratorService();
        this.cookieService = new CookieService();
        this.pkceService = new PKCEService();
        this.urlGenerator = new UrlGeneratorService();
        this.accessTokenService = new AccessTokenService();
        this.identityService = new IdentityService();
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

    public authorize() {

        const searchParams = useSearchParams()
        const code = searchParams.get("code")
        const state = searchParams.get("state")
        console.log('code', code)
        console.log('state', state)
        if (code !== null && state === this.cookieService.getCookie(OauthConstants.STATE)) {
            this.cookieService.deleteCookie(OauthConstants.STATE)
            this.accessTokenService.exchange(code)
        }

    }

    public isAuthenticated() {
        return this.identityService.get(this.cookieService.getCookie('iridium-token'))
    }
}
