import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {LoginModel} from "./model/login.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private clientId: string = "buginatorWebApp";
  private clientSecret: string = "$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC";
  private clientBtoa: string = "YnVnaW5hdG9yV2ViQXBwOnNlY3JldA==";

  constructor(private httpClient: HttpClient) {
  }

  oauth2Login(loginCredentials: LoginModel): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const headers = new HttpHeaders()
        .append("Authorization", "Basic " + this.clientBtoa)
        .append("Content-Type", "application/x-www-form-urlencoded");

      const body = new HttpParams()
        .set("grant_type", "password")
        .set("username", loginCredentials.username)
        .set("password", loginCredentials.password)
        .set("clientId", this.clientId)
        .set("clientSecret", this.clientSecret);

      return this.httpClient.post(`${environment.api.url}/oauth/token`, body, {headers: headers})
        .toPromise()
        .then(res => resolve(res),
            error => reject(error));
    });
  }
}
