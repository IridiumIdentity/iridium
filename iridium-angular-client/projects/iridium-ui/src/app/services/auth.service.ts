import { Injectable } from '@angular/core';
import { CookieService } from './cookie.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private cookieService: CookieService) { }


  getCurrentUserToken(): string {
    return this.cookieService.getCookie('iridium-token');
  }
}
