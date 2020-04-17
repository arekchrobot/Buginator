import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Application, ApplicationDetails, BaseApplication} from "./model/application.model";
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

  createApplication(newApplication: BaseApplication): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      return this.httpClient.post(`${environment.api.url}/api/buginator/application`, newApplication)
        .toPromise()
        .then(res => resolve(), error => reject(error));
    })
  }

  getApplicationDetails(id: number): Promise<any> {
    return new Promise<ApplicationDetails>((resolve, reject) => {
      return this.httpClient.get(`${environment.api.url}/api/buginator/application/${id}`)
        .toPromise()
        .then((res: ApplicationDetails) => resolve(res),
          error => reject(error));
    })
  }
}
