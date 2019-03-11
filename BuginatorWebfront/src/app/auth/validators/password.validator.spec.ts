import {PasswordValidator} from "./password.validator";
import {FormControl, ValidationErrors} from "@angular/forms";

describe('PasswordValidator', () => {

  it('should match passwords when even', () => {
    //given
    let pass = new FormControl();

    let confirmPass = new FormControl();
    confirmPass.setValue('test@gmail.com');
    pass.setValue('test@gmail.com');

    let passwordsMatchValidatorFn = PasswordValidator.passwordsMatch(pass);

    //when
    let validationErrors : ValidationErrors | null = passwordsMatchValidatorFn(confirmPass);

    //then
    expect(validationErrors).toBeNull();
  });

  it('should not match passwords when source is null or empty', () => {
    //given
    let pass = new FormControl();

    let confirmPass = new FormControl();
    confirmPass.setValue('test@gmail.com');

    let passwordsMatchValidatorFn = PasswordValidator.passwordsMatch(pass);

    //when
    let validationErrors : ValidationErrors | null = passwordsMatchValidatorFn(confirmPass);

    //then
    expect(validationErrors).not.toBeNull();
    expect(validationErrors.passwordNotMatch).toBeTruthy();
  });

  it('should not match passwords when confirm is null or empty', () => {
    //given
    let pass = new FormControl();

    let confirmPass = new FormControl();
    pass.setValue('test@gmail.com');

    let passwordsMatchValidatorFn = PasswordValidator.passwordsMatch(pass);

    //when
    let validationErrors : ValidationErrors | null = passwordsMatchValidatorFn(confirmPass);

    //then
    expect(validationErrors).not.toBeNull();
    expect(validationErrors.passwordNotMatch).toBeTruthy();
  });

  it('should not match passwords when values are different', () => {
    //given
    let pass = new FormControl();

    let confirmPass = new FormControl();
    pass.setValue('test@gmail.com');
    confirmPass.setValue('test123@gmail.com');

    let passwordsMatchValidatorFn = PasswordValidator.passwordsMatch(pass);

    //when
    let validationErrors : ValidationErrors | null = passwordsMatchValidatorFn(confirmPass);

    //then
    expect(validationErrors).not.toBeNull();
    expect(validationErrors.passwordNotMatch).toBeTruthy();
  });
});
