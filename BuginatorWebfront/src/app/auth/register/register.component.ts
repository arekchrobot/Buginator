import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'buginator-auth-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private registerForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['']
    });
  }

  get regForm() {
    return this.registerForm.controls;
  }

  onRegisterFormSubmit() {
    console.log('register form submit');
    console.log(this.registerForm);
  }
}
