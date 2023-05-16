import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';

/** Pass untouched request through to the next request handler. */
@Injectable()
export class NoopInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        // Get the auth token from the service.
        //const authToken = this.auth.getAuthorizationToken();
    
        // Clone the request and replace the original headers with
        // cloned headers, updated with the authorization.
        // const authReq = req.clone({
        //   headers: req.headers.set('Authorization', authToken)
        // });
        console.log(req)
    
        // send cloned request with header to the next handler.
        return next.handle(req);
      }
}