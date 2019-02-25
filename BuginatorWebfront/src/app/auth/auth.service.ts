import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {LoginDTO} from "./model/loginDTO";
import {environment} from "../../environments/environment";
import {CookieService} from "ngx-cookie-service";
import {LoggedUserDTO} from "./model/logged-user.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private clientId: string = "buginatorWebApp";
  private clientSecret: string = "$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC";
  private clientBtoa: string = "YnVnaW5hdG9yV2ViQXBwOnNlY3JldA==";

  constructor(private httpClient: HttpClient, private cookieService: CookieService) {
  }

  oauth2Login(loginCredentials: LoginDTO): Promise<any> {
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
        .then(res => {
            this.saveAuthToken(res);
            resolve();
          },
          error => reject(error));
    });
  }

  private saveAuthToken(result: Object) {
    const expiresIn = new Date(Date.now() + (result["expires_in"] * 1000));
    this.cookieService.set("access_token", result["access_token"], expiresIn);
  }

  getLoggedUser(): Promise<any> {
    return new Promise<any>((resolve) => {
      return this.httpClient.get(`${environment.api.url}/api/auth/session/user/current`)
        .toPromise()
        .then((res: LoggedUserDTO) => {
            this.saveLoggedUserInSession(res);
            resolve();
          },
          error => resolve());
    });
  }

  private saveLoggedUserInSession(loggedUser: LoggedUserDTO) {
    sessionStorage.setItem("loggedUser", JSON.stringify(loggedUser));
  }
}
