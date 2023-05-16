import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NgxIridiumClientService } from '../../../../ngx-iridium-client/src/lib/ngx-iridium-client.service';
import { CookieService } from './cookie.service';
import { TenantSummaryResponse } from './domain/tenant-summary-response';
import { environment } from '../../environments/environment';
import { AbstractBaseService } from '../../../../ngx-iridium-client/src/lib/service/abstract-base-service';
import { catchError } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { TenantCreateRequest } from './domain/tenant-create-request';
import { TenantCreateResponse } from './domain/tenant-create-response';

@Injectable({
  providedIn: 'root'
})
export class TenantService extends AbstractBaseService {

  constructor(private http: HttpClient, private cookieService: CookieService) {
    super();
  }

  public getTenantSummaries() {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      Accept:  'application/vnd.iridium.id.tenant-summary-list.1+json',
      'Authorization': 'Bearer ' + token
    })

    const options = { headers: headers }
    return this.http.get<TenantSummaryResponse[]>(environment.iridium.domain + 'tenants', options)
      .pipe(
        catchError(this.handleError)
      )
  }

  public create(formGroup: FormGroup) {
    console.log('create tenant')
    console.log(formGroup.controls['tenantName'].value)
    console.log(formGroup.controls['environment'].value)
    const request = new TenantCreateRequest();
    request.environment = formGroup.controls['environment'].value;
    request.subdomain = formGroup.controls['tenantName'].value;
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      Accept:  'application/vnd.iridium.id.authn.tenant-create-response1+json',
      'Content-Type': 'application/vnd.iridium.id.authn.tenant-create-request.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.post<TenantCreateResponse>(environment.iridium.domain + 'tenants', request, options)

  }
}
