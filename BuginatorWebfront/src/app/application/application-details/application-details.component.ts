import {Component, OnInit} from '@angular/core';
import {ApplicationDetails} from "../model/application.model";
import {SessionService} from "../../shared/service/session.service";
import {PageableComponent} from "../../shared/component/pageable.component";
import {ActivatedRoute} from "@angular/router";
import {ApplicationService} from "../application.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-application-details',
  templateUrl: './application-details.component.html',
  styleUrls: ['./application-details.component.css']
})
export class ApplicationDetailsComponent extends PageableComponent implements OnInit {

  application: ApplicationDetails;

  constructor(private route: ActivatedRoute, private sessionService: SessionService,
              private applicationService: ApplicationService, private toastr: ToastrService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      let applicationId = parseInt(paramMap.get('id'));
      this.applicationService.getApplicationDetails(applicationId)
        .then(app => this.application = app,
          error => this.toastr.error(error.message, 'Error'));
    });
  }

  get canManageAggregators(): boolean {
    return this.sessionService.hasPermission("app_show_notification");
  }

  get canManageUsers(): boolean {
    return this.sessionService.hasPermission("app_manage_users");
  }
}
