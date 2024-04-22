import { Component } from '@angular/core';
import { AbstractControl, FormControl, ValidationErrors, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-register',
  templateUrl: '../auth-form/auth-form.component.html',
  styleUrls: ['../auth-form/auth-form.component.scss']
})
export class RegisterComponent extends AuthFormComponent {
  private static readonly PasswordMinLength: number = 7;
  private static readonly SpecialCharacterPattern: string = '[!@#$%&*()_+=|<>?{}\\[\\]~-]';

  formTitle: string = 'Register new account';
  navigateButtonText: string = 'Go To Login';
  navigateButtonRoute: string = '/login';
  submitButtonText: string = 'Register';

  constructor(private dialog: MatDialog) { super(); }

  override initControl(controlName: string, control: FormControl) {
    switch (controlName) {
      case 'username':
        control.addValidators([Validators.required]);
        break;
      case 'password':
        control.addValidators([Validators.required, this.passwordStrengthValidator]);
        break;
      default:
        console.warn('unhandled control found');
        break;
    }
  }

  override submit(): void {

  }


  private passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    if (!control || !control.value) {
      return null;
    }

    const value = control.value;
    const checks: { [key: string]: () => boolean } = {
      tooShort: () => value.length < RegisterComponent.PasswordMinLength,
      noNumber: () => !new RegExp('[0-9]').test(value),
      noLowerCase: () => !new RegExp('[a-z]').test(value),
      noUpperCase: () => !new RegExp('[A-Z]').test(value),
      noSpecialCharacter: () => !new RegExp(RegisterComponent.SpecialCharacterPattern).test(value),
    };

    return Object.entries(checks).reduce((a, c) => c[1]() ? { ...a, [`${c[0]}`]: true } : a, {})
  }
}
