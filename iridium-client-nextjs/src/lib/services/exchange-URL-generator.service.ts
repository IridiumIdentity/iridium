import { CookieService } from './cookie.service';
import { OauthConstants } from './oauth-constants';
import { exchange } from './accessToken.service';

export class ExchangeURLGeneratorService {
    generate() {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const output = this.authorize();
        return process.env.NEXT_PUBLIC_IRIDIUM_DOMAIN + 'oauth/token?grant_type=authorization_code&code=' + urlParams.get('code') + '&redirect_uri=' + output?.redirectUri + '&client_id=' + output?.clientId + '&code_verifier=' + output?.codeVerifier + '&state=' + output?.state;
    }

    authorize() {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const cookieService = new CookieService();
        const code = urlParams.get('code')
        const state = urlParams.get("state")
        if (code !== null && state === cookieService.getCookie(OauthConstants.STATE)) {
            return exchange(code)
        }
        console.log('code was null or state didn\'t match')

    }
}
