import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-message-box',
  templateUrl: './message-box.component.html',
  styleUrls: ['./message-box.component.scss']
})
export class MessageBoxComponent {
  constructor(@Inject(MAT_DIALOG_DATA) private data: string[]) { }

  getErrorMessages(): string[] {
    return this.data;
  }
}