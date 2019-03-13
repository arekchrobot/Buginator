import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PasswordValidator} from "../validators/password.validator";
import {AuthService} from "../auth.service";
import {ErrorResponseDTO} from "../../shared/model/error-response.model";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'buginator-auth-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private registerForm: FormGroup;
  private registerError: boolean = false;
  private registerErrorMessage: string;

  constructor(private formBuilder: FormBuilder, private authService: AuthService,
              private translateService: TranslateService) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      userName: [''],
      userEmail: ['', [Validators.required, Validators.email]],
      userPassword: ['', Validators.required],
      userPasswordConfirm: [''],
      companyName: ['', Validators.required],
      companyAddress: [''],
    });

    this.registerForm.get("userPasswordConfirm")
      .setValidators([Validators.required, PasswordValidator.passwordsMatch(this.registerForm.controls.userPassword)]);
  }

  get regForm() {
    return this.registerForm.controls;
  }

  onRegisterFormSubmit() {
    this.registerError = false;
    this.registerErrorMessage = null;
    this.authService.register(this.registerForm.value)
      .then(res => {
        this.authService.registerSuccessSubject.next(true);
      }, (error: ErrorResponseDTO) => {
        this.registerError = true;
          this.translateService.get(error.message.split(',')).subscribe(result => {
            this.registerErrorMessage = Object.keys(result)
              .map(key => result[key])
              .join(',');
          });
      });
  }

  isRegisterError() {
    return this.registerError;
  }

  get regErrorMessage() {
    return this.registerErrorMessage;
  }
}
