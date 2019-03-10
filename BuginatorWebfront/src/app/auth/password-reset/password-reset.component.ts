import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";

@Component({
  selector: 'buginator-auth-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  private passwordResetForm: FormGroup;
  private passwordResetError: boolean = false;
  private passwordResetSuccess: boolean = false;

  constructor(private formBuilder: FormBuilder, private authService: AuthService) {
  }

  ngOnInit() {
    this.passwordResetForm = this.formBuilder.group({
      email: ['', [Validators.email, Validators.required]]
    });
  }

  get passResetForm() {
    return this.passwordResetForm.controls;
  }

  isPasswordResetError(): boolean {
    return this.passwordResetError;
  }

  isPasswordResetSuccess(): boolean {
    return this.passwordResetSuccess;
  }

  onPasswordResetSubmit() {
    this.passwordResetError = false;
    this.passwordResetSuccess = false;
    this.authService.passwordReset(this.passwordResetForm.value.email)
      .then(res => this.passwordResetSuccess = true,
        error => this.passwordResetError = true);
  }

}
