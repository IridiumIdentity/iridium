import { OauthConstants } from './oauth-constants';
import { CookieService } from './cookie.service';

export class ExchangeResponseProcessor {
     processExchangeResponse(response: Response) {
        if (response.status === 200) {
            // access token good.

            response.json().then(result => {
                console.log('second method 200')
                const cookieService = new CookieService();
                cookieService.setCookie('iridium-token', result.access_token, 1, OauthConstants.COOKIE_PATH)
                return true;
            })
            return false;
        } else {

            console.log('external exchange incorrect: ', response)
            return false
        }
    }

}
