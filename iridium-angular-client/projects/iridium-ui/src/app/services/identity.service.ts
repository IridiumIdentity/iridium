import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { FormGroup } from '@angular/forms';
import { TenantCreateResponse } from './domain/tenant-create-response';
import { environment } from '../../environments/environment';
import { AddUserRequest } from './domain/add-user-request';

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

  getIdentitySummaries(tenantId: string) {
    // todo
  }
}
