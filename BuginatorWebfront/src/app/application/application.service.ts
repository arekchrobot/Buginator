import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Application} from "./model/application.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private httpClient: HttpClient) {
  }

  getUserApplications(): Promise<Array<Application>> {
    return new Promise<Array<Application>>((resolve, reject) => {
      return this.httpClient.get(`${environment.api.url}/api/buginator/application/by-user`)
        .toPromise()
        .then((res: Array<Application>) => resolve(res),
            error => reject(error));
    })
  }
}
