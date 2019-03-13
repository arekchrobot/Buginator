import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {RegisterComponent} from './register.component';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslateTestingModule} from "ngx-translate-testing";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../auth.service";
import {environment} from "../../../environments/environment";
import {skip} from 'rxjs/operators';
import {By} from "@angular/platform-browser";
import {ErrorResponseDTO} from "../../shared/model/error-response.model";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
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

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    authService = TestBed.get(AuthService);
    httpMock = TestBed.get(HttpTestingController);

    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.isRegisterError()).toBeFalsy();
    expect(component.regErrorMessage).toBeUndefined();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeTruthy();
  });

  it('should emit registerSuccessSubject event on success register', fakeAsync(() => {
    //given
    let authServiceRegisterSpy = spyOn(authService, 'register').and.callThrough();
    //skip initial state on subscribe
    authService.registerSuccessSubject
      .pipe(skip(1))
      .subscribe((res: boolean) => expect(res).toBeTruthy());

    //when
    component.onRegisterFormSubmit();

    //then
    expect(authServiceRegisterSpy).toHaveBeenCalled();

    const registerRequest = httpMock.expectOne(`${environment.api.url}/api/auth/register`);
    expect(registerRequest.request.method).toBe('POST');
    registerRequest.flush({}, {status: 201, statusText: ''});

    tick();

    httpMock.verify();
  }));

  it('should show required messages for inputs', () => {
    //given
    let userEmail = component.regForm.userEmail;
    let userPassword = component.regForm.userPassword;
    let companyName = component.regForm.companyName;

    //when
    userEmail.markAsTouched();
    userPassword.markAsTouched();
    companyName.markAsTouched();

    //then
    fixture.detectChanges();

    let htmlErrors = fixture.debugElement.queryAll(By.css('span'));
    expect(htmlErrors.length).toBe(3);
    let companyNameSpan = htmlErrors[0].nativeElement;
    let userEmailSpan = htmlErrors[1].nativeElement;
    let userPasswordSpan = htmlErrors[2].nativeElement;
    expect(companyNameSpan.innerText).toBe('Company name is required');
    expect(userEmailSpan.innerText).toBe('Email is required');
    expect(userPasswordSpan.innerText).toBe('Password is required');


    let userEmailErrors = userEmail.errors || {};
    let userPasswordErrors = userPassword.errors || {};
    let companyNameErrors = companyName.errors || {};

    expect(userEmailErrors['required']).toBeTruthy();
    expect(userEmailErrors['email']).toBeFalsy();
    expect(userPasswordErrors['required']).toBeTruthy();
    expect(companyNameErrors['required']).toBeTruthy();

    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeTruthy();
  });

  it('should show wrong email message when wrong email typed', () => {
    //given
    let userEmail = component.regForm.userEmail;

    //when
    userEmail.setValue('wrongMail');
    userEmail.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    let userEmailEmailError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let userEmailErrors = userEmail.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(userEmailEmailError.innerText).toBe('Not a valid email');
    expect(userEmailErrors['required']).toBeFalsy();
    expect(userEmailErrors['email']).toBeTruthy();
  });

  it('should show passwords not match error', () => {
    //given
    let userPassword = component.regForm.userPassword;
    let userPasswordConfirm = component.regForm.userPasswordConfirm;

    //when
    userPassword.setValue('hardPassHardPass');
    userPasswordConfirm.setValue('wrongPass');
    userPassword.markAsTouched();
    userPasswordConfirm.markAsTouched();

    //then
    fixture.detectChanges();

    let userPasswordConfirmError = fixture.debugElement.query(By.css('span')).nativeElement;
    expect(userPasswordConfirmError.innerText).toBe("Passwords don\'t match");

    let userPasswordErrors = userPassword.errors || {};
    let userPasswordConfirmErrors = userPasswordConfirm.errors || {};

    expect(userPasswordErrors['required']).toBeFalsy();
    expect(userPasswordConfirmErrors['required']).toBeFalsy();
    expect(userPasswordConfirmErrors['passwordNotMatch']).toBeTruthy();
  });

  it('should correctly validate form', () => {
    //given
    let companyName = component.regForm.companyName;
    let userEmail = component.regForm.userEmail;
    let userPassword = component.regForm.userPassword;
    let userPasswordConfirm = component.regForm.userPasswordConfirm;

    //when
    companyName.setValue("testCompany");
    userEmail.setValue('test@gmail.com');
    userPassword.setValue("testPass");
    userPasswordConfirm.setValue("testPass");

    //then
    fixture.detectChanges();

    let button = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(button.disabled).toBeFalsy();

    let companyNameErrors = companyName.errors || {};
    let userEmailErrors = userEmail.errors || {};
    let userPasswordErrors = userPassword.errors || {};
    let userPasswordConfirmErrors = userPasswordConfirm.errors || {};

    expect(companyNameErrors['required']).toBeFalsy();
    expect(userEmailErrors['required']).toBeFalsy();
    expect(userEmailErrors['email']).toBeFalsy();
    expect(userPasswordErrors['required']).toBeFalsy();
    expect(userPasswordConfirmErrors['required']).toBeFalsy();
    expect(userPasswordConfirmErrors['passwordNotMatch']).toBeFalsy();
  });

  it('should show error and correctly translate error messages on failed register', fakeAsync(() => {
    //given
    let authServiceRegisterSpy = spyOn(authService, 'register').and.callThrough();
    let errorResponseDTO: ErrorResponseDTO = new ErrorResponseDTO();
    errorResponseDTO.message = "company.duplicate,user.duplicate";

    //when
    component.onRegisterFormSubmit();

    //then
    expect(authServiceRegisterSpy).toHaveBeenCalled();

    const registerRequest = httpMock.expectOne(`${environment.api.url}/api/auth/register`);
    expect(registerRequest.request.method).toBe('POST');
    registerRequest.flush(errorResponseDTO, {status: 400, statusText: ''});

    tick();

    fixture.detectChanges();

    expect(component.isRegisterError()).toBeTruthy();
    expect(component.regErrorMessage).toBe('Given company already exists,Given email already exists');

    let registerError = fixture.debugElement.query(By.css('.alert-danger')).nativeElement;
    expect(registerError.innerText).toBe('Given company already exists,Given email already exists');

    httpMock.verify();
  }));
});
