import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { environment } from '../../environments/environment';
import { FormGroup } from '@angular/forms';
import { ApplicationCreateResponse } from '../components/dashboard/domain/application-create-response';
import {
  CreateExternalIdentityProviderRequest
} from '../components/dashboard/domain/create-external-identity-provider-request';
import { ExternalIdentityProviderSummary } from './external-identity-provider-summary';
import { ApplicationUpdateRequest } from '../components/dashboard/domain/application-update-request';
import { ApplicationUpdateResponse } from '../components/dashboard/domain/application-update-response';
import { ExternalProviderResponse } from '../components/dashboard/domain/external-provider-response';
import { ExternalProviderUpdateRequest } from './domain/external-provider-update-request';

@Injectable({
  providedIn: 'root'
})
export class ExternalIdentityProviderService {

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  create(formGroup: FormGroup, tenantId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Content-Type':  'application/vnd.iridium.id.external-identity-provider-create-request.1+json',
      'Accept': 'application/vnd.iridium.id.external-identity-provider-create-response.1+json',
      'Authorization': 'Bearer ' + token
    })
    const request = new CreateExternalIdentityProviderRequest();
    request.externalProviderTemplateId = formGroup.controls['externalProviderTemplateId'].value;
    request.clientId = formGroup.controls['clientId'].value;
    request.clientSecret = formGroup.controls['clientSecret'].value;
    const options = { headers: headers }
    return this.http.post<ApplicationCreateResponse>(environment.iridium.domain + `tenants/${tenantId}/external-providers`, request, options)
  }

  getAll(tenantId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      Accept: 'application/vnd.iridium.id.external-identity-provider-summary-list.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<ExternalIdentityProviderSummary[]>(environment.iridium.domain + `tenants/${tenantId}/external-providers`, options)
  }

  get(tenantId: string, externalProviderId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      Accept: 'application/vnd.iridium.id.external-provider-response.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<ExternalProviderResponse>(environment.iridium.domain + `tenants/${tenantId}/external-providers/${externalProviderId}`, options)
  }

  update(formGroup: FormGroup, tenantId: string, externalProviderId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.iridium.id.external-provider-update-response.1+json',
      'Content-Type': 'application/vnd.iridium.id.external-provider-update-request.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    const request = new ExternalProviderUpdateRequest();
    request.clientId = formGroup.controls['clientId'].value;
    request.clientSecret = formGroup.controls['clientSecret'].value;
    return this.http.put<ApplicationUpdateResponse>(environment.iridium.domain + `tenants/${tenantId}/external-providers/${externalProviderId}`, request, options)

  }
}
