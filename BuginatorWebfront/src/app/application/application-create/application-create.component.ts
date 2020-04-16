import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SessionService} from "../../shared/service/session.service";
import {ApplicationService} from "../application.service";
import {ToastrService} from "ngx-toastr";
import {ErrorResponseDTO} from "../../shared/model/error-response.model";
import {UserApplication} from "../model/application.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-application-create',
  templateUrl: './application-create.component.html',
  styleUrls: ['./application-create.component.css']
})
export class ApplicationCreateComponent implements OnInit {

  private applicationCreateForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private sessionService: SessionService,
              private applicationService: ApplicationService, private toastr: ToastrService,
              private router: Router) {
  }

  ngOnInit() {
    this.applicationCreateForm = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
  }

  get appCreateForm() {
    return this.applicationCreateForm.controls;
  }

  get canCreateApps(): boolean {
    return this.sessionService.hasPermission("create_application");
  }

  onCreateApplicationSubmit() {
    this.applicationService.createApplication(this.applicationCreateForm.value)
      .then((application: UserApplication) => this.router.navigateByUrl("/application"),
        (error: ErrorResponseDTO) => this.toastr.error(error.message, 'Error'));
  }

}
