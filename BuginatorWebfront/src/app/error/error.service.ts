import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {ApplicationErrorDetails} from "./model/error.model";

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
}
