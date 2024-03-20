import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';
import { Hotel } from 'src/app/models/hotel';

@Component({
  selector: 'app-hotel-list',
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.scss']
})
export class HotelListComponent {
  hotels: Hotel[] = [];

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    for (let i: number = 1; i <= 5; i++) {
      this.hotels.push({ id: i, name: 'hotel' + i, address: 'hotel' + i + '_address' });
    }
  }

  navigate(hotel: Hotel) {
    this.router.navigateByUrl(AppRoutes.buildRoute(AppRoutes.HOTELS, ':' + hotel.id));
  }
}
