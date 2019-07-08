import {Component, OnInit} from '@angular/core';
import {Application} from "../model/application.model";
import {ApplicationService} from "../application.service";
import {PageableComponent} from "../../shared/component/pageable.component";
import {ToastrService} from "ngx-toastr";
import {ErrorResponseDTO} from "../../shared/model/error-response.model";
import {SessionService} from "../../shared/service/session.service";

@Component({
  selector: 'buginator-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent extends PageableComponent implements OnInit {

  private applications: Array<Application>;
  applicationFilter: string = "";

  constructor(private applicationService: ApplicationService, private toastr: ToastrService,
              private sessionService: SessionService) {
    super();
  }

  ngOnInit() {
    this.applicationService.getUserApplications()
      .then(apps => this.applications = apps,
        (error: ErrorResponseDTO) => this.toastr.error(error.message, 'Error'));
  }

  get canCreateApps(): boolean {
    return this.sessionService.hasPermission("create_application");
  }

  get userApps() {
    if (this.applicationFilter != "") {
      return this.applications
        .filter((element) => element.name.startsWith(this.applicationFilter));
    }
    return this.applications;
  }

  setFilter(event) {
    this.applicationFilter = event.target.value;
  }
}
