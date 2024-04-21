import { Component } from '@angular/core';
import { AbstractControl, ValidationErrors, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PasswordHelpDialogComponent } from './password-help-dialog/password-help-dialog.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private static readonly PasswordMinLength: number = 7;
  private static readonly SpecialCharacterPattern: string = '[!@#$%&*()_+=|<>?{}\\[\\]~-]';

  fieldProperties = {
    username: {
      validators: [Validators.required]
    },
    password: {
      validators: [Validators.required, this.passwordStrengthValidator],
      showHelp: () => this.dialog.open(PasswordHelpDialogComponent, {
        data: [
          `password must have at least ${RegisterComponent.PasswordMinLength} characters`,
          'password must have a number',
          'password must have a lowercase character',
          'password must have an uppercase character',
          'password must have a special character'
        ]
      })
    }
  }

  constructor(private dialog: MatDialog) { }

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
