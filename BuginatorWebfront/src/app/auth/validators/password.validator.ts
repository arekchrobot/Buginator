import {AbstractControl, ValidatorFn} from "@angular/forms";

export class PasswordValidator {

  public static passwordsMatch(sourcePassword: AbstractControl): ValidatorFn {
    return (control: AbstractControl) => {
      const passwordMatch = control.value;

      return (sourcePassword.value === passwordMatch) ? null : {passwordNotMatch: true};
    }
  }
}
