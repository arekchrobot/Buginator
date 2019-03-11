import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PasswordValidator} from "../validators/password.validator";

@Component({
  selector: 'buginator-auth-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private registerForm: FormGroup;
  private registerError: boolean = false;
  private registerErrorMessage: string;

  constructor(private formBuilder: FormBuilder) {
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
    console.log('register form submit');
    console.log(this.registerForm);
  }

  isRegisterError() {
    return this.registerError;
  }

  get regErrorMessage() {
    return this.registerErrorMessage;
  }
}
