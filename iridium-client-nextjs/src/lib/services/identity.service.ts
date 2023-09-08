import { useEffect } from 'react';
import { OauthConstants } from './oauth-constants';
import { CookieService } from './cookie.service';

export class IdentityService {

    private cookieService: CookieService;
    constructor() {
        this.cookieService = new CookieService();
    }

    public get(token: string) {
        useEffect(() => {
            fetch(process.env.NEXT_PUBLIC_IRIDIUM_DOMAIN + 'identities', {headers: {
                    'Accept': 'application/vnd.iridium.id.identity-response.1+json', 'Authorization': 'Bearer ' + token,
                }, method: 'POST'})
                .then((response) => response.json())
                .then((res) => {
                    if (res.status === 200) {
                        this.cookieService.setCookie('iridium-token', res.access_token, 1, OauthConstants.COOKIE_PATH)
                    } else {
                      console.error('error!')
                    }
                })

        }, [])
    }
}
