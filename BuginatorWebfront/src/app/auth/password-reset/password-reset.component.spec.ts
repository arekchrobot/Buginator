import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {PasswordResetComponent} from './password-reset.component';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {AuthService} from "../auth.service";
import {RouterTestingModule} from "@angular/router/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslateTestingModule} from "ngx-translate-testing";
import {CookieService} from "ngx-cookie-service";
import {By} from "@angular/platform-browser";
import {environment} from "../../../environments/environment";

describe('PasswordResetComponent', () => {
  let component: PasswordResetComponent;
  let fixture: ComponentFixture<PasswordResetComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordResetComponent],
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

    fixture = TestBed.createComponent(PasswordResetComponent);
    component = fixture.componentInstance;

    authService = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);

    fixture.detectChanges();
  }));

  it('should create PasswordResetComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should have disabled button after component loads', () => {
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeTruthy();
  });

  it('should enable button when email correctly inserted', () => {
    //given
    let email = component.passResetForm.email;

    //when
    email.setValue('test@gmail.com');

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeFalsy();

    let emailErrors = email.errors || {};
    expect(emailErrors['required']).toBeFalsy();
    expect(emailErrors['email']).toBeFalsy();
  });

  it('should show required messages for email input', () => {
    //given
    let email = component.passResetForm.email;

    //when
    email.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    let emailRequiredError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let emailErrors = email.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(emailRequiredError.innerText).toBe('Email is required');
    expect(emailErrors['required']).toBeTruthy();
    expect(emailErrors['email']).toBeFalsy();
  });

  it('should show wrong email message when wrong username passed', () => {
    //given
    let email = component.passResetForm.email;

    //when
    email.setValue('wrongMail');
    email.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    let emailEmailError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let emailErrors = email.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(emailEmailError.innerText).toBe('Not a valid email');
    expect(emailErrors['required']).toBeFalsy();
    expect(emailErrors['email']).toBeTruthy();
  });

  it('should correctly send password reset and show success message', fakeAsync(() => {
    //given
    let authServicePasswordResetSpy = spyOn(authService, 'passwordReset').and.callThrough();
    let email = component.passResetForm.email;
    email.setValue('test@gmail.com');

    //when
    component.onPasswordResetSubmit();

    //then
    const passwordResetRequest = httpMock.expectOne(`${environment.api.url}/api/auth/password/reset`);
    expect(passwordResetRequest.request.method).toBe('POST');
    passwordResetRequest.flush({}, {status: 201, statusText: ''});

    expect(authServicePasswordResetSpy).toHaveBeenCalled();

    tick();

    fixture.detectChanges();

    let passwordResetSuccess = fixture.debugElement.query(By.css('.alert-success')).nativeElement;
    expect(passwordResetSuccess.innerText).toBe('An email with password reset token has been sent');

    expect(component.isPasswordResetSuccess()).toBeTruthy();
    expect(component.isPasswordResetError()).toBeFalsy();

    httpMock.verify();
  }));

  it('should not send password reset and show error message', fakeAsync(() => {
    //given
    let authServicePasswordResetSpy = spyOn(authService, 'passwordReset').and.callThrough();
    let email = component.passResetForm.email;
    email.setValue('test@gmail.com');

    //when
    component.onPasswordResetSubmit();

    //then
    const passwordResetRequest = httpMock.expectOne(`${environment.api.url}/api/auth/password/reset`);
    expect(passwordResetRequest.request.method).toBe('POST');
    passwordResetRequest.error(new ErrorEvent('JMS not responding'));

    expect(authServicePasswordResetSpy).toHaveBeenCalled();

    tick();

    fixture.detectChanges();

    let passwordResetSuccess = fixture.debugElement.query(By.css('.alert-danger')).nativeElement;
    expect(passwordResetSuccess.innerText).toBe('There has been error while sending reset token');

    expect(component.isPasswordResetSuccess()).toBeFalsy();
    expect(component.isPasswordResetError()).toBeTruthy();

    httpMock.verify();
  }));
});
