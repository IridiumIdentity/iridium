import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse
} from '@angular/common/http';

import { map, Observable } from 'rxjs';

@Injectable()
export class NoopInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler) {

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
