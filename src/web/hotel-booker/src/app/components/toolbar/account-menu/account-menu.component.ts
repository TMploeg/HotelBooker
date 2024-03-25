import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-account-menu',
  templateUrl: './account-menu.component.html',
  styleUrls: ['./account-menu.component.scss']
})
export class AccountMenuComponent implements OnInit {
  public usernameDisplayValue: String = '';

  ngOnInit(): void {
    this.usernameDisplayValue = 'myUsername';
  }
}
