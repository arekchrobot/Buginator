import {fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ErrorService} from './error.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ApplicationErrorDetails} from "./model/error.model";
import {environment} from "../../environments/environment";

describe('ErrorServiceService', () => {

  let httpMock: HttpTestingController;
  let errorService: ErrorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
    }).compileComponents();

    httpMock = TestBed.get(HttpTestingController);
    errorService = TestBed.get(ErrorService);
  });

  it('should correctly fetch error details', fakeAsync(() => {
    //given
    let errorDetails: ApplicationErrorDetails = createErrorDetails(5, 'Test error');

    //when
    errorService.getErrorDetails(errorDetails.id)
      .then(res => {
        expect(res).toBeDefined();
        expect(res).toEqual(errorDetails);
      });

    //then
    const getErrorDetailsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/error/5`);
    expect(getErrorDetailsRequest.request.method).toBe('GET');
    getErrorDetailsRequest.flush(errorDetails);

    tick();
  }));

  it('should return error when unable to fetch data', fakeAsync(() => {
    //given
    let errorDetails: ApplicationErrorDetails = createErrorDetails(5, 'Test error');

    //when
    errorService.getErrorDetails(errorDetails.id)
      .then(res => {
        expect(res).not.toBeDefined();
      }, error => expect(error).toBeDefined());

    //then
    const getErrorDetailsRequest = httpMock.expectOne(`${environment.api.url}/api/buginator/error/5`);
    expect(getErrorDetailsRequest.request.method).toBe('GET');
    getErrorDetailsRequest.error(new ErrorEvent('fail fetch'));

    tick();
  }));

  function createErrorDetails(id: number, description: string) {
    let errorDetails = new ApplicationErrorDetails();
    errorDetails.id = id;
    errorDetails.description = description;
    return errorDetails;
  }
})
;
