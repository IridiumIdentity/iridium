import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { FormGroup } from '@angular/forms';
import { TenantCreateResponse } from './domain/tenant-create-response';
import { environment } from '../../environments/environment';
import { AddUserRequest } from './domain/add-user-request';
import { PagedListResponse } from './domain/paged-list-response';
import { ApplicationSummaryResponse } from './domain/application-summary-response';
import { IdentitySummaryResponse } from '../components/dashboard/domain/identity-summary-response';

@Injectable({
  providedIn: 'root'
})
export class IdentityService {

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  manualAddUser(formGroup: FormGroup, tenantId: string) {
    const request = new AddUserRequest();
    request.email = formGroup.controls['email'].value;
    request.password = formGroup.controls['password'].value;
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.post<TenantCreateResponse>(environment.iridium.domain + 'tenants', request, options)

  }

  getSummariesByParentTenant(tenantId: string, count: number) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Accept': 'application/vnd.iridium.id.identity-summary-response-list.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<PagedListResponse<IdentitySummaryResponse>>(environment.iridium.domain + `tenants/${tenantId}/identities?page=0&size=${count}`, options)
  }
}
