import { Component } from '@angular/core';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  fieldProperties = {
    username: {
      validators: [Validators.required]
    },
    password: {
      validators: [Validators.required],
    }
  }
}
