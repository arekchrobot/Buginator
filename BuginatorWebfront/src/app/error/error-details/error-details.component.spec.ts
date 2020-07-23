import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ErrorDetailsComponent} from './error-details.component';
import {ToastrModule} from "ngx-toastr";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateTestingModule} from "ngx-translate-testing";
import {ApplicationErrorDetails, ErrorSeverity, ErrorStatus} from "../model/error.model";

describe('ErrorDetailsComponent', () => {
  let component: ErrorDetailsComponent;
  let fixture: ComponentFixture<ErrorDetailsComponent>;

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
    })
      .compileComponents();
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

  function createErrorDetails() {
    let errorDetails = new ApplicationErrorDetails();
    errorDetails.id = 1;
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
