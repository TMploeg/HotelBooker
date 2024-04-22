import { Directive, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthDTO } from 'src/app/models/dtos/dtos.auth';

@Directive({
  selector: 'app-auth-form'
})
export abstract class AuthFormComponent implements OnInit {
  formGroup!: FormGroup;
  private helpData: { [key: string]: () => void } = {};

  private static readonly DefaultFieldNames: string[] = ['username', 'password'];

  passwordInvisible: boolean = true;

  ngOnInit(): void {
    this.formGroup = new FormGroup({});
    AuthFormComponent.DefaultFieldNames.forEach(name => {
      const control: FormControl = new FormControl('');
      this.initControl(name, control);
      this.formGroup.addControl(name, control);
    }
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

  getHelpFn(controlName: string): () => void | undefined {
    return this.helpData[controlName];
  }

  showHelp(controlName: string) {
    const helpFn = this.getHelpFn(controlName);
    if (!helpFn) {
      console.error('help function missing');
      return;
    }

    helpFn();
  }

  abstract initControl(controlName: string, control: FormControl): void;

  abstract submit(): void;

  protected setHelpDataForControl(controlName: string, helpFn: () => void) {
    this.helpData[controlName] = helpFn;
  }
}
