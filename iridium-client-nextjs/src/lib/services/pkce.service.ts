import { CookieService } from './cookie.service';
import { OauthConstants } from './oauth-constants';

export class PKCEService {

    private cookieService: CookieService;
    constructor() {
        this.cookieService = new CookieService();
    }
    public generateCodeChallenge() {
        const verifier = this.generateRandomString();
        this.cookieService.setCookie(OauthConstants.PKCE_CODE_VERIFIER, verifier, 1, OauthConstants.COOKIE_PATH);
        return this.generateBase64EncodedChallenge(verifier);
    }
    public async generateBase64EncodedChallenge(verifier: string) {
        const signature = await this.generateSha256Signature(verifier);
        return this.base64UrlEncode(signature);
    }
    private generateRandomString(): string {
        const randomValues = window.crypto.getRandomValues(new Uint32Array(28));
        return Array.from(randomValues, dec =>
            ('0' + dec.toString(16)).substring(-2)).join('');
    }

    private async generateSha256Signature(clearText: string) {
        const data = new TextEncoder().encode(clearText);
        const hashBuffer = await window.crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));                     // convert buffer to byte array
        return hashArray.map(b => b.toString(16).padStart(2, '0')).join(''); // convert bytes to hex string

    }

    base64UrlEncode(input: string) {
        return btoa(input)
            .replace(/\+/g, '-')
            .replace(/\//g, '_')
            .replace(/=+$/, '');
    }
}
