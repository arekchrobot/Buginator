import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {ApplicationErrorDetails, ErrorStatus} from "./model/error.model";

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(private httpClient: HttpClient) { }

  getErrorDetails(id: number): Promise<any> {
    return new Promise<ApplicationErrorDetails>((resolve, reject) => {
      return this.httpClient.get(`${environment.api.url}/api/buginator/error/${id}`)
        .toPromise()
        .then((res: ApplicationErrorDetails) => resolve(res),
          error => reject(error));
    })
  }

  changeStatus(id: number, newStatus: ErrorStatus): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      let params = new HttpParams().set('status', newStatus);
      return this.httpClient.put(`${environment.api.url}/api/buginator/error/${id}`, null, { params: params })
        .toPromise()
        .then((res) => resolve(res),
          error => reject(error));
    })
  }
}
