import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { FormGroup } from '@angular/forms';
import { TenantCreateRequest } from './domain/tenant-create-request';
import { TenantCreateResponse } from './domain/tenant-create-response';
import { environment } from '../../environments/environment';
import { AddUserRequest } from './domain/add-user-request';

@Injectable({
  providedIn: 'root'
})
export class IdentityService {

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  manualAddUser(formGroup: FormGroup, tenantId: string) {
    console.log('create user')
    console.log(formGroup.controls['email'].value)
    console.log(formGroup.controls['password'].value)
    const request = new AddUserRequest();
    request.email = formGroup.controls['email'].value;
    request.password = formGroup.controls['password'].value;
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
