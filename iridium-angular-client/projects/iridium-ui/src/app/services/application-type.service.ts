import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from './cookie.service';
import { AbstractBaseService } from '../../../../ngx-iridium-client/src/lib/service/abstract-base-service';
import { TenantCreateResponse } from './domain/tenant-create-response';
import { environment } from '../../environments/environment';
import { ApplicationTypeSummary } from '../components/dashboard/domain/application-type-summary';

@Injectable({
  providedIn: 'root'
})
export class ApplicationTypeService extends AbstractBaseService {

  constructor(private http: HttpClient, private cookieService: CookieService) {
    super();
  }

  get() {
    const token = this.cookieService.getCookie('iridium-token')
    const headers = new HttpHeaders({
      Accept:  'application/vnd.iridium.id.application-type-summary-list.1+json',
      'Authorization': 'Bearer ' + token
    })
    const options = { headers: headers }
    return this.http.get<ApplicationTypeSummary[]>(environment.iridium.domain + 'application-types', options)
  }
}
