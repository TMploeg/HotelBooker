import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { AuthDTO } from 'src/app/models/dtos/dtos.auth';

@Component({
  selector: 'app-auth-form',
  templateUrl: './auth-form.component.html',
  styleUrls: ['./auth-form.component.scss']
})
export class AuthFormComponent implements OnInit {
  formGroup!: FormGroup;

  @Input() fieldProperties: {
    [key: string]: {
      validators: ValidatorFn[],
      showHelp?: () => void
    }
  } = {};

  @Input() submitButtonText?: string;

  private static readonly DefaultFieldNames: string[] = ['username', 'password'];

  passwordInvisible: boolean = true;

  ngOnInit(): void {
    this.formGroup = new FormGroup({});
    AuthFormComponent.DefaultFieldNames.forEach(
      name => this.formGroup.addControl(
        name,
        new FormControl('', this.fieldProperties[name] ?? [])
      )
    );
  }

  getFormData(): AuthDTO {
    return {
      username: this.formGroup.controls['username'].value,
      password: this.formGroup.controls['password'].value
    };
  }

  hasError(controlName: string) {
    const control = this.formGroup.controls[controlName];
    return control.errors && Object.keys(control.errors).length > 0;
  }

  getControlError(controlName: string): string | null {
    if (!this.formGroup.contains(controlName)) {
      console.error(`control '${controlName}' could not be found`);
    }

    const control = this.formGroup.controls[controlName];
    if (!control.errors || Object.keys(control.errors).length === 0) {
      return null;
    }

    if (control.hasError('required')) {
      return `${controlName} is required`;
    }

    return `${controlName} is invalid`;
  }

  hasHelp(controlName: string) {
    const properties = this.fieldProperties[controlName];

    return properties && properties.showHelp;
  }

  showHelp(controlName: string) {
    if (!this.hasHelp(controlName)) {
      console.error('help function missing');
      return;
    }

    this.fieldProperties[controlName].showHelp!();
  }
}
