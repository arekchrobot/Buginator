import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, of, throwError} from "rxjs";
import {Router} from "@angular/router";
import {SessionService} from "../../shared/service/session.service";
import {tap} from "rxjs/operators";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private router: Router, private sessionService: SessionService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(tap((event) => {
    }, (err) => {
      this.handleError(err);
    }));
  }

  private handleError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401) {
      this.sessionService.logout();
      this.router.navigateByUrl("/");

      return of(err.message);
    }
    return throwError(err);
  }
}
