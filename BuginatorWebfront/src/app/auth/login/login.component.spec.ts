import {async, ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslateTestingModule} from "ngx-translate-testing";
import {CookieService} from "ngx-cookie-service";
import {By} from "@angular/platform-browser";
import {AuthService} from "../auth.service";
import {OAuthResponseDTO} from "../model/oauth-response.model";
import {LoggedUserDTO} from "../model/logged-user.model";
import {environment} from "../../../environments/environment";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
        TranslateTestingModule
          .withTranslations('en', require('../../../assets/i18n/en.json'))
          .withTranslations('pl', require('../../../assets/i18n/pl.json'))
          .withDefaultLanguage('en')
      ],
      providers: [
        CookieService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    authService = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create LoginComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should have disabled button after component loads', () => {
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeTruthy();
  });

  it('should enable button when all data correctly inserted', () => {
    //given
    let username = component.logForm.username;
    let password = component.logForm.password;

    //when
    username.setValue('test@gmail.com');
    password.setValue('123');

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeFalsy();

    let usernameErrors = username.errors || {};
    let passwordErrors = password.errors || {};
    expect(usernameErrors['required']).toBeFalsy();
    expect(usernameErrors['email']).toBeFalsy();
    expect(passwordErrors['required']).toBeFalsy();
  });

  it('should show required messages for inputs', () => {
    //given
    let username = component.logForm.username;
    let password = component.logForm.password;

    //when
    username.markAsTouched();
    password.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    let usernameRequiredError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let passwordRequiredError = fixture.debugElement.queryAll(By.css('span'))[1].nativeElement;
    let usernameErrors = username.errors || {};
    let passwordErrors = password.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(usernameRequiredError.innerText).toBe('Username is required');
    expect(passwordRequiredError.innerText).toBe('Password is required');
    expect(usernameErrors['required']).toBeTruthy();
    expect(usernameErrors['email']).toBeFalsy();
    expect(passwordErrors['required']).toBeTruthy();
  });

  it('should show wrong email message when wrong username passed', () => {
    //given
    let username = component.logForm.username;

    //when
    username.setValue('wrongMail');
    username.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    let usernameEmailError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let usernameErrors = username.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(usernameEmailError.innerText).toBe('Username must be a valid email');
    expect(usernameErrors['required']).toBeFalsy();
    expect(usernameErrors['email']).toBeTruthy();
  });

  it('should correctly authenticate and get logged user data', fakeAsync(() => {
    //given
    let authServiceOAuth2LoginSpy = spyOn(authService, 'oauth2Login').and.callThrough();
    let authServiceGetLoggedUserSpy = spyOn(authService, 'getLoggedUser').and.callThrough();
    let username = component.logForm.username;
    let password = component.logForm.password;

    username.setValue('test@gmail.com');
    password.setValue('123');

    //when
    component.onLoginSubmit();

    //then
    const oauth2Request = httpMock.expectOne(`${environment.api.url}/oauth/token`);
    expect(oauth2Request.request.method).toBe('POST');
    let oAuthResponseMock = new OAuthResponseDTO();
    oAuthResponseMock.access_token = "test_mock_token";
    oAuthResponseMock.expires_in = 3600;
    oauth2Request.flush(oAuthResponseMock);

    expect(authServiceOAuth2LoginSpy).toHaveBeenCalled();

    tick();

    const getLoggedUserRequest = httpMock.expectOne(`${environment.api.url}/api/auth/session/user/current`);
    expect(getLoggedUserRequest.request.method).toBe('GET');
    let loggedUserDto = new LoggedUserDTO();
    loggedUserDto.email = 'test@gmail.com';
    loggedUserDto.name = 'test';
    loggedUserDto.permissions = ['USER'];

    getLoggedUserRequest.flush(loggedUserDto);

    expect(authServiceGetLoggedUserSpy).toHaveBeenCalled();

    httpMock.verify();
  }));

  it('should show error when authentication fails', fakeAsync(() => {
    //given
    let authServiceOAuth2LoginSpy = spyOn(authService, 'oauth2Login').and.callThrough();
    let authServiceGetLoggedUserSpy = spyOn(authService, 'getLoggedUser').and.callThrough();
    let username = component.logForm.username;
    let password = component.logForm.password;

    username.setValue('test@wrong.com');
    password.setValue('123');

    //when
    component.onLoginSubmit();

    //then
    const oauth2Request = httpMock.expectOne(`${environment.api.url}/oauth/token`);
    expect(oauth2Request.request.method).toBe('POST');
    let oAuthResponseMock = new OAuthResponseDTO();
    oAuthResponseMock.access_token = "test_mock_token";
    oAuthResponseMock.expires_in = 3600;
    oauth2Request.error(new ErrorEvent('fails'));

    tick();

    expect(component.isLoginError()).toBeTruthy();

    expect(authServiceOAuth2LoginSpy).toHaveBeenCalled();
    expect(authServiceGetLoggedUserSpy).not.toHaveBeenCalled();

    fixture.detectChanges();
    let loginError = fixture.debugElement.query(By.css('.alert')).nativeElement;

    expect(loginError).toBeTruthy();
    expect(loginError.innerText).toBe('Username or password is invalid');

    httpMock.verify();
  }));
});
