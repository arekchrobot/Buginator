import {Component, OnInit} from '@angular/core';
import {ApplicationErrorDetails, ErrorSeverity, ErrorStatus} from "../model/error.model";
import {ActivatedRoute} from "@angular/router";
import {ErrorService} from "../error.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-error-details',
  templateUrl: './error-details.component.html',
  styleUrls: ['./error-details.component.css']
})
export class ErrorDetailsComponent implements OnInit {

  error: ApplicationErrorDetails;
  status = ErrorStatus;
  severity = ErrorSeverity;
  appId: number;
  private errorId: number;

  constructor(private route: ActivatedRoute, private errorService: ErrorService,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.appId = parseInt(paramMap.get('appId'));
      this.errorId = parseInt(paramMap.get('id'));
      this.fetchErrorDetails(this.errorId);
    });
  }

  private fetchErrorDetails(id: number) {
    this.errorService.getErrorDetails(id)
      .then(errorDetails => this.error = errorDetails,
        error => this.toastr.error(error.message, 'Error'));
  }

  changeStatus(newStatus: ErrorStatus) {
    //TODO: implement PUT change
  }
}
