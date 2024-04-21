import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent {
  @Output() menuClicked = new EventEmitter();
  constructor(private router: Router) { }
  titleClicked(): void {
    this.router.navigateByUrl('');
  }
}
