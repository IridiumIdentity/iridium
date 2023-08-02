import { Inject, Injectable } from '@angular/core';
import { HttpHeaders, HttpParams } from '@angular/common/http';
import { StateGeneratorService } from './state-generator.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationCodeFlowParameterService {


  constructor(private stateGenerator: StateGeneratorService, @Inject('config') private config:any) {
  }

  public generateHttpParams(): { headers: HttpHeaders; params: HttpParams } {
    console.log('iridium subscribe config: ',  this.config.iridium);
    const domain =  this.config.iridium.domain;
    console.log('domain is: ', domain);
    const state = this.stateGenerator.generate();
    const codeChallengeMethod = 'S256';
    const httpParams =  new HttpParams()
      .set('response_type', 'code')
      .set('client_id',  this.config.iridium.clientId)
      .set('redirect_uri',  this.config.iridium.redirectUri)
      .set('scope', 'user')
      .set('state', state);

    return {
      params: httpParams,

      headers: new HttpHeaders({
        'Content-Type':  'application/x-www-form-urlencoded',
      })
    };
  }
}
