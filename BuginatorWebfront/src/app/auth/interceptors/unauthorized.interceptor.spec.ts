import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HTTP_INTERCEPTORS, HttpClient, HttpErrorResponse} from "@angular/common/http";
import {async, inject, TestBed} from "@angular/core/testing";
import {Router} from "@angular/router";
import {SessionService} from "../../shared/service/session.service";
import {UnauthorizedInterceptorFactory} from "../../app.module";

class MockRouter {
  navigateByUrl(url: string) {
    return url;
  }
}

describe('UnauthorizedInterceptor', () => {

  let httpMock: HttpTestingController;
  let http: HttpClient;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: Router,
          useClass: MockRouter
        },
        {
          provide: HTTP_INTERCEPTORS,
          useFactory: UnauthorizedInterceptorFactory,
          deps:[Router, SessionService],
          multi: true
        }
      ]
    }).compileComponents();

    httpMock = TestBed.get(HttpTestingController);
    http = TestBed.get(HttpClient);
  }));

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  it('should navigate to login on 401', inject([HttpClient, HttpTestingController, Router],
    (http: HttpClient, httpMock: HttpTestingController, router: Router) => {
      const routerSpy = spyOn(router, 'navigateByUrl');

      http.get('/api/test/get/1').subscribe(response => expect(response).toBeTruthy(),
        (httpError: HttpErrorResponse) => {
          expect(httpError).toBeTruthy();
          expect(httpError.message).toBe('Http failure response for /api/test/get/1: 401 Unauthorized');
        });

      const request = httpMock.expectOne(req => true);

      request.flush({}, {status: 401, statusText: 'Unauthorized'});

      expect(routerSpy).toHaveBeenCalledTimes(1);
      const url = routerSpy.calls.first().args[0];
      expect(url).toBe('/');

      httpMock.verify();
    })
  );

  it('should throw Observable when error status != 401', inject([HttpClient, HttpTestingController, Router],
    (http: HttpClient, httpMock: HttpTestingController, router: Router) => {
      const routerSpy = spyOn(router, 'navigateByUrl');

      http.get('/api/test/get/1').subscribe(response => expect(response).toBeTruthy(),
        (httpError: HttpErrorResponse) => {
          expect(httpError).toBeTruthy();
          expect(httpError.message).toBe('Http failure response for /api/test/get/1: 405 Method not allowed');
        });

      const request = httpMock.expectOne(req => true);

      request.flush({}, {status: 405, statusText: 'Method not allowed'});

      expect(routerSpy).not.toHaveBeenCalled();

      httpMock.verify();
    })
  );

  it('should do nothing on positive response', inject([HttpClient, HttpTestingController, Router],
    (http: HttpClient, httpMock: HttpTestingController, router: Router) => {
      const routerSpy = spyOn(router, 'navigateByUrl');

      http.get('/api/test/get/1').subscribe(response => expect(response).toBeTruthy());

      const request = httpMock.expectOne(req => true);

      request.flush({});

      expect(routerSpy).not.toHaveBeenCalled();

      httpMock.verify();
    })
  );
});
