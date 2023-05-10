import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { environment } from '../../environments/environment';
import { FormGroup } from '@angular/forms';
import { CreateApplicationResponse } from '../components/dashboard/domain/create-application-response';
import { CreateApplicationRequest } from '../components/dashboard/domain/create-application-request';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  create(formGroup: FormGroup) {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      'Content-Type':  'application/vnd.iridium.id.application-create-request.1+json',
      'Accept': 'application/vnd.iridium.id.application-create-response.1+json',
      'Authorization': 'Bearer ' + token
    })
    const request = new CreateApplicationRequest();
    request.name = formGroup.controls['applicationName'].value;
    request.applicationTypeId = formGroup.controls['applicationTypeId'].value;
    const options = { headers: headers }
    const tenantId = 'somethign'
    return this.http.post<CreateApplicationResponse>(environment.iridium.domain + `/tenants/${tenantId}/applications`, request, options)
  }
}
