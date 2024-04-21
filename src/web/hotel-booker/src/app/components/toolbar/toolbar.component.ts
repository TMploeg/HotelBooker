import { Component, EventEmitter, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {
  @Output() menuClicked = new EventEmitter();
  constructor(private router: Router, private dialog: MatDialog) { }
  titleClicked(): void {
    this.router.navigateByUrl('');
  }
}
