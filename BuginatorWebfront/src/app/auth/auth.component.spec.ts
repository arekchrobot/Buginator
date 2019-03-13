import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {AuthComponent} from './auth.component';
import {RouterTestingModule} from "@angular/router/testing";
import {TranslateTestingModule} from "ngx-translate-testing";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {LoginComponent} from "./login/login.component";
import {PasswordResetComponent} from "./password-reset/password-reset.component";
import {RegisterComponent} from "./register/register.component";
import {CookieService} from "ngx-cookie-service";
import {By} from "@angular/platform-browser";
import {AuthService} from "./auth.service";

describe('AuthComponent', () => {
  let component: AuthComponent;
  let fixture: ComponentFixture<AuthComponent>;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        TranslateTestingModule
          .withTranslations('en', require('../../assets/i18n/en.json')),
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      declarations: [
        AuthComponent,
        LoginComponent,
        PasswordResetComponent,
        RegisterComponent
      ],
      providers: [
        CookieService
      ]
    }).compileComponents();

    authService = TestBed.get(AuthService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and set body class', () => {
    expect(component).toBeTruthy();
    expect(document.body.classList.contains('login')).toBeTruthy();

    expect(component.activeTab).toBe('login');

    const elements = fixture.debugElement.queryAll(By.css('.active'));
    expect(elements.length).toBe(1);

    let expectedDiv = elements[0].nativeElement;
    expect(expectedDiv.id).toBe('login');
  });

  it('should remove document body class on destroy', () => {
    //when
    fixture.destroy();

    //then
    expect(document.body.classList.contains('login')).toBeFalsy();
  });

  it('should correctly change activeTab to register', () => {
    //when
    component.changeTab('register');

    //then
    expect(component.activeTab).toBe('register');
    fixture.detectChanges();

    const elements = fixture.debugElement.queryAll(By.css('.active'));
    expect(elements.length).toBe(1);

    let expectedDiv = elements[0].nativeElement;
    expect(expectedDiv.id).toBe('signup');
  });

  it('should correctly change activeTab to forgot', () => {
    //when
    component.changeTab('forgot');

    //then
    expect(component.activeTab).toBe('forgot');
    fixture.detectChanges();

    const elements = fixture.debugElement.queryAll(By.css('.active'));
    expect(elements.length).toBe(1);

    let expectedDiv = elements[0].nativeElement;
    expect(expectedDiv.id).toBe('forgot');
  });

  it('should change tab to login and show register success message', fakeAsync(() => {
    //when
    authService.registerSuccessSubject.next(true);

    //then
    tick();

    fixture.detectChanges();

    expect(component.activeTab).toBe('login');
    const registerSuccessMessage = fixture.debugElement.query(By.css('.alert-success')).nativeElement;
    expect(registerSuccessMessage.innerText).toBe('Thank you for registering. Details has been sent to mail');
  }));
});
