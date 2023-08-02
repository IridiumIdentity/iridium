import { Inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UrlGeneratorService {

  constructor(@Inject('config') private config:any) { }

  retrieveIridiumAuthUrl(state: string, codeChallenge: string): string {
    const baseUrl = this.config.iridium.domain;
    const redirectUri = this.config.iridium.redirectUri;
    const clientId = this.config.iridium.clientId;

    return `${baseUrl}login?response_type=code&state=${state}&redirect_uri=${redirectUri}&client_id=${clientId}&code_challenge_method=S256&code_challenge=${codeChallenge}`;
  }
}
