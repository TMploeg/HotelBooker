import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-password-help-dialog',
  templateUrl: './password-help-dialog.component.html',
  styleUrls: ['./password-help-dialog.component.scss']
})
export class PasswordHelpDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: string[]) { }
}
