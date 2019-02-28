import {TestBed, async} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AppComponent} from './app.component';
import {TranslateTestingModule} from "ngx-translate-testing";
import {TranslateService} from "@ngx-translate/core";
import {AuthComponent} from "./auth/auth.component";
import {LoginComponent} from "./auth/login/login.component";
import {PasswordResetComponent} from "./auth/password-reset/password-reset.component";
import {RegisterComponent} from "./auth/register/register.component";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {CookieService} from "ngx-cookie-service";

describe('AppComponent', () => {

  let translateService: TranslateService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        TranslateTestingModule
          .withTranslations('en', require('../../src/assets/i18n/en.json')),
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      declarations: [
        AppComponent,
        AuthComponent,
        LoginComponent,
        PasswordResetComponent,
        RegisterComponent
      ],
      providers: [
        CookieService
      ]
    }).compileComponents();

    translateService = TestBed.get(TranslateService);
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should correctly set default lang and available langs', () => {
    const fixture = TestBed.createComponent(AppComponent);
    expect(translateService.getDefaultLang()).toBe('en');
    expect(translateService.getLangs().length).toBe(2);
    expect(translateService.getLangs()).toContain('en');
    expect(translateService.getLangs()).toContain('pl');
  });
});
