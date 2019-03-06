import {TestBed, async, ComponentFixture} from '@angular/core/testing';
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
import {DashboardComponent} from "./dashboard/dashboard.component";
import {environment} from "../environments/environment";
import {By} from "@angular/platform-browser";

describe('AppComponent', () => {

  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
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
        RegisterComponent,
        DashboardComponent
      ],
      providers: [
        CookieService
      ]
    }).compileComponents();

    translateService = TestBed.get(TranslateService);
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  afterEach(() => {
    sessionStorage.removeItem(environment.api.loggedUserStorage);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should correctly set default lang and available langs', () => {
    expect(translateService.getDefaultLang()).toBe('en');
    expect(translateService.getLangs().length).toBe(2);
    expect(translateService.getLangs()).toContain('en');
    expect(translateService.getLangs()).toContain('pl');
  });

  it('should show login screen when not authenticated', () => {
    //given
    sessionStorage.removeItem(environment.api.loggedUserStorage);
    //when
    fixture.detectChanges();
    //then
    expect(component.isAuthenticated).toBeFalsy();
    const buginatorAuthElement = fixture.debugElement.query(By.css('buginator-auth'));
    const buginatorDashboardElement = fixture.debugElement.query(By.css('buginator-dashboard'));
    const footer = fixture.debugElement.query(By.css('.Footer'));
    expect(buginatorAuthElement).toBeDefined();
    expect(buginatorDashboardElement).toBeNull();
    expect(footer).toBeNull();
  });

  it('should show dashboard and footer screen when authenticated', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage, "test");
    //when
    fixture.detectChanges();
    //then
    expect(component.isAuthenticated).toBeTruthy();
    const buginatorAuthElement = fixture.debugElement.query(By.css('buginator-auth'));
    const buginatorDashboardElement = fixture.debugElement.query(By.css('buginator-dashboard'));
    const footer = fixture.debugElement.query(By.css('.Footer'));
    expect(buginatorAuthElement).toBeNull();
    expect(buginatorDashboardElement).toBeDefined();
    expect(footer).toBeDefined();
  });
});
