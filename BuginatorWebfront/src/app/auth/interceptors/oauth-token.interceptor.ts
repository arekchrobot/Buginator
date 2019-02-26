import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable, Injector} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class OAuthTokenInterceptor implements HttpInterceptor {

  constructor(private cookieService: CookieService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.cookieService.get(environment.api.accessTokenCookie);

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
