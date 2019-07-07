import {Component, OnInit} from '@angular/core';
import {Application} from "../model/application.model";
import {ApplicationService} from "../application.service";
import {LoggedUserDTO} from "../../auth/model/logged-user.model";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'buginator-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {

  private applications: Array<Application>;
  private loggedUser: LoggedUserDTO;
  applicationFilter: string = "";

  constructor(private applicationService: ApplicationService) {
  }

  ngOnInit() {
    //TODO: handle failure!!!
    this.applicationService.getUserApplications()
      .then(apps => this.applications = apps);
    //TODO: move to some separate service!
    let loggedUserStorage = sessionStorage.getItem(environment.api.loggedUserStorage);
    if(loggedUserStorage != null) {
      this.loggedUser = JSON.parse(loggedUserStorage);
    }
  }

  get canCreateApps(): boolean {
    return this.loggedUser != null && this.loggedUser.permissions.includes("create_application");
  }

  get userApps() {
    if(this.applicationFilter != "") {
      return this.applications
        .filter((element) => element.name.startsWith(this.applicationFilter));
    }
    return this.applications;
  }
}
