import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse
} from '@angular/common/http';

import { map, Observable } from 'rxjs';

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
      return next.handle(req).pipe(map((event: HttpEvent<any>) => {

        if (event instanceof HttpResponse) {
          event = event.clone({body: this.modifyBody(event.body)});
        }
        return event;
      }));
    }
  private modifyBody(body: any) {
      return body;
    /*
    * write your logic to modify the body
    * */
  }

}
