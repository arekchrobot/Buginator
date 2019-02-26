import {HTTP_INTERCEPTORS, HttpClient} from "@angular/common/http";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {async, fakeAsync, TestBed} from "@angular/core/testing";
import {CookieService} from "ngx-cookie-service";
import {OAuthTokenInterceptor} from "./oauth-token.interceptor";
import {environment} from "../../../environments/environment";

describe('OAuthTokenInterceptor', () => {

  let cookieService: CookieService;
  let httpMock: HttpTestingController;
  let http: HttpClient;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CookieService,
        {
          provide: HTTP_INTERCEPTORS,
          useClass: OAuthTokenInterceptor,
          multi: true
        }
      ]
    }).compileComponents();

    cookieService = TestBed.get(CookieService);
    httpMock = TestBed.get(HttpTestingController);
    http = TestBed.get(HttpClient);
  }));

  afterEach(() => {
    cookieService.delete(environment.api.accessTokenCookie);
  });

  it('should not append authorization header when no token is present', fakeAsync(() => {
    //given
    http.get('/api/test/get/1').subscribe(response => expect(response).toBeTruthy());

    //when
    const request = httpMock.expectOne(req => !req.headers.has('Authorization'));

    //then
    request.flush({});
    httpMock.verify();
  }));

  it('should not append authorization header if login request', fakeAsync(() => {
    //given
    cookieService.set(environment.api.accessTokenCookie, "123456");
    http.get('/oauth/token').subscribe(response => expect(response).toBeTruthy());

    //when
    const request = httpMock.expectOne(req => !req.headers.has('Authorization'));

    //then
    request.flush({});
    httpMock.verify();
  }));

  it('should append Authorization header with token from cookie', fakeAsync(() => {
    //given
    let token = "newVal_access_token";
    cookieService.set(environment.api.accessTokenCookie, token);
    http.get('/api/test/get/1').subscribe(response => expect(response).toBeTruthy());


    const request = httpMock.expectOne(req => req.headers.has('Authorization')
      && req.headers.get('Authorization') === ('Bearer ' + token));

    request.flush({});
    httpMock.verify();
  }));
});
