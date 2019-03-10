import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {LoginRequestDTO} from "./model/login-request.model";
import {environment} from "../../environments/environment";
import {CookieService} from "ngx-cookie-service";
import {LoggedUserDTO} from "./model/logged-user.model";
import {OAuthResponseDTO} from "./model/oauth-response.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private clientId: string = "buginatorWebApp";
  private clientSecret: string = "$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC";
  private clientBtoa: string = "YnVnaW5hdG9yV2ViQXBwOnNlY3JldA==";

  constructor(private httpClient: HttpClient, private cookieService: CookieService) {
  }

  oauth2Login(loginCredentials: LoginRequestDTO): Promise<any> {
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
        .then((res: OAuthResponseDTO) => {
            this.saveAuthToken(res);
            resolve();
          },
          error => reject(error));
    });
  }

  private saveAuthToken(result: OAuthResponseDTO) {
    const expiresIn = new Date(Date.now() + (result.expires_in * 1000));
    this.cookieService.set(environment.api.accessTokenCookie, result.access_token, expiresIn);
  }

  getLoggedUser(): Promise<any> {
    return new Promise<any>((resolve) => {
      return this.httpClient.get(`${environment.api.url}/api/auth/session/user/current`)
        .toPromise()
        .then((res: LoggedUserDTO) => {
            this.saveLoggedUserInSession(res);
            resolve(res);
          },
          error => resolve());
    });
  }

  private saveLoggedUserInSession(loggedUser: LoggedUserDTO) {
    sessionStorage.setItem(environment.api.loggedUserStorage, JSON.stringify(loggedUser));
  }

  passwordReset(email: string): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      return this.httpClient.post(`${environment.api.url}/api/auth/password/reset`, email)
        .toPromise()
        .then(res => resolve(res),
          error => reject(error));
    });
  }
}
