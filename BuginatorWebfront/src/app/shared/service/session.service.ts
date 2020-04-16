import {Injectable} from "@angular/core";
import {LoggedUserDTO} from "../../auth/model/logged-user.model";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private loggedUser: LoggedUserDTO;

  private getLoggedUser(): LoggedUserDTO {
    if (this.loggedUser == null) {
      let loggedUserStorage = sessionStorage.getItem(environment.api.loggedUserStorage);
      if (loggedUserStorage != null) {
        this.loggedUser = JSON.parse(loggedUserStorage);
      }
    }
    return this.loggedUser;
  }

  isAuthenticated(): boolean {
    return this.getLoggedUser() != null;
  }

  logout() {
    this.loggedUser = null;
    sessionStorage.removeItem(environment.api.loggedUserStorage);
  }

  hasPermission(permission: string): boolean {
    return this.getLoggedUser() != null && this.getLoggedUser().permissions.includes(permission);
  }

}
