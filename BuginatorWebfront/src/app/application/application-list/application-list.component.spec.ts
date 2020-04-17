import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { ApplicationListComponent } from './application-list.component';
import {ToastrModule} from "ngx-toastr";
import {NgxPaginationModule} from "ngx-pagination";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TranslateTestingModule} from "ngx-translate-testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ApplicationService} from "../application.service";
import {environment} from "../../../environments/environment";
import {Application} from "../model/application.model";
import {RouterTestingModule} from "@angular/router/testing";

describe('ApplicationListComponent', () => {
  let component: ApplicationListComponent;
  let fixture: ComponentFixture<ApplicationListComponent>;
  let httpMock: HttpTestingController;
  let applicationService: ApplicationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ToastrModule.forRoot(),
        NgxPaginationModule,
        BrowserAnimationsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        TranslateTestingModule
          .withTranslations('en', require('../../../assets/i18n/en.json'))
          .withTranslations('pl', require('../../../assets/i18n/pl.json'))
          .withDefaultLanguage('en')
      ],
      declarations: [ ApplicationListComponent ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.get(HttpTestingController);
    applicationService = TestBed.get(ApplicationService);

    sessionStorage.removeItem(environment.api.loggedUserStorage);
  });

  it('should create ApplicationListComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should correctly fetch applications from server', fakeAsync(() => {
    //given
    let applicationServiceGetUserAppsSpy = spyOn(applicationService, 'getUserApplications').and.callThrough();
    let apps = [createApplication("TestApp1", 1), createApplication("App2", 2)];

    //when
    fixture.detectChanges();

    //then
    const getApplicationsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/application/by-user`);
    expect(getApplicationsRequest.request.method).toBe('GET');
    getApplicationsRequest.flush(apps);

    expect(applicationServiceGetUserAppsSpy).toHaveBeenCalled();

    tick();

    expect(component.userApps.length).toBe(2);
  }));

  it('should correctly filter applications', fakeAsync(() => {
    //given
    let apps = [createApplication("TestApp1", 1), createApplication("App2", 2)];

    fixture.detectChanges();

    const getApplicationsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/application/by-user`);
    getApplicationsRequest.flush(apps);
    tick();
    let filter1 = {
      target: {
        value: "Test"
      }
    };

    let filter2 = {
      target: {
        value: "App"
      }
    };

    //when
    component.setFilter(filter1);
    let userAppsAfterFilter1 = component.userApps;

    component.setFilter(filter2);
    let userAppsAfterFilter2 = component.userApps;

    //then
    expect(userAppsAfterFilter1.length).toBe(1);
    expect(userAppsAfterFilter1[0].name).toBe("TestApp1");

    expect(userAppsAfterFilter2.length).toBe(1);
    expect(userAppsAfterFilter2[0].name).toBe("App2");
  }));

  it('should give access to creating apps', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');

    //then
    expect(component.canCreateApps).toBeTruthy();
  });

  it('should not grant access to creating apps', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":[]}');

    expect(component.canCreateApps).toBeFalsy();
  });

  function createApplication(name: string, id: number) {
    let app = new Application();
    app.name = name;
    app.id = id;
    app.modify = true;
    app.allErrorCount = 2;
    app.lastWeekErrorCount = 3;
    return app;
  }
});
