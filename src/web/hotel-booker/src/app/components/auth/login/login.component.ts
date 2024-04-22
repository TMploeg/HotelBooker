import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { MessageBoxComponent } from '../../message-box/message-box.component';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-login',
  templateUrl: '../auth-form/auth-form.component.html',
  styleUrls: ['../auth-form/auth-form.component.scss']
})
export class LoginComponent extends AuthFormComponent {
  formTitle: string = 'Login to account';
  navigateButtonText: string = 'Go To Register';
  navigateButtonRoute: string = '/register';
  submitButtonText: string = 'Login'

  constructor(private userService: UserService, private router: Router, private dialog: MatDialog) { super(); }

  override initControl(controlName: string, control: FormControl) {
    switch (controlName) {
      case 'username':
        control.addValidators([Validators.required]);
        break;
      case 'password':
        control.addValidators([Validators.required]);
        break;
      default:
        console.warn('unhandled control found');
        break;
    }
  }

  override submit(): void {
    this.userService.login(this.getFormData()).subscribe(loginSucces => {
      if (loginSucces) {
        this.router.navigateByUrl('');
      }
      else {
        this.dialog.open(MessageBoxComponent, { data: ['username or password is incorrect'] });
      }
    });
  }
}
