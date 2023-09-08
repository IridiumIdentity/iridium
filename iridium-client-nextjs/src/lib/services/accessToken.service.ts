import { CookieService } from './cookie.service';
import { OauthConstants } from './oauth-constants';
import { useEffect } from 'react';

export class AccessTokenService {

    private cookieService: CookieService;

    constructor() {
        this.cookieService = new CookieService();
    }
}
export function exchange(code: string) {
    const cookieService = new CookieService();
    const redirectUri = process.env.NEXT_PUBLIC_IRIDIUM_REDIRECT_URI;
    const clientId = process.env.NEXT_PUBLIC_IRIDIUM_CLIENT_ID;
    const codeVerifier = cookieService.getCookie(OauthConstants.PKCE_CODE_VERIFIER);
    const state = cookieService.getCookie(OauthConstants.STATE);
    return {
        redirectUri: redirectUri,
        clientId: clientId,
        codeVerifier: codeVerifier,
        state: state
    }
}
