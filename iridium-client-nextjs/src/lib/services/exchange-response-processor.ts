import { OauthConstants } from './oauth-constants';
import { CookieService } from './cookie.service';

export class ExchangeResponseProcessor {
     async processExchangeResponse(response: Response) {
        if (response.status === 200) {
            const cookieService = new CookieService();
            // access token good.

            const jsonResponse = await response.json();
            cookieService.setCookie('iridium-token', jsonResponse.access_token, 1, OauthConstants.COOKIE_PATH)
            return true;
        } else {

            console.log('external exchange incorrect: ', response)
            return false
        }
    }

}
