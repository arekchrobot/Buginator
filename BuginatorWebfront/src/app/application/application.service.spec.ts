import {async, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ApplicationService} from './application.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Application} from "./model/application.model";
import {environment} from "../../environments/environment";

describe('ApplicationService', () => {

  let httpMock: HttpTestingController;
  let applicationService: ApplicationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
    }).compileComponents();

    httpMock = TestBed.get(HttpTestingController);
    applicationService = TestBed.get(ApplicationService);
  }));

  it('should correctly fetch user applications', fakeAsync(() => {
    //given
    let apps = [createApplication("TestApp1", 1), createApplication("App2", 2)];

    //when
    applicationService.getUserApplications()
      .then(res => {
        expect(res.length).toBe(2);
        expect(res).toContain(apps[0]);
        expect(res).toContain(apps[1]);
      });

    //then
    const getApplicationsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/application/by-user`);
    expect(getApplicationsRequest.request.method).toBe('GET');
    getApplicationsRequest.flush(apps);

    tick();
  }));

  it('should return error when unable to fetch data', fakeAsync(() => {
    //when
    applicationService.getUserApplications()
      .then(res => {
        expect(res).not.toBeDefined();
      }, error => expect(error).toBeDefined());

    //then
    const getApplicationsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/application/by-user`);
    expect(getApplicationsRequest.request.method).toBe('GET');
    getApplicationsRequest.error(new ErrorEvent('fail fetch'));

    tick();
  }));

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
