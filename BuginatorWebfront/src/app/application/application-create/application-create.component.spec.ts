import {async, ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';

import {ApplicationCreateComponent} from './application-create.component';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslateTestingModule} from "ngx-translate-testing";
import {ToastrModule} from "ngx-toastr";
import {ApplicationService} from "../application.service";
import {environment} from "../../../environments/environment";
import {By} from "@angular/platform-browser";
import {UserApplication} from "../model/application.model";
import {Router} from "@angular/router";

class MockRouter {
  navigateByUrl(url: string) {
    return url;
  }
}

describe('ApplicationCreateComponent', () => {
  let component: ApplicationCreateComponent;
  let fixture: ComponentFixture<ApplicationCreateComponent>;
  let httpMock: HttpTestingController;
  let applicationService: ApplicationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationCreateComponent],
      imports: [
        ToastrModule.forRoot(),
        HttpClientTestingModule,
        ReactiveFormsModule,
        TranslateTestingModule
          .withTranslations('en', require('../../../assets/i18n/en.json'))
          .withTranslations('pl', require('../../../assets/i18n/pl.json'))
          .withDefaultLanguage('en')
      ],
      providers: [
        {
          provide: Router,
          useClass: MockRouter
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationCreateComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.get(HttpTestingController);
    applicationService = TestBed.get(ApplicationService);

    sessionStorage.removeItem(environment.api.loggedUserStorage);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have disabled button after component loads', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('#createAppBtn')).nativeElement;
    //expect
    expect(button.disabled).toBeTruthy();
  });

  it('should not grant access to creating app', () => {
    let button = fixture.debugElement.query(By.css('#createAppBtn'));
    expect(button).toBeNull();
  });

  it('should show required messages for inputs', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');
    let applicationName = component.appCreateForm.name;

    //when
    applicationName.markAsTouched();

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('#createAppBtn')).nativeElement;
    let appNameRequiredError = fixture.debugElement.queryAll(By.css('span'))[0].nativeElement;
    let appNameErrors = applicationName.errors || {};

    expect(button.disabled).toBeTruthy();
    expect(appNameRequiredError.innerText).toBe('Name is required');
    expect(appNameErrors['required']).toBeTruthy();
  });

  it('should enable button when all data correctly inserted', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');
    let applicationName = component.appCreateForm.name;

    //when
    applicationName.setValue('TestApplication1');

    //then
    fixture.detectChanges();
    let button = fixture.debugElement.query(By.css('#createAppBtn')).nativeElement;
    expect(button.disabled).toBeFalsy();

    let appNameErrors = applicationName.errors || {};
    expect(appNameErrors['required']).toBeFalsy();
  });

  it('should correctly submit new application', fakeAsync(inject([Router],
    (router: Router) => {
      //given
      sessionStorage.setItem(environment.api.loggedUserStorage,
        '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["create_application"]}');
      let applicationName = component.appCreateForm.name;
      let applicationServiceCreateApplicationSpy = spyOn(applicationService, 'createApplication').and.callThrough();
      const routerSpy = spyOn(router, 'navigateByUrl');

      applicationName.setValue('TestApplication');

      //when
      component.onCreateApplicationSubmit();

      //then
      const createAppRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/application`);
      expect(createAppRequest.request.method).toBe('POST');
      let userApplication = new UserApplication();
      userApplication.id = 1;
      userApplication.name = 'TestApplication';
      userApplication.modify = true;
      createAppRequest.flush(userApplication);

      expect(applicationServiceCreateApplicationSpy).toHaveBeenCalled();

      tick();

      expect(routerSpy).toHaveBeenCalledTimes(1);
      const url = routerSpy.calls.first().args[0];
      expect(url).toBe('/application');

      httpMock.verify();
    })));
});
