import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { environment } from '../../environments/environment';
import { FormGroup } from '@angular/forms';
import { ApplicationCreateResponse } from '../components/dashboard/domain/application-create-response';
import { ApplicationCreateRequest } from '../components/dashboard/domain/application-create-request';
import { ApplicationSummaryResponse } from './domain/application-summary-response';
import { PagedListResponse } from './domain/paged-list-response';
import { ApplicationResponse } from './domain/application-response';
import { ApplicationUpdateRequest } from '../components/dashboard/domain/application-update-request';
import { ApplicationUpdateResponse } from '../components/dashboard/domain/application-update-response';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  create(formGroup: FormGroup, tenantId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Content-Type':  'application/vnd.iridium.id.application-create-request.1+json',
      'Accept': 'application/vnd.iridium.id.application-create-response.1+json',
      'Authorization': 'Bearer ' + token
    })
    const request = new ApplicationCreateRequest();
    request.name = formGroup.controls['applicationName'].value;
    request.applicationTypeId = formGroup.controls['applicationTypeId'].value;
    request.callbackURL = formGroup.controls['authorizationCallbackURL'].value;
    request.description = formGroup.controls['description'].value;
    request.homepageURL = formGroup.controls['homepageURL'].value;
    const options = { headers: headers }
    return this.http.post<ApplicationCreateResponse>(environment.iridium.domain + `tenants/${tenantId}/applications`, request, options)
  }

  getPage(tenantId: string, count: number) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.iridium.id.application-summary-list.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<PagedListResponse<ApplicationSummaryResponse>>(environment.iridium.domain + `tenants/${tenantId}/applications?page=0&size=${count}`, options)
  }

  get(tenantId: string, applicationId: string) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.iridium.id.application-response.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<ApplicationResponse>(environment.iridium.domain + `tenants/${tenantId}/applications/${applicationId}`, options)
  }

  update(formGroup: FormGroup, tenantId: string, applicationId: string) {
    console.log('update controls are', formGroup.controls)
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.iridium.id.application-update-response.1+json',
      'Content-Type': 'application/vnd.iridium.id.application-update-request.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    const request = new ApplicationUpdateRequest();
    request.applicationTypeId = formGroup.controls['applicationTypeId'].value;
    request.name = formGroup.controls['applicationName'].value;
    request.description = formGroup.controls['description'].value;
    request.homePageUrl = formGroup.controls['homepageURL'].value;
    request.privacyPolicyUrl = formGroup.controls['privacyPolicyURL'].value;
    request.redirectUri = formGroup.controls['authorizationCallbackURL'].value;
    request.iconUrl = formGroup.controls['iconURL'].value;
    return this.http.put<ApplicationUpdateResponse>(environment.iridium.domain + `tenants/${tenantId}/applications/${applicationId}`, request, options)

  }
}
