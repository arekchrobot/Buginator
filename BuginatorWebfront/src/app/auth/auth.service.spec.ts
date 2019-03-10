import {async, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {CookieService} from "ngx-cookie-service";
import {LoginRequestDTO} from "./model/login-request.model";
import {environment} from "../../environments/environment";
import {OAuthResponseDTO} from "./model/oauth-response.model";
import {LoggedUserDTO} from "./model/logged-user.model";

describe('AuthService', () => {

  let authService: AuthService;
  let httpMock: HttpTestingController;
  let cookieService: CookieService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CookieService
      ]
    }).compileComponents();

    authService = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);
    cookieService = TestBed.get(CookieService);
  }));

  afterEach(() => {
    cookieService.delete(environment.api.accessTokenCookie);
    sessionStorage.removeItem(environment.api.loggedUserStorage);
  });

  it('should correctly authenticate and save access token in cookie', fakeAsync(() => {
    //given
    let loginRequest: LoginRequestDTO = new LoginRequestDTO();
    loginRequest.username = "testUser@gmail.com";
    loginRequest.password = "testPass";
    let oauthResponse: OAuthResponseDTO = new OAuthResponseDTO();
    oauthResponse.access_token = "test-token-123";
    oauthResponse.expires_in = 3600;

    //when
    authService.oauth2Login(loginRequest);

    //then
    const oauth2Request = httpMock.expectOne(`${environment.api.url}/oauth/token`);
    expect(oauth2Request.request.method).toBe('POST');
    expect(oauth2Request.request.headers.get("Authorization")).toContain('Basic ');
    expect(oauth2Request.request.headers.get("Content-Type")).toBe('application/x-www-form-urlencoded');
    expect(oauth2Request.request.body.toString()).toBe(`grant_type=password&username=${loginRequest.username}&password=${loginRequest.password}&clientId=buginatorWebApp&clientSecret=$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC`);
    oauth2Request.flush(oauthResponse);

    tick();

    let accessToken = cookieService.get(environment.api.accessTokenCookie);
    expect(accessToken).toBe(oauthResponse.access_token);

    httpMock.verify();
  }));

  it('should not save access token on error', fakeAsync(() => {
    //given
    let loginRequest: LoginRequestDTO = new LoginRequestDTO();
    loginRequest.username = "wrong@gmail.com";
    loginRequest.password = "wrong";

    //when
    authService.oauth2Login(loginRequest)
      .then(success => fail('should not go into success'), error => expect(error).toBeDefined());

    //then
    const oauth2Request = httpMock.expectOne(`${environment.api.url}/oauth/token`);
    expect(oauth2Request.request.method).toBe('POST');
    expect(oauth2Request.request.headers.get("Authorization")).toContain('Basic ');
    expect(oauth2Request.request.headers.get("Content-Type")).toBe('application/x-www-form-urlencoded');
    expect(oauth2Request.request.body.toString()).toBe(`grant_type=password&username=${loginRequest.username}&password=${loginRequest.password}&clientId=buginatorWebApp&clientSecret=$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC`);
    oauth2Request.error(new ErrorEvent('fail'));

    tick();

    let accessToken = cookieService.get(environment.api.accessTokenCookie);

    expect(accessToken).toBeFalsy();
    expect(accessToken).toBe('');

    httpMock.verify();
  }));

  it('should fetch user and store in sessionStorage', fakeAsync(() => {
    //given
    let loggedUser: LoggedUserDTO = new LoggedUserDTO();
    loggedUser.name = "Tester";
    loggedUser.email = "tester@gmail.com";
    loggedUser.permissions = ['USER'];

    //when
    authService.getLoggedUser()
      .then(res => expect(res instanceof LoggedUserDTO).toBeTruthy());

    //then
    const getLoggedUserRequest = httpMock.expectOne(`${environment.api.url}/api/auth/session/user/current`);
    expect(getLoggedUserRequest.request.method).toBe('GET');
    getLoggedUserRequest.flush(loggedUser);

    tick();

    const stringifiedUser = sessionStorage.getItem(environment.api.loggedUserStorage);
    const sessionUser: LoggedUserDTO = JSON.parse(stringifiedUser);
    expect(sessionUser.name).toBe(loggedUser.name);
    expect(sessionUser.email).toBe(loggedUser.email);
    expect(sessionUser.permissions.length).toBe(loggedUser.permissions.length);
    expect(sessionUser.permissions.length).toBe(1);
    expect(sessionUser.permissions[0]).toBe(loggedUser.permissions[0]);

    httpMock.verify();
  }));

  it('should resolve with undefined when error happen in fetching logged user', fakeAsync(() => {
    //when
    authService.getLoggedUser()
      .then(res => expect(res).not.toBeDefined());

    //then
    const getLoggedUserRequest = httpMock.expectOne(`${environment.api.url}/api/auth/session/user/current`);
    expect(getLoggedUserRequest.request.method).toBe('GET');
    getLoggedUserRequest.error(new ErrorEvent('fail fetch'));

    tick();

    const stringifiedUser = sessionStorage.getItem(environment.api.loggedUserStorage);
    expect(stringifiedUser).toBeNull();

    httpMock.verify();
  }));

  it('should send reset password token', fakeAsync(() => {
    //given
    let email: string = "emailToReset@gmail.com";

    //when
    authService.passwordReset(email).then(res => expect(res).toBeDefined(),
        error => fail('should not throw error'));

    //then
    const passwordResetRequest = httpMock.expectOne(`${environment.api.url}/api/auth/password/reset`);
    expect(passwordResetRequest.request.method).toBe('POST');
    passwordResetRequest.flush({}, {status: 201, statusText: ''});

    tick();

    httpMock.verify();
  }));

  it('should not send reset password token', fakeAsync(() => {
    //given
    let email: string = "emailToReset@gmail.com";

    //when
    authService.passwordReset(email).then(res => fail('should throw error'),
      error => expect(error).toBeDefined());

    //then
    const passwordResetRequest = httpMock.expectOne(`${environment.api.url}/api/auth/password/reset`);
    expect(passwordResetRequest.request.method).toBe('POST');
    passwordResetRequest.error(new ErrorEvent('JMS not responding'));

    tick();

    httpMock.verify();
  }));
});
