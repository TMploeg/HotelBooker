import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';
import { Hotel } from 'src/app/models/hotel';
import { HotelService } from 'src/app/services/hotel.service';

@Component({
  selector: 'app-hotel-list',
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.scss']
})
export class HotelListComponent {
  hotels: Hotel[] = [];

  constructor(
    private router: Router,
    private hotelService: HotelService
  ) { }

  ngOnInit(): void {
    this.hotelService.getAll().subscribe(hotels => {
      this.hotels = hotels ?? [];
    })
  }

  navigate(hotel: Hotel) {
    this.router.navigateByUrl(AppRoutes.buildRoute(AppRoutes.HOTELS, hotel.id.toString()));
  }
}
