import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'buginator-auth-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  private passwordResetForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.passwordResetForm = this.formBuilder.group({
      email: ['']
    });
  }

  get passResetForm() {
    return this.passwordResetForm.controls;
  }

  onPasswordResetSubmit() {
    console.log('password reset submit');
    console.log(this.passwordResetForm);
  }

}
