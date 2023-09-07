import { CookieService } from './cookie.service';
import { OauthConstants } from './oauth-constants';
import { useEffect } from 'react';

export class AccessTokenService {

    private cookieService: CookieService;


    constructor() {
        this.cookieService = new CookieService();
    }

    exchange(code: string) {
        const redirectUri = process.env.NEXT_PUBLIC_IRIDIUM_REDIRECT_URI;
        const clientId = process.env.NEXT_PUBLIC_IRIDIUM_CLIENT_ID;
        const codeVerifier = this.cookieService.getCookie(OauthConstants.PKCE_CODE_VERIFIER);
        console.log('exchanging')
        useEffect(() => {
            fetch(process.env.NEXT_PUBLIC_IRIDIUM_DOMAIN + 'oauth/token?grant_type=authorization_code&code=' + code + '&redirect_uri=' + redirectUri + '&client_id=' + clientId + '&code_verifier=' + codeVerifier, {headers: {
                    'Accept': 'application/json'
                }, method: 'POST'})
                .then((response) => response.json())
                .then((res) => {
                    console.log("access token response ", res)
                    if (res.status === 200) {
                        console.log("status is 200")
                        this.cookieService.setCookie('iridium-token', res.access_token, 1, OauthConstants.COOKIE_PATH)
                    } else {
                        const state = this.cookieService.getCookie(OauthConstants.STATE);
                        fetch(process.env.NEXT_PUBLIC_IRIDIUM_DOMAIN + 'oauth/token?grant_type=authorization_code&code=' + code + '&redirect_uri=' + redirectUri + '&client_id=' + clientId + '&code_verifier=' + codeVerifier + '&state=' + state, {headers: {
                                'Accept': 'application/json'
                            }, method: 'POST'})
                            .then((response) => {
                                if (response.status === 200) {
                                    // access token good.
                                    response.json().then(result => {
                                        this.cookieService.setCookie('iridium-token', result.access_token,1, OauthConstants.COOKIE_PATH)
                                    })

                                } else {
                                    console.log('external exchange incorrect: ', response)
                                }
                            })
                    }
                })

        }, [])

    }
}

export function exchange(code: string) {
    console.log('in exchange')
    const cookieService = new CookieService();
    const redirectUri = process.env.NEXT_PUBLIC_IRIDIUM_REDIRECT_URI;
    const clientId = process.env.NEXT_PUBLIC_IRIDIUM_CLIENT_ID;
    const codeVerifier = cookieService.getCookie(OauthConstants.PKCE_CODE_VERIFIER);
    const state = cookieService.getCookie(OauthConstants.STATE);
    console.log('exchanging')
    return {
        redirectUri: redirectUri,
        clientId: clientId,
        codeVerifier: codeVerifier,
        state: state
    }
}
