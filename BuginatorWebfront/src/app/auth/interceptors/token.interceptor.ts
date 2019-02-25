import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable, Injector} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {Observable} from "rxjs";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private cookieService: CookieService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.cookieService.get("access_token");

    if (accessToken && req.url.indexOf('/oauth/token') == -1) {
      let authReq = req.clone({
        setHeaders: {
          'Authorization': 'Bearer ' + accessToken
        }
      });

      return next.handle(authReq);
    }

    return next.handle(req);
  }
}
