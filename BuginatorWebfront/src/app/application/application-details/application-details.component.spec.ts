import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ApplicationDetailsComponent} from './application-details.component';
import {ToastrModule} from "ngx-toastr";
import {NgxPaginationModule} from "ngx-pagination";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateTestingModule} from "ngx-translate-testing";
import {ApplicationDetails} from "../model/application.model";

describe('ApplicationDetailsComponent', () => {
  let component: ApplicationDetailsComponent;
  let fixture: ComponentFixture<ApplicationDetailsComponent>;

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
      declarations: [ ApplicationDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationDetailsComponent);
    component = fixture.componentInstance;
    component.application = createAppDetails();
    fixture.detectChanges();
  });

  it('should create', async(() => {
    expect(component).toBeTruthy();
  }));

  function createAppDetails(): ApplicationDetails {
    let appDetails = new ApplicationDetails();
    appDetails.id = 1;
    appDetails.name = 'TestApp';
    appDetails.modify = true;
    appDetails.errors = [];
    return appDetails;
  }
});
