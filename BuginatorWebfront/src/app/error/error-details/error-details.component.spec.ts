import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ErrorDetailsComponent} from './error-details.component';
import {ToastrModule} from "ngx-toastr";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {TranslateTestingModule} from "ngx-translate-testing";
import {ApplicationErrorDetails, ErrorSeverity, ErrorStatus} from "../model/error.model";
import {environment} from "../../../environments/environment";

describe('ErrorDetailsComponent', () => {
  let component: ErrorDetailsComponent;
  let fixture: ComponentFixture<ErrorDetailsComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ToastrModule.forRoot(),
        RouterTestingModule,
        HttpClientTestingModule,
        TranslateTestingModule
          .withTranslations('en', require('../../../assets/i18n/en.json'))
          .withTranslations('pl', require('../../../assets/i18n/pl.json'))
          .withDefaultLanguage('en')
      ],
      declarations: [ErrorDetailsComponent]
    }).compileComponents();

    httpMock = TestBed.get(HttpTestingController);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorDetailsComponent);
    component = fixture.componentInstance;
    component.error = createErrorDetails();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update status', fakeAsync(() => {
    //given
    expect(component.error.status == ErrorStatus.CREATED);
    component.errorId=1;
    let newStatus = ErrorStatus.RESOLVED;

    //when
    component.changeStatus(newStatus);

    //then
    const changeErrorStatusRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/error/${component.errorId}?status=${newStatus}`);
    changeErrorStatusRequest.flush({});

    tick();

    expect(component.error.status).toEqual(ErrorStatus.RESOLVED);
  }));

  it('should not update status', fakeAsync(() => {
    //given
    expect(component.error.status == ErrorStatus.CREATED);
    component.errorId=1;
    let newStatus = ErrorStatus.RESOLVED;

    //when
    component.changeStatus(newStatus);

    //then
    const changeErrorStatusRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/error/${component.errorId}?status=${newStatus}`);
    changeErrorStatusRequest.error(new ErrorEvent('fail update'));

    tick(10000);

    expect(component.error.status).toEqual(ErrorStatus.CREATED);
  }));

  function createErrorDetails() {
    let errorDetails = new ApplicationErrorDetails();
    errorDetails.title = 'Test title';
    errorDetails.description = 'Test description';
    errorDetails.status = ErrorStatus.CREATED;
    errorDetails.severity = ErrorSeverity.ERROR;
    errorDetails.dateTime = new Date();
    errorDetails.lastOccurrence = new Date();
    errorDetails.count = 1;

    return errorDetails;
  }
});
