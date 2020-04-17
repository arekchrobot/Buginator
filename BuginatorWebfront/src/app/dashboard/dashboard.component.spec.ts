import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardComponent} from './dashboard.component';
import {RouterTestingModule} from "@angular/router/testing";
import {CookieService} from "ngx-cookie-service";
import {environment} from "../../environments/environment";
import {SessionService} from "../shared/service/session.service";

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let cookieService: CookieService;
  let sessionService: SessionService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [
        DashboardComponent
      ],
      providers: [
        CookieService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    cookieService = TestBed.get(CookieService);
    sessionService = TestBed.get(SessionService);

    fixture.detectChanges();
  });

  afterEach(() => {
    cookieService.delete(environment.api.accessTokenCookie);
    sessionStorage.removeItem(environment.api.loggedUserStorage);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should correctly logout', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');
    cookieService.set(environment.api.accessTokenCookie, '123456');

    //when
    component.logout();

    //then
    expect(sessionStorage.getItem(environment.api.loggedUserStorage)).toBeNull();
    expect(cookieService.get(environment.api.accessTokenCookie)).toBe('');
    expect(sessionService.isAuthenticated()).toBeFalsy();
  });
});
